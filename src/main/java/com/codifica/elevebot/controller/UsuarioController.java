package com.codifica.elevebot.controller;

import com.codifica.elevebot.model.Usuario;
import com.codifica.elevebot.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<Usuario> cadastrarUsuario(@RequestBody Usuario usuario) {
        Usuario usuarioSalvo = usuarioService.cadastrar(usuario);
        return ResponseEntity.status(201).body(usuarioSalvo);
    }

    @PostMapping("/login")
    public ResponseEntity<Usuario> login(@RequestBody Usuario usuarioRequest) {
        Usuario usuario = usuarioService.login(usuarioRequest);
        return ResponseEntity.ok(usuario);
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        List<Usuario> usuarios = usuarioService.listar();
        if (usuarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarUsuarioPorId(@PathVariable Integer id) {
        Usuario usuario = usuarioService.buscarUsuarioPorId(id);
        return ResponseEntity.ok(usuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizarUsuario(@PathVariable Integer id,
                                                    @RequestBody Usuario usuario) {
        Usuario usuarioAtualizado = usuarioService.atualizar(id, usuario);
        return ResponseEntity.ok(usuarioAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Integer id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}