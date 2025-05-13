package com.codifica.elevebot.model;

import jakarta.persistence.*;

@Entity
@Table(name="servico")
public class Servico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nome;
    private Double valorBase;

    public Servico() {}

    public Servico(String nome, Double valorBase) {
        this.nome = nome;
        this.valorBase = valorBase;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getValorBase() {
        return valorBase;
    }

    public void setValorBase(Double valorBase) {
        this.valorBase = valorBase;
    }
}
