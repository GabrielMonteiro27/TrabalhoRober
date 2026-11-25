package servico;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import excecao.CadastroDuplicadoException;
import excecao.EntidadeNaoEncontradaException;
import modelo.Organizador;
import modelo.Participante;
import modelo.Pessoa;
import persistencia.BaseDeDados;
import persistencia.Repositorio;


public class GerenciadorEventos {

    private final BaseDeDados dados;
    private final Repositorio repositorio;

  
    public GerenciadorEventos(Repositorio repositorio) {
        if (repositorio == null) {
            throw new IllegalArgumentException("Repositório não pode ser nulo.");
        }
        this.repositorio = repositorio;
        this.dados = repositorio.carregar();
    }

  
    public void cadastrarPessoa(Pessoa p) throws CadastroDuplicadoException {
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

   
    public Pessoa buscarPessoa(String cpf) throws EntidadeNaoEncontradaException {
        String chave = Pessoa.normalizarCpf(cpf);
        Pessoa p = dados.getPessoas().get(chave);
        if (p == null) {
            throw new EntidadeNaoEncontradaException(
                    "Nenhuma pessoa encontrada com o CPF " + Pessoa.formatarCpf(chave) + ".");
        }
        return p;
    }

   
    public Participante buscarParticipante(String cpf) throws EntidadeNaoEncontradaException {
        Pessoa p = buscarPessoa(cpf);
        if (!(p instanceof Participante)) {
            throw new EntidadeNaoEncontradaException(
                    "O CPF " + p.getCpfFormatado() + " não pertence a um participante.");
        }
        return (Participante) p;
    }

    public Organizador buscarOrganizador(String cpf) throws EntidadeNaoEncontradaException {
        Pessoa p = buscarPessoa(cpf);
        if (!(p instanceof Organizador)) {
            throw new EntidadeNaoEncontradaException(
                    "O CPF " + p.getCpfFormatado() + " não pertence a um organizador.");
        }
        return (Organizador) p;
    }

   
    public void removerPessoa(String cpf) throws EntidadeNaoEncontradaException {
        Pessoa p = buscarPessoa(cpf); 
        dados.getPessoas().remove(p.getCpf());
        salvar();
    }

    
    public List<Pessoa> listarPessoas() {
        List<Pessoa> lista = new ArrayList<>(dados.getPessoas().values());
        lista.sort(Comparator.comparing(Pessoa::getNome, String.CASE_INSENSITIVE_ORDER));
        return lista;
    }

   
    public void salvar() {
        repositorio.salvar(dados);
    }
}
