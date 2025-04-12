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
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@Profile("dev")
public class DevConfig {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Bean
    public CommandLineRunner initDatabase() {
        return args -> {
            //Criação de Tabelas Estáticas (Não existem referências no Java)
            // Tabela PACOTE
            jdbcTemplate.execute(
                    "CREATE TABLE IF NOT EXISTS PACOTE (" +
                            "    id_pacote INT PRIMARY KEY AUTO_INCREMENT, " +
                            "    tipo VARCHAR(9) " +
                            ")"
            );

            // Tabela RACA
            jdbcTemplate.execute(
                    "CREATE TABLE IF NOT EXISTS RACA (" +
                            "    id_raca INT PRIMARY KEY AUTO_INCREMENT, " +
                            "    nome VARCHAR(100) NOT NULL" +
                            ")"
            );

            // Tabela SERVICO
            jdbcTemplate.execute(
                    "CREATE TABLE IF NOT EXISTS SERVICO (" +
                            "    id_servico INT PRIMARY KEY AUTO_INCREMENT, " +
                            "    nome VARCHAR(100) NOT NULL, " +
                            "    valor_base FLOAT" +
                            ")"
            );

            // Tabela CATEGORIA_DESPESAS
            jdbcTemplate.execute(
                    "CREATE TABLE IF NOT EXISTS CATEGORIA_DESPESAS (" +
                            "    id_categoria_despesas INT PRIMARY KEY AUTO_INCREMENT, " +
                            "    nome VARCHAR(100) NOT NULL" +
                            ")"
            );

            // Tabela PRODUTO
//            jdbcTemplate.execute(
//                    "CREATE TABLE IF NOT EXISTS PRODUTO (" +
//                            "    id_produto INT PRIMARY KEY AUTO_INCREMENT, " +
//                            "    fk_categoria_despesas INT NOT NULL, " +
//                            "    nome VARCHAR(100) NOT NULL, " +
//                            "    FOREIGN KEY (fk_categoria_despesas) " +
//                            "       REFERENCES CATEGORIA_DESPESAS(id_categoria_despesas)" +
//                            ")"
//            );

            // Tabela DESPESAS
//            jdbcTemplate.execute(
//                    "CREATE TABLE IF NOT EXISTS DESPESAS (" +
//                            "    id_despesas INT PRIMARY KEY AUTO_INCREMENT, " +
//                            "    fk_produto INT NOT NULL, " +
//                            "    valor FLOAT NOT NULL, " +
//                            "    data DATETIME, " +
//                            "    FOREIGN KEY (fk_produto) REFERENCES PRODUTO(id_produto)" +
//                            ")"
//            );

            //Populando Tabelas
            //PACOTE
            jdbcTemplate.update("INSERT INTO PACOTE (tipo) VALUES ('Mensal')");
            jdbcTemplate.update("INSERT INTO PACOTE (tipo) VALUES ('Quinzenal')");

            //RACA
            jdbcTemplate.update("INSERT INTO RACA (nome) VALUES ('Pug')");
            jdbcTemplate.update("INSERT INTO RACA (nome) VALUES ('Rotweiller')");
            jdbcTemplate.update("INSERT INTO RACA (nome) VALUES ('Shih Tzu')");
            jdbcTemplate.update("INSERT INTO RACA (nome) VALUES ('Golden Retriever')");
            jdbcTemplate.update("INSERT INTO RACA (nome) VALUES ('Labrador')");

            //SERVICO
            jdbcTemplate.update("INSERT INTO SERVICO (nome, valor_base) VALUES ('Banho', 50.0)");
            jdbcTemplate.update("INSERT INTO SERVICO (nome, valor_base) VALUES ('Tosa', 70.0)");
            jdbcTemplate.update("INSERT INTO SERVICO (nome, valor_base) VALUES ('Hidratação', 30.0)");

            //CATEGORIA_DESPESAS
            jdbcTemplate.update("INSERT INTO CATEGORIA_DESPESAS (nome) VALUES ('Gastos fixos')");
            jdbcTemplate.update("INSERT INTO CATEGORIA_DESPESAS (nome) VALUES ('Manutenção')");
            jdbcTemplate.update("INSERT INTO CATEGORIA_DESPESAS (nome) VALUES ('Insumos')");
            jdbcTemplate.update("INSERT INTO CATEGORIA_DESPESAS (nome) VALUES ('Produtos')");

            //USUARIO
            Usuario usuario = new Usuario();
            usuario.setNome("User Test");
            usuario.setEmail("user@test.com");
            usuario.setSenha("test");
            usuarioRepository.save(usuario);

            //CLIENTE
            Cliente cliente = new Cliente();
            cliente.setNome("Cliente Test");
            cliente.setNumeroCelular("11900000000");
            cliente.setCep("00000000");
            cliente.setNumeroEndereco(0);
            cliente.setComplemento("");
            clienteRepository.save(cliente);

            //PET
            Pet pet = new Pet();
            pet.setIdRaca(1);
            pet.setNome("Pet Test");
            pet.setCliente(cliente);
            cliente.getPets().add(pet);
            clienteRepository.save(cliente);
        };
    }
}