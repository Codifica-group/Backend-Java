package com.codifica.elevebot.adapter;

import com.codifica.elevebot.dto.DespesaDTO;
import com.codifica.elevebot.dto.ProdutoDTO;
import com.codifica.elevebot.model.Despesa;
import com.codifica.elevebot.model.Produto;
import org.springframework.stereotype.Component;

@Component
public class DespesaAdapter {

    public Despesa toEntity(DespesaDTO despesaDTO, Produto produto) {
        Despesa despesa = new Despesa();
        despesa.setProduto(produto);
        despesa.setValor(despesaDTO.getValor());
        despesa.setData(despesaDTO.getData());
        return despesa;
    }

    public DespesaDTO toDTO(Despesa despesa) {
        DespesaDTO dto = new DespesaDTO();
        dto.setId(despesa.getId());
        dto.setValor(despesa.getValor());
        dto.setData(despesa.getData());

        ProdutoDTO produtoDTO = new ProdutoDTO();
        produtoDTO.setId(despesa.getProduto().getId());
        produtoDTO.setNome(despesa.getProduto().getNome());

        dto.setProduto(produtoDTO);
        return dto;
    }
}