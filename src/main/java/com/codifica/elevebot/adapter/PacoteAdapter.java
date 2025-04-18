package com.codifica.elevebot.adapter;

import com.codifica.elevebot.dto.PacoteDTO;
import com.codifica.elevebot.model.Cliente;
import com.codifica.elevebot.model.Pacote;

public class PacoteAdapter {

    public static Pacote toEntity(PacoteDTO dto, Cliente cliente) {
        Pacote pacote = new Pacote();
        pacote.setCliente(cliente);
        pacote.setIdPacote(dto.getIdPacote());
        return pacote;
    }

    public static PacoteDTO toDTO(Pacote pacote) {
        PacoteDTO dto = new PacoteDTO();
        dto.setId(pacote.getId());
        dto.setIdCliente(pacote.getCliente().getId());
        dto.setIdPacote(pacote.getIdPacote());
        dto.setDataExpiracao(pacote.getDataExpiracao());
        return dto;
    }
}