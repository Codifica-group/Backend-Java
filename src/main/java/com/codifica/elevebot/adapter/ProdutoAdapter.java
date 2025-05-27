package com.codifica.elevebot.adapter;

import com.codifica.elevebot.dto.ProdutoDTO;
import com.codifica.elevebot.model.CategoriaProduto;
import com.codifica.elevebot.model.Produto;
import com.codifica.elevebot.repository.CategoriaProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProdutoAdapter {

    @Autowired
    private CategoriaProdutoRepository categoriaProdutoRepository;

    public Produto toEntity(ProdutoDTO produtoDTO) {
        Produto produto = new Produto();
        produto.setNome(produtoDTO.getNome());

        CategoriaProduto categoriaProduto = categoriaProdutoRepository.findById(produtoDTO.getCategoriaId())
                .orElseThrow(() -> new IllegalArgumentException("Categoria inválida para o ID: " + produtoDTO.getCategoriaId()));
        produto.setCategoriaProduto(categoriaProduto);

        return produto;
    }

    public ProdutoDTO toDTO(Produto produto) {
        ProdutoDTO produtoDTO = new ProdutoDTO();
        produtoDTO.setId(produto.getId());
        produtoDTO.setCategoriaId(produto.getCategoriaProduto().getId());
        produtoDTO.setNome(produto.getNome());
        return produtoDTO;
    }
}