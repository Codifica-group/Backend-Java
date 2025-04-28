package com.codifica.elevebot.controller;

import com.codifica.elevebot.dto.CepDTO;
import com.codifica.elevebot.service.CepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cep")
public class CepController {

    @Autowired
    private CepService cepService;

    @GetMapping("/{cep}")
    public ResponseEntity<CepDTO> buscarCep(@PathVariable String cep) {
        CepDTO endereco = cepService.buscarCep(cep);
        return ResponseEntity.ok(endereco);
    }
}