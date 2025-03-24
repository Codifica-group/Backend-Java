package com.codifica.elevebot.controller;

import com.codifica.elevebot.model.Usuario;
import com.codifica.elevebot.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    public ResponseEntity<Map<String, Object>> cadastrarUsuario(@RequestBody Usuario usuario) {
        if (usuarioRepository.findByEmail(usuario.getEmail()) != null) {
            Map<String, Object> resposta = new LinkedHashMap<>();
            resposta.put("timestamp", LocalDateTime.now());
            resposta.put("status", HttpStatus.CONFLICT.value());
            resposta.put("error", "CONFLICT");
            resposta.put("message", "Não é possível cadastrar dois usuários com o mesmo e-mail.");

            return ResponseEntity.status(HttpStatus.CONFLICT).body(resposta);
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
            Map<String, Object> resposta = new LinkedHashMap<>();
            resposta.put("timestamp", LocalDateTime.now());
            resposta.put("status", HttpStatus.UNAUTHORIZED.value());
            resposta.put("error", "UNAUTHORIZED");
            resposta.put("message", "Email ou senha incorretos.");

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resposta);
        }

        Map<String, Object> resposta = new LinkedHashMap<>();
        resposta.put("timestamp", LocalDateTime.now());
        resposta.put("status", HttpStatus.OK.value());
        resposta.put("message", "Acesso Autorizado");

        return ResponseEntity.ok(resposta);
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();

        if (usuarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarUsuarioPorId(@PathVariable Integer id) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);

        if (usuarioOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(usuarioOptional.get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> atualizarUsuario(@PathVariable Integer id,
                                                                @RequestBody Usuario usuario) {
        if (!usuarioRepository.existsById(id)) {
            Map<String, Object> resposta = new LinkedHashMap<>();
            resposta.put("timestamp", LocalDateTime.now());
            resposta.put("status", HttpStatus.NOT_FOUND.value());
            resposta.put("error", "NOT_FOUND");
            resposta.put("message", "Usuário não encontrado.");

            return new ResponseEntity<>(resposta, HttpStatus.NOT_FOUND);
        }

        usuario.setId(id);
        Usuario usuarioAtualizado = usuarioRepository.save(usuario);

        Map<String, Object> resposta = new LinkedHashMap<>();
        resposta.put("timestamp", LocalDateTime.now());
        resposta.put("status", HttpStatus.OK.value());
        resposta.put("message", "Usuário atualizado com sucesso!");

        return ResponseEntity.ok(resposta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deletarUsuario(@PathVariable Integer id) {
        if (!usuarioRepository.existsById(id)) {
            Map<String, Object> resposta = new LinkedHashMap<>();
            resposta.put("timestamp", LocalDateTime.now());
            resposta.put("status", HttpStatus.NOT_FOUND.value());
            resposta.put("error", "NOT FOUND");
            resposta.put("message", "Usuário não encontrado.");

            return new ResponseEntity<>(resposta, HttpStatus.NOT_FOUND);
        }

        usuarioRepository.deleteById(id);

        Map<String, Object> resposta = new LinkedHashMap<>();
        resposta.put("timestamp", LocalDateTime.now());
        resposta.put("status", HttpStatus.NO_CONTENT.value());
        resposta.put("message", "Usuário deletado com sucesso.");

        return new ResponseEntity<>(resposta, HttpStatus.NO_CONTENT);
    }
}