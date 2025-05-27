package com.codifica.elevebot.controller;

import com.codifica.elevebot.model.CategoriaProduto;
import com.codifica.elevebot.service.CategoriaProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaProdutoController {

    @Autowired
    private CategoriaProdutoService categoriaProdutoService;

    @PostMapping
    public ResponseEntity<Object> cadastrarCategoria(@RequestBody CategoriaProduto categoriaProduto) {
        Object json = categoriaProdutoService.cadastrar(categoriaProduto);
        return ResponseEntity.status(HttpStatus.CREATED).body(json);
    }

    @GetMapping
    public ResponseEntity<List<CategoriaProduto>> listarCategorias() {
        List<CategoriaProduto> categorias = categoriaProdutoService.listar();
        return categorias.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(categorias);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizarCategoria(@PathVariable Integer id, @RequestBody CategoriaProduto categoriaProduto) {
        Object json = categoriaProdutoService.atualizar(id, categoriaProduto);
        return ResponseEntity.ok(json);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarCategoria(@PathVariable Integer id) {
        categoriaProdutoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}