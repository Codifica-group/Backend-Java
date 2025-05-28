package com.codifica.elevebot.controller;

import com.codifica.elevebot.model.Servico;
import com.codifica.elevebot.service.ServicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/servicos")
public class ServicoController {

    @Autowired
    private ServicoService servicoService;

    @PostMapping
    public ResponseEntity<Object> cadastrarServico(@RequestBody Servico servico) {
        Object json = servicoService.cadastrar(servico);
        return ResponseEntity.status(HttpStatus.CREATED).body(json);
    }

    @GetMapping
    public ResponseEntity<List<Servico>> listarServicos() {
        List<Servico> servicos = servicoService.listar();
        return servicos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(servicos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> atualizarServico(@PathVariable Integer id, @RequestBody Servico servico) {
        String mensagem = servicoService.atualizar(id, servico);
        return ResponseEntity.ok(mensagem);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletarServico(@PathVariable Integer id) {
        servicoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
