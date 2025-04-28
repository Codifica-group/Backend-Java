package com.codifica.elevebot.controller;

import com.codifica.elevebot.dto.PetDTO;
import com.codifica.elevebot.exception.ConflictException;
import com.codifica.elevebot.exception.NotFoundException;
import com.codifica.elevebot.model.Cliente;
import com.codifica.elevebot.model.Pet;
import com.codifica.elevebot.service.PetService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PetController.class)
@AutoConfigureMockMvc(addFilters = false)
@SuppressWarnings("removal")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PetControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PetService service;

    private static Cliente clientePadrao;
    private static PetDTO petDTOPadrao;
    private static Pet petPadrao;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static String asJson(Object obj) {
        try {
            return MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeAll
    static void setUp() {
        clientePadrao = new Cliente();
        clientePadrao.setId(1);
        clientePadrao.setNome("Mariana Souza");
        clientePadrao.setNumCelular("11912345678");
        clientePadrao.setCep("12345678");
        clientePadrao.setNumEndereco(1);
        clientePadrao.setComplemento("");

        petDTOPadrao = new PetDTO();
        petDTOPadrao.setRacaId(1);
        petDTOPadrao.setNome("Thor");
        petDTOPadrao.setClienteId(1);

        petPadrao = new Pet();
        petPadrao.setId(1);
        petPadrao.setNome("Thor");
        petPadrao.setRacaId(1);
        petPadrao.setCliente(clientePadrao);
    }

    /* ---------- POST ---------- */
    @Test @Order(1)
    void deveCadastrarPet() throws Exception {
        Mockito.when(service.cadastrar(any(PetDTO.class)))
                .thenReturn("Pet cadastrado com sucesso.");

        mvc.perform(post("/api/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(petDTOPadrao)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Pet cadastrado com sucesso."));
    }

    @Test @Order(2)
    void deveFalharCadastro_PetJaExiste() throws Exception {
        Mockito.when(service.cadastrar(any(PetDTO.class)))
                .thenThrow(new ConflictException("Pet já cadastrado."));

        mvc.perform(post("/api/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(petDTOPadrao)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("CONFLICT"))
                .andExpect(jsonPath("$.message").value("Pet já cadastrado."));
    }

    /* ---------- GET ALL ---------- */
    @Test @Order(3)
    void deveListarPets() throws Exception {
        Mockito.when(service.listar()).thenReturn(List.of(petPadrao));

        mvc.perform(get("/api/pets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Thor"))
                .andExpect(jsonPath("$[0].racaId").value(1));
    }

    @Test @Order(4)
    void deveRetornar204_ListaVazia() throws Exception {
        Mockito.when(service.listar()).thenReturn(List.of());

        mvc.perform(get("/api/pets"))
                .andExpect(status().isNoContent());
    }

    /* ---------- GET by ID ---------- */
    @Test @Order(5)
    void deveBuscarPetPorId() throws Exception {
        Mockito.when(service.buscarPorId(1)).thenReturn(petPadrao);

        mvc.perform(get("/api/pets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Thor"))
                .andExpect(jsonPath("$.racaId").value(1));
    }

    @Test @Order(6)
    void deveRetornar404_PetNaoEncontrado() throws Exception {
        Mockito.when(service.buscarPorId(99))
                .thenThrow(new NotFoundException("Pet não encontrado."));

        mvc.perform(get("/api/pets/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Pet não encontrado."));
    }

    /* ---------- PUT ---------- */
    @Test @Order(7)
    void deveAtualizarPet() throws Exception {
        Mockito.when(service.atualizar(Mockito.eq(1), any(PetDTO.class)))
                .thenReturn("Pet atualizado!");

        mvc.perform(put("/api/pets/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(petDTOPadrao)))
                .andExpect(status().isOk())
                .andExpect(content().string("Pet atualizado!"));
    }

    @Test @Order(8)
    void deveFalharAtualizacao_PetInexistente() throws Exception {
        Mockito.when(service.atualizar(Mockito.eq(99), any(PetDTO.class)))
                .thenThrow(new NotFoundException("Pet não encontrado."));

        mvc.perform(put("/api/pets/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(petDTOPadrao)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Pet não encontrado."));
    }

    /* ---------- DELETE ---------- */
    @Test @Order(9)
    void deveDeletarPet() throws Exception {
        Mockito.when(service.deletar(1)).thenReturn("Pet removido!");

        mvc.perform(delete("/api/pets/1"))
                .andExpect(status().isNoContent())
                .andExpect(content().string("Pet removido!"));
    }

    @Test @Order(10)
    void deveFalharAoDeletar_PetInexistente() throws Exception {
        Mockito.when(service.deletar(99))
                .thenThrow(new NotFoundException("Pet não encontrado."));

        mvc.perform(delete("/api/pets/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Pet não encontrado."));
    }

    @Test @Order(11)
    void deveFalharAoDeletar_ExisteVinculo() throws Exception {
        Mockito.when(service.deletar(1))
                .thenThrow(new ConflictException(
                        "Não é possível deletar um pet que possui serviços agendados."));

        mvc.perform(delete("/api/pets/1"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("CONFLICT"))
                .andExpect(jsonPath("$.message").value(
                        "Não é possível deletar um pet que possui serviços agendados."));
    }
}