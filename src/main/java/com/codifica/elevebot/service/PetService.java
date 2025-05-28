package com.codifica.elevebot.service;

import com.codifica.elevebot.adapter.PetAdapter;
import com.codifica.elevebot.dto.PetDTO;
import com.codifica.elevebot.exception.ConflictException;
import com.codifica.elevebot.exception.NotFoundException;
import com.codifica.elevebot.model.Cliente;
import com.codifica.elevebot.model.Pet;
import com.codifica.elevebot.model.Raca;
import com.codifica.elevebot.repository.AgendaRepository;
import com.codifica.elevebot.repository.ClienteRepository;
import com.codifica.elevebot.repository.PetRepository;
import com.codifica.elevebot.repository.RacaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class PetService {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private RacaRepository racaRepository;

    @Autowired
    private PetAdapter petAdapter;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private AgendaRepository agendaRepository;

    public Object cadastrar(PetDTO petDTO) {
        Cliente cliente = clienteRepository.findById(petDTO.getClienteId())
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado."));

        Raca raca = racaRepository.findById(petDTO.getRacaId())
                .orElseThrow(() -> new NotFoundException("Raça não encontrada."));


        Pet pet = petAdapter.toEntity(petDTO, cliente, raca);

        if (petExiste(pet)) {
            throw new ConflictException("Pet já cadastrado.");
        }

        petRepository.save(pet);

        var resposta = new HashMap<String, Object>();
        resposta.put("mensagem", "Pet cadastrado com sucesso.");
        resposta.put("id", pet.getId());
        return resposta;
    }

    public List<PetDTO> listar() {
        List<Pet> pets = petRepository.findAll();

        return pets.stream()
                .map(pet -> petAdapter.toDTO(pet))
                .toList();
    }

    public PetDTO buscarPorId(Integer id) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pet não encontrado."));

        return petAdapter.toDTO(pet);
    }

    public String atualizar(Integer id, PetDTO petDTO) {
        Pet petExistente = petRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pet não encontrado."));

        Cliente cliente = clienteRepository.findById(petDTO.getClienteId())
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado."));

        Raca raca = racaRepository.findById(petDTO.getRacaId())
                .orElseThrow(() -> new NotFoundException("Raça não encontrada."));

        petExistente.setRaca(raca);
        petExistente.setNome(petDTO.getNome());
        petExistente.setCliente(cliente);

        petRepository.save(petExistente);
        return "Pet atualizado com sucesso!";
    }

    public void deletar(Integer id) {
        if (!petRepository.existsById(id)) {
            throw new NotFoundException("Pet não encontrado.");
        }

        if (agendaRepository.existsByPetId(id)) {
            throw new ConflictException("Não é possível deletar pets que possuem agendas associadas.");
        }

        petRepository.deleteById(id);
    }

    private boolean petExiste(Pet pet) {
        Pet petFiltro = new Pet(pet.getRaca(), pet.getNome(), pet.getCliente());

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withIgnorePaths("id")
                .withMatcher("nome", ExampleMatcher.GenericPropertyMatchers.ignoreCase());

        Example<Pet> example = Example.of(petFiltro, matcher);
        return petRepository.exists(example);
    }
}