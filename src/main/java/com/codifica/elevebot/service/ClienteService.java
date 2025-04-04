package com.codifica.elevebot.service;

import com.codifica.elevebot.exception.ConflictException;
import com.codifica.elevebot.exception.NotFoundException;
import com.codifica.elevebot.model.Cliente;
import com.codifica.elevebot.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public String cadastrar(Cliente cliente) {
        if (clienteExiste(cliente)) {
            throw new ConflictException("Cliente já cadastrado.");
        }

        clienteRepository.save(cliente);
        return "Cliente cadastrado com sucesso.";
    }

    public List<Cliente> listar() {
        return clienteRepository.findAll();
    }

    public Cliente buscarPorId(Integer id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado."));
    }

    public String atualizar(Integer id, Cliente cliente) {
        if (!clienteRepository.existsById(id)) {
            throw new NotFoundException("Cliente não encontrado.");
        }

        cliente.setId(id);
        clienteRepository.save(cliente);
        return "Cliente atualizado com sucesso!";
    }

    public String deletar(Integer id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente nao encontrado."));

        if (cliente.getPets() != null && !cliente.getPets().isEmpty()) {
            throw new ConflictException("Não é possível deletar clientes que possui pets cadastrados.");
        }

        clienteRepository.deleteById(id);
        return "Cliente deletado com sucesso.";
    }

    private boolean clienteExiste(Cliente cliente) {
        Cliente clienteFiltro = new Cliente(cliente.getNome(),
                                            cliente.getNumeroCelular(),
                                            cliente.getCep(),
                                            cliente.getNumeroEndereco(),
                                            cliente.getComplemento());

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withIgnoreCase("nome", "numeroCelular", "cep", "complemento");

        Example<Cliente> example = Example.of(clienteFiltro, matcher);
        return clienteRepository.exists(example);
    }
}
