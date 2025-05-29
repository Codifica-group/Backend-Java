package com.codifica.elevebot.controller;

import com.codifica.elevebot.dto.AgendaDTO;
import com.codifica.elevebot.dto.ServicoDTO;
import com.codifica.elevebot.exception.ConflictException;
import com.codifica.elevebot.exception.NotFoundException;
import com.codifica.elevebot.model.Filtro;
import com.codifica.elevebot.model.Total;
import com.codifica.elevebot.service.AgendaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AgendaController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AgendaControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AgendaService service;

    private static AgendaDTO agendaPadrao;
    private static final ObjectMapper MAPPER = new ObjectMapper()
            .registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private static String asJson(Object obj) {
        try {
            return MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeAll
    static void setUp() {
        agendaPadrao = new AgendaDTO();
        agendaPadrao.setId(1);
        agendaPadrao.setPetId(1);
        agendaPadrao.setDataHoraInicio(LocalDateTime.of(2023, 10, 15, 10, 0));
        agendaPadrao.setDataHoraFim(LocalDateTime.of(2023, 10, 15, 11, 0));
        agendaPadrao.setValorTotal(200.0);
        agendaPadrao.setServicos(List.of( new ServicoDTO(1, 100.0), new ServicoDTO(2, 100.0)));
    }

    // ---------- POST ----------
    @Test
    @Order(1)
    void deveCadastrarAgenda() throws Exception {
        var resposta = Map.of(
                "mensagem", "Agenda cadastrada com sucesso.",
                "id", 1
        );

        when(service.cadastrar(any(AgendaDTO.class))).thenReturn(resposta);

        mvc.perform(post("/api/agendas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(agendaPadrao)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.mensagem").value("Agenda cadastrada com sucesso."))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @Order(2)
    void deveFalharAoCadastrarAgenda_ConflitoDeHorario() throws Exception {
        when(service.cadastrar(any(AgendaDTO.class)))
                .thenThrow(new ConflictException("Já existe outro agendamento no período informado."));

        mvc.perform(post("/api/agendas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(agendaPadrao)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("CONFLICT"))
                .andExpect(jsonPath("$.message").value("Já existe outro agendamento no período informado."));
    }

    // ---------- GET ALL ----------
    @Test
    @Order(3)
    void deveListarTodasAgendas() throws Exception {
        when(service.listar()).thenReturn(List.of(agendaPadrao));

        mvc.perform(get("/api/agendas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].valor").value(200.0));
    }

    @Test
    @Order(4)
    void deveRetornar204_ListaVazia() throws Exception {
        when(service.listar()).thenReturn(Collections.emptyList());

        mvc.perform(get("/api/agendas"))
                .andExpect(status().isNoContent());
    }

    // ---------- GET by ID ----------
    @Test
    @Order(5)
    void deveBuscarAgendaPorId() throws Exception {
        when(service.buscarPorId(1)).thenReturn(agendaPadrao);

        mvc.perform(get("/api/agendas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.valor").value(200.0));
    }

    @Test
    @Order(6)
    void deveRetornar404_AgendaNaoEncontrada() throws Exception {
        when(service.buscarPorId(99))
                .thenThrow(new NotFoundException("Agenda não encontrada."));

        mvc.perform(get("/api/agendas/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Agenda não encontrada."));
    }

    // ---------- PUT ----------
    @Test
    @Order(7)
    void deveAtualizarAgenda() throws Exception {
        when(service.atualizar(Mockito.eq(1), any(AgendaDTO.class)))
                .thenReturn("Agenda atualizada com sucesso!");

        mvc.perform(put("/api/agendas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(agendaPadrao)))
                .andExpect(status().isOk())
                .andExpect(content().string("Agenda atualizada com sucesso!"));
    }

    @Test
    @Order(8)
    void deveFalharAoAtualizarAgenda_ConflitoDeHorario() throws Exception {
        when(service.atualizar(Mockito.eq(1), any(AgendaDTO.class)))
                .thenThrow(new ConflictException("Já existe outro agendamento no período informado."));

        mvc.perform(put("/api/agendas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(agendaPadrao)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("CONFLICT"))
                .andExpect(jsonPath("$.message").value("Já existe outro agendamento no período informado."));
    }

    // ---------- DELETE ----------
    @Test
    @Order(9)
    void deveDeletarAgenda() throws Exception {
        mvc.perform(delete("/api/agendas/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(10)
    void deveFalharAoDeletarAgendaNaoEncontrada() throws Exception {
        when(service.deletar(99))
                .thenThrow(new NotFoundException("Agenda não encontrada."));

        mvc.perform(delete("/api/agendas/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Agenda não encontrada."));
    }

    // ---------- FILTRAR ----------
    @Test
    @Order(11)
    void deveFiltrarAgendasComSucesso() throws Exception {
        Filtro filtro = new Filtro();
        filtro.setDataInicio(LocalDateTime.of(2023, 10, 15, 0, 0).toLocalDate());
        filtro.setDataFim(LocalDateTime.of(2023, 10, 16, 0, 0).toLocalDate());

        when(service.filtrar(any(Filtro.class))).thenReturn(List.of(agendaPadrao));

        mvc.perform(post("/api/agendas/filtrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(filtro)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    @Order(12)
    void deveRetornar204_FiltroSemResultados() throws Exception {
        Filtro filtro = new Filtro();
        filtro.setDataInicio(LocalDateTime.of(2023, 10, 15, 0, 0).toLocalDate());
        filtro.setDataFim(LocalDateTime.of(2023, 10, 16, 0, 0).toLocalDate());

        when(service.filtrar(any(Filtro.class))).thenReturn(Collections.emptyList());

        mvc.perform(post("/api/agendas/filtrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(filtro)))
                .andExpect(status().isNoContent());
    }

    // ---------- CALCULAR ----------
    @Test
    @Order(13)
    void deveCalcularLucro() throws Exception {
        Total total = new Total();
        total.setEntrada(500.0);
        total.setSaida(200.0);
        total.setTotal(300.0);

        when(service.calcularLucro(any(Total.class))).thenReturn(total);

        mvc.perform(post("/api/agendas/calcular/lucro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(total)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(300.0));
    }

    @Test
    @Order(14)
    void deveCalcularServico() throws Exception {
        Map<String, Object> resposta = Map.of(
                "valorServico", 150.0,
                "sugestaoValor", 200.0
        );

        when(service.calcularServico(any(AgendaDTO.class))).thenReturn(resposta);

        mvc.perform(post("/api/agendas/calcular/servico")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(agendaPadrao)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valorServico").value(150.0))
                .andExpect(jsonPath("$.sugestaoValor").value(200.0));
    }
}
