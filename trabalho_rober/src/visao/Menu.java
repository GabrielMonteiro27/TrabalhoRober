package visao;

import java.util.List;

import excecao.SistemaEventosException;
import mensagens.Mensagens;
import mensagens.MensagensPrograma;
import modelo.Inscricao;
import modelo.Organizador;
import modelo.Participante;
import modelo.Pessoa;
import servico.GerenciadorEventos;

/**
 * Menus do console.
 *
 * Seções do arquivo (combinar quem edita cada uma, para evitar conflito no Git):
 * menu principal e Pessoas (Anderson, cartão 11), Eventos e Inscrições
 * (Uriel, cartão 12), Check-in (Heitor, cartão 14) e Relatórios (Uriel, cartão 15).
 *
 * Padrão visual dos menus (Telas 1 e 2 da documentação): título entre traços,
 * opções numeradas, 0 volta ao nível anterior, erros com "[ERRO]" e sucesso
 * com "[OK]".
 */
public class Menu {

    private static final String LINHA = "------------------------------------------";

    private final GerenciadorEventos gerenciador;
    private final LeitorEntrada leitor;

    /**
     * @param gerenciador serviço com as regras de negócio
     * @param leitor      leitor de entrada validada
     */
    public Menu(GerenciadorEventos gerenciador, LeitorEntrada leitor) {
        this.gerenciador = gerenciador;
        this.leitor = leitor;
    }

    // ------------------------- Menu principal (Tela 1) -------------------------

    /** Mostra o menu principal e repete até o usuário escolher "Salvar e sair". */
    public void executar() {
        int opcao;
        do {
            System.out.println();
            System.out.println("==========================================");
            System.out.println("SISTEMA DE GERENCIAMENTO DE EVENTOS");
            System.out.println("==========================================");
            // TODO (cartão 8): incluir a quantidade de eventos, ex.: "12 pessoas, 3 eventos"
            System.out.println("Dados carregados: " + gerenciador.listarPessoas().size() + " pessoas.");
            System.out.println("1. Pessoas");
            System.out.println("2. Eventos");
            System.out.println("3. Inscrições");
            System.out.println("4. Check-in (registrar presença)");
            System.out.println("5. Relatórios");
            System.out.println("0. Salvar e sair");
            System.out.println(LINHA);
            opcao = leitor.lerInteiro("Escolha uma opção: ", 0, 5);

            switch (opcao) {
                case 1:
                    menuPessoas();
                    break;
                case 2:
                    menuEventos();
                    break;
                case 3:
                    menuInscricoes();
                    break;
                case 4:
                    menuCheckIn();
                    break;
                case 5:
                    menuRelatorios();
                    break;
                default:
                    break;
            }
        } while (opcao != 0);
    }

    // ------------------------------ Pessoas (Tela 2) ------------------------------

    /** Submenu Pessoas: cadastrar, listar, buscar, editar e remover. */
    public void menuPessoas() {
        int opcao;
        do {
            System.out.println();
            System.out.println("---------- PESSOAS ----------");
            System.out.println("1. Cadastrar participante");
            System.out.println("2. Cadastrar organizador");
            System.out.println("3. Listar pessoas");
            System.out.println("4. Buscar por CPF");
            System.out.println("5. Editar pessoa");
            System.out.println("6. Remover pessoa");
            System.out.println("0. Voltar");
            opcao = leitor.lerInteiro("Escolha uma opção: ", 0, 6);

            // Um único catch trata qualquer exceção do sistema (RN01, RN05, RN09...)
            try {
                switch (opcao) {
                    case 1:
                        cadastrarParticipante();
                        break;
                    case 2:
                        cadastrarOrganizador();
                        break;
                    case 3:
                        listarPessoas();
                        break;
                    case 4:
                        buscarPessoa();
                        break;
                    case 5:
                        editarPessoa();
                        break;
                    case 6:
                        removerPessoa();
                        break;
                    default:
                        break;
                }
            } catch (SistemaEventosException | IllegalArgumentException e) {
                System.out.println("[ERRO] " + e.getMessage());
            }
        } while (opcao != 0);
    }

    private void cadastrarParticipante() throws SistemaEventosException {
        System.out.println("------ CADASTRAR PARTICIPANTE ------");
        String nome = leitor.lerTexto("Nome: ");
        String cpf = leitor.lerCpf("CPF: ");
        String email = leitor.lerEmail("E-mail: ");
        String instituicao = leitor.lerTexto("Instituição: ");
        gerenciador.cadastrarPessoa(new Participante(nome, cpf, email, instituicao));
        System.out.println("[OK] Participante cadastrado com sucesso.");
    }

    private void cadastrarOrganizador() throws SistemaEventosException {
        System.out.println("------ CADASTRAR ORGANIZADOR ------");
        String nome = leitor.lerTexto("Nome: ");
        String cpf = leitor.lerCpf("CPF: ");
        String email = leitor.lerEmail("E-mail: ");
        String cargo = leitor.lerTexto("Cargo: ");
        gerenciador.cadastrarPessoa(new Organizador(nome, cpf, email, cargo));
        System.out.println("[OK] Organizador cadastrado com sucesso.");
    }

    private void listarPessoas() {
        List<Pessoa> pessoas = gerenciador.listarPessoas();
        System.out.println("------------ PESSOAS ------------");
        if (pessoas.isEmpty()) {
            System.out.println("Nenhuma pessoa cadastrada.");
            return;
        }
        // Polimorfismo: cada tipo de pessoa responde getDetalhe() do seu jeito
        for (Pessoa p : pessoas) {
            System.out.println(p);
            System.out.println("    " + p.getDetalhe());
        }
    }

    private void buscarPessoa() throws SistemaEventosException {
        String cpf = leitor.lerCpf("CPF: ");
        Pessoa p = gerenciador.buscarPessoa(cpf);
        System.out.println(p);
        System.out.println("    " + p.getDetalhe());
    }

    private void editarPessoa() throws SistemaEventosException {
        String cpf = leitor.lerCpf("CPF da pessoa a editar: ");
        Pessoa p = gerenciador.buscarPessoa(cpf);
        System.out.println("Editando: " + p);
        p.setNome(leitor.lerTexto("Novo nome: "));
        p.setEmail(leitor.lerEmail("Novo e-mail: "));
        gerenciador.salvar();
        System.out.println("[OK] Pessoa atualizada com sucesso.");
    }

    private void removerPessoa() throws SistemaEventosException {
        String cpf = leitor.lerCpf("CPF da pessoa a remover: ");
        Pessoa p = gerenciador.buscarPessoa(cpf);
        String resposta = leitor.lerTexto("Remover " + p.getNome() + "? (s/n): ");
        if (resposta.equalsIgnoreCase("s")) {
            gerenciador.removerPessoa(cpf);
            System.out.println("[OK] Pessoa removida.");
        } else {
            System.out.println("Remoção cancelada.");
        }
    }

    // ------------------- Eventos e Inscrições (Uriel, cartão 12) -------------------

    /** Submenu Eventos. TODO (Uriel, cartão 12). */
    public void menuEventos() {
        System.out.println("[ERRO] Seção Eventos ainda não implementada.");
    }

    /** Submenu Inscrições. TODO (Uriel, cartão 12). */
    public void menuInscricoes() {
        System.out.println("[ERRO] Seção Inscrições ainda não implementada.");
    }

    // ------------------------------ Check-in ------------------------------

    /** Check-in (parte do Heitor). */
    public void menuCheckIn() {
        try {
            String codigo = leitor.lerTexto(MensagensPrograma.pegar(Mensagens.PEDIR_CODIGO_EVENTO));
            List<Inscricao> inscricoes = gerenciador.listarInscricoes(codigo);

            if (inscricoes.isEmpty()) {
                System.out.println(MensagensPrograma.pegar(Mensagens.EVENTO_SEM_INSCRICOES));
                return;
            }

            System.out.println(MensagensPrograma.pegar(Mensagens.TITULO_INSCRICOES));
            for (Inscricao i : inscricoes) {
                System.out.println(i);
            }

            int numero = leitor.lerInteiro(MensagensPrograma.pegar(Mensagens.PEDIR_NUMERO_CHECKIN));
            gerenciador.registrarPresenca(codigo, numero);
            System.out.println(MensagensPrograma.pegar(Mensagens.PRESENCA_OK));
        } catch (SistemaEventosException | IllegalArgumentException | IllegalStateException e) {
            System.out.println(MensagensPrograma.pegar(Mensagens.ERRO, e.getMessage()));
        }
    }

    // ------------------------ Relatórios (Uriel, cartão 15) ------------------------

    /** Submenu Relatórios. TODO (Uriel, cartão 15). */
    public void menuRelatorios() {
        System.out.println("[ERRO] Seção Relatórios ainda não implementada.");
    }
}
