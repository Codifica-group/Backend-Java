package com.codifica.elevebot.controller;

import com.codifica.elevebot.model.Pet;
import com.codifica.elevebot.dto.PetDTO;
import com.codifica.elevebot.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pets")
public class PetController {

    @Autowired
    private PetService petService;

    @PostMapping
    public ResponseEntity<Object> cadastrarPet(@RequestBody PetDTO petDTO) {
        Object json = petService.cadastrar(petDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(json);
    }

    @GetMapping
    public ResponseEntity<List<PetDTO>> listarPets() {
        List<PetDTO> pets = petService.listar();
        return pets.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(pets);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PetDTO> buscarPetPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(petService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> atualizarPet(@PathVariable Integer id, @RequestBody PetDTO petDTO) {
        String mensagem = petService.atualizar(id, petDTO);
        return ResponseEntity.ok(mensagem);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletarPet(@PathVariable Integer id) {
        petService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
