package com.codifica.elevebot.config;

import com.codifica.elevebot.dto.ProdutoDTO;
import com.codifica.elevebot.model.Pacote;
import com.codifica.elevebot.model.Usuario;
import com.codifica.elevebot.model.Cliente;
import com.codifica.elevebot.model.Pet;
import com.codifica.elevebot.repository.PacoteRepository;
import com.codifica.elevebot.repository.UsuarioRepository;
import com.codifica.elevebot.repository.ClienteRepository;
import com.codifica.elevebot.repository.PetRepository;
import com.codifica.elevebot.service.ProdutoService;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    private PacoteRepository pacoteRepository;

    @Autowired
    private ProdutoService produtoService;

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

            //USUARIO
            Usuario usuario = new Usuario();
            usuario.setNome("User Test");
            usuario.setEmail("user@test.com");
            usuario.setSenha("test");
            usuarioRepository.save(usuario);

            //CLIENTE
            Cliente cliente = new Cliente();
            cliente.setNome("Cliente Test");
            cliente.setNumCelular("11900000000");
            cliente.setCep("00000000");
            cliente.setNumEndereco(0);
            cliente.setComplemento("");
            clienteRepository.save(cliente);

            //PET
            Pet pet = new Pet();
            pet.setRacaId(1);
            pet.setNome("Pet Test");
            pet.setCliente(cliente);
            cliente.getPets().add(pet);
            clienteRepository.save(cliente);

            //PACOTE (expirado)
            Pacote pacote = new Pacote();
            pacote.setPacoteId(1);
            pacote.setCliente(cliente);
//            pacote.setDataExpiracao(LocalDate.now().minusDays(1));
            pacote.setDataExpiracao(LocalDate.now());
            pacoteRepository.save(pacote);

            //PACOTE (ativo)
            pacote = new Pacote();
            pacote.setPacoteId(1);
            pacote.setCliente(cliente);
            pacote.setDataExpiracao(LocalDate.now().plusDays(31));
            pacoteRepository.save(pacote);

//            PRODUTOS
            List<ProdutoDTO> produtos = new ArrayList<ProdutoDTO>();
            produtos.add(new ProdutoDTO(1, "Conta de Luz"));
            produtos.add(new ProdutoDTO(2, "Máquina de Tosa"));
            produtos.add(new ProdutoDTO(3, "Algodão"));
            produtos.add(new ProdutoDTO(4, "Shampoo"));
            produtoService.cadastrar(produtos);
        };
    }
}