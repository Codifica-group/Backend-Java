package com.codifica.elevebot.service;

import com.codifica.elevebot.exception.ConflictException;
import com.codifica.elevebot.exception.NotFoundException;
import com.codifica.elevebot.model.Cliente;
import com.codifica.elevebot.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public String cadastrar(Cliente cliente) {
        if (clienteRepository.findByNome(cliente.getNome()) != null) {
            throw new ConflictException("Não é possível cadastrar dois clientes com o mesmo nome.");
        }

        clienteRepository.save(cliente);
        return "Cliente cadastrado com sucesso!";
    }

    public List<Cliente> listar() {
        return clienteRepository.findAll();
    }

    public Cliente buscarPorId(Integer id) {
        return clienteRepository.findById(id).orElse(null);
    }

    public String atualizar(Integer id, Cliente cliente) {
        if (!clienteRepository.existsById(id)) {
            throw new NotFoundException("Cliente nao encontrado.");
        }

        cliente.setId(id);
        clienteRepository.save(cliente);
        return "Cliente atualizado com sucesso!";
    }

    public String deletar(Integer id) {
        if (!clienteRepository.existsById(id)) {
            throw new NotFoundException("Cliente nao encontrado.");
        }

        clienteRepository.deleteById(id);
        return "Cliente deletado com sucesso!";
    }
}
