package com.codifica.elevebot.controller;

import com.codifica.elevebot.model.Usuario;
import com.codifica.elevebot.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/cadastro")
    public ResponseEntity<Map<String, Object>> cadastrarUsuario(@RequestBody Usuario usuario) {
        Map<String, Object> response = new HashMap<>();

        Usuario usuarioExistente = usuarioRepository.findByEmail(usuario.getEmail());
        if (usuarioExistente != null) {
            response.put("status", "error");
            response.put("message", "");
            response.put("error", "Este e-mail já está em uso.");
            return ResponseEntity.badRequest().body(response);
        }

        usuarioRepository.save(usuario);

        response.put("status", "success");
        response.put("message", "Usuário cadastrado com sucesso!");
        response.put("error", null);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Usuario usuarioRequest) {
        Map<String, Object> response = new HashMap<>();

        Usuario usuario = usuarioRepository.findByEmailAndSenha(
                usuarioRequest.getEmail(),
                usuarioRequest.getSenha()
        );

        if (usuario == null) {
            response.put("status", "error");
            response.put("message", "");
            response.put("error", "Email ou senha incorretos.");
            return ResponseEntity.badRequest().body(response);
        }

        response.put("status", "success");
        response.put("message", "Login realizado com sucesso. Bem-vindo(a), " + usuario.getNome() + "!");
        response.put("error", null);

        return ResponseEntity.ok(response);
    }
}