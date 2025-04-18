package com.codifica.elevebot.controller;

import com.codifica.elevebot.model.Despesa;
import com.codifica.elevebot.service.DespesaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/despesas")
public class DespesaController {

    @Autowired
    private DespesaService despesaService;

    @PostMapping
    public ResponseEntity<String> cadastrarDespesa(@RequestBody List<Despesa> despesas) {
        String mensagem = despesaService.cadastrar(despesas);
        return ResponseEntity.status(HttpStatus.CREATED).body(mensagem);
    }

    @GetMapping
    public ResponseEntity<List<Despesa>> listarDespesas() {
        List<Despesa> despesas = despesaService.listar();
        return despesas.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok().body(despesas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Despesa> buscarDespesaPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(despesaService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> atualizarDespesa(@PathVariable Integer id, @RequestBody Despesa despesa) {
        String mensagem = despesaService.atualizar(id, despesa);
        return ResponseEntity.ok(mensagem);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarDespesa(@PathVariable Integer id) {
        String mensagem = despesaService.deletar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(mensagem);
    }
}
