package servico;

import java.io.File;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.*;

import excecao.*;
import mensagens.Mensagens;
import mensagens.MensagensPrograma;
import modelo.Evento;
import modelo.Inscricao;
import modelo.Participante;
import modelo.Pessoa;

/**
 * VERSÃO PROVISÓRIA para testar a parte do Heitor.
 * Tudo fora da seção "Inscrições" é só um apoio mínimo.
 */
public class GerenciadorEventos {

    // ===== PROVISÓRIO (Guilherme / Arthur): apagar quando juntar com o projeto real =====
    private final Map<String, Pessoa> pessoas = new HashMap<>();
    private final Map<String, Evento> eventos = new HashMap<>();

    private void salvarPessoas(String arquivo) throws Exception {
        PrintWriter printWriter = new PrintWriter(arquivo);
        for (String chave : pessoas.keySet()) {
            printWriter.println(chave);
            printWriter.println(pessoas.get(chave).getCpf());
            printWriter.println(pessoas.get(chave).getNome());
        }
        printWriter.close();
    }

    private void recuperarPessoas(String arquivo, HashMap<String, Pessoa> mapaPessoas) throws Exception {
        File file = new File(arquivo);
        if (!file.exists()) {
            return;
        }
        Scanner arquivoScanner = new Scanner(file);
        while (arquivoScanner.hasNextLine()) {
            String chave = arquivoScanner.nextLine();
            String cpf = arquivoScanner.nextLine();
            String nome = arquivoScanner.nextLine();
            mapaPessoas.put(chave, new Pessoa(cpf, nome));
        }
        arquivoScanner.close();
    }

    private void salvarEventos(String arquivo) throws Exception {
        PrintWriter printWriter = new PrintWriter(arquivo);
        for (String chave : eventos.keySet()) {
            printWriter.println(chave);
            printWriter.println(eventos.get(chave).getCodigo());
            printWriter.println(eventos.get(chave).getNome());
        }
        printWriter.close();
    }

    private void recuperarEventos(String arquivo, HashMap<String, Evento> mapaEventos) throws Exception {
        File file = new File(arquivo);
        if (!file.exists()) {
            return;
        }
        Scanner arquivoScanner = new Scanner(file);
        while (arquivoScanner.hasNextLine()) {
            String chave = arquivoScanner.nextLine();
            String codigo = arquivoScanner.nextLine();
            String nome = arquivoScanner.nextLine();
            int vagas = Integer.parseInt(arquivoScanner.nextLine());
            LocalDate dataLimite = LocalDate.parse(arquivoScanner.nextLine());
            mapaEventos.put(chave, new Evento(codigo, nome, vagas, dataLimite));
        }
        arquivoScanner.close();
    }

    private void salvar() throws SistemaEventosException {
        try {
            salvarPessoas("pessoas.txt");
            salvarEventos("eventos.txt");
        } catch (Exception e) {
            throw new SistemaEventosException(MensagensPrograma.pegar(Mensagens.ERRO_SALVAR) + ": " + e.getMessage());
        }
    }

    public void carregar() {
        try {
            recuperarPessoas("pessoas.txt", (HashMap<String, Pessoa>) pessoas);
            recuperarEventos("eventos.txt", (HashMap<String, Evento>) eventos);
        } catch (Exception e) {
            System.out.println(MensagensPrograma.pegar(Mensagens.ERRO_CARREGAR) + ": " + e.getMessage());
        }
    }


    public void adicionarPessoa(Pessoa p) throws SistemaEventosException {
        pessoas.put(p.getCpf(), p);
        salvar();
    }

    public void adicionarEvento(Evento e) throws SistemaEventosException {
        eventos.put(e.getCodigo(), e);
        salvar();
    }

    private Pessoa buscarPessoa(String cpf) throws EntidadeNaoEncontradaException {
        Pessoa p = pessoas.get(cpf);
        if (p == null) throw new EntidadeNaoEncontradaException(MensagensPrograma.pegar(Mensagens.CPF_NAO_ENCONTRADO, cpf));
        return p;
    }

    private Evento buscarEvento(String codigo) throws EntidadeNaoEncontradaException {
        Evento e = eventos.get(codigo);
        if (e == null) throw new EntidadeNaoEncontradaException(MensagensPrograma.pegar(Mensagens.EVENTO_NAO_ENCONTRADO, codigo));
        return e;
    }

    // ---------------------------- Inscrições ----------------------------

    public Inscricao inscrever(String cpf, String codigoEvento) throws SistemaEventosException {
        Pessoa pessoa = buscarPessoa(cpf);
        if (!(pessoa instanceof Participante)) {
            throw new IllegalArgumentException(MensagensPrograma.pegar(Mensagens.SO_PARTICIPANTE_INSCREVE));
        }
        Participante participante = (Participante) pessoa;
        Evento evento = buscarEvento(codigoEvento);

        evento.aceitaInscricao(LocalDate.now(), MensagensPrograma.pegar(Mensagens.PRAZO_ACABOU));                      // RN04

        for (Inscricao i : evento.getInscricoes()) {                  // RN03
            if (i.isAtiva() && i.getParticipante().getCpf().equals(cpf)) {
                throw new InscricaoDuplicadaException(
                        MensagensPrograma.pegar(Mensagens.JA_INSCRITO));
            }
        }

        if (evento.getVagasDisponiveis() <= 0) {                      // RN02
            throw new VagasEsgotadasException(MensagensPrograma.pegar(Mensagens.SEM_VAGAS));
        }

        Inscricao inscricao = new Inscricao(evento.getInscricoes().size() + 1, participante, evento);
        evento.adicionarInscricao(inscricao);
        salvar();
        return inscricao;
    }

    public Inscricao buscarInscricao(String codigoEvento, int numero) throws SistemaEventosException {
        Evento evento = buscarEvento(codigoEvento);
        for (Inscricao i : evento.getInscricoes()) {
            if (i.getNumero() == numero) {
                return i;
            }
        }
        throw new EntidadeNaoEncontradaException(MensagensPrograma.pegar(Mensagens.INSCRICAO_NAO_ENCONTRADA, numero));
    }

    public void cancelarInscricao(String codigoEvento, int numero) throws SistemaEventosException {
        buscarInscricao(codigoEvento, numero).cancelar();             // RN06
        salvar();
    }

    public void listarCadastradosTela() throws SistemaEventosException {
        for (Pessoa pessoa : pessoas.values()) {
            System.out.println(String.format("%s | %s", pessoa.getCpf(), pessoa.getNome()));
        }
    }

    public void listaEventosTela() throws SistemaEventosException {
        for (Evento evento : eventos.values()) {
            System.out.println(String.format("%s | %s", evento.getCodigo(), evento.getNome()));
        }
    }

    public List<Inscricao> listarInscricoes(String codigoEvento) throws SistemaEventosException {
        return new ArrayList<>(buscarEvento(codigoEvento).getInscricoes());
    }

    // Cartão 14
    public void registrarPresenca(String codigoEvento, int numero) throws SistemaEventosException {
        buscarInscricao(codigoEvento, numero).registrarPresenca();
        salvar();
    }
}
