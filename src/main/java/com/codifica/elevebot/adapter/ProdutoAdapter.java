package com.codifica.elevebot.adapter;

import com.codifica.elevebot.dto.ProdutoDTO;
import com.codifica.elevebot.model.Produto;
import org.springframework.stereotype.Component;

@Component
public class ProdutoAdapter {

    public Produto toEntity(ProdutoDTO produtoDTO) {
        Produto produto = new Produto();
        produto.setCategoriaProduto(produtoDTO.getCategoria());
        produto.setNome(produtoDTO.getNome());
        return produto;
    }

    public  ProdutoDTO toDTO(Produto produto) {
        ProdutoDTO produtoDTO = new ProdutoDTO();
        produtoDTO.setId(produto.getId());
        produtoDTO.setCategoriaId(produto.getCategoriaProduto().getCode());
        produtoDTO.setNome(produto.getNome());
        return produtoDTO;
    }
}
