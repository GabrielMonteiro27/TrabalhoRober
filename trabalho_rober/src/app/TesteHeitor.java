package app;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import excecao.SistemaEventosException;
import mensagens.Mensagens;
import mensagens.MensagensPrograma;
import modelo.Evento;
import modelo.Participante;
import servico.GerenciadorEventos;
import servico.OpcoesDisponiveis;
import visao.LeitorEntrada;
import visao.Menu;

/** PROVISÓRIO: menu de teste só para a parte do Heitor. Não vai para o projeto final. */
public class TesteHeitor {
    public static void main(String[] args) {

        GerenciadorEventos g = new GerenciadorEventos();
        // Aqui eu cadastro alguns usuarios e eventos para teste.
        /*
        try {
            g.adicionarPessoa(new Participante("111", "Ana"));
            g.adicionarPessoa(new Participante("222", "Bruno"));
            g.adicionarEvento(new Evento("E1", "Palestra Java", 1, LocalDate.now().plusDays(7)));
            g.adicionarEvento(new Evento("E2", "Workshop Encerrado", 10, LocalDate.now().minusDays(1)));
        } catch (Exception e) {
            System.out.println(MensagensPrograma.pegar(Mensagens.ERRO, e.getMessage()));
        }
        */
        g.carregar();

        // INicio o leitor de entrada e o menu, que vai chamar o gerenciador para fazer as operações.
        LeitorEntrada leitor = new LeitorEntrada();
        Menu menu = new Menu(g, leitor);

        System.out.println(MensagensPrograma.pegar(Mensagens.TESTE_INFO));
        boolean emExecucao = true;

        while ( emExecucao ) {
            OpcoesDisponiveis op = OpcoesDisponiveis.CADASTRAR_PESSOA;
            do {
                System.out.println(MensagensPrograma.pegar(Mensagens.TESTE_MENU));
                try {
                    op = OpcoesDisponiveis.inteiroParaOpcao(leitor.lerInteiro(MensagensPrograma.pegar(Mensagens.PEDIR_OPCAO)));
                    switch (op) {
                        case OpcoesDisponiveis.SAIR:
                            emExecucao = false;
                            break;
                        case OpcoesDisponiveis.INSCRICAO:
                            String cpf = leitor.lerTexto(MensagensPrograma.pegar(Mensagens.PEDIR_CPF));
                            String cod = leitor.lerTexto(MensagensPrograma.pegar(Mensagens.PEDIR_CODIGO_EVENTO));
                            System.out.println(MensagensPrograma.pegar(Mensagens.INSCRICAO_OK, g.inscrever(cpf, cod)));
                            break;
                        case OpcoesDisponiveis.CANCELAR_INSCRICAO:
                            String c = leitor.lerTexto(MensagensPrograma.pegar(Mensagens.PEDIR_CODIGO_EVENTO));
                            g.cancelarInscricao(c, leitor.lerInteiro(MensagensPrograma.pegar(Mensagens.PEDIR_NUMERO_INSCRICAO)));
                            System.out.println(MensagensPrograma.pegar(Mensagens.INSCRICAO_CANCELADA));
                            break;
                        case OpcoesDisponiveis.CHECKIN:
                            menu.menuCheckIn();
                            break;
                        case OpcoesDisponiveis.CADASTRAR_PESSOA:
                            String cpfNovo = leitor.lerTexto(MensagensPrograma.pegar(Mensagens.PEDIR_CPF));
                            String nomeNovo = leitor.lerTexto(MensagensPrograma.pegar(Mensagens.PEDIR_NOME));
                            g.adicionarPessoa(new Participante(cpfNovo, nomeNovo));
                            break;
                        case OpcoesDisponiveis.CADASTRAR_EVENTO:
                            String codigoEvento = leitor.lerTexto(MensagensPrograma.pegar(Mensagens.PEDIR_CODIGO_EVENTO));
                            String nomeEvento = leitor.lerTexto(MensagensPrograma.pegar(Mensagens.PEDIR_NOME));
                            int capacidade = leitor.lerInteiro(MensagensPrograma.pegar(Mensagens.PEDIR_CAPACIDADE));
                            String data = leitor.lerTexto(MensagensPrograma.pegar(Mensagens.PEDIR_DATA));
                            g.adicionarEvento(new Evento(codigoEvento, nomeEvento, capacidade, LocalDate.parse(data, DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
                            break;
                        case OpcoesDisponiveis.LISTAR_PESSOAS:
                            g.listarCadastradosTela();
                            break;
                        case OpcoesDisponiveis.LISTAR_EVENTOS:
                            g.listaEventosTela();
                            break;
                        default:
                            break;
                    }
                } catch (SistemaEventosException | IllegalArgumentException | IllegalStateException e) {
                    System.out.println(MensagensPrograma.pegar(Mensagens.ERRO, e.getMessage()));
                    op = OpcoesDisponiveis.CADASTRAR_PESSOA;
                }
            } while (op != OpcoesDisponiveis.SAIR);
        }
    }
}
