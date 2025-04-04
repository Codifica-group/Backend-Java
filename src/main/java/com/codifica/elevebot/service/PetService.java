package com.codifica.elevebot.service;

import com.codifica.elevebot.dto.PetDTO;
import com.codifica.elevebot.exception.ConflictException;
import com.codifica.elevebot.exception.NotFoundException;
import com.codifica.elevebot.model.Cliente;
import com.codifica.elevebot.model.Pet;
import com.codifica.elevebot.repository.ClienteRepository;
import com.codifica.elevebot.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetService {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    public String cadastrar(PetDTO petDTO) {
        Cliente cliente = clienteRepository.findById(petDTO.getIdCliente())
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado."));

        Pet pet = new Pet(petDTO.getIdRaca(), petDTO.getNome(), cliente);

        if (petExiste(pet)) {
            throw new ConflictException("Pet já cadastrado.");
        }

        petRepository.save(pet);
        return "Pet cadastrado com sucesso.";
    }

    public List<Pet> listar() {
        return petRepository.findAll();
    }

    public Pet buscarPorId(Integer id) {
        return petRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pet não encontrado."));
    }

    public String atualizar(Integer id, PetDTO petDTO) {
        Pet petExistente = petRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pet não encontrado."));

        Cliente cliente = clienteRepository.findById(petDTO.getIdCliente())
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado."));

        petExistente.setIdRaca(petDTO.getIdRaca());
        petExistente.setNome(petDTO.getNome());
        petExistente.setCliente(cliente);

        petRepository.save(petExistente);
        return "Pet atualizado com sucesso!";
    }

    public String deletar(Integer id) {
        if (!petRepository.existsById(id)) {
            throw new NotFoundException("Pet não encontrado.");
        }

        petRepository.deleteById(id);
        return "Pet deletado com sucesso.";
    }

    private boolean petExiste(Pet pet) {
        Pet petFiltro = new Pet(pet.getIdRaca(), pet.getNome(), pet.getCliente());

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withIgnorePaths("id")
                .withMatcher("nome", ExampleMatcher.GenericPropertyMatchers.ignoreCase());

        Example<Pet> example = Example.of(petFiltro, matcher);
        return petRepository.exists(example);
    }
}