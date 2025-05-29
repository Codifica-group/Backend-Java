package com.codifica.elevebot.controller;

import com.codifica.elevebot.dto.PacoteDTO;
import com.codifica.elevebot.exception.ConflictException;
import com.codifica.elevebot.exception.NotFoundException;
import com.codifica.elevebot.exception.IllegalArgumentException;
import com.codifica.elevebot.model.Cliente;
import com.codifica.elevebot.model.Pacote;
import com.codifica.elevebot.service.PacoteService;
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

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PacoteController.class)
@AutoConfigureMockMvc(addFilters = false)
@SuppressWarnings("removal")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PacoteControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PacoteService service;

    private static Cliente clientePadrao;
    private static PacoteDTO pacoteDTOPadrao;
    private static Pacote pacotePadrao;

    private static final ObjectMapper MAPPER = new ObjectMapper().findAndRegisterModules();

    private static String asJson(Object o) {
        try { return MAPPER.writeValueAsString(o); }
        catch (JsonProcessingException e) { throw new RuntimeException(e); }
    }

    @BeforeAll
    static void setUp() {
        clientePadrao = new Cliente();
        clientePadrao.setId(1);
        clientePadrao.setNome("Mariana Souza");

        pacoteDTOPadrao = new PacoteDTO();
        pacoteDTOPadrao.setId(1);
        pacoteDTOPadrao.setClienteId(1);
        pacoteDTOPadrao.setPacoteId(1);
        pacoteDTOPadrao.setDataInicio(LocalDate.of(2025, 4, 1));
        pacoteDTOPadrao.setDataExpiracao(LocalDate.of(2025, 5, 2));
        pacoteDTOPadrao.setStatus("Ativo");

        pacotePadrao = new Pacote();
        pacotePadrao.setId(1);
        pacotePadrao.setPacoteId(1);
        pacotePadrao.setCliente(clientePadrao);
        pacoteDTOPadrao.setDataInicio(LocalDate.of(2025, 4, 1));
        pacotePadrao.setDataExpiracao(LocalDate.of(2025, 5, 2));
    }

    /* ========== POST ========== */

    @Test @Order(1)
    void deveCadastrarPacote() throws Exception {
        Mockito.when(service.cadastrar(any(PacoteDTO.class)))
                .thenReturn(new HashMap<>() {{
                    put("mensagem", "Pacote cadastrado com sucesso.");
                    put("id", 1);
                }});

        mvc.perform(post("/api/pacotes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(pacoteDTOPadrao)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.mensagem").value("Pacote cadastrado com sucesso."))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test @Order(2)
    void deveFalharCadastro_PacoteDuplicadoParaCliente() throws Exception {
        Mockito.when(service.cadastrar(any(PacoteDTO.class)))
                .thenThrow(new ConflictException("Cliente já possui um pacote ativo."));

        mvc.perform(post("/api/pacotes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(pacoteDTOPadrao)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("CONFLICT"))
                .andExpect(jsonPath("$.message").value("Cliente já possui um pacote ativo."));
    }

    @Test @Order(3)
    void deveFalharCadastro_ClienteInexistente() throws Exception {
        Mockito.when(service.cadastrar(any(PacoteDTO.class)))
                .thenThrow(new NotFoundException("Cliente nao encontrado."));

        mvc.perform(post("/api/pacotes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(pacoteDTOPadrao)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Cliente nao encontrado."));
    }

    @Test @Order(4)
    void deveFalharCadastro_IdPacoteInvalido() throws Exception {
        Mockito.when(service.cadastrar(any(PacoteDTO.class)))
                .thenThrow(new IllegalArgumentException(
                        "Tipo (id) do pacote deve ser 1 (Mensal) ou 2 (Quinzenal)."));

        mvc.perform(post("/api/pacotes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(pacoteDTOPadrao)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value(
                        "Tipo (id) do pacote deve ser 1 (Mensal) ou 2 (Quinzenal)."));
    }

    @Test @Order(5)
    void deveFalharCadastro_PacoteJaExpirado() throws Exception {
        Mockito.when(service.cadastrar(any(PacoteDTO.class)))
                .thenThrow(new ConflictException("Impossível cadastrar um pacote expirado."));

        mvc.perform(post("/api/pacotes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(pacoteDTOPadrao)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("CONFLICT"))
                .andExpect(jsonPath("$.message").value("Impossível cadastrar um pacote expirado."));
    }

    /* ========== GET ALL ========== */

    @Test @Order(6)
    void deveListarPacotes() throws Exception {
        Mockito.when(service.listar()).thenReturn(List.of(pacoteDTOPadrao));

        mvc.perform(get("/api/pacotes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].clienteId").value(1))
                .andExpect(jsonPath("$[0].pacoteId").value(1))
                .andExpect(jsonPath("$[0].dataInicio").value("2025-04-01"))
                .andExpect(jsonPath("$[0].dataExpiracao").isNotEmpty())
                .andExpect(jsonPath("$[0].status").value("Ativo"));
    }

    @Test @Order(7)
    void deveRetornar204_quandoListaVazia() throws Exception {
        Mockito.when(service.listar()).thenReturn(List.of());

        mvc.perform(get("/api/pacotes"))
                .andExpect(status().isNoContent());
    }

    /* ========== GET BY ID ========== */

    @Test @Order(8)
    void deveBuscarPacotePorId() throws Exception {
        Mockito.when(service.buscarPorId(1)).thenReturn(pacoteDTOPadrao);

        mvc.perform(get("/api/pacotes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.clienteId").value(1))
                .andExpect(jsonPath("$.pacoteId").value(1))
                .andExpect(jsonPath("$.dataInicio").value("2025-04-01"))
                .andExpect(jsonPath("$.dataExpiracao").isNotEmpty())
                .andExpect(jsonPath("$.status").value("Ativo"));
    }

    @Test @Order(9)
    void deveRetornar404_PacoteNaoEncontrado() throws Exception {
        Mockito.when(service.buscarPorId(99))
                .thenThrow(new NotFoundException("Registro cliente_pacote não encontrado."));

        mvc.perform(get("/api/pacotes/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value(
                        "Registro cliente_pacote não encontrado."));
    }

    /* ========== PUT ========== */

    @Test @Order(10)
    void deveAtualizarPacote() throws Exception {
        Mockito.when(service.atualizar(Mockito.eq(1), any(PacoteDTO.class)))
                .thenReturn("Pacote atualizado com sucesso.");

        mvc.perform(put("/api/pacotes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(pacoteDTOPadrao)))
                .andExpect(status().isOk())
                .andExpect(content().string("Pacote atualizado com sucesso."));
    }

    @Test @Order(11)
    void deveFalharAtualizacao_PacoteInexistente() throws Exception {
        Mockito.when(service.atualizar(Mockito.eq(99), any(PacoteDTO.class)))
                .thenThrow(new NotFoundException("Pacote não encontrado."));

        mvc.perform(put("/api/pacotes/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(pacoteDTOPadrao)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Pacote não encontrado."));
    }

    @Test @Order(12)
    void deveFalharAtualizacao_RegistroExpirado() throws Exception {
        Mockito.when(service.atualizar(Mockito.eq(1), any(PacoteDTO.class)))
                .thenThrow(new ConflictException("Impossível atualizar um pacote expirado."));

        mvc.perform(put("/api/pacotes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(pacoteDTOPadrao)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("CONFLICT"))
                .andExpect(jsonPath("$.message").value("Impossível atualizar um pacote expirado."));
    }

    @Test @Order(13)
    void deveFalharAtualizacao_IdPacoteInvalido() throws Exception {
        Mockito.when(service.atualizar(Mockito.eq(1), any(PacoteDTO.class)))
                .thenThrow(new IllegalArgumentException(
                        "Tipo (id) do pacote deve ser 1 (Mensal) ou 2 (Quinzenal)."));

        mvc.perform(put("/api/pacotes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(pacoteDTOPadrao)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value(
                        "Tipo (id) do pacote deve ser 1 (Mensal) ou 2 (Quinzenal)."));
    }

    @Test @Order(14)
    void deveFalharAtualizacao_NovaDataExpirada() throws Exception {
        Mockito.when(service.atualizar(Mockito.eq(1), any(PacoteDTO.class)))
                .thenThrow(new ConflictException(
                        "Impossível atualizar um pacote ativo para um pacote expirado."));

        mvc.perform(put("/api/pacotes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(pacoteDTOPadrao)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("CONFLICT"))
                .andExpect(jsonPath("$.message").value(
                        "Impossível atualizar um pacote ativo para um pacote expirado."));
    }

    /* ========== DELETE ========== */

    @Test @Order(15)
    void deveDeletarPacote() throws Exception {
        Mockito.doNothing().when(service).deletar(1);

        mvc.perform(delete("/api/pacotes/1"))
                .andExpect(status().isNoContent());
    }

    @Test @Order(16)
    void deveFalharDelecao_PacoteInexistente() throws Exception {
        Mockito.doThrow(new NotFoundException("Pacote não encontrado."))
                        .when(service).deletar(99);

        mvc.perform(delete("/api/pacotes/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Pacote não encontrado."));
    }
}