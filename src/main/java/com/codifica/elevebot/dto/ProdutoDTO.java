package com.codifica.elevebot.dto;

import com.codifica.elevebot.model.CategoriaProduto;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ProdutoDTO {

    private Integer id = 0;
    private Integer idCategoria;
    private String nome;

    public ProdutoDTO() {}

    public ProdutoDTO(Integer idCategoria, String nome) {
        this.idCategoria = idCategoria;
        this.nome = nome;
    }

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

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public CategoriaProduto getCategoria() {
        return CategoriaProduto.fromCode(idCategoria);
    }
}
