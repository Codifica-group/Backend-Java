package com.codifica.elevebot.config;

import com.codifica.elevebot.adapter.ProdutoAdapter;
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

import static com.codifica.elevebot.model.CategoriaProduto.*;

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
    private ProdutoRepository produtoRepository;

    @Autowired
    private DespesaRepository despesaRepository;

    @Autowired
    private ServicoRepository servicoRepository;

    @Autowired
    private AgendaRepository agendaRepository;

    @Autowired
    private AgendaServicoRepository agendaServicoRepository;

    @Autowired
    private CategoriaProdutoRepository categoriaProdutoRepository;

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
            List<Cliente> clientes = new ArrayList<>(List.of(
                    new Cliente("Cliente Test", "11900000000", "01001000", 0, ""),
                    new Cliente("Cliente Test 2", "11900000001", "06020010", 1400, ""),
                    new Cliente("Cliente Test 3", "11900000002", "06020194", 200, "")
            ));
            clienteRepository.saveAll(clientes);

            //PORTE
            List<Porte> portes = List.of(
                    new Porte("Pequeno"),
                    new Porte("Médio"),
                    new Porte("Grande")
            );
            porteRepository.saveAll(portes);

            //RAÇA
            List<Raca> racas = List.of(
                    new Raca("Raça Test", portes.get(0)),
                    new Raca("Raça Test 2", portes.get(1)),
                    new Raca("Raça Test 3", portes.get(2))
            );
            racaRepository.saveAll(racas);

            // PETS
            List<Pet> pets = new ArrayList<>(List.of(
                    new Pet(racas.get(0), "Pet Test", clientes.get(0)),
                    new Pet(racas.get(1), "Pet Test 2", clientes.get(0)),
                    new Pet(racas.get(2), "Pet Test 3", clientes.get(1)),
                    new Pet(racas.get(2), "Pet Test", clientes.get(2))
            ));

            for (Pet pet : pets) {
                for (Cliente cliente : clientes) {
                    if (pet.getCliente().equals(cliente)) {
                        cliente.getPets().add(pet);
                        break;
                    }
                }
            }

            petRepository.saveAll(pets);

            //PACOTE (expirado)
            Pacote pacote = new Pacote();
            pacote.setPacoteId(1);
            pacote.setCliente(clientes.get(0));
            pacote.setDataInicio(LocalDate.now().minusDays(31));
//            pacote.setDataExpiracao(LocalDate.now().minusDays(1));
            pacote.setDataExpiracao(LocalDate.now());
            pacoteRepository.save(pacote);

            //PACOTE (ativo)
            pacote = new Pacote();
            pacote.setPacoteId(1);
            pacote.setCliente(clientes.get(0));
            pacote.setDataInicio(LocalDate.now());
            pacote.setDataExpiracao(LocalDate.now().plusDays(31));
            pacoteRepository.save(pacote);

            //CATEGORIA_PRODUTO
            List<CategoriaProduto> categorias = List.of(
                    new CategoriaProduto("Gasto Fixo"),
                    new CategoriaProduto("Manutenção"),
                    new CategoriaProduto("Insumo")
            );
            categoriaProdutoRepository.saveAll(categorias);

            //PRODUTOS
            List<Produto> produtos = List.of(
                    new Produto(categorias.get(0), "Conta de Luz"),
                    new Produto(categorias.get(1), "Máquina de Tosa"),
                    new Produto(categorias.get(2), "Algodão")
            );
            produtoRepository.saveAll(produtos);

            //DESPESAS
            List<Despesa> despesas = List.of(
                    new Despesa(produtos.get(0), 150.0, LocalDate.now()),
                    new Despesa(produtos.get(1), 90.0, LocalDate.now()),
                    new Despesa(produtos.get(2), 5.0, LocalDate.now())
            );
            despesaRepository.saveAll(despesas);

            // AGENDAS
            LocalDateTime dataHoraInicio = LocalDateTime.now().withHour(8).withMinute(0).withSecond(0).withNano(0);

            // Pet 1: Banho (08:00 - 09:00, R$30,00)
            Agenda agenda1 = new Agenda();
            agenda1.setPet(pets.get(0));
            agenda1.setDataHoraInicio(dataHoraInicio);
            agenda1.setDataHoraFim(dataHoraInicio.plusHours(1));
            agendaRepository.save(agenda1);

            AgendaServico agendaServico1 = new AgendaServico();
            agendaServico1.setAgenda(agenda1);
            agendaServico1.setServico(servicos.get(0));
            agendaServico1.setValor(servicos.get(0).getValorBase());
            agendaServicoRepository.save(agendaServico1);

            dataHoraInicio = dataHoraInicio.plusHours(1).plusSeconds(1);

            // Pet 1: Tosa (09:00 - 10:00, R$40,00)
            Agenda agenda2 = new Agenda();
            agenda2.setPet(pets.get(0));
            agenda2.setDataHoraInicio(dataHoraInicio);
            agenda2.setDataHoraFim(dataHoraInicio.plusHours(1));
            agendaRepository.save(agenda2);

            AgendaServico agendaServico2 = new AgendaServico();
            agendaServico2.setAgenda(agenda2);
            agendaServico2.setServico(servicos.get(1));
            agendaServico2.setValor(servicos.get(1).getValorBase());
            agendaServicoRepository.save(agendaServico2);

            dataHoraInicio = dataHoraInicio.plusHours(1).plusSeconds(1);

            // Pet 2: Hidratação (10:00 - 11:00, R$50,00)
            Agenda agenda3 = new Agenda();
            agenda3.setPet(pets.get(1));
            agenda3.setDataHoraInicio(dataHoraInicio);
            agenda3.setDataHoraFim(dataHoraInicio.plusHours(1));
            agendaRepository.save(agenda3);

            AgendaServico agendaServico3 = new AgendaServico();
            agendaServico3.setAgenda(agenda3);
            agendaServico3.setServico(servicos.get(2));
            agendaServico3.setValor(servicos.get(2).getValorBase());
            agendaServicoRepository.save(agendaServico3);

            dataHoraInicio = dataHoraInicio.plusHours(1).plusSeconds(1);

            // Pet 2: Banho + Tosa (11:00 - 12:00, R$70,00)
            Agenda agenda4 = new Agenda();
            agenda4.setPet(pets.get(1));
            agenda4.setDataHoraInicio(dataHoraInicio);
            agenda4.setDataHoraFim(dataHoraInicio.plusHours(1));
            agendaRepository.save(agenda4);

            AgendaServico agendaServico4a = new AgendaServico();
            agendaServico4a.setAgenda(agenda4);
            agendaServico4a.setServico(servicos.get(0));
            agendaServico4a.setValor(servicos.get(0).getValorBase());
            agendaServicoRepository.save(agendaServico4a);

            AgendaServico agendaServico4b = new AgendaServico();
            agendaServico4b.setAgenda(agenda4);
            agendaServico4b.setServico(servicos.get(1));
            agendaServico4b.setValor(servicos.get(1).getValorBase());
            agendaServicoRepository.save(agendaServico4b);

            dataHoraInicio = dataHoraInicio.plusHours(1).plusSeconds(1);

            // Pet 3: Banho + Hidratação (12:00 - 13:00, R$80,00)
            Agenda agenda5 = new Agenda();
            agenda5.setPet(pets.get(2));
            agenda5.setDataHoraInicio(dataHoraInicio);
            agenda5.setDataHoraFim(dataHoraInicio.plusHours(1));
            agendaRepository.save(agenda5);

            AgendaServico agendaServico5a = new AgendaServico();
            agendaServico5a.setAgenda(agenda5);
            agendaServico5a.setServico(servicos.get(0));
            agendaServico5a.setValor(servicos.get(0).getValorBase());
            agendaServicoRepository.save(agendaServico5a);

            AgendaServico agendaServico5b = new AgendaServico();
            agendaServico5b.setAgenda(agenda5);
            agendaServico5b.setServico(servicos.get(2));
            agendaServico5b.setValor(servicos.get(2).getValorBase());
            agendaServicoRepository.save(agendaServico5b);

            dataHoraInicio = dataHoraInicio.plusHours(1).plusSeconds(1);

            // Pet 3: Tosa + Hidratação (13:00 - 14:00, R$90,00)
            Agenda agenda6 = new Agenda();
            agenda6.setPet(pets.get(2));
            agenda6.setDataHoraInicio(dataHoraInicio);
            agenda6.setDataHoraFim(dataHoraInicio.plusHours(1));
            agendaRepository.save(agenda6);

            AgendaServico agendaServico6a = new AgendaServico();
            agendaServico6a.setAgenda(agenda6);
            agendaServico6a.setServico(servicos.get(1));
            agendaServico6a.setValor(servicos.get(1).getValorBase());
            agendaServicoRepository.save(agendaServico6a);

            AgendaServico agendaServico6b = new AgendaServico();
            agendaServico6b.setAgenda(agenda6);
            agendaServico6b.setServico(servicos.get(2));
            agendaServico6b.setValor(servicos.get(2).getValorBase());
            agendaServicoRepository.save(agendaServico6b);

            dataHoraInicio = dataHoraInicio.plusHours(1).plusSeconds(1);

            // Pet 4: Banho + Tosa + Hidratação (14:00 - 15:00, R$120,00)
            Agenda agenda7 = new Agenda();
            agenda7.setPet(pets.get(3));
            agenda7.setDataHoraInicio(dataHoraInicio);
            agenda7.setDataHoraFim(dataHoraInicio.plusHours(1));
            agendaRepository.save(agenda7);

            for (Servico servico : servicos) {
                AgendaServico agendaServico7 = new AgendaServico();
                agendaServico7.setAgenda(agenda7);
                agendaServico7.setServico(servico);
                agendaServico7.setValor(servico.getValorBase());
                agendaServicoRepository.save(agendaServico7);
            }

            dataHoraInicio = dataHoraInicio.plusHours(1).plusSeconds(1);

            // Pet 4: Banho (15:00 - 16:00, R$30,00)
            Agenda agenda8 = new Agenda();
            agenda8.setPet(pets.get(3));
            agenda8.setDataHoraInicio(dataHoraInicio);
            agenda8.setDataHoraFim(dataHoraInicio.plusHours(1));
            agendaRepository.save(agenda8);

            AgendaServico agendaServico8 = new AgendaServico();
            agendaServico8.setAgenda(agenda8);
            agendaServico8.setServico(servicos.get(0));
            agendaServico8.setValor(servicos.get(0).getValorBase());
            agendaServicoRepository.save(agendaServico8);
        };
    }
}