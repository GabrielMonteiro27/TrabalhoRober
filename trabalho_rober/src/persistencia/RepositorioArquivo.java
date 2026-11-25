package persistencia;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Objects;

import excecao.SistemaEventosException;

/**
 * Persiste a {@link BaseDeDados} em um único arquivo ({@code dados.dat})
 * usando {@link ObjectOutputStream} e {@link ObjectInputStream}.
 *
 * @author Guilherme
 */
public class RepositorioArquivo implements Repositorio {

    private static final String ARQUIVO_PADRAO = "dados.dat";

    private final String caminho;

    /** Usa o arquivo padrão, {@code dados.dat}, na pasta onde o programa roda. */
    public RepositorioArquivo() {
        this(ARQUIVO_PADRAO);
    }

    /**
     * Usa um arquivo escolhido (útil nos testes, para não mexer no dados.dat).
     *
     * @param caminho caminho do arquivo
     * @throws IllegalArgumentException se o caminho estiver vazio
     */
    public RepositorioArquivo(String caminho) {
        if (caminho == null || caminho.trim().isEmpty()) {
            throw new IllegalArgumentException("O caminho do arquivo não pode ficar vazio.");
        }
        this.caminho = caminho.trim();
    }

    /**
     * Grava a base inteira com um único {@code writeObject}.
     *
     * @param dados base a gravar
     * @throws SistemaEventosException se ocorrer um erro de entrada e saída ao gravar
     */
    @Override
    public void salvar(BaseDeDados dados) throws SistemaEventosException {
        if (dados == null) {
            throw new IllegalArgumentException("A base de dados não pode ser nula.");
        }
        try (ObjectOutputStream saida = new ObjectOutputStream(new FileOutputStream(caminho))) {
            saida.writeObject(dados);
        } catch (IOException e) {
            throw new SistemaEventosException(
                    "Não foi possível salvar os dados em " + caminho + ": " + detalhe(e));
        }
    }

    /**
     * Lê a base do arquivo. Se o arquivo não existir (primeira execução),
     * devolve uma base vazia.
     *
     * @return a base carregada, ou uma base vazia
     * @throws SistemaEventosException se o arquivo estiver ilegível ou for de uma versão incompatível
     */
    @Override
    public BaseDeDados carregar() throws SistemaEventosException {
        File arquivo = new File(caminho);
        if (!arquivo.exists()) {
            return new BaseDeDados();
        }
        try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(arquivo))) {
            return (BaseDeDados) entrada.readObject();
        } catch (IOException e) {
            throw new SistemaEventosException(
                    "Não foi possível ler os dados de " + caminho + ": " + detalhe(e));
        } catch (ClassNotFoundException e) {
            throw new SistemaEventosException(
                    "O arquivo " + caminho + " contém uma classe que não existe mais no sistema.");
        }
    }

    /**
     * Texto do erro para a mensagem; alguns erros de E/S vêm sem mensagem.
     *
     * @param e erro ocorrido
     * @return a mensagem do erro ou o nome do tipo de erro
     */
    private static String detalhe(IOException e) {
        return e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName();
    }

    /** @return por exemplo {@code RepositorioArquivo{caminho='dados.dat'}} */
    @Override
    public String toString() {
        return "RepositorioArquivo{caminho='" + caminho + "'}";
    }

    /**
     * Dois repositórios são iguais quando apontam para o mesmo arquivo.
     *
     * @param obj objeto a comparar
     * @return {@code true} se o caminho for o mesmo
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof RepositorioArquivo)) return false;
        return caminho.equals(((RepositorioArquivo) obj).caminho);
    }

    /**
     * Calculado só pelo caminho, coerente com {@link #equals(Object)}.
     *
     * @return código hash do repositório
     */
    @Override
    public int hashCode() {
        return Objects.hash(caminho);
    }
}
