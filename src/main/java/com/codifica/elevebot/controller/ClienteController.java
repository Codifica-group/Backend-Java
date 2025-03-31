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
    public ResponseEntity<String> cadastrarCliente(@RequestBody Cliente cliente) {
        String mensagem = clienteService.cadastrar(cliente);
        return ResponseEntity.created(null).body(mensagem);
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> listarClientes() {
        return ResponseEntity.ok(clienteService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarClientePorId(@PathVariable Integer id) {
        return ResponseEntity.ok(clienteService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> atualizarCliente(@PathVariable Integer id,
                                                   @RequestBody Cliente cliente) {
        return ResponseEntity.ok(clienteService.atualizar(id, cliente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarCliente(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(clienteService.deletar(id));
    }

}
