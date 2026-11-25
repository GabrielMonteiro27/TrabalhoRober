package servico;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import excecao.CadastroDuplicadoException;
import excecao.EntidadeNaoEncontradaException;
import excecao.InscricaoDuplicadaException;
import excecao.InscricoesEncerradasException;
import excecao.SistemaEventosException;
import excecao.VagasEsgotadasException;
import modelo.Evento;
import modelo.EventoOnline;
import modelo.EventoPresencial;
import modelo.Inscricao;
import modelo.Organizador;
import modelo.Participante;
import modelo.Pessoa;
import modelo.StatusEvento;
import persistencia.BaseDeDados;
import persistencia.Repositorio;

/**
 * Classe principal das regras de negócio. O Menu só lê e mostra as coisas,
 * quem decide se pode ou não pode é esta classe.
 *
 * Ela depende só da interface {@link Repositorio}, então não sabe se os
 * dados são salvos em arquivo, JSON ou outra coisa.
 *
 * Toda vez que alguma coisa muda, os dados são salvos (assim se o programa
 * fechar do nada não perde tudo).
 *
 * Seções: Pessoas (Guilherme), Eventos (Arthur), Inscrições e Check-in (Heitor).
 */
public class GerenciadorEventos {

    private final BaseDeDados dados;
    private final Repositorio repositorio;

    /**
     * Cria o gerenciador e já carrega os dados salvos.
     *
     * @param repositorio onde os dados são salvos e carregados
     * @throws SistemaEventosException se não conseguir ler o arquivo de dados
     */
    public GerenciadorEventos(Repositorio repositorio) throws SistemaEventosException {
        if (repositorio == null) {
            throw new IllegalArgumentException("Repositório não pode ser nulo.");
        }
        this.repositorio = repositorio;
        this.dados = repositorio.carregar();
        atualizarStatusEventos();
    }

    /**
     * Grava todos os dados.
     *
     * @throws SistemaEventosException se der erro ao gravar
     */
    public void salvar() throws SistemaEventosException {
        repositorio.salvar(dados);
    }

    // =============================== PESSOAS (Guilherme) ===============================

    /**
     * Cadastra um participante ou organizador.
     *
     * @param p pessoa a cadastrar
     * @throws CadastroDuplicadoException se já existir alguém com o mesmo CPF
     * @throws SistemaEventosException    se der erro ao salvar
     */
    public void cadastrarPessoa(Pessoa p) throws SistemaEventosException {
        if (p == null) {
            throw new IllegalArgumentException("Pessoa não pode ser nula.");
        }
        if (dados.getPessoas().containsKey(p.getCpf())) {
            throw new CadastroDuplicadoException(
                    "Já existe uma pessoa cadastrada com o CPF " + p.getCpfFormatado() + ".");
        }
        dados.getPessoas().put(p.getCpf(), p);
        salvar();
    }

    /**
     * Busca uma pessoa pelo CPF.
     *
     * @param cpf CPF com 11 dígitos
     * @return a pessoa encontrada
     * @throws EntidadeNaoEncontradaException se o CPF não estiver cadastrado
     */
    public Pessoa buscarPessoa(String cpf) throws EntidadeNaoEncontradaException {
        String chave = Pessoa.normalizarCpf(cpf);
        Pessoa p = dados.getPessoas().get(chave);
        if (p == null) {
            throw new EntidadeNaoEncontradaException(
                    "Nenhuma pessoa encontrada com o CPF " + Pessoa.formatarCpf(chave) + ".");
        }
        return p;
    }

    /**
     * Busca uma pessoa e confere se ela é participante.
     *
     * @param cpf CPF com 11 dígitos
     * @return o participante
     * @throws EntidadeNaoEncontradaException se não existir ou não for participante
     */
    public Participante buscarParticipante(String cpf) throws EntidadeNaoEncontradaException {
        Pessoa p = buscarPessoa(cpf);
        if (!(p instanceof Participante)) {
            throw new EntidadeNaoEncontradaException(
                    "O CPF " + p.getCpfFormatado() + " não pertence a um participante.");
        }
        return (Participante) p;
    }

    /**
     * Busca uma pessoa e confere se ela é organizador.
     *
     * @param cpf CPF com 11 dígitos
     * @return o organizador
     * @throws EntidadeNaoEncontradaException se não existir ou não for organizador
     */
    public Organizador buscarOrganizador(String cpf) throws EntidadeNaoEncontradaException {
        Pessoa p = buscarPessoa(cpf);
        if (!(p instanceof Organizador)) {
            throw new EntidadeNaoEncontradaException(
                    "O CPF " + p.getCpfFormatado() + " não pertence a um organizador.");
        }
        return (Organizador) p;
    }

    /**
     * Remove uma pessoa do sistema.
     *
     * Não deixa remover quem organiza algum evento ou tem inscrição confirmada,
     * senão o evento ficava sem organizador ou com inscrito "fantasma".
     *
     * @param cpf CPF da pessoa
     * @throws EntidadeNaoEncontradaException se o CPF não existir
     * @throws SistemaEventosException        se a pessoa não puder ser removida ou der erro ao salvar
     */
    public void removerPessoa(String cpf) throws SistemaEventosException {
        Pessoa p = buscarPessoa(cpf);

        for (Evento ev : dados.getEventos()) {
            if (ev.getOrganizador().equals(p) && ev.getStatus() != StatusEvento.CANCELADO) {
                throw new SistemaEventosException(p.getNome() + " organiza o evento "
                        + ev.getCodigo() + " e não pode ser removido(a).");
            }
            if (p instanceof Participante) {
                Optional<Inscricao> inscricao = ev.buscarInscricao((Participante) p);
                if (inscricao.isPresent() && inscricao.get().isConfirmada()) {
                    throw new SistemaEventosException(p.getNome() + " tem inscrição confirmada no evento "
                            + ev.getCodigo() + ". Cancele a inscrição antes de remover.");
                }
            }
        }

        dados.getPessoas().remove(p.getCpf());
        salvar();
    }

    /**
     * @return todas as pessoas em ordem alfabética de nome
     */
    public List<Pessoa> listarPessoas() {
        List<Pessoa> lista = new ArrayList<>(dados.getPessoas().values());
        lista.sort(Comparator.comparing(Pessoa::getNome, String.CASE_INSENSITIVE_ORDER));
        return lista;
    }

    /** @return quantas pessoas estão cadastradas */
    public int getQuantidadePessoas() {
        return dados.getPessoas().size();
    }

    // =============================== EVENTOS (Arthur) ===============================

    /**
     * Cadastra um evento presencial. O código (EV001, EV002...) é
     * gerado aqui.
     *
     * @param titulo         título
     * @param descricao      descrição
     * @param dataHora       data e hora do evento
     * @param dataLimite     data-limite de inscrição
     * @param vagas          quantidade de vagas
     * @param cpfOrganizador CPF do organizador
     * @param endereco       endereço
     * @param sala           sala
     * @return o evento cadastrado
     * @throws SistemaEventosException se o organizador não existir ou der erro ao salvar
     */
    public Evento cadastrarEventoPresencial(String titulo, String descricao, LocalDateTime dataHora,
                                            LocalDate dataLimite, int vagas, String cpfOrganizador,
                                            String endereco, String sala) throws SistemaEventosException {
        Organizador org = buscarOrganizador(cpfOrganizador);
        Evento ev = new EventoPresencial(dados.gerarCodigoEvento(), titulo, descricao, dataHora,
                dataLimite, vagas, org, endereco, sala);
        dados.getEventos().add(ev);
        salvar();
        return ev;
    }

    /**
     * Cadastra um evento online.
     *
     * @param titulo         título
     * @param descricao      descrição
     * @param dataHora       data e hora do evento
     * @param dataLimite     data-limite de inscrição
     * @param vagas          quantidade de vagas
     * @param cpfOrganizador CPF do organizador
     * @param link           link de acesso
     * @param plataforma     plataforma (Meet, Zoom...)
     * @return o evento cadastrado
     * @throws SistemaEventosException se o organizador não existir ou der erro ao salvar
     */
    public Evento cadastrarEventoOnline(String titulo, String descricao, LocalDateTime dataHora,
                                        LocalDate dataLimite, int vagas, String cpfOrganizador,
                                        String link, String plataforma) throws SistemaEventosException {
        Organizador org = buscarOrganizador(cpfOrganizador);
        Evento ev = new EventoOnline(dados.gerarCodigoEvento(), titulo, descricao, dataHora,
                dataLimite, vagas, org, link, plataforma);
        dados.getEventos().add(ev);
        salvar();
        return ev;
    }

    /**
     * Busca um evento pelo código (não importa maiúscula ou minúscula).
     *
     * @param codigo código, ex.: EV001
     * @return o evento
     * @throws EntidadeNaoEncontradaException se o código não existir
     */
    public Evento buscarEvento(String codigo) throws EntidadeNaoEncontradaException {
        if (codigo != null) {
            for (Evento ev : dados.getEventos()) {
                if (ev.getCodigo().equalsIgnoreCase(codigo.trim())) {
                    return ev;
                }
            }
        }
        throw new EntidadeNaoEncontradaException("Evento não encontrado: " + codigo);
    }

    /**
     * Lista os eventos em ordem de data. Usa o compareTo do Evento.
     *
     * @return lista ordenada dos eventos
     */
    public List<Evento> listarEventos() {
        atualizarStatusEventos();
        List<Evento> lista = new ArrayList<>(dados.getEventos());
        Collections.sort(lista);
        return lista;
    }

    /** @return quantos eventos estão cadastrados */
    public int getQuantidadeEventos() {
        return dados.getEventos().size();
    }

    /**
     * Cancela um evento e todas as inscrições dele.
     *
     * @param codigo código do evento
     * @return o evento cancelado
     * @throws SistemaEventosException se o evento não existir, já estiver cancelado ou der erro ao salvar
     */
    public Evento cancelarEvento(String codigo) throws SistemaEventosException {
        Evento ev = buscarEvento(codigo);
        if (ev.getStatus() == StatusEvento.CANCELADO) {
            throw new SistemaEventosException("O evento " + ev.getCodigo() + " já está cancelado.");
        }
        ev.cancelar();
        salvar();
        return ev;
    }

    /**
     * Passa para REALIZADO os eventos abertos cuja data já passou.
     */
    private void atualizarStatusEventos() {
        LocalDateTime agora = LocalDateTime.now();
        for (Evento ev : dados.getEventos()) {
            ev.atualizarStatus(agora);
        }
    }

    // ========================= INSCRIÇÕES e CHECK-IN (Heitor) =========================

    /**
     * Inscreve um participante em um evento. Confere as regras nessa
     * ordem: prazo/status, inscrição repetida e vagas.
     *
     * Se a pessoa já tinha se inscrito e cancelou, a inscrição antiga é
     * reativada em vez de criar outra.
     *
     * @param cpf    CPF do participante
     * @param codigo código do evento
     * @return a inscrição confirmada
     * @throws SistemaEventosException se alguma regra for quebrada ou der erro ao salvar
     */
    public Inscricao inscrever(String cpf, String codigo) throws SistemaEventosException {
        Participante p = buscarParticipante(cpf);
        Evento ev = buscarEvento(codigo);
        ev.atualizarStatus(LocalDateTime.now());

        // confere o prazo e o status
        if (!ev.inscricoesAbertas(LocalDate.now())) {
            throw new InscricoesEncerradasException("As inscrições para \"" + ev.getTitulo()
                    + "\" estão encerradas (evento " + ev.getStatus() + ", prazo até "
                    + ev.getDataLimiteFormatada() + ").");
        }

        // só pode ter uma inscrição confirmada por evento
        Optional<Inscricao> anterior = ev.buscarInscricao(p);
        if (anterior.isPresent() && anterior.get().isConfirmada()) {
            throw new InscricaoDuplicadaException(p.getNome() + " já está inscrito(a) neste evento.");
        }

        // precisa ter vaga
        if (ev.getVagasDisponiveis() <= 0) {
            throw new VagasEsgotadasException("Não há vagas disponíveis para \"" + ev.getTitulo() + "\".");
        }

        Inscricao inscricao;
        if (anterior.isPresent()) {
            // ja tinha cancelado antes, entao so reativa a mesma inscrição
            inscricao = anterior.get();
            inscricao.reativar();
        } else {
            inscricao = new Inscricao(p, ev);
            ev.adicionarInscricao(inscricao);
        }
        salvar();
        return inscricao;
    }

    /**
     * Cancela a inscrição de um participante em um evento. A vaga fica
     * livre de novo.
     *
     * @param cpf    CPF do participante
     * @param codigo código do evento
     * @throws SistemaEventosException se não houver inscrição confirmada, se a presença já foi registrada
     *                                 ou se der erro ao salvar
     */
    public void cancelarInscricao(String cpf, String codigo) throws SistemaEventosException {
        Participante p = buscarParticipante(cpf);
        Evento ev = buscarEvento(codigo);

        Optional<Inscricao> inscricao = ev.buscarInscricao(p);
        if (!inscricao.isPresent() || !inscricao.get().isConfirmada()) {
            throw new EntidadeNaoEncontradaException(p.getNome() + " não tem inscrição confirmada no evento "
                    + ev.getCodigo() + ".");
        }
        if (inscricao.get().isPresente()) {
            throw new SistemaEventosException("Não é possível cancelar: a presença já foi registrada.");
        }
        inscricao.get().cancelar();
        salvar();
    }

    /**
     * Lista os inscritos confirmados de um evento.
     *
     * @param codigo código do evento
     * @return as inscrições confirmadas
     * @throws EntidadeNaoEncontradaException se o evento não existir
     */
    public List<Inscricao> listarInscritos(String codigo) throws EntidadeNaoEncontradaException {
        Evento ev = buscarEvento(codigo);
        return ev.getInscricoesConfirmadas();
    }

    /**
     * Lista todas as inscrições de um participante, em todos os eventos.
     * Mostra as canceladas também, pra pessoa ver o histórico.
     *
     * @param cpf CPF do participante
     * @return lista de inscrições (em ordem de data dos eventos)
     * @throws EntidadeNaoEncontradaException se o CPF não for de um participante
     */
    public List<Inscricao> listarInscricoesDoParticipante(String cpf) throws EntidadeNaoEncontradaException {
        Participante p = buscarParticipante(cpf);
        List<Inscricao> lista = new ArrayList<>();
        for (Evento ev : listarEventos()) {
            Optional<Inscricao> inscricao = ev.buscarInscricao(p);
            if (inscricao.isPresent()) {
                lista.add(inscricao.get());
            }
        }
        return lista;
    }

    /**
     * Registra a presença (check-in) de um participante.
     *
     * @param codigo código do evento
     * @param cpf    CPF do participante
     * @return a inscrição com a presença registrada
     * @throws SistemaEventosException se o evento não existir, estiver cancelado, não houver inscrição
     *                                 confirmada, a presença já tiver sido registrada ou der erro ao salvar
     */
    public Inscricao registrarPresenca(String codigo, String cpf) throws SistemaEventosException {
        Evento ev = buscarEvento(codigo);
        if (ev.getStatus() == StatusEvento.CANCELADO) {
            throw new SistemaEventosException("O evento " + ev.getCodigo() + " foi cancelado.");
        }

        Optional<Inscricao> inscricao = ev.buscarInscricaoConfirmada(Pessoa.normalizarCpf(cpf));
        if (!inscricao.isPresent()) {
            throw new EntidadeNaoEncontradaException("Nenhuma inscrição confirmada para este CPF neste evento.");
        }

        try {
            inscricao.get().registrarPresenca();
        } catch (IllegalStateException e) {
            // transformo na exceção do sistema pro menu tratar igual as outras
            throw new SistemaEventosException(e.getMessage());
        }
        salvar();
        return inscricao.get();
    }

    // ================================ toString / equals ================================

    /** @return quantidade de pessoas e eventos carregados */
    @Override
    public String toString() {
        return "GerenciadorEventos{pessoas=" + getQuantidadePessoas() + ", eventos=" + getQuantidadeEventos() + "}";
    }

    /**
     * Dois gerenciadores são iguais se tiverem os mesmos dados.
     *
     * @param obj objeto a comparar
     * @return true se os dados forem iguais
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof GerenciadorEventos)) return false;
        GerenciadorEventos outro = (GerenciadorEventos) obj;
        return dados.equals(outro.dados);
    }

    /** @return hash dos dados */
    @Override
    public int hashCode() {
        return dados.hashCode();
    }
}
