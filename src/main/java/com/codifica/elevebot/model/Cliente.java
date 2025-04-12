package com.codifica.elevebot.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name= "cliente")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;
    private String numeroCelular;
    private String cep;
    private Integer numeroEndereco;
    private String complemento;

    @OneToMany(mappedBy = "cliente",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Pet> pets = new ArrayList<>();

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<Pacote> pacotes;

    public Cliente() {}

    public Cliente(String nome, String numeroCelular, String cep, Integer numeroEndereco, String complemento) {
        this.nome = nome;
        this.numeroCelular = numeroCelular;
        this.cep = cep;
        this.numeroEndereco = numeroEndereco;
        this.complemento = complemento;
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

    public String getNumeroCelular() {
        return numeroCelular;
    }

    public void setNumeroCelular(String numeroCelular) {
        this.numeroCelular = numeroCelular;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public Integer getNumeroEndereco() {
        return numeroEndereco;
    }

    public void setNumeroEndereco(Integer numeroEndereco) {
        this.numeroEndereco = numeroEndereco;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public List<Pet> getPets() {
        return pets;
    }

    public void setPets(List<Pet> pets) {
        this.pets = pets;
    }

    public List<Pacote> getPacotes() {
        return pacotes;
    }

    public void setPacotes(List<Pacote> pacotes) {
        this.pacotes = pacotes;
    }
}
