package com.codifica.elevebot.adapter;

import com.codifica.elevebot.dto.PacoteDTO;
import com.codifica.elevebot.model.Cliente;
import com.codifica.elevebot.model.Pacote;
import org.springframework.stereotype.Component;

@Component
public class PacoteAdapter {

    public Pacote toEntity(PacoteDTO dto, Cliente cliente) {
        Pacote pacote = new Pacote();
        pacote.setCliente(cliente);
        pacote.setPacoteId(dto.getPacoteId());
        return pacote;
    }

    public PacoteDTO toDTO(Pacote pacote) {
        PacoteDTO dto = new PacoteDTO();
        dto.setId(pacote.getId());
        dto.setClienteId(pacote.getCliente().getId());
        dto.setPacoteId(pacote.getPacoteId());
        dto.setDataExpiracao(pacote.getDataExpiracao());
        return dto;
    }
}