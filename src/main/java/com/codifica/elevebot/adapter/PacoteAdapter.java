package com.codifica.elevebot.adapter;

import com.codifica.elevebot.dto.PacoteDTO;
import com.codifica.elevebot.model.Cliente;
import com.codifica.elevebot.model.Pacote;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

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
        dto.setDataInicio(pacote.getDataInicio());
        dto.setDataExpiracao(pacote.getDataExpiracao());

        LocalDate hoje = LocalDate.now();
        if (hoje.isBefore(pacote.getDataInicio())) {
            dto.setStatus("Espera");
        } else if (!pacote.getDataExpiracao().isAfter(hoje)) {
            dto.setStatus("Expirado");
        } else {
            dto.setStatus("Ativo");
        }
        return dto;
    }
}