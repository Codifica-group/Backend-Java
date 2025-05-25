package com.codifica.elevebot.controller;

import com.codifica.elevebot.dto.RacaDTO;
import com.codifica.elevebot.exception.ConflictException;
import com.codifica.elevebot.exception.NotFoundException;
import com.codifica.elevebot.service.RacaService;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RacaController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RacaControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RacaService service;

    private static RacaDTO racaPadrao;
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
        racaPadrao = new RacaDTO();
        racaPadrao.setId(1);
        racaPadrao.setNome("Pug");
        racaPadrao.setPorteId(1);
        racaPadrao.setPorteNome("Pequeno");
    }

    // ---------- POST ----------
    @Test
    @Order(1)
    void deveCadastrarRaca() throws Exception {
        var resposta = new HashMap<String, Object>();
        resposta.put("mensagem", "Raça cadastrada com sucesso.");
        resposta.put("id", 1);

        when(service.cadastrar(any(RacaDTO.class))).thenReturn(resposta);

        mvc.perform(post("/api/racas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(racaPadrao)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.mensagem").value("Raça cadastrada com sucesso."))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @Order(2)
    void deveFalharNoCadastro_RacaJaExiste() throws Exception {
        when(service.cadastrar(any(RacaDTO.class)))
                .thenThrow(new ConflictException("Raça já cadastrada."));

        mvc.perform(post("/api/racas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(racaPadrao)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("CONFLICT"))
                .andExpect(jsonPath("$.message").value("Raça já cadastrada."));
    }

    // ---------- GET ALL ----------
    @Test
    @Order(3)
    void deveListarRacas() throws Exception {
        Map<String, List<RacaDTO>> racasAgrupadas = Map.of("Pequeno", List.of(racaPadrao));

        when(service.listar()).thenReturn(racasAgrupadas);

        mvc.perform(get("/api/racas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Pequeno[0].id").value(1))
                .andExpect(jsonPath("$.Pequeno[0].nome").value("Pug"));
    }

    @Test
    @Order(4)
    void deveRetornar204_ListaVazia() throws Exception {
        when(service.listar()).thenReturn(Map.of());

        mvc.perform(get("/api/racas"))
                .andExpect(status().isNoContent());
    }

    // ---------- PUT ----------
    @Test
    @Order(5)
    void deveAtualizarRaca() throws Exception {
        when(service.atualizar(Mockito.eq(1), any(RacaDTO.class)))
                .thenReturn("Raça atualizada com sucesso.");

        mvc.perform(put("/api/racas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(racaPadrao)))
                .andExpect(status().isOk())
                .andExpect(content().string("Raça atualizada com sucesso."));
    }

    @Test
    @Order(6)
    void deveFalharNaAtualizacao_RacaInexistente() throws Exception {
        when(service.atualizar(Mockito.eq(99), any(RacaDTO.class)))
                .thenThrow(new NotFoundException("Raça não encontrada."));

        mvc.perform(put("/api/racas/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(racaPadrao)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Raça não encontrada."));
    }

    // ---------- DELETE ----------
    @Test
    @Order(7)
    void deveDeletarRaca() throws Exception {
        mvc.perform(delete("/api/racas/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(8)
    void deveFalharAoDeletar_RacaPossuiPets() throws Exception {
        when(service.deletar(1))
                .thenThrow(new ConflictException("Não é possível deletar raça que possui pets cadastrados."));

        mvc.perform(delete("/api/racas/1"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("CONFLICT"))
                .andExpect(jsonPath("$.message").value("Não é possível deletar raça que possui pets cadastrados."));
    }

    @Test
    @Order(9)
    void deveFalharAoDeletar_RacaInexistente() throws Exception {
        when(service.deletar(99))
                .thenThrow(new NotFoundException("Raça não encontrada."));

        mvc.perform(delete("/api/racas/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Raça não encontrada."));
    }
}
