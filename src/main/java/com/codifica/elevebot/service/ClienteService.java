package com.codifica.elevebot.service;

import com.codifica.elevebot.adapter.ClienteAdapter;
import com.codifica.elevebot.dto.ClienteDTO;
import com.codifica.elevebot.exception.ConflictException;
import com.codifica.elevebot.exception.NotFoundException;
import com.codifica.elevebot.model.Cliente;
import com.codifica.elevebot.repository.ClienteRepository;
import com.codifica.elevebot.repository.PacoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PacoteRepository pacoteRepository;

    @Autowired
    private ClienteAdapter clienteAdapter;

    public Object cadastrar(Cliente cliente) {
        if (clienteExiste(cliente)) {
            throw new ConflictException("Impossível cadastrar dois clientes com dados iguais.");
        }

        clienteRepository.save(cliente);

        var resposta = new HashMap<String, Object>();
        resposta.put("mensagem", "Cliente cadastrado com sucesso.");
        resposta.put("id", cliente.getId());
        return resposta;
    }

    public List<ClienteDTO> listar() {
        List<Cliente> clientes = clienteRepository.findAll();
        return clientes.stream().map(cliente -> clienteAdapter.toDTO(cliente)).toList();
    }

    public ClienteDTO buscarPorId(Integer id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado."));

        return clienteAdapter.toDTO(cliente);
    }

    public String atualizar(Integer id, Cliente cliente) {
        if (!clienteRepository.existsById(id)) {
            throw new NotFoundException("Cliente não encontrado.");
        }

        cliente.setId(id);
        clienteRepository.save(cliente);
        return "Cliente atualizado com sucesso!";
    }

    public void deletar(Integer id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado."));

        if (cliente.getPets() != null && !cliente.getPets().isEmpty()) {
            throw new ConflictException("Não é possível deletar clientes que possui pets cadastrados.");
        }

        clienteRepository.deleteById(id);
    }

    private boolean clienteExiste(Cliente cliente) {
        Cliente clienteFiltro = new Cliente(cliente.getNome(),
                                            cliente.getNumCelular(),
                                            cliente.getCep(),
                                            cliente.getRua(),
                                            cliente.getNumEndereco(),
                                            cliente.getBairro(),
                                            cliente.getCidade(),
                                            cliente.getComplemento());

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withIgnoreCase("nome", "numeroCelular", "cep", "complemento");

        Example<Cliente> example = Example.of(clienteFiltro, matcher);
        return clienteRepository.exists(example);
    }
}
