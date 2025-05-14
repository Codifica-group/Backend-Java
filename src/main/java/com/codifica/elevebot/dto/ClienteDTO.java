package com.codifica.elevebot.dto;

import com.codifica.elevebot.model.Pacote;

import java.util.List;

public class ClienteDTO {
    private Integer id;
    private String nome;
    private String numCelular;
    private String cep;
    private Integer numEndereco;
    private String complemento;
    private List<PetDTO> pets;
    private List<PacoteDTO> pacotes;

    public ClienteDTO() {}

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

    public String getNumCelular() {
        return numCelular;
    }

    public void setNumCelular(String numCelular) {
        this.numCelular = numCelular;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public Integer getNumEndereco() {
        return numEndereco;
    }

    public void setNumEndereco(Integer numEndereco) {
        this.numEndereco = numEndereco;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public List<PetDTO> getPets() {
        return pets;
    }

    public void setPets(List<PetDTO> pets) {
        this.pets = pets;
    }

    public List<PacoteDTO> getPacotes() {
        return pacotes;
    }

    public void setPacotes(List<PacoteDTO> pacotes) {
        this.pacotes = pacotes;
    }
}