package com.codifica.elevebot.controller;

import com.codifica.elevebot.exception.ConflictException;
import com.codifica.elevebot.exception.NotFoundException;
import com.codifica.elevebot.model.Usuario;
import com.codifica.elevebot.service.TokenService;
import com.codifica.elevebot.service.UsuarioService;
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

@WebMvcTest(UsuarioController.class)
@AutoConfigureMockMvc(addFilters = false)
@SuppressWarnings("removal")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UsuarioService service;

    @MockBean
    private TokenService tokenService;

    private static Usuario usuarioPadrao;

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
        usuarioPadrao = new Usuario();
        usuarioPadrao.setId(1);
        usuarioPadrao.setNome("User Test");
        usuarioPadrao.setEmail("test@email.com");
        usuarioPadrao.setSenha("test");
    }

    // ---------- POST ----------
    @Test @Order(1)
    void deveCadastrarUsuarioComSucesso() throws Exception {
        when(service.cadastrar(any(Usuario.class))).thenReturn("Usuário criado!");

        mvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(usuarioPadrao)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Usuário criado!"));
    }

    @Test @Order(2)
    void deveFalharAoCadastrarUsuario_EmailRepetido() throws Exception {
        when(service.cadastrar(any(Usuario.class)))
                .thenThrow(new ConflictException("E-mail já cadastrado."));

        mvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(usuarioPadrao)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("CONFLICT"))
                .andExpect(jsonPath("$.message").value("E-mail já cadastrado."));
    }

    // ---------- LOGIN ----------
    @Test
    @Order(3)
    void deveLogarComSucesso() throws Exception {
        when(service.login(any(Usuario.class))).thenReturn("Login OK");
        when(tokenService.generateToken(any(String.class))).thenReturn("fake-token");

        mvc.perform(post("/api/usuarios/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(usuarioPadrao)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensagem").value("Acesso autorizado."))
                .andExpect(jsonPath("$.token").value("fake-token"));
    }

    // ---------- GET ALL ----------
    @Test @Order(4)
    void deveListarUsuariosComConteudo() throws Exception {
        when(service.listar()).thenReturn(List.of(usuarioPadrao));

        mvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("test@email.com"));
    }

    @Test @Order(5)
    void deveRetornar204QuandoListaVazia() throws Exception {
        when(service.listar()).thenReturn(List.of());

        mvc.perform(get("/api/usuarios"))
                .andExpect(status().isNoContent());
    }

    // ---------- GET by ID ----------
    @Test @Order(6)
    void deveBuscarUsuarioPorId() throws Exception {
        when(service.buscarUsuarioPorId(1)).thenReturn(usuarioPadrao);

        mvc.perform(get("/api/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("User Test"));
    }

    @Test @Order(7)
    void deveRetornar404_UsuarioNaoEncontrado() throws Exception {
        when(service.buscarUsuarioPorId(99))
                .thenThrow(new NotFoundException("Usuário não encontrado."));

        mvc.perform(get("/api/usuarios/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Usuário não encontrado."));
    }

    // ---------- PUT ----------
    @Test @Order(8)
    void deveAtualizarUsuario() throws Exception {
        when(service.atualizar(Mockito.eq(1), any(Usuario.class)))
                .thenReturn("Atualizado!");

        mvc.perform(put("/api/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(usuarioPadrao)))
                .andExpect(status().isOk())
                .andExpect(content().string("Atualizado!"));
    }

    @Test @Order(9)
    void deveFalharNaAtualizacao_UsuarioInexistente() throws Exception {
        when(service.atualizar(Mockito.eq(99), any(Usuario.class)))
                .thenThrow(new NotFoundException("Usuário não encontrado."));

        mvc.perform(put("/api/usuarios/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(usuarioPadrao)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Usuário não encontrado."));
    }

    // ---------- DELETE ----------
    @Test @Order(10)
    void deveDeletarUsuario() throws Exception {
        when(service.deletar(1)).thenReturn("Removido!");

        mvc.perform(delete("/api/usuarios/1"))
                .andExpect(status().isNoContent())
                .andExpect(content().string("Removido!"));
    }

    @Test @Order(11)
    void deveFalharAoDeletarUsuarioInexistente() throws Exception {
        when(service.deletar(99))
                .thenThrow(new NotFoundException("Usuário não encontrado."));

        mvc.perform(delete("/api/usuarios/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Usuário não encontrado."));
    }
}