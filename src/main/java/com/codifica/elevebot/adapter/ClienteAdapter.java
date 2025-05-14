package com.codifica.elevebot.adapter;

import com.codifica.elevebot.dto.ClienteDTO;
import com.codifica.elevebot.dto.PacoteDTO;
import com.codifica.elevebot.model.Cliente;
import com.codifica.elevebot.model.Pacote;
import com.codifica.elevebot.model.Pet;
import com.codifica.elevebot.repository.PacoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class ClienteAdapter {

    @Autowired
    private PetAdapter petAdapter;

    @Autowired
    private PacoteAdapter pacoteAdapter;

    @Autowired
    private PacoteRepository pacoteRepository;

    public Cliente toEntity(ClienteDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setNome(dto.getNome());
        cliente.setNumCelular(dto.getNumCelular());
        cliente.setCep(dto.getCep());
        cliente.setNumEndereco(dto.getNumEndereco());
        cliente.setComplemento(dto.getComplemento());
        return cliente;
    }

    public ClienteDTO toDTO(Cliente entity) {
        ClienteDTO dto = new ClienteDTO();
        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        dto.setNumCelular(entity.getNumCelular());
        dto.setCep(entity.getCep());
        dto.setNumEndereco(entity.getNumEndereco());
        dto.setComplemento(entity.getComplemento());

        Pacote pacoteAtivo = pacoteRepository.findActivePacoteByCliente(entity.getId(), LocalDate.now());
        if (pacoteAtivo != null) {
            dto.setPacotes(List.of(pacoteAdapter.toDTO(pacoteAtivo)));
        } else {
            dto.setPacotes(List.of());
        }

        List<Pet> pets = entity.getPets();
        dto.setPets(pets.stream().map(pet -> petAdapter.toDTO(pet)).toList());
        return dto;
    }
}
