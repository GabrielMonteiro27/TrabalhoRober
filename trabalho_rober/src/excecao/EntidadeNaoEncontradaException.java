package excecao;

/**
 * Lançada quando uma busca não encontra a entidade procurada
 * (por exemplo, participante ou evento inexistente para o código informado).
 */
public class EntidadeNaoEncontradaException extends SistemaEventosException {

    /**
     * Cria a exceção com a mensagem padrão.
     */
    public EntidadeNaoEncontradaException() {
        super("Registro não encontrado.");
    }

    /**
     * Cria a exceção com uma mensagem personalizada.
     *
     * @param mensagem texto específico, por exemplo indicando qual registro faltou
     */
    public EntidadeNaoEncontradaException(String mensagem) {
        super(mensagem);
    }
}
