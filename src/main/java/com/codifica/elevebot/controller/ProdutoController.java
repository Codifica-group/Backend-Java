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
    public ResponseEntity<String> cadastrarProduto(@RequestBody List<ProdutoDTO> produtosDTO) {
        String mensagem = produtoService.cadastrar(produtosDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(mensagem);
    }

    @GetMapping
    public ResponseEntity<Map<String, List<ProdutoDTO>>> listarProdutos() {
        Map<String, List<ProdutoDTO>> produtos = produtoService.listar();
        return produtos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(produtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> atualizarProduto(@PathVariable Integer id, @RequestBody ProdutoDTO produtoDTO) {
        String mensagem = produtoService.atualizar(id, produtoDTO);
        return ResponseEntity.ok(mensagem);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarProduto(@PathVariable Integer id) {
        String mensagem = produtoService.deletar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(mensagem);
    }
}
