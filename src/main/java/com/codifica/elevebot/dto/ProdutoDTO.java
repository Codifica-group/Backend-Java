package com.codifica.elevebot.dto;

import com.codifica.elevebot.model.CategoriaProduto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProdutoDTO {

    private Integer id = 0;
    private Integer categoriaId;
    private String nome;

    public ProdutoDTO() {}

    public ProdutoDTO(Integer categoriaId, String nome) {
        this.categoriaId = categoriaId;
        this.nome = nome;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Integer categoriaId) {
        this.categoriaId = categoriaId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public CategoriaProduto getCategoria() {
        return CategoriaProduto.fromCode(categoriaId);
    }
}
