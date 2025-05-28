package com.codifica.elevebot.controller;

import com.codifica.elevebot.dto.RacaDTO;
import com.codifica.elevebot.service.RacaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/racas")
public class RacaController {

    @Autowired
    private RacaService racaService;

    @PostMapping
    public ResponseEntity<Object> cadastrarRaca(@RequestBody RacaDTO racaDTO) {
        Object json = racaService.cadastrar(racaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(json);
    }

    @GetMapping
    public ResponseEntity<List<RacaDTO>> listarRacas() {
        List<RacaDTO> racas = racaService.listar();
        return racas.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(racas);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> atualizarRaca(@PathVariable Integer id, @RequestBody RacaDTO racaDTO) {
        String mensagem = racaService.atualizar(id, racaDTO);
        return ResponseEntity.ok(mensagem);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletarRaca(@PathVariable Integer id) {
        racaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
