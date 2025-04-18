package com.codifica.elevebot.dto;

import com.codifica.elevebot.model.CategoriaProduto;

public class ProdutoDTO {

    private Integer id = 0;
    private Integer idCategoria;
    private String nome;

    public ProdutoDTO() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public CategoriaProduto getCategoria() {
        return CategoriaProduto.values()[idCategoria];
    }
}
