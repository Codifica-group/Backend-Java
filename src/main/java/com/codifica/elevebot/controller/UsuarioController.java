package com.codifica.elevebot.controller;

import com.codifica.elevebot.model.Usuario;
import com.codifica.elevebot.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/cadastro")
    public ResponseEntity<Map<String, Object>> cadastrarUsuario(@RequestBody Usuario usuario) {
        Usuario usuarioExistente = usuarioRepository.findByEmail(usuario.getEmail());

        if (usuarioExistente != null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Este e-mail já está em uso."
            );
        }

        usuarioRepository.save(usuario);

        Map<String, Object> resposta = new LinkedHashMap<>();
        resposta.put("timestamp", LocalDateTime.now());
        resposta.put("status", HttpStatus.CREATED.value());
        resposta.put("message", "Usuário cadastrado com sucesso!");

        return new ResponseEntity<>(resposta, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Usuario usuarioRequest) {
        Usuario usuario = usuarioRepository.findByEmailAndSenha(
                usuarioRequest.getEmail(),
                usuarioRequest.getSenha()
        );

        if (usuario == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Email ou senha incorretos."
            );
        }

        Map<String, Object> resposta = new LinkedHashMap<>();
        resposta.put("timestamp", LocalDateTime.now());
        resposta.put("status", HttpStatus.OK.value());
        resposta.put("message", "Acesso Autorizado");

        return ResponseEntity.ok(resposta);
    }
}