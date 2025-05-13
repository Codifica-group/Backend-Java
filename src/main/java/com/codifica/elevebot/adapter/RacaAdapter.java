package com.codifica.elevebot.adapter;

import com.codifica.elevebot.dto.RacaDTO;
import com.codifica.elevebot.model.Porte;
import com.codifica.elevebot.model.Raca;

public class RacaAdapter {

    public static Raca toEntity(RacaDTO dto, Porte porte) {
        Raca raca = new Raca();
        raca.setId(dto.getId());
        raca.setNome(dto.getNome());
        raca.setPorte(porte);
        return raca;
    }

    public static RacaDTO toDTO(Raca raca) {
        RacaDTO dto = new RacaDTO();
        dto.setId(raca.getId());
        dto.setNome(raca.getNome());
        dto.setPorteId(raca.getPorte() != null ? raca.getPorte().getId() : null);
        dto.setPorteNome(raca.getPorte() != null ? raca.getPorte().getNome() : null);
        return dto;
    }
}