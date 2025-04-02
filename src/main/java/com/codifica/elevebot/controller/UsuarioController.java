package com.codifica.elevebot.controller;

import com.codifica.elevebot.model.Usuario;
import com.codifica.elevebot.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<String> cadastrarUsuario(@RequestBody Usuario usuario) {
        String mensagem = usuarioService.cadastrar(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(mensagem);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Usuario usuarioRequest) {
        String mensagem = usuarioService.login(usuarioRequest);
        return ResponseEntity.ok(mensagem);
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        List<Usuario> usuarios = usuarioService.listar();
        return usuarios.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarUsuarioPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(usuarioService.buscarUsuarioPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> atualizarUsuario(@PathVariable Integer id,
                                                   @RequestBody Usuario usuario) {
        String mensagem = usuarioService.atualizar(id, usuario);
        return ResponseEntity.ok(mensagem);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarUsuario(@PathVariable Integer id) {
        String mensagem = usuarioService.deletar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(mensagem);
    }
}