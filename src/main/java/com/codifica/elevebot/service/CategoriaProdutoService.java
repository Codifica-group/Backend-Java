package com.codifica.elevebot.service;

import com.codifica.elevebot.exception.ConflictException;
import com.codifica.elevebot.exception.NotFoundException;
import com.codifica.elevebot.model.CategoriaProduto;
import com.codifica.elevebot.repository.CategoriaProdutoRepository;
import com.codifica.elevebot.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoriaProdutoService {

    @Autowired
    private CategoriaProdutoRepository categoriaProdutoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    public Object cadastrar(CategoriaProduto categoriaProduto) {
        if (categoriaProdutoRepository.existsByNome(categoriaProduto.getNome())) {
            throw new ConflictException("Não é possível cadastrar duas categorias iguais.");
        }

        categoriaProdutoRepository.save(categoriaProduto);

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("mensagem", "Categoria cadastrado com sucesso.");
        resposta.put("id", categoriaProduto.getId());
        return resposta;
    }

    public List<CategoriaProduto> listar() {
        return categoriaProdutoRepository.findAll();
    }

    public Object atualizar(Integer id, CategoriaProduto novacategoriaProduto) {
        CategoriaProduto categoriaProduto = categoriaProdutoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Categoria não encontrada."));

        if (categoriaProdutoRepository.existsByNome(novacategoriaProduto.getNome())) {
            throw new ConflictException("Uma categoria com este nome já existe.");
        }

        categoriaProduto.setNome(novacategoriaProduto.getNome());
        categoriaProdutoRepository.save(categoriaProduto);

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("mensagem", "Categoria atualizada com sucesso.");
        resposta.put("id", categoriaProduto.getId());
        return resposta;
    }

    public void deletar(Integer id) {
        CategoriaProduto categoriaProduto = categoriaProdutoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Categoria não encontrada."));

        if (produtoRepository.existsByCategoriaProduto(categoriaProduto)) {
            throw new ConflictException("Não é possível deletar categorias que possuem produtos cadastrados.");
        }

        categoriaProdutoRepository.deleteById(id);
    }
}