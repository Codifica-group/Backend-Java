package com.codifica.elevebot.adapter;

import com.codifica.elevebot.dto.ClienteDTO;
import com.codifica.elevebot.dto.PetDTO;
import com.codifica.elevebot.dto.RacaDTO;
import com.codifica.elevebot.model.Cliente;
import com.codifica.elevebot.model.Pet;
import com.codifica.elevebot.model.Raca;
import org.springframework.stereotype.Component;

@Component
public class PetAdapter {

    public Pet toEntity(PetDTO dto, Cliente cliente, Raca raca) {
        Pet pet = new Pet();
        pet.setRaca(raca);
        pet.setNome(dto.getNome());
        pet.setCliente(cliente);
        return pet;
    }

    public PetDTO toDTO(Pet pet) {
        PetDTO dto = new PetDTO();
        dto.setId(pet.getId());
        dto.setNome(pet.getNome());

        RacaDTO racaDTO = new RacaDTO();
        racaDTO.setId(pet.getRaca().getId());
        racaDTO.setNome(pet.getRaca().getNome());
        dto.setRaca(racaDTO);

        if (pet.getCliente() != null) {
            ClienteDTO clienteDTO = new ClienteDTO();
            clienteDTO.setId(pet.getCliente().getId());
            clienteDTO.setNome(pet.getCliente().getNome());
            dto.setCliente(clienteDTO);
        }
        return dto;
    }
}