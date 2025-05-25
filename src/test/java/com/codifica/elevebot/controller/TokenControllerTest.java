package com.codifica.elevebot.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.codifica.elevebot.service.TokenService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TokenController.class)
class TokenControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TokenService tokenService;

    @Test
    void deveValidarToken() throws Exception {
        String validToken = JWT.create()
                .withSubject("fernanda")
                .withIssuer("elevebot")
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hora
                .sign(Algorithm.HMAC256("default-secret-key"));

        Mockito.when(tokenService.validateToken(anyString())).thenAnswer(invocation -> {
            String token = invocation.getArgument(0);
            return JWT.require(Algorithm.HMAC256("default-secret-key"))
                    .withIssuer("elevebot")
                    .build()
                    .verify(token);
        });

        mvc.perform(post("/api/auth/validate")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + validToken))
                .andExpect(status().isOk())
                .andExpect(content().string("Token válido para: fernanda"));
    }

    @Test
    void deveRetornar401_QuandoTokenInvalido() throws Exception {
        String invalidToken = "token-invalido";

        Mockito.when(tokenService.validateToken(anyString())).thenThrow(new RuntimeException("Token inválido ou expirado"));

        mvc.perform(post("/api/auth/validate")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + invalidToken))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Token inválido ou expirado"));
    }
}