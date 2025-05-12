package com.codifica.elevebot.controller;

import com.codifica.elevebot.dto.PacoteDTO;
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
    public ResponseEntity<Object> cadastrarPacote(@RequestBody PacoteDTO pacoteDTO) {
        Object json = pacoteService.cadastrar(pacoteDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(json);
    }

    @GetMapping
    public ResponseEntity<List<PacoteDTO>> listarPacotes() {
        List<PacoteDTO> pacotes = pacoteService.listar();
        return pacotes.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(pacotes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pacote> buscarPacotePorId(@PathVariable Integer id) {
        return ResponseEntity.ok(pacoteService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> atualizarPacote(@PathVariable Integer id, @RequestBody PacoteDTO pacoteDTO) {
        String mensagem = pacoteService.atualizar(id, pacoteDTO);
        return ResponseEntity.ok(mensagem);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarPacote(@PathVariable Integer id) {
        String mensagem = pacoteService.deletar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(mensagem);
    }
}
