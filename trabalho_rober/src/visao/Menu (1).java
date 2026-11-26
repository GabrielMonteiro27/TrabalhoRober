package visao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import excecao.SistemaEventosException;
import modelo.Evento;
import modelo.EventoOnline;
import modelo.EventoPresencial;
import modelo.Inscricao;
import modelo.Organizador;
import modelo.Participante;
import modelo.Pessoa;
import modelo.StatusEvento;
import servico.GerenciadorEventos;

/**
 * Menus do console.
 *
 * Seções do arquivo (combinar quem edita cada uma, para evitar conflito no Git):
 * menu principal e Pessoas (Anderson, cartão 11), Eventos e Inscrições
 * (Uriel, cartão 12), Check-in (Heitor, cartão 14) e Relatórios (Uriel, cartão 15).
 *
 * Padrão visual dos menus: título entre traços,
 * opções numeradas, 0 volta ao nível anterior, erros com "[ERRO]" e sucesso
 * com "[OK]".
 *
 * Cada submenu tem um try/catch só, pegando SistemaEventosException (que é
 * mãe de todas as nossas exceções) e IllegalArgumentException (que os setters
 * lançam). Assim o programa nunca fecha por causa de um erro de regra.
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

    // ------------------------- Menu principal -------------------------

    /** Mostra o menu principal e repete até o usuário escolher "Salvar e sair". */
    public void executar() {
        int opcao;
        do {
            System.out.println();
            System.out.println("==========================================");
            System.out.println(" SISTEMA DE GERENCIAMENTO DE EVENTOS");
            System.out.println("==========================================");
            System.out.println("Dados carregados: " + gerenciador.getQuantidadePessoas() + " pessoas, "
                    + gerenciador.getQuantidadeEventos() + " eventos.");
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

    // ------------------------------ Pessoas (Anderson) ------------------------------

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

            // Um único catch trata qualquer exceção do sistema
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
                leitor.mostrarErro(e.getMessage());
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
        // Polimorfismo: cada tipo de pessoa responde getTipo() e getDetalhe() do seu jeito
        for (Pessoa p : pessoas) {
            mostrarPessoa(p);
        }
    }

    private void mostrarPessoa(Pessoa p) {
        System.out.println(p.getTipo() + ": " + p.getNome() + " (CPF " + p.getCpfFormatado() + ", " + p.getEmail() + ")");
        System.out.println("    " + p.getDetalhe());
    }

    private void buscarPessoa() throws SistemaEventosException {
        String cpf = leitor.lerCpf("CPF: ");
        Pessoa p = gerenciador.buscarPessoa(cpf);
        mostrarPessoa(p);
    }

    private void editarPessoa() throws SistemaEventosException {
        String cpf = leitor.lerCpf("CPF da pessoa a editar: ");
        Pessoa p = gerenciador.buscarPessoa(cpf);
        System.out.println("Editando: ");
        mostrarPessoa(p);
        System.out.println("O que deseja alterar?");
        System.out.println("1. Nome");
        System.out.println("2. E-mail");
        if (p instanceof Participante) {
            System.out.println("3. Instituição");
        } else {
            System.out.println("3. Cargo");
        }
        System.out.println("0. Voltar");
        int opcao = leitor.lerInteiro("Escolha uma opção: ", 0, 3);

        // o CPF não pode ser alterado porque ele identifica a pessoa
        switch (opcao) {
            case 1:
                p.setNome(leitor.lerTexto("Novo nome: "));
                break;
            case 2:
                p.setEmail(leitor.lerEmail("Novo e-mail: "));
                break;
            case 3:
                if (p instanceof Participante) {
                    ((Participante) p).setInstituicao(leitor.lerTexto("Nova instituição: "));
                } else {
                    ((Organizador) p).setCargo(leitor.lerTexto("Novo cargo: "));
                }
                break;
            default:
                return;
        }
        gerenciador.salvar();
        System.out.println("[OK] Pessoa atualizada com sucesso.");
    }

    private void removerPessoa() throws SistemaEventosException {
        String cpf = leitor.lerCpf("CPF da pessoa a remover: ");
        Pessoa p = gerenciador.buscarPessoa(cpf);
        if (leitor.lerSimNao("Remover " + p.getNome() + "?")) {
            gerenciador.removerPessoa(cpf);
            System.out.println("[OK] Pessoa removida.");
        } else {
            System.out.println("Remoção cancelada.");
        }
    }

    // ------------------------------ Eventos (Uriel) ------------------------------

    /** Tela 2: submenu Eventos. */
    public void menuEventos() {
        int opcao;
        do {
            System.out.println();
            System.out.println("---------- EVENTOS ----------");
            System.out.println("1. Cadastrar evento presencial");
            System.out.println("2. Cadastrar evento online");
            System.out.println("3. Listar eventos");
            System.out.println("4. Ver detalhes de um evento");
            System.out.println("5. Editar evento");
            System.out.println("6. Cancelar evento");
            System.out.println("0. Voltar");
            opcao = leitor.lerInteiro("Escolha uma opção: ", 0, 6);

            // Um único catch: pega as exceções do sistema e as dos setters
            try {
                switch (opcao) {
                    case 1:
                        cadastrarEvento(true);
                        break;
                    case 2:
                        cadastrarEvento(false);
                        break;
                    case 3:
                        listarEventos();
                        break;
                    case 4:
                        detalharEvento();
                        break;
                    case 5:
                        editarEvento();
                        break;
                    case 6:
                        cancelarEvento();
                        break;
                    default:
                        break;
                }
            } catch (SistemaEventosException | IllegalArgumentException e) {
                leitor.mostrarErro(e.getMessage());
            }
        } while (opcao != 0);
    }

    /**
     * Tela 3: cadastro de evento presencial ou online. A primeira parte é
     * igual para os dois tipos; só o final muda (endereço e sala, ou link
     * e plataforma).
     *
     * @param presencial true para presencial, false para online
     */
    private void cadastrarEvento(boolean presencial) throws SistemaEventosException {
        if (presencial) {
            System.out.println("------ CADASTRAR EVENTO PRESENCIAL ------");
        } else {
            System.out.println("------ CADASTRAR EVENTO ONLINE ------");
        }
        String titulo = leitor.lerTexto("Título: ");
        String descricao = leitor.lerTexto("Descrição: ");
        LocalDateTime dataHora = leitor.lerDataHora("Data e hora");
        LocalDate dataLimite = leitor.lerDataLimite("Data-limite de inscrição", dataHora);
        int vagas = leitor.lerInteiro("Número de vagas: ", 1, 5000);
        String cpfOrganizador = leitor.lerCpf("CPF do organizador: ");
        // busca já aqui para avisar se o CPF estiver errado antes de pedir o resto
        gerenciador.buscarOrganizador(cpfOrganizador);

        Evento ev;
        if (presencial) {
            String endereco = leitor.lerTexto("Endereço: ");
            String sala = leitor.lerTexto("Sala: ");
            ev = gerenciador.cadastrarEventoPresencial(titulo, descricao, dataHora, dataLimite,
                    vagas, cpfOrganizador, endereco, sala);
        } else {
            String link = leitor.lerTexto("Link: ");
            String plataforma = leitor.lerTexto("Plataforma: ");
            ev = gerenciador.cadastrarEventoOnline(titulo, descricao, dataHora, dataLimite,
                    vagas, cpfOrganizador, link, plataforma);
        }
        System.out.println("[OK] Evento " + ev.getCodigo() + " cadastrado com sucesso.");
    }

    /** Tela 5 (parte 1): lista os eventos em ordem de data e oferece ver os detalhes. */
    private void listarEventos() throws SistemaEventosException {
        List<Evento> eventos = gerenciador.listarEventos();
        System.out.println("------------------------------------- EVENTOS -------------------------------------");
        if (eventos.isEmpty()) {
            System.out.println("Nenhum evento cadastrado.");
            return;
        }
        System.out.printf("%-7s %-17s %-28s %-11s %-9s %s%n",
                "CÓDIGO", "DATA", "TÍTULO", "TIPO", "VAGAS", "STATUS");
        for (Evento ev : eventos) {
            String vagas = ev.getVagasDisponiveis() + "/" + ev.getVagas();
            System.out.printf("%-7s %-17s %-28s %-11s %-9s %s%n",
                    ev.getCodigo(), ev.getDataHoraFormatada(), ev.getTitulo(),
                    ev.getTipo(), vagas, ev.getStatus());
        }
        System.out.println("(VAGAS = livres/total)");

        String codigo = leitor.lerCodigoEvento("Detalhar qual evento? (código ou 0 para voltar): ");
        if (!codigo.equals("0")) {
            mostrarDetalhesEvento(gerenciador.buscarEvento(codigo));
        }
    }

    /** Tela 5 (parte 2): pede o código e mostra os detalhes. */
    private void detalharEvento() throws SistemaEventosException {
        String codigo = leitor.lerCodigoEvento("Código do evento: ");
        mostrarDetalhesEvento(gerenciador.buscarEvento(codigo));
    }

    /**
     * Tela 5 (parte 3): mostra tudo de um evento. O getInformacoesAcesso()
     * é polimórfico: o presencial mostra o local e o online mostra o link.
     */
    private void mostrarDetalhesEvento(Evento ev) {
        Organizador org = ev.getOrganizador();
        System.out.println("------------------------------------------");
        System.out.println(" " + ev.getCodigo() + " - " + ev.getTitulo() + " - " + ev.getTipo());
        System.out.println(" Descrição: " + ev.getDescricao());
        System.out.println(" Data: " + ev.getDataHoraFormatada());
        System.out.println(" Inscrições até: " + ev.getDataLimiteFormatada());
        System.out.println(" " + ev.getInformacoesAcesso());
        System.out.println(" Organizador: " + org.getNome() + " - " + org.getCargo());
        System.out.println(" Vagas: " + ev.getVagasDisponiveis() + " livres de " + ev.getVagas());
        System.out.println(" Status: " + ev.getStatus());
        System.out.println("------------------------------------------");
    }

    /** Edita um campo do evento. Evento cancelado ou já realizado não é editado. */
    private void editarEvento() throws SistemaEventosException {
        String codigo = leitor.lerCodigoEvento("Código do evento a editar: ");
        Evento ev = gerenciador.buscarEvento(codigo);
        if (ev.getStatus() == StatusEvento.CANCELADO) {
            throw new SistemaEventosException("Não é possível editar um evento cancelado.");
        }
        if (ev.getStatus() == StatusEvento.REALIZADO) {
            throw new SistemaEventosException("Não é possível editar um evento que já foi realizado.");
        }
        mostrarDetalhesEvento(ev);

        System.out.println("O que deseja alterar?");
        System.out.println("1. Título");
        System.out.println("2. Descrição");
        System.out.println("3. Data e hora / data-limite");
        System.out.println("4. Número de vagas");
        if (ev instanceof EventoPresencial) {
            System.out.println("5. Endereço e sala");
        } else {
            System.out.println("5. Link e plataforma");
        }
        System.out.println("0. Voltar");
        int opcao = leitor.lerInteiro("Escolha uma opção: ", 0, 5);

        switch (opcao) {
            case 1:
                ev.setTitulo(leitor.lerTexto("Novo título: "));
                break;
            case 2:
                ev.setDescricao(leitor.lerTexto("Nova descrição: "));
                break;
            case 3:
                LocalDateTime novaData = leitor.lerDataHora("Nova data e hora");
                LocalDate novoLimite = leitor.lerDataLimite("Nova data-limite de inscrição", novaData);
                ev.alterarDatas(novaData, novoLimite);
                break;
            case 4:
                // não deixa colocar menos vagas do que já há de inscritos
                int minimo = Math.max(1, ev.getQuantidadeConfirmadas());
                ev.setVagas(leitor.lerInteiro("Novo número de vagas: ", minimo, 5000));
                break;
            case 5:
                if (ev instanceof EventoPresencial) {
                    EventoPresencial presencial = (EventoPresencial) ev;
                    presencial.setEndereco(leitor.lerTexto("Novo endereço: "));
                    presencial.setSala(leitor.lerTexto("Nova sala: "));
                } else {
                    EventoOnline online = (EventoOnline) ev;
                    online.setLink(leitor.lerTexto("Novo link: "));
                    online.setPlataforma(leitor.lerTexto("Nova plataforma: "));
                }
                break;
            default:
                return;
        }
        gerenciador.salvar();
        System.out.println("[OK] Evento atualizado com sucesso.");
    }

    /** Cancela o evento (e todas as inscrições dele) depois de pedir confirmação. */
    private void cancelarEvento() throws SistemaEventosException {
        String codigo = leitor.lerCodigoEvento("Código do evento a cancelar: ");
        Evento ev = gerenciador.buscarEvento(codigo);
        if (ev.getStatus() == StatusEvento.CANCELADO) {
            throw new SistemaEventosException("O evento " + ev.getCodigo() + " já está cancelado.");
        }
        System.out.println("Atenção: as " + ev.getQuantidadeConfirmadas()
                + " inscrições confirmadas também serão canceladas.");
        if (leitor.lerSimNao("Cancelar o evento \"" + ev.getTitulo() + "\"?")) {
            gerenciador.cancelarEvento(codigo);
            System.out.println("[OK] Evento " + ev.getCodigo() + " cancelado.");
        } else {
            System.out.println("O evento não foi cancelado.");
        }
    }

    // ------------------------------ Inscrições (Uriel) ------------------------------

    /** Tela 4: submenu Inscrições. */
    public void menuInscricoes() {
        int opcao;
        do {
            System.out.println();
            System.out.println("-------- INSCRIÇÕES ---------");
            System.out.println("1. Inscrever participante");
            System.out.println("2. Cancelar inscrição");
            System.out.println("3. Inscritos de um evento");
            System.out.println("4. Inscrições de um participante");
            System.out.println("0. Voltar");
            opcao = leitor.lerInteiro("Escolha uma opção: ", 0, 4);

            try {
                switch (opcao) {
                    case 1:
                        inscreverParticipante();
                        break;
                    case 2:
                        cancelarInscricao();
                        break;
                    case 3:
                        listarInscritosDoEvento();
                        break;
                    case 4:
                        listarInscricoesDoParticipante();
                        break;
                    default:
                        break;
                }
            } catch (SistemaEventosException | IllegalArgumentException e) {
                leitor.mostrarErro(e.getMessage());
            }
        } while (opcao != 0);
    }

    /** Inscreve o participante e mostra os dados da inscrição. */
    private void inscreverParticipante() throws SistemaEventosException {
        System.out.println("--------- INSCREVER PARTICIPANTE ---------");
        String cpf = leitor.lerCpf("CPF do participante: ");
        String codigo = leitor.lerCodigoEvento("Código do evento: ");
        Inscricao inscricao = gerenciador.inscrever(cpf, codigo);
        Evento ev = inscricao.getEvento();

        System.out.println("[OK] Inscrição confirmada!");
        System.out.println(" Participante: " + inscricao.getParticipante().getNome());
        System.out.println(" Evento: " + ev.getTitulo() + " (" + ev.getDataHoraFormatada() + ")");
        System.out.println(" " + ev.getInformacoesAcesso());
        System.out.println(" Vagas restantes: " + ev.getVagasDisponiveis());
    }

    /** Cancela a inscrição e libera a vaga. */
    private void cancelarInscricao() throws SistemaEventosException {
        System.out.println("--------- CANCELAR INSCRIÇÃO ---------");
        String cpf = leitor.lerCpf("CPF do participante: ");
        String codigo = leitor.lerCodigoEvento("Código do evento: ");
        gerenciador.cancelarInscricao(cpf, codigo);
        System.out.println("[OK] Inscrição cancelada. A vaga foi liberada.");
    }

    /** Lista os inscritos confirmados de um evento. */
    private void listarInscritosDoEvento() throws SistemaEventosException {
        String codigo = leitor.lerCodigoEvento("Código do evento: ");
        Evento ev = gerenciador.buscarEvento(codigo);
        List<Inscricao> inscritos = gerenciador.listarInscritos(codigo);

        System.out.println("------ INSCRITOS: " + ev.getCodigo() + " - " + ev.getTitulo() + " ------");
        if (inscritos.isEmpty()) {
            System.out.println("Este evento não tem inscrições confirmadas.");
            return;
        }
        int numero = 1;
        for (Inscricao i : inscritos) {
            Participante p = i.getParticipante();
            String presenca = i.isPresente() ? " [presente]" : "";
            System.out.println(numero + ". " + p.getNome() + " - CPF " + p.getCpfFormatado()
                    + " - " + p.getInstituicao() + presenca);
            numero++;
        }
        System.out.println("Total: " + inscritos.size() + " inscritos de " + ev.getVagas() + " vagas.");
    }

    /** Lista todas as inscrições de um participante (inclusive as canceladas). */
    private void listarInscricoesDoParticipante() throws SistemaEventosException {
        String cpf = leitor.lerCpf("CPF do participante: ");
        Participante p = gerenciador.buscarParticipante(cpf);
        List<Inscricao> inscricoes = gerenciador.listarInscricoesDoParticipante(cpf);

        System.out.println("------ INSCRIÇÕES DE " + p.getNome().toUpperCase() + " ------");
        if (inscricoes.isEmpty()) {
            System.out.println("Este participante não tem inscrições.");
            return;
        }
        for (Inscricao i : inscricoes) {
            Evento ev = i.getEvento();
            String presenca = i.isPresente() ? " [presente]" : "";
            System.out.println(ev.getCodigo() + " - " + ev.getTitulo() + " (" + ev.getDataHoraFormatada()
                    + ") - " + i.getStatus() + presenca);
        }
    }

    // ------------------------------ Check-in (Heitor) ------------------------------

    /**
     * Check-in. Pede o evento uma vez e depois vai pedindo os
     * CPFs, porque no dia do evento o pessoal vai chegando um atrás do outro.
     * Digita 0 para voltar.
     */
    public void menuCheckIn() {
        System.out.println();
        System.out.println("---------------- CHECK-IN ----------------");
        try {
            String codigo = leitor.lerCodigoEvento("Código do evento: ");
            Evento ev = gerenciador.buscarEvento(codigo);
            if (ev.getStatus() == StatusEvento.CANCELADO) {
                leitor.mostrarErro("O evento " + ev.getCodigo() + " foi cancelado.");
                return;
            }

            String cpf = leitor.lerCpfOuZero("CPF do participante (0 para voltar): ");
            while (!cpf.equals("0")) {
                try {
                    Inscricao inscricao = gerenciador.registrarPresenca(codigo, cpf);
                    System.out.println("[OK] Presença registrada: " + inscricao.getParticipante().getNome()
                            + " - " + ev.getTitulo() + ".");
                } catch (SistemaEventosException e) {
                    // aqui o erro de um CPF não pode tirar a pessoa do check-in, entao o catch fica dentro do while
                    leitor.mostrarErro(e.getMessage());
                }
                cpf = leitor.lerCpfOuZero("CPF do participante (0 para voltar): ");
            }
        } catch (SistemaEventosException | IllegalArgumentException e) {
            leitor.mostrarErro(e.getMessage());
        }
    }

    // ------------------------------ Relatórios (Uriel) ------------------------------

    /** Submenu Relatórios. */
    public void menuRelatorios() {
        int opcao;
        do {
            System.out.println();
            System.out.println("-------- RELATÓRIOS ---------");
            System.out.println("1. Vagas por evento");
            System.out.println("2. Presença por evento");
            System.out.println("0. Voltar");
            opcao = leitor.lerInteiro("Escolha uma opção: ", 0, 2);

            try {
                switch (opcao) {
                    case 1:
                        relatorioVagas();
                        break;
                    case 2:
                        relatorioPresenca();
                        break;
                    default:
                        break;
                }
            } catch (SistemaEventosException | IllegalArgumentException e) {
                leitor.mostrarErro(e.getMessage());
            }
        } while (opcao != 0);
    }

    /** Relatório de vagas ocupadas e restantes de cada evento. */
    private void relatorioVagas() {
        List<Evento> eventos = gerenciador.listarEventos();
        System.out.println("------------------------- RELATÓRIO DE VAGAS -------------------------");
        if (eventos.isEmpty()) {
            System.out.println("Nenhum evento cadastrado.");
            return;
        }
        System.out.printf("%-7s %-28s %-9s %-9s %-6s %s%n", "CÓDIGO", "TÍTULO", "OCUPADAS", "LIVRES", "TOTAL", "STATUS");
        for (Evento ev : eventos) {
            System.out.printf("%-7s %-28s %-9d %-9d %-6d %s%n", ev.getCodigo(), ev.getTitulo(),
                    ev.getQuantidadeConfirmadas(), ev.getVagasDisponiveis(), ev.getVagas(), ev.getStatus());
        }
    }

    /** Relatório de presença: inscritos, presentes e taxa de presença de um evento. */
    private void relatorioPresenca() throws SistemaEventosException {
        String codigo = leitor.lerCodigoEvento("Código do evento: ");
        Evento ev = gerenciador.buscarEvento(codigo);

        // String.format usa ponto, mas no Brasil a gente usa vírgula (85,0%)
        String taxa = String.format("%.1f", ev.getTaxaPresenca()).replace('.', ',');

        System.out.println("------- RELATÓRIO DE PRESENÇA: " + ev.getCodigo() + " -------");
        System.out.println("Evento: " + ev.getTitulo() + " (" + ev.getDataFormatada() + ")");
        System.out.println("Inscritos confirmados: " + ev.getQuantidadeConfirmadas());
        System.out.println("Presentes: " + ev.getQuantidadePresentes());
        System.out.println("Taxa de presença: " + taxa + "%");
    }

    // ------------------------------ toString / equals ------------------------------

    /** @return versão simples, só para cumprir o requisito */
    @Override
    public String toString() {
        return "Menu{gerenciador=" + gerenciador + "}";
    }

    /**
     * Dois menus são iguais se usarem o mesmo gerenciador e o mesmo leitor.
     *
     * @param obj objeto a comparar
     * @return true se forem iguais
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Menu)) return false;
        Menu outro = (Menu) obj;
        return gerenciador.equals(outro.gerenciador) && leitor.equals(outro.leitor);
    }

    /** @return hash do gerenciador e do leitor */
    @Override
    public int hashCode() {
        return 31 * gerenciador.hashCode() + leitor.hashCode();
    }
}
