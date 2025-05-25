package com.codifica.elevebot.controller;

import com.codifica.elevebot.dto.PetDTO;
import com.codifica.elevebot.exception.ConflictException;
import com.codifica.elevebot.exception.NotFoundException;
import com.codifica.elevebot.model.Cliente;
import com.codifica.elevebot.model.Raca;
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
import static org.mockito.Mockito.when;
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
    private static Raca racaPadrao;
    private static PetDTO petDTOPadrao;

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
        // Cliente padrão
        clientePadrao = new Cliente();
        clientePadrao.setId(1);
        clientePadrao.setNome("Mariana Souza");
        clientePadrao.setNumCelular("11912345678");
        clientePadrao.setCep("12345678");
        clientePadrao.setNumEndereco(1);
        clientePadrao.setComplemento("");

        // Raça padrão
        racaPadrao = new Raca();
        racaPadrao.setId(1);
        racaPadrao.setNome("Golden Retriever");

        // PetDTO padrão
        petDTOPadrao = new PetDTO();
        petDTOPadrao.setId(1);
        petDTOPadrao.setNome("Thor");
        petDTOPadrao.setRacaId(1);
        petDTOPadrao.setClienteId(1);
    }

    /* ---------- POST ---------- */
    @Test
    @Order(1)
    void deveCadastrarPet() throws Exception {
        var resposta = new String("Pet cadastrado com sucesso.");
        when(service.cadastrar(any(PetDTO.class))).thenReturn(resposta);

        mvc.perform(post("/api/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(petDTOPadrao)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Pet cadastrado com sucesso."));
    }

    @Test
    @Order(2)
    void deveFalharCadastro_PetJaExiste() throws Exception {
        when(service.cadastrar(any(PetDTO.class)))
                .thenThrow(new ConflictException("Pet já cadastrado."));

        mvc.perform(post("/api/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(petDTOPadrao)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("CONFLICT"))
                .andExpect(jsonPath("$.message").value("Pet já cadastrado."));
    }

    /* ---------- GET ALL ---------- */
    @Test
    @Order(3)
    void deveListarPets() throws Exception {
        when(service.listar()).thenReturn(List.of(petDTOPadrao));

        mvc.perform(get("/api/pets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Thor"))
                .andExpect(jsonPath("$[0].racaId").value(1));
    }

    @Test
    @Order(4)
    void deveRetornar204_ListaVazia() throws Exception {
        when(service.listar()).thenReturn(List.of());

        mvc.perform(get("/api/pets"))
                .andExpect(status().isNoContent());
    }

    /* ---------- GET by ID ---------- */
    @Test
    @Order(5)
    void deveBuscarPetPorId() throws Exception {
        when(service.buscarPorId(1)).thenReturn(petDTOPadrao);

        mvc.perform(get("/api/pets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Thor"))
                .andExpect(jsonPath("$.racaId").value(1));
    }

    @Test
    @Order(6)
    void deveRetornar404_PetNaoEncontrado() throws Exception {
        when(service.buscarPorId(99))
                .thenThrow(new NotFoundException("Pet não encontrado."));

        mvc.perform(get("/api/pets/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Pet não encontrado."));
    }

    /* ---------- PUT ---------- */
    @Test
    @Order(7)
    void deveAtualizarPet() throws Exception {
        when(service.atualizar(Mockito.eq(1), any(PetDTO.class)))
                .thenReturn("Pet atualizado com sucesso!");

        mvc.perform(put("/api/pets/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(petDTOPadrao)))
                .andExpect(status().isOk())
                .andExpect(content().string("Pet atualizado com sucesso!"));
    }

    @Test
    @Order(8)
    void deveFalharAtualizacao_PetInexistente() throws Exception {
        when(service.atualizar(Mockito.eq(99), any(PetDTO.class)))
                .thenThrow(new NotFoundException("Pet não encontrado."));

        mvc.perform(put("/api/pets/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(petDTOPadrao)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Pet não encontrado."));
    }

    /* ---------- DELETE ---------- */
    @Test
    @Order(9)
    void deveDeletarPet() throws Exception {
        when(service.deletar(1)).thenReturn("Pet deletado com sucesso.");

        mvc.perform(delete("/api/pets/1"))
                .andExpect(status().isNoContent())
                .andExpect(content().string("Pet deletado com sucesso."));
    }

    @Test
    @Order(10)
    void deveFalharAoDeletar_PetInexistente() throws Exception {
        when(service.deletar(99))
                .thenThrow(new NotFoundException("Pet não encontrado."));

        mvc.perform(delete("/api/pets/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Pet não encontrado."));
    }
}
