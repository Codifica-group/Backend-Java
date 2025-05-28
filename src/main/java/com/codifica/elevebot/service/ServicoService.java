package com.codifica.elevebot.service;

import com.codifica.elevebot.exception.ConflictException;
import com.codifica.elevebot.exception.NotFoundException;
import com.codifica.elevebot.model.Servico;
import com.codifica.elevebot.repository.AgendaServicoRepository;
import com.codifica.elevebot.repository.ServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class ServicoService {

    @Autowired
    private ServicoRepository servicoRepository;

    @Autowired
    private AgendaServicoRepository agendaServicoRepository;

    public Object cadastrar(Servico servico) {
        if (servicoRepository.existsByNome(servico.getNome())) {
            throw new ConflictException("Servico já cadastrado.");
        }

        servicoRepository.save(servico);

        var resposta = new HashMap<String, Object>();
        resposta.put("mensagem", "Serviço cadastrado com sucesso.");
        resposta.put("id", servico.getId());
        return resposta;
    }

    public List<Servico> listar() {
        return servicoRepository.findAll();
    }

    public String atualizar(Integer id, Servico servico) {
        Servico servicoExistente = servicoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Serviço não encontrado."));

        servicoExistente.setNome(servico.getNome());
        servicoExistente.setValorBase(servico.getValorBase());
        servicoRepository.save(servicoExistente);
        return "Serviço atualizado com sucesso";
    }

    public void deletar(Integer id) {
        Servico servicoExistente = servicoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Serviço não encontrado."));

        if (agendaServicoRepository.existsByServicoId(id)) {
            throw new ConflictException("Não é possível deletar um serviço com agenda cadastrada.");
        }

        servicoRepository.deleteById(id);
    }
}
