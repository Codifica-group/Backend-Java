package com.codifica.elevebot.controller;

import com.codifica.elevebot.model.Cliente;
import com.codifica.elevebot.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping
    public ResponseEntity<Object> cadastrarCliente(@RequestBody Cliente cliente) {
        Object json = clienteService.cadastrar(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(json);
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> listarClientes() {
        List<Cliente> clientes = clienteService.listar();
        return clientes.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok().body(clientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarClientePorId(@PathVariable Integer id) {
        return ResponseEntity.ok(clienteService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> atualizarCliente(@PathVariable Integer id,
                                                   @RequestBody Cliente cliente) {
        String mensagem = clienteService.atualizar(id, cliente);
        return ResponseEntity.ok(mensagem);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarCliente(@PathVariable Integer id) {
        String mensagem = clienteService.deletar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(mensagem);
    }

}
