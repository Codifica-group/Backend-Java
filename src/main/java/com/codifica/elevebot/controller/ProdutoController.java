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
    public ResponseEntity<Object> cadastrarProduto(@RequestBody List<ProdutoDTO> produtosDTO) {
        Object json = produtoService.cadastrar(produtosDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(json);
    }

    @GetMapping
    public ResponseEntity<Map<String, List<ProdutoDTO>>> listarProdutos() {
        Map<String, List<ProdutoDTO>> produtos = produtoService.listar();
        return produtos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(produtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizarProduto(@PathVariable Integer id, @RequestBody ProdutoDTO produtoDTO) {
        Object json = produtoService.atualizar(id, produtoDTO);
        return ResponseEntity.ok(json);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletarProduto(@PathVariable Integer id) {
        produtoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
