package com.codifica.elevebot.controller;

import com.codifica.elevebot.dto.RacaDTO;
import com.codifica.elevebot.model.Raca;
import com.codifica.elevebot.service.RacaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/racas")
public class RacaController {

    @Autowired
    private RacaService racaService;

    @PostMapping
    public ResponseEntity<String> cadastrarRaca(@RequestBody RacaDTO racaDTO) {
        String mensagem = racaService.cadastrar(racaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(mensagem);
    }

    @GetMapping
    public ResponseEntity<Map<String, List<RacaDTO>>> listarRacas() {
        Map<String, List<RacaDTO>> racas = racaService.listar();
        return racas.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(racas);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> atualizarRaca(@PathVariable Integer id, @RequestBody RacaDTO racaDTO) {
        String mensagem = racaService.atualizar(id, racaDTO);
        return ResponseEntity.ok(mensagem);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarRaca(@PathVariable Integer id) {
        String mensagem = racaService.deletar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(mensagem);
    }
}
