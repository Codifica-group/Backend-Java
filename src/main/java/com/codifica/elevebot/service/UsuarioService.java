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

    public String cadastrar(Usuario usuario) {
        if (usuarioRepository.findByEmail(usuario.getEmail()) != null) {
            throw new ConflictException("Não é possível cadastrar dois usuários com o mesmo e-mail.");
        }

        usuarioRepository.save(usuario);
        return "Usuário cadastrado com sucesso.";
    }

    public String login(Usuario usuarioRequest) {
        Usuario usuario = usuarioRepository.findByEmailAndSenha(
                usuarioRequest.getEmail(),
                usuarioRequest.getSenha()
        );

        if (usuario == null) {
            throw new UnauthorizedException("Credenciais inválidas.");
        }

        return "Acesso autorizado.";
    }

    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarUsuarioPorId(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado."));
    }

    public String atualizar(Integer id, Usuario usuario) {
        if (!usuarioRepository.existsById(id)) {
            throw new NotFoundException("Usuário não encontrado.");
        }

        usuario.setId(id);
        usuarioRepository.save(usuario);
        return "Usuário atualizado com sucesso.";
    }

    public String deletar(Integer id) {
        if (!usuarioRepository.existsById(id)) {
            throw new NotFoundException("Usuário não encontrado.");
        }

        usuarioRepository.deleteById(id);
        return "Usuário deletado com sucesso.";
    }
}