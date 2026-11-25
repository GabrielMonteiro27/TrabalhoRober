package excecao;

/**
 * Lançada quando o usuário informa um CPF, código de evento ou inscrição
 * que não existe.
 *
 * @author Uriel
 */
public class EntidadeNaoEncontradaException extends SistemaEventosException {

    private static final long serialVersionUID = 1L;

    /**
     * @param mensagem diz o que não foi encontrado
     */
    public EntidadeNaoEncontradaException(String mensagem) {
        super(mensagem);
    }
}
