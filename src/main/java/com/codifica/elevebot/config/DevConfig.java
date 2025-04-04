package com.codifica.elevebot.config;

import com.codifica.elevebot.model.Usuario;
import com.codifica.elevebot.model.Cliente;
import com.codifica.elevebot.model.Pet;
import com.codifica.elevebot.repository.UsuarioRepository;
import com.codifica.elevebot.repository.ClienteRepository;
import com.codifica.elevebot.repository.PetRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
@Profile("dev")
public class DevConfig {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PetRepository petRepository;

    @Bean
    public CommandLineRunner initDatabase() {
        return args -> {
            Usuario usuario = new Usuario();
            usuario.setNome("User Test");
            usuario.setEmail("user@test.com");
            usuario.setSenha("test");
            usuarioRepository.save(usuario);

            Cliente cliente = new Cliente();
            cliente.setNome("Cliente Test");
            cliente.setNumeroCelular("11900000000");
            cliente.setCep("00000000");
            cliente.setNumeroEndereco(0);
            cliente.setComplemento("");
            clienteRepository.save(cliente);

            Pet pet = new Pet();
            pet.setIdRaca(1);
            pet.setNome("Pet Test");
            pet.setCliente(cliente);
            cliente.getPets().add(pet);
            clienteRepository.save(cliente);
        };
    }
}