package com.codifica.elevebot.adapter;

import com.codifica.elevebot.dto.PacoteDTO;
import com.codifica.elevebot.dto.PacoteHistoricoDTO;
import com.codifica.elevebot.model.Cliente;
import com.codifica.elevebot.model.Pacote;

public class PacoteAdapter {

    public static Pacote toEntity(PacoteDTO dto, Cliente cliente) {
        Pacote pacote = new Pacote();
        pacote.setCliente(cliente);
        pacote.setPacoteId(dto.getIdPacote());
        return pacote;
    }

    public static PacoteHistoricoDTO toHistoricoDTO(Pacote pacote) {
        PacoteHistoricoDTO dto = new PacoteHistoricoDTO();
        dto.setId(pacote.getId());
        dto.setIdCliente(pacote.getCliente().getId());
        dto.setIdPacote(pacote.getPacoteId());
        dto.setDataExpiracao(pacote.getDataExpiracao());
        return dto;
    }
}