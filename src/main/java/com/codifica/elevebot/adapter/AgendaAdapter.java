package com.codifica.elevebot.adapter;

import com.codifica.elevebot.dto.*;
import com.codifica.elevebot.model.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AgendaAdapter {

    public Agenda toEntity(AgendaDTO agendaDTO, Pet pet) {
        Agenda agenda = new Agenda();
        agenda.setId(agendaDTO.getId());
        agenda.setPet(pet);
        agenda.setValorDeslocamento(agendaDTO.getValorDeslocamento());
        agenda.setDataHoraInicio(agendaDTO.getDataHoraInicio());
        agenda.setDataHoraFim(agendaDTO.getDataHoraFim());
        return agenda;
    }

    public AgendaDTO toDTO(Agenda agenda, List<ServicoDTO> servicos) {
        Pet pet = agenda.getPet();
        RacaDTO racaDTO = new RacaDTO();
        racaDTO.setId(pet.getRaca().getId());
        racaDTO.setNome(pet.getRaca().getNome());
        PetDTO petDTO = new PetDTO();
        petDTO.setId(pet.getId());
        petDTO.setNome(pet.getNome());
        petDTO.setRaca(racaDTO);

        Cliente cliente = pet.getCliente();
        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setId(cliente.getId());
        clienteDTO.setNome(cliente.getNome());

        AgendaDTO agendaDTO = new AgendaDTO();
        agendaDTO.setId(agenda.getId());
        agendaDTO.setPet(petDTO);
        agendaDTO.setCliente(clienteDTO);
        agendaDTO.setServicos(servicos);
        agendaDTO.setValorDeslocamento(agenda.getValorDeslocamento());
        agendaDTO.setDataHoraInicio(agenda.getDataHoraInicio());
        agendaDTO.setDataHoraFim(agenda.getDataHoraFim());

        Double valorTotal = servicos.stream()
                .mapToDouble(ServicoDTO::getValor)
                .sum();
        agendaDTO.setValorTotal(valorTotal + agenda.getValorDeslocamento());
        return agendaDTO;
    }
}