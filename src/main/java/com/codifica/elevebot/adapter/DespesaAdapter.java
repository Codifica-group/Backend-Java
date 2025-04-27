package com.codifica.elevebot.adapter;

import com.codifica.elevebot.dto.DespesaDTO;
import com.codifica.elevebot.model.Despesa;
import com.codifica.elevebot.model.Produto;

public class DespesaAdapter {

    public static Despesa toEntity(DespesaDTO despesaDTO, Produto produto) {
        Despesa despesa = new Despesa();
        despesa.setProduto(produto);
        despesa.setValor(despesaDTO.getValor());
        despesa.setData(despesaDTO.getData());
        return despesa;
    }

    public static DespesaDTO toDTO(Despesa despesa) {
        DespesaDTO dto = new DespesaDTO();
        dto.setId(despesa.getId());
        dto.setProdutoId(despesa.getProduto().getId());
        dto.setValor(despesa.getValor());
        dto.setData(despesa.getData());
        return dto;
    }
}