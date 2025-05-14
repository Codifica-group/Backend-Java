package com.codifica.elevebot.config;

import com.codifica.elevebot.dto.ProdutoDTO;
import com.codifica.elevebot.model.*;
import com.codifica.elevebot.repository.*;
import com.codifica.elevebot.service.ProdutoService;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private PorteRepository porteRepository;

    @Autowired
    private RacaRepository racaRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private PacoteRepository pacoteRepository;

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private ServicoRepository servicoRepository;

    @Autowired
    private AgendaRepository agendaRepository;

    @Autowired
    private AgendaServicoRepository agendaServicoRepository;

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

            //Populando Tabelas
            //PACOTE
            jdbcTemplate.update("INSERT INTO PACOTE (tipo) VALUES ('Mensal')");
            jdbcTemplate.update("INSERT INTO PACOTE (tipo) VALUES ('Quinzenal')");

            //SERVICO
            List<Servico> servicos = List.of(
                    new Servico("Banho", 30.0),
                    new Servico("Tosa", 40.0),
                    new Servico("Hidratação", 50.0)
            );
            servicoRepository.saveAll(servicos);

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

            //PORTE
            List<Porte> portes = List.of(
                    new Porte("Pequeno"),
                    new Porte("Médio"),
                    new Porte("Grande")
            );
            porteRepository.saveAll(portes);

            //RAÇA
            Raca raca = new Raca();
            raca.setNome("Raça Test");
            raca.setPorte(portes.get(0));
            racaRepository.save(raca);

            //PET
            Pet pet = new Pet();
            pet.setRaca(raca);
            pet.setNome("Pet Test");
            pet.setCliente(cliente);
            cliente.getPets().add(pet);
            petRepository.save(pet);

            cliente.getPets().add(pet);
            clienteRepository.save(cliente);

            //AGENDA
            Agenda agenda = new Agenda();
            agenda.setPet(pet);
            agenda.setDataHoraInicio(LocalDateTime.now());
            agenda.setDataHoraFim(LocalDateTime.now().plusHours(1));
            agenda.setValor(30.0);
            agendaRepository.save(agenda);

            //AGENDA_SERVICO
            AgendaServico agendaServico = new AgendaServico();
            agendaServico.setAgenda(agenda);
            agendaServico.setServico(servicos.get(0));
            agendaServicoRepository.save(agendaServico);

            //PACOTE (expirado)
            Pacote pacote = new Pacote();
            pacote.setPacoteId(1);
            pacote.setCliente(cliente);
            pacote.setDataInicio(LocalDate.now().minusDays(31));
//            pacote.setDataExpiracao(LocalDate.now().minusDays(1));
            pacote.setDataExpiracao(LocalDate.now());
            pacoteRepository.save(pacote);

            //PACOTE (ativo)
            pacote = new Pacote();
            pacote.setPacoteId(1);
            pacote.setCliente(cliente);
            pacote.setDataInicio(LocalDate.now());
            pacote.setDataExpiracao(LocalDate.now().plusDays(31));
            pacoteRepository.save(pacote);

            //PRODUTOS
            List<ProdutoDTO> produtos = new ArrayList<ProdutoDTO>();
            produtos.add(new ProdutoDTO(1, "Conta de Luz"));
            produtos.add(new ProdutoDTO(2, "Máquina de Tosa"));
            produtos.add(new ProdutoDTO(3, "Algodão"));
            produtos.add(new ProdutoDTO(4, "Shampoo"));
            produtoService.cadastrar(produtos);
        };
    }
}