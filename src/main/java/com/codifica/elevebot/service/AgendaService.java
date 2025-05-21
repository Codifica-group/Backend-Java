package com.codifica.elevebot.service;

import com.codifica.elevebot.adapter.AgendaAdapter;
import com.codifica.elevebot.dto.AgendaDTO;
import com.codifica.elevebot.exception.NotFoundException;
import com.codifica.elevebot.model.*;
import com.codifica.elevebot.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AgendaService {

    @Autowired
    private AgendaRepository agendaRepository;

    @Autowired
    private AgendaServicoRepository agendaServicoRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private ServicoRepository servicoRepository;

    @Autowired
    private DespesaRepository despesaRepository;

    @Autowired
    private AgendaAdapter agendaAdapter;

    public Object cadastrar(AgendaDTO agendaDTO) {
        Pet pet = petRepository.findById(agendaDTO.getPetId())
                .orElseThrow(() -> new NotFoundException("Pet não encontrado."));

        Agenda agenda = agendaAdapter.toEntity(agendaDTO, pet);
        agendaRepository.save(agenda);

        List<Integer> servicos = agendaDTO.getServicos();
        if (servicos == null || servicos.isEmpty()) {
            throw new IllegalArgumentException("É necessário informar ao menos um serviço.");
        }

        for (Integer servicoId : servicos) {
            Servico servico = servicoRepository.findById(servicoId)
                    .orElseThrow(() -> new NotFoundException("Serviço com ID " + servicoId + " não encontrado."));

            AgendaServico agendaServico = new AgendaServico();
            agendaServico.setAgenda(agenda);
            agendaServico.setServico(servico);
            agendaServicoRepository.save(agendaServico);
        }

        var resposta = new HashMap<String, Object>();
        resposta.put("mensagem", "Agenda cadastrada com sucesso.");
        resposta.put("id", agenda.getId());
        return resposta;
    }

    public List<AgendaDTO> listar() {
        return agendaRepository.findAll().stream()
                .map(agenda -> {
                    List<Servico> servicos = agendaServicoRepository.findByAgenda(agenda).stream()
                            .map(AgendaServico::getServico)
                            .collect(Collectors.toList());

                    return agendaAdapter.toDTO(agenda, servicos);
                })
                .toList();
    }

    public AgendaDTO buscarPorId(Integer id) {
        Agenda agenda = agendaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Agenda não encontrada."));

        List<Servico> servicos = agendaServicoRepository.findByAgenda(agenda).stream()
                .map(AgendaServico::getServico)
                .collect(Collectors.toList());

        return agendaAdapter.toDTO(agenda, servicos);
    }

    public String atualizar(Integer id, AgendaDTO agendaDTO) {
        Agenda agendaExistente = agendaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Agenda não encontrada."));

        Pet pet = petRepository.findById(agendaDTO.getPetId())
                .orElseThrow(() -> new NotFoundException("Pet não encontrado."));

        agendaExistente.setPet(pet);
        agendaExistente.setDataHoraInicio(agendaDTO.getDataHoraInicio());
        agendaExistente.setDataHoraFim(agendaDTO.getDataHoraFim());
        agendaExistente.setValor(agendaDTO.getValor());
        agendaRepository.save(agendaExistente);

        List<AgendaServico> agendaServicosAntigos = agendaServicoRepository.findByAgenda(agendaExistente);
        agendaServicoRepository.deleteAll(agendaServicosAntigos);

        List<Integer> novosServicosIds = agendaDTO.getServicos();
        if (novosServicosIds == null || novosServicosIds.isEmpty()) {
            throw new IllegalArgumentException("É necessário informar ao menos um serviço.");
        }

        for (Integer servicoId : novosServicosIds) {
            Servico servico = servicoRepository.findById(servicoId)
                    .orElseThrow(() -> new NotFoundException("Serviço com ID " + servicoId + " não encontrado."));

            AgendaServico novoAgendaServico = new AgendaServico();
            novoAgendaServico.setAgenda(agendaExistente);
            novoAgendaServico.setServico(servico);
            agendaServicoRepository.save(novoAgendaServico);
        }

        return "Agenda atualizada com sucesso!";
    }

    public String deletar(Integer id) {
        Agenda agenda = agendaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Agenda não encontrada."));

        List<AgendaServico> agendaServicos = agendaServicoRepository.findByAgenda(agenda);
        agendaServicoRepository.deleteAll(agendaServicos);
        agendaRepository.deleteById(id);
        return "Agenda deletada com sucesso.";
    }

    public List<AgendaDTO> filtrar(Filtro filtro) {
        if (filtro.getDataInicio() == null || filtro.getDataFim() == null) {
            throw new IllegalArgumentException("Os campos 'dataInicio' e 'dataFim' são obrigatórios.");
        }

        List<Agenda> agendas = agendaRepository.findAll();

        agendas = agendas.stream()
                .filter(agenda -> !agenda.getDataHoraInicio().toLocalDate().isBefore(filtro.getDataInicio()) &&
                        !agenda.getDataHoraFim().toLocalDate().isAfter(filtro.getDataFim()))
                .toList();

        if (filtro.getClienteId() != null) {
            agendas = agendas.stream()
                    .filter(agenda -> agenda.getPet().getCliente().getId().equals(filtro.getClienteId()))
                    .toList();
        }

        if (filtro.getPetId() != null) {
            agendas = agendas.stream()
                    .filter(agenda -> agenda.getPet().getId().equals(filtro.getPetId()))
                    .toList();
        }

        if (filtro.getRacaId() != null) {
            agendas = agendas.stream()
                    .filter(agenda -> agenda.getPet().getRaca().getId().equals(filtro.getRacaId()))
                    .toList();
        }

        if (filtro.getServicoId() != null && !filtro.getServicoId().isEmpty()) {
            agendas = agendas.stream()
                    .filter(agenda -> {
                        List<Integer> agendaServicosIds = agendaServicoRepository.findByAgenda(agenda).stream()
                                .map(agendaServico -> agendaServico.getServico().getId())
                                .collect(Collectors.toList());

                        return agendaServicosIds.size() == filtro.getServicoId().size() &&
                                agendaServicosIds.containsAll(filtro.getServicoId());
                    })
                    .toList();
        }


        List<AgendaDTO> agendasDTO = agendas.stream().map(agenda -> {
            List<Servico> servicos = agendaServicoRepository.findByAgenda(agenda).stream()
                    .map(AgendaServico::getServico)
                    .collect(Collectors.toList());

            return agendaAdapter.toDTO(agenda, servicos);
        }).toList();
        return agendasDTO;
    }

    public Total calcular(Total total) {
        if (total.getDataInicio().isAfter(total.getDataFim())) {
            throw new IllegalArgumentException("A data de início deve ser anterior ou igual à data de fim.");
        }

        List<Agenda> agendasNoPeriodo = agendaRepository.findAll().stream()
                .filter(agenda -> !agenda.getDataHoraInicio().toLocalDate().isBefore(total.getDataInicio()) &&
                        !agenda.getDataHoraFim().toLocalDate().isAfter(total.getDataFim()))
                .toList();

        double totalGanhos = agendasNoPeriodo.stream()
                .mapToDouble(Agenda::getValor)
                .sum();

        List<Despesa> despesasNoPeriodo = despesaRepository.findAll().stream()
                .filter(despesa -> !despesa.getData().isBefore(total.getDataInicio()) &&
                        !despesa.getData().isAfter(total.getDataFim()))
                .toList();

        double totalGastos = despesasNoPeriodo.stream()
                .mapToDouble(Despesa::getValor)
                .sum();

        double lucro = totalGanhos - totalGastos;

        total.setEntrada(totalGanhos);
        total.setSaida(totalGastos);
        total.setTotal(lucro);

        return total;
    }
}