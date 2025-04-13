package com.codifica.elevebot.service;

import com.codifica.elevebot.adapter.PacoteAdapter;
import com.codifica.elevebot.dto.PacoteDTO;
import com.codifica.elevebot.dto.PacoteHistoricoDTO;
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

    private PacoteAdapter pacoteAdapter;

    @Autowired
    private ClienteRepository clienteRepository;

    public String cadastrar(PacoteDTO pacoteDTO) {
        Cliente cliente = clienteRepository.findById(pacoteDTO.getIdCliente())
                .orElseThrow(() -> new NotFoundException("Cliente nao encontrado."));

        boolean clientePossuiPacoteAtivo = pacoteRepository.existsByClienteIdAndDataExpiracaoGreaterThan(
                cliente.getId(), LocalDate.now());

        if (clientePossuiPacoteAtivo) {
            throw new ConflictException("Cliente já possui um pacote ativo.");
        }

        Pacote pacote = pacoteAdapter.toEntity(pacoteDTO, cliente);

        switch (pacoteDTO.getIdPacote()) {
            case 1:
                // Mensal (31 dias)
                pacote.setIdPacote(1);
                pacote.setDataExpiracao(LocalDate.now().plusDays(31));
                break;
            case 2:
                // Quinzenal (16 dias)
                pacote.setIdPacote(2);
                pacote.setDataExpiracao(LocalDate.now().plusDays(16));
                break;
            default:
                throw new IllegalArgumentException("Tipo (id) do pacote deve ser 1 (Mensal) ou 2 (Quinzenal).");
        }

        pacoteRepository.save(pacote);
        return "Pacote cadastrado com sucesso.";
    }

    public List<PacoteHistoricoDTO> listar() {
        List<Pacote> pacotes = pacoteRepository.findAll();

        return pacotes.stream()
                .map(PacoteAdapter::toHistoricoDTO)
                .toList();
    }

    public Pacote buscarPorId(Integer id) {
        return pacoteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Registro cliente_pacote não encontrado."));
    }

    public String deletar(Integer id) {
        Pacote pacote = buscarPorId(id);
        pacoteRepository.delete(pacote);

        return "Pacote deletado com sucesso.";
    }
}
