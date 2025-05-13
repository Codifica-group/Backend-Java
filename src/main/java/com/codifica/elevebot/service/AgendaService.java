package com.codifica.elevebot.service;

import com.codifica.elevebot.adapter.AgendaAdapter;
import com.codifica.elevebot.dto.AgendaDTO;
import com.codifica.elevebot.exception.NotFoundException;
import com.codifica.elevebot.model.Agenda;
import com.codifica.elevebot.model.AgendaServico;
import com.codifica.elevebot.model.Pet;
import com.codifica.elevebot.model.Servico;
import com.codifica.elevebot.repository.AgendaRepository;
import com.codifica.elevebot.repository.AgendaServicoRepository;
import com.codifica.elevebot.repository.PetRepository;
import com.codifica.elevebot.repository.ServicoRepository;
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

    public Object cadastrar(AgendaDTO agendaDTO) {
        Pet pet = petRepository.findById(agendaDTO.getPetId())
                .orElseThrow(() -> new NotFoundException("Pet não encontrado."));

        Agenda agenda = AgendaAdapter.toEntity(agendaDTO, pet);
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

                    // Converte a agenda para DTO, incluindo os serviços
                    return AgendaAdapter.toDTO(agenda, servicos);
                })
                .toList();
    }

    public AgendaDTO buscarPorId(Integer id) {
        Agenda agenda = agendaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Agenda não encontrada."));

        List<Servico> servicos = agendaServicoRepository.findByAgenda(agenda).stream()
                .map(AgendaServico::getServico)
                .collect(Collectors.toList());

        return AgendaAdapter.toDTO(agenda, servicos);
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
}