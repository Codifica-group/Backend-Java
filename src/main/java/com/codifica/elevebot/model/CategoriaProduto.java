package com.codifica.elevebot.model;

public enum CategoriaProduto {
    GASTOS_FIXOS("Gastos Fixos"),
    MANUTENCAO("Manutenção"),
    INSUMOS("Insumos"),
    PRODUTO("Produto");

    private String nome;

    CategoriaProduto(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
}
