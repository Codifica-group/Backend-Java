package com.codifica.elevebot.service;

import com.codifica.elevebot.adapter.PacoteAdapter;
import com.codifica.elevebot.dto.PacoteDTO;
import com.codifica.elevebot.exception.ConflictException;
import com.codifica.elevebot.exception.NotFoundException;
import com.codifica.elevebot.model.Cliente;
import com.codifica.elevebot.model.Pacote;
import com.codifica.elevebot.repository.PacoteRepository;
import com.codifica.elevebot.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PacoteService {

    @Autowired
    private PacoteRepository pacoteRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    private PacoteAdapter pacoteAdapter;

    public String cadastrar(PacoteDTO pacoteDTO) {
        Cliente cliente = clienteRepository.findById(pacoteDTO.getClienteId())
                .orElseThrow(() -> new NotFoundException("Cliente nao encontrado."));

        boolean clientePossuiPacoteAtivo = pacoteRepository.existsByClienteIdAndDataExpiracaoGreaterThan(cliente.getId(), LocalDate.now());
        if (clientePossuiPacoteAtivo) {
            throw new ConflictException("Cliente já possui um pacote ativo.");
        }

        Pacote pacote = pacoteAdapter.toEntity(pacoteDTO, cliente);

        LocalDate dataInicio = (pacoteDTO.getDataExpiracao() != null) ? pacoteDTO.getDataExpiracao() : LocalDate.now();

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
        return "Pacote cadastrado com sucesso.";
    }

    public List<PacoteDTO> listar() {
        List<Pacote> pacotes = pacoteRepository.findAll();

        return pacotes.stream()
                .map(PacoteAdapter::toDTO)
                .toList();
    }

    public Pacote buscarPorId(Integer id) {
        return pacoteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Registro cliente_pacote não encontrado."));
    }

    public String atualizar(Integer id, PacoteDTO pacoteDTO) {
        Pacote pacoteExistente = pacoteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pacote não encontrado."));

        if (!pacoteExistente.getDataExpiracao().isAfter(LocalDate.now())) {
            throw new ConflictException("Impossível atualizar um pacote expirado.");
        }

        pacoteExistente.setPacoteId(pacoteDTO.getPacoteId());
        LocalDate dataInicio = pacoteDTO.getDataExpiracao();

        switch (pacoteDTO.getPacoteId()) {
            case 1: // Mensal = +31 dias
                pacoteExistente.setDataExpiracao(dataInicio.plusDays(31));
                break;
            case 2: // Quinzenal = +16 dias
                pacoteExistente.setDataExpiracao(dataInicio.plusDays(16));
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

    public String deletar(Integer id) {
        Pacote pacote = buscarPorId(id);
        pacoteRepository.delete(pacote);

        return "Pacote deletado com sucesso.";
    }
}
