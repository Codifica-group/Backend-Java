package com.codifica.elevebot.adapter;

import com.codifica.elevebot.dto.AgendaDTO;
import com.codifica.elevebot.model.Agenda;
import com.codifica.elevebot.model.Pet;
import com.codifica.elevebot.model.Servico;

import java.util.List;
import java.util.stream.Collectors;

public class AgendaAdapter {

    public static Agenda toEntity(AgendaDTO agendaDTO, Pet pet) {
        Agenda agenda = new Agenda();
        agenda.setId(agendaDTO.getId());
        agenda.setPet(pet);
        agenda.setDataHoraInicio(agendaDTO.getDataHoraInicio());
        agenda.setDataHoraFim(agendaDTO.getDataHoraFim());
        agenda.setValor(agendaDTO.getValor());
        return agenda;
    }

    public static AgendaDTO toDTO(Agenda agenda, List<Servico> servicos) {
        AgendaDTO agendaDTO = new AgendaDTO();
        agendaDTO.setId(agenda.getId());
        agendaDTO.setPetId(agenda.getPet().getId());
        agendaDTO.setDataHoraInicio(agenda.getDataHoraInicio());
        agendaDTO.setDataHoraFim(agenda.getDataHoraFim());
        agendaDTO.setValor(agenda.getValor());

        List<Integer> servicoIds = servicos.stream()
                .map(Servico::getId)
                .collect(Collectors.toList());
        agendaDTO.setServicos(servicoIds);

        return agendaDTO;
    }
}