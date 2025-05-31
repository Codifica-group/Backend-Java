package com.codifica.elevebot.controller;

import com.codifica.elevebot.dto.AgendaDTO;
import com.codifica.elevebot.dto.SugestaoDTO;
import com.codifica.elevebot.model.Filtro;
import com.codifica.elevebot.model.Total;
import com.codifica.elevebot.service.AgendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agendas")
public class AgendaController {

    @Autowired
    private AgendaService agendaService;

    @PostMapping
    public ResponseEntity<Object> cadastrarAgenda(@RequestBody AgendaDTO agendaDTO) {
        Object json = agendaService.cadastrar(agendaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(json);
    }

    @GetMapping
    public ResponseEntity<List<AgendaDTO>> listarAgendas() {
        List<AgendaDTO> agendas = agendaService.listar();
        return agendas.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(agendas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgendaDTO> buscarAgendaPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(agendaService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> atualizarAgenda(@PathVariable Integer id, @RequestBody AgendaDTO agendaDTO) {
        String mensagem = agendaService.atualizar(id, agendaDTO);
        return ResponseEntity.ok(mensagem);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletarAgenda(@PathVariable Integer id) {
        agendaService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/filtrar")
    public ResponseEntity<List<AgendaDTO>> filtrarAgendas(@RequestBody Filtro filtro) {
        List<AgendaDTO> agendas = agendaService.filtrar(filtro);
        return agendas.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(agendas);
    }

    @PostMapping("/calcular/lucro")
    public ResponseEntity<Total> calcularLucro(@RequestBody Total total){
        return ResponseEntity.ok(agendaService.calcularLucro(total));
    }

    @PostMapping("/calcular/servico")
    public ResponseEntity<SugestaoDTO> calcularServico(@RequestBody AgendaDTO agendaDTO) {
        return ResponseEntity.ok(agendaService.calcularServico(agendaDTO));
    }
}
