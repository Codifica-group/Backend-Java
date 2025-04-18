package com.codifica.elevebot.controller;

import com.codifica.elevebot.model.Pet;
import com.codifica.elevebot.dto.PetDTO;
import com.codifica.elevebot.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/pets")
public class PetController {

    @Autowired
    private PetService petService;

    @PostMapping
    public ResponseEntity<String> cadastrarPet(@RequestBody PetDTO petDTO) {
        String mensagem = petService.cadastrar(petDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(mensagem);
    }

    @GetMapping
    public ResponseEntity<List<Pet>> listarPets() {
        List<Pet> pets = petService.listar();
        return pets.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(pets);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pet> buscarPetPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(petService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> atualizarPet(@PathVariable Integer id, @RequestBody PetDTO petDTO) {
        String mensagem = petService.atualizar(id, petDTO);
        return ResponseEntity.ok(mensagem);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarPet(@PathVariable Integer id) {
        String mensagem = petService.deletar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(mensagem);
    }
}
