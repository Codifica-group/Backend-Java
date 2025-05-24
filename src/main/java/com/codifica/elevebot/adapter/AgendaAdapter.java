package com.codifica.elevebot.adapter;

import com.codifica.elevebot.dto.AgendaDTO;
import com.codifica.elevebot.dto.ClienteDTO;
import com.codifica.elevebot.dto.PetDTO;
import com.codifica.elevebot.dto.RacaDTO;
import com.codifica.elevebot.model.Agenda;
import com.codifica.elevebot.model.Cliente;
import com.codifica.elevebot.model.Pet;
import com.codifica.elevebot.model.Servico;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AgendaAdapter {

    public Agenda toEntity(AgendaDTO agendaDTO, Pet pet) {
        Agenda agenda = new Agenda();
        agenda.setId(agendaDTO.getId());
        agenda.setPet(pet);
        agenda.setDataHoraInicio(agendaDTO.getDataHoraInicio());
        agenda.setDataHoraFim(agendaDTO.getDataHoraFim());
        agenda.setValor(agendaDTO.getValor());
        return agenda;
    }

    public AgendaDTO toDTO(Agenda agenda, List<Servico> servicos) {
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
        agendaDTO.setDataHoraInicio(agenda.getDataHoraInicio());
        agendaDTO.setDataHoraFim(agenda.getDataHoraFim());
        agendaDTO.setValor(agenda.getValor());
        return agendaDTO;
    }
}