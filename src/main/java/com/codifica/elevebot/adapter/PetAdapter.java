package com.codifica.elevebot.adapter;

import com.codifica.elevebot.dto.PetDTO;
import com.codifica.elevebot.model.Cliente;
import com.codifica.elevebot.model.Pet;
import com.codifica.elevebot.model.Raca;

public class PetAdapter {

    public static Pet toEntity(PetDTO dto, Cliente cliente, Raca raca) {
        Pet pet = new Pet();
        pet.setRaca(raca);
        pet.setNome(dto.getNome());
        pet.setCliente(cliente);
        return pet;
    }

    public static PetDTO toDTO(Pet pet) {
        PetDTO dto = new PetDTO();
        dto.setRacaId(pet.getRaca().getId());
        dto.setNome(pet.getNome());
        if (pet.getCliente() != null) {
            dto.setClienteId(pet.getCliente().getId());
        }
        return dto;
    }
}