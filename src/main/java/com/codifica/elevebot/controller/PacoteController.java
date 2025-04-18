package com.codifica.elevebot.controller;

import com.codifica.elevebot.dto.PacoteDTO;
import com.codifica.elevebot.dto.PacoteHistoricoDTO;
import com.codifica.elevebot.model.Pacote;
import com.codifica.elevebot.service.PacoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pacotes")
public class PacoteController {

    @Autowired
    private PacoteService pacoteService;

    @PostMapping
    public ResponseEntity<String> cadastrarPacote(@RequestBody PacoteDTO pacoteDTO) {
        String mensagem = pacoteService.cadastrar(pacoteDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(mensagem);
    }

    @GetMapping
    public ResponseEntity<List<PacoteHistoricoDTO>> listarPacotes() {
        List<PacoteHistoricoDTO> pacotes = pacoteService.listar();
        return pacotes.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(pacotes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pacote> buscarPacotePorId(@PathVariable Integer id) {
        return ResponseEntity.ok(pacoteService.buscarPorId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarPacote(@PathVariable Integer id) {
        String mensagem = pacoteService.deletar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(mensagem);
    }
}
