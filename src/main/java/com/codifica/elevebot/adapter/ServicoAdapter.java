package com.codifica.elevebot.adapter;

import com.codifica.elevebot.dto.*;
import com.codifica.elevebot.model.*;
import org.springframework.stereotype.Component;

@Component
public class ServicoAdapter {

    public ServicoDTO toDTO(Servico servico, Double valor) {
        ServicoDTO dto = new ServicoDTO();
        dto.setId(servico.getId());
        dto.setNome(servico.getNome());
        dto.setValor(valor);
        return dto;
    }
}