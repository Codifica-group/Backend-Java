package com.codifica.elevebot.controller;

import com.codifica.elevebot.dto.ProdutoDTO;
import com.codifica.elevebot.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @PostMapping
    public ResponseEntity<String> cadastrarProduto(@RequestBody ProdutoDTO produtoDTO) {
        String mensagem = produtoService.cadastrar(produtoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(mensagem);
    }

    @GetMapping
    public ResponseEntity<Map<String, List<ProdutoDTO>>> listarProdutos() {
        Map<String, List<ProdutoDTO>> produtos = produtoService.listar();
        return produtos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(produtos);
    }
}
