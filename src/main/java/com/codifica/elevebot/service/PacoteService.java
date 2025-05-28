package com.codifica.elevebot.service;

import com.codifica.elevebot.adapter.PacoteAdapter;
import com.codifica.elevebot.dto.PacoteDTO;
import com.codifica.elevebot.exception.ConflictException;
import com.codifica.elevebot.exception.NotFoundException;
import com.codifica.elevebot.exception.IllegalArgumentException;
import com.codifica.elevebot.model.Cliente;
import com.codifica.elevebot.model.Pacote;
import com.codifica.elevebot.repository.PacoteRepository;
import com.codifica.elevebot.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@Service
public class PacoteService {

    @Autowired
    private PacoteRepository pacoteRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PacoteAdapter pacoteAdapter;

    public Object cadastrar(PacoteDTO pacoteDTO) {
        Cliente cliente = clienteRepository.findById(pacoteDTO.getClienteId())
                .orElseThrow(() -> new NotFoundException("Cliente nao encontrado."));

        boolean clientePossuiPacoteAtivo = pacoteRepository.existsByClienteIdAndDataExpiracaoGreaterThan(cliente.getId(), LocalDate.now());
        if (clientePossuiPacoteAtivo) {
            throw new ConflictException("Cliente já possui um pacote ativo.");
        }

        Pacote pacote = pacoteAdapter.toEntity(pacoteDTO, cliente);

        LocalDate dataInicio = (pacoteDTO.getDataInicio() != null) ? pacoteDTO.getDataInicio() : LocalDate.now();
        pacote.setDataInicio(dataInicio);

        switch (pacoteDTO.getPacoteId()) {
            case 1: // Mensal = +31 dias
                pacote.setPacoteId(1);
                pacote.setDataExpiracao(dataInicio.plusDays(31));
                break;
            case 2: // Quinzenal = +16 dias
                pacote.setPacoteId(2);
                pacote.setDataExpiracao(dataInicio.plusDays(16));
                break;
            default:
                throw new IllegalArgumentException("Tipo (id) do pacote deve ser 1 (Mensal) ou 2 (Quinzenal).");
        }

        if (!pacote.getDataExpiracao().isAfter(LocalDate.now())) {
            throw new ConflictException("Impossível cadastrar um pacote expirado.");
        }

        pacoteRepository.save(pacote);

        var resposta = new HashMap<String, Object>();
        resposta.put("mensagem", "Pacote cadastrado com sucesso.");
        resposta.put("id", pacote.getId());
        return resposta;
    }

    public List<PacoteDTO> listar() {
        List<Pacote> pacotes = pacoteRepository.findAll();
        return pacotes.stream()
                .map(pacoteAdapter::toDTO)
                .toList();
    }

    public PacoteDTO buscarPorId(Integer id) {
        Pacote pacote = pacoteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pacote não encontrado."));
        return pacoteAdapter.toDTO(pacote);
    }

    public String atualizar(Integer id, PacoteDTO pacoteDTO) {
        Pacote pacoteExistente = pacoteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pacote não encontrado."));

        if (!pacoteExistente.getDataExpiracao().isAfter(LocalDate.now())) {
            throw new ConflictException("Impossível atualizar um pacote expirado.");
        }

        pacoteExistente.setPacoteId(pacoteDTO.getPacoteId());

        switch (pacoteDTO.getPacoteId()) {
            case 1: // Mensal = +31 dias
                pacoteExistente.setDataExpiracao(pacoteDTO.getDataInicio().plusDays(31));
                break;
            case 2: // Quinzenal = +16 dias
                pacoteExistente.setDataExpiracao(pacoteDTO.getDataInicio().plusDays(16));
                break;
            default:
                throw new IllegalArgumentException("Tipo (id) do pacote deve ser 1 (Mensal) ou 2 (Quinzenal).");
        }

        if (!pacoteExistente.getDataExpiracao().isAfter(LocalDate.now())) {
            throw new ConflictException("Impossível atualizar um pacote ativo para um pacote expirado.");
        }

        pacoteRepository.save(pacoteExistente);
        return "Pacote atualizado com sucesso.";
    }

    public void deletar(Integer id) {
        Pacote pacote = pacoteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pacote não encontrado."));

        pacoteRepository.delete(pacote);
    }
}
