package com.codifica.elevebot.controller;

import com.codifica.elevebot.dto.DespesaDTO;
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
    public ResponseEntity<String> cadastrarDespesa(@RequestBody List<DespesaDTO> despesasDTO) {
        String mensagem = despesaService.cadastrar(despesasDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(mensagem);
    }

    @GetMapping
    public ResponseEntity<List<DespesaDTO>> listarDespesas() {
        List<DespesaDTO> despesas = despesaService.listar();
        return despesas.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(despesas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DespesaDTO> buscarDespesaPorId(@PathVariable Integer id) {
        DespesaDTO despesaDTO = despesaService.buscarPorId(id);
        return ResponseEntity.ok(despesaDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> atualizarDespesa(@PathVariable Integer id, @RequestBody DespesaDTO despesaDTO) {
        String mensagem = despesaService.atualizar(id, despesaDTO);
        return ResponseEntity.ok(mensagem);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletarDespesa(@PathVariable Integer id) {
        despesaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}