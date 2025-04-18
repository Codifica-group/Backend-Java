package com.codifica.elevebot.adapter;

import com.codifica.elevebot.dto.ProdutoDTO;
import com.codifica.elevebot.model.Produto;

public class ProdutoAdapter {

    public static Produto toEntity(ProdutoDTO produtoDTO) {
        Produto produto = new Produto();
        produto.setCategoriaProduto(produtoDTO.getCategoria());
        produto.setNome(produtoDTO.getNome());
        return produto;
    }

    public static  ProdutoDTO toDTO(Produto produto) {
        ProdutoDTO produtoDTO = new ProdutoDTO();
        produtoDTO.setId(produto.getId());
        produtoDTO.setIdCategoria(produto.getCategoriaProduto().getCode());
        produtoDTO.setNome(produto.getNome());
        return produtoDTO;
    }
}
