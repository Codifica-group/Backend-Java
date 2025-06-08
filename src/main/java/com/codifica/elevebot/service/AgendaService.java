package com.codifica.elevebot.service;

import com.codifica.elevebot.adapter.AgendaAdapter;
import com.codifica.elevebot.adapter.ServicoAdapter;
import com.codifica.elevebot.dto.*;
import com.codifica.elevebot.exception.ConflictException;
import com.codifica.elevebot.exception.NotFoundException;
import com.codifica.elevebot.exception.IllegalArgumentException;
import com.codifica.elevebot.filter.LogFilter;
import com.codifica.elevebot.model.*;
import com.codifica.elevebot.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Autowired
    private ServicoAdapter servicoAdapter;

    @Autowired
    private CepService cepService;

    private static final String URL_DESLOCAMENTO = System.getenv("URL_DESLOCAMENTO");

    private static final Logger logger = LoggerFactory.getLogger(LogFilter.class);


    public Object cadastrar(AgendaDTO agendaDTO) {
        if (agendaDTO.getDataHoraInicio().isAfter(agendaDTO.getDataHoraFim())) {
            throw new IllegalArgumentException("A data/hora de inicio deve ser menor que a data/hora de fim.");
        }

        List<Agenda> conflitos = agendaRepository.findConflitos(agendaDTO.getDataHoraInicio(), agendaDTO.getDataHoraFim());
        boolean temConflito = conflitos.stream().anyMatch(agenda ->
                !(agendaDTO.getDataHoraInicio().isEqual(agenda.getDataHoraFim()) ||
                        agendaDTO.getDataHoraFim().isEqual(agenda.getDataHoraInicio()))
        );
        if (temConflito) {
            throw new ConflictException("Já existe outro agendamento no período informado.");
        }

        Pet pet = petRepository.findById(agendaDTO.getPetId())
                .orElseThrow(() -> new NotFoundException("Pet não encontrado."));

        Agenda agenda = agendaAdapter.toEntity(agendaDTO, pet);
        agendaRepository.save(agenda);

        List<ServicoDTO> servicos = agendaDTO.getServicos();
        if (servicos == null || servicos.isEmpty()) {
            throw new IllegalArgumentException("É necessário informar ao menos um serviço.");
        }
        List<AgendaServico> agendaServicos = agendaDTO.getServicos().stream().map(servicoDTO -> {
            Servico servico = servicoRepository.findById(servicoDTO.getId())
                    .orElseThrow(() -> new NotFoundException("Serviço com ID " + servicoDTO.getId() + " não encontrado."));

            AgendaServico agendaServico = new AgendaServico();
            agendaServico.setAgenda(agenda);
            agendaServico.setServico(servico);
            agendaServico.setValor(servicoDTO.getValor());
            return agendaServico;
        }).collect(Collectors.toList());
        agendaServicoRepository.saveAll(agendaServicos);

        var resposta = new HashMap<String, Object>();
        resposta.put("mensagem", "Agenda cadastrada com sucesso.");
        resposta.put("id", agenda.getId());
        return resposta;
    }

    public List<AgendaDTO> listar() {
        return agendaRepository.findAll().stream()
                .map(agenda -> {
                    List<AgendaServico> agendaServicos = agendaServicoRepository.findByAgenda(agenda);
                    List<ServicoDTO> servicos = agendaServicos.stream()
                            .map(agendaServico -> {
                                Servico servico = agendaServico.getServico();
                                Double valor = agendaServico.getValor();
                                return servicoAdapter.toDTO(servico, valor);
                            })
                            .toList();

                    AgendaDTO agendaDTO = agendaAdapter.toDTO(agenda, servicos);
                    return agendaDTO;
                })
                .toList();
    }

    public AgendaDTO buscarPorId(Integer id) {
        Agenda agenda = agendaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Agenda não encontrada."));

        List<AgendaServico> agendaServicos = agendaServicoRepository.findByAgenda(agenda);
        List<ServicoDTO> servicos = agendaServicos.stream()
                .map(agendaServico -> {
                    Servico servico = agendaServico.getServico();
                    Double valor = agendaServico.getValor();
                    return servicoAdapter.toDTO(servico, valor);
                })
                .toList();

        AgendaDTO agendaDTO = agendaAdapter.toDTO(agenda, servicos);
        return agendaDTO;
    }

    public String atualizar(Integer id, AgendaDTO agendaDTO) {
        if (agendaDTO.getDataHoraInicio().isAfter(agendaDTO.getDataHoraFim())) {
            throw new IllegalArgumentException("A data/hora de inicio deve ser menor que a data/hora de fim.");
        }

        Agenda agendaExistente = agendaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Agenda não encontrada."));

        List<Agenda> conflitos = agendaRepository.findConflitosExcluindoId(id, agendaDTO.getDataHoraInicio(), agendaDTO.getDataHoraFim());
        boolean temConflito = conflitos.stream().anyMatch(agenda ->
                !(agendaDTO.getDataHoraInicio().isEqual(agenda.getDataHoraFim()) ||
                        agendaDTO.getDataHoraFim().isEqual(agenda.getDataHoraInicio()))
        );
        if (temConflito) {
            throw new ConflictException("Já existe outro agendamento no período informado.");
        }

        Pet pet = petRepository.findById(agendaDTO.getPetId())
                .orElseThrow(() -> new NotFoundException("Pet não encontrado."));

        agendaExistente.setPet(pet);
        agendaExistente.setDataHoraInicio(agendaDTO.getDataHoraInicio());
        agendaExistente.setDataHoraFim(agendaDTO.getDataHoraFim());
        agendaExistente.setValorDeslocamento(agendaDTO.getValorDeslocamento());
        agendaRepository.save(agendaExistente);

        List<AgendaServico> agendaServicosAntigos = agendaServicoRepository.findByAgenda(agendaExistente);
        agendaServicoRepository.deleteAll(agendaServicosAntigos);

        if (agendaDTO.getServicos() == null || agendaDTO.getServicos().isEmpty()) {
            throw new IllegalArgumentException("É necessário informar ao menos um serviço.");
        }

        for (ServicoDTO servicoDTO : agendaDTO.getServicos()) {
            Servico servico = servicoRepository.findById(servicoDTO.getId())
                    .orElseThrow(() -> new NotFoundException("Serviço com ID " + servicoDTO.getId() + " não encontrado."));

            AgendaServico novoAgendaServico = new AgendaServico();
            novoAgendaServico.setAgenda(agendaExistente);
            novoAgendaServico.setServico(servico);
            novoAgendaServico.setValor(servicoDTO.getValor());
            agendaServicoRepository.save(novoAgendaServico);
        }

        return "Agenda atualizada com sucesso!";
    }

    public void deletar(Integer id) {
        Agenda agenda = agendaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Agenda não encontrada."));

        List<AgendaServico> agendaServicos = agendaServicoRepository.findByAgenda(agenda);
        agendaServicoRepository.deleteAll(agendaServicos);
        agendaRepository.deleteById(id);
    }

    public List<AgendaDTO> filtrar(Filtro filtro) {
        if (filtro.getDataInicio().isAfter(filtro.getDataFim())) {
            throw new IllegalArgumentException("A data de início deve ser anterior ou igual à data de fim.");
        }

        List<Agenda> agendas = agendaRepository.findAll();

        if (filtro.getDataInicio() != null && filtro.getDataFim() != null) {
            agendas = agendas.stream()
                    .filter(agenda -> !agenda.getDataHoraInicio().toLocalDate().isBefore(filtro.getDataInicio()) &&
                            !agenda.getDataHoraFim().toLocalDate().isAfter(filtro.getDataFim()))
                    .toList();
        } else if (filtro.getDataInicio() != null) {
            agendas = agendas.stream()
                    .filter(agenda -> !agenda.getDataHoraInicio().toLocalDate().isBefore(filtro.getDataInicio()))
                    .toList();
        } else if (filtro.getDataFim() != null) {
            agendas = agendas.stream()
                    .filter(agenda -> !agenda.getDataHoraFim().toLocalDate().isAfter(filtro.getDataFim()))
                    .toList();
        }

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

        return agendas.stream().map(agenda -> {
            List<AgendaServico> agendaServicos = agendaServicoRepository.findByAgenda(agenda);
            List<ServicoDTO> servicos = agendaServicos.stream()
                    .map(agendaServico -> {
                        Servico servico = agendaServico.getServico();
                        Double valor = agendaServico.getValor();
                        return servicoAdapter.toDTO(servico, valor);
                    })
                    .toList();
            return agendaAdapter.toDTO(agenda, servicos);
        }).toList();
    }

    public Total calcularLucro(Total total) {
        if (total.getDataInicio().isAfter(total.getDataFim())) {
            throw new IllegalArgumentException("A data de início deve ser anterior ou igual à data de fim.");
        }

        List<Agenda> agendasNoPeriodo = agendaRepository.findAll().stream()
                .filter(agenda -> !agenda.getDataHoraInicio().toLocalDate().isBefore(total.getDataInicio()) &&
                        !agenda.getDataHoraFim().toLocalDate().isAfter(total.getDataFim()))
                .toList();

        double totalGanhos = agendasNoPeriodo.stream()
                .mapToDouble(agenda -> {
                    List<AgendaServico> servicos = agendaServicoRepository.findByAgenda(agenda);
                    return servicos.stream()
                            .mapToDouble(AgendaServico::getValor)
                            .sum();
                })
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

    public SugestaoDTO calcularServico(AgendaDTO agendaDTO) {
        Pet pet = petRepository.findById(agendaDTO.getPetId())
                .orElseThrow(() -> new NotFoundException("Pet não encontrado."));

        Cliente cliente = pet.getCliente();
        if (cliente == null || cliente.getCep() == null) {
            throw new IllegalArgumentException("Cliente ou CEP do cliente não encontrado.");
        }

        DeslocamentoDTO deslocamentoDTO = new DeslocamentoDTO(0.0, 0.0, 0.0);
        try {
            Map<String, Double> deslocamentoResponse = consultarMicroservicoDeslocamento(cliente);

            deslocamentoDTO.setTempo(deslocamentoResponse.get("tempoMinutos"));
            deslocamentoDTO.setDistanciaKm(deslocamentoResponse.get("distanciaKm"));
            deslocamentoDTO.setValor(deslocamentoResponse.get("taxa"));
        }
        catch (Exception e) {
            logger.error("Erro ao consultar microserviço de deslocamento: {}", e.getMessage());
        }

        List<ServicoDTO> servicosDTO = agendaDTO.getServicos().stream()
                .map(servicoDTO -> {
                    Servico servico = servicoRepository.findById(servicoDTO.getId())
                            .orElseThrow(() -> new NotFoundException("Serviço com ID " + servicoDTO.getId() + " não encontrado."));
                    return new ServicoDTO(servico.getId(), servico.getNome(), servico.getValorBase());
                })
                .collect(Collectors.toList());

        Double valorServico = servicosDTO.stream()
                .mapToDouble(ServicoDTO::getValor)
                .sum();
        Double total = valorServico + deslocamentoDTO.getValor();

        return new SugestaoDTO(total, servicosDTO, deslocamentoDTO);
    }

    private Map<String, Double> consultarMicroservicoDeslocamento(Cliente cliente) {
        RestTemplate restTemplate = new RestTemplate();

        Map<String, String> deslocamentoRequest = new HashMap<>();
        deslocamentoRequest.put("rua", cliente.getRua());
        deslocamentoRequest.put("numero", cliente.getNumEndereco().toString());
        deslocamentoRequest.put("cidade", cliente.getCidade());
        deslocamentoRequest.put("cep", cliente.getCep());

        return restTemplate.postForObject(URL_DESLOCAMENTO, deslocamentoRequest, Map.class);
    }

    private Double calcularValorServico(List<Servico> servicos, Pet pet) {
        Double valorServico = 0.0;

        for (Servico servico : servicos) {
            valorServico += servico.getValorBase();
        }

        String porte = pet.getRaca().getPorte().getNome();
        switch (porte) {
            case "Pequeno":
                valorServico += 10.0;
                break;
            case "Médio":
                valorServico += 20.0;
                break;
            case "Grande":
                valorServico += 30.0;
                break;
            default:
                throw new IllegalArgumentException("Porte do pet inválido.");
        }

        return valorServico;
    }
}