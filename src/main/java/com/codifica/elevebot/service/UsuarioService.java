package com.codifica.elevebot.service;

import com.codifica.elevebot.exception.ConflictException;
import com.codifica.elevebot.exception.NotFoundException;
import com.codifica.elevebot.exception.UnauthorizedException;
import com.codifica.elevebot.model.Usuario;
import com.codifica.elevebot.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario cadastrar(Usuario usuario) {
        if (usuarioRepository.findByEmail(usuario.getEmail()) != null) {
            throw new ConflictException("Não é possível cadastrar dois usuários com o mesmo e-mail.");
        }
        return usuarioRepository.save(usuario);
    }

    public Usuario login(Usuario usuarioRequest) {
        Usuario usuario = usuarioRepository.findByEmailAndSenha(
                usuarioRequest.getEmail(),
                usuarioRequest.getSenha()
        );
        if (usuario == null) {
            throw new UnauthorizedException("Credenciais inválidas.");
        }
        return usuario;
    }

    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarUsuarioPorId(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado."));
    }

    public Usuario atualizar(Integer id, Usuario usuario) {
        if (!usuarioRepository.existsById(id)) {
            throw new NotFoundException("Usuário não encontrado.");
        }
        usuario.setId(id);
        return usuarioRepository.save(usuario);
    }

    public void deletar(Integer id) {
        if (!usuarioRepository.existsById(id)) {
            throw new NotFoundException("Usuário não encontrado.");
        }
        usuarioRepository.deleteById(id);
    }
}