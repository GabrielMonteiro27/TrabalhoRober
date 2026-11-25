package excecao;

/**
 * Lançada quando a data-limite de inscrição já passou ou o evento não
 * está mais ABERTO.
 *
 * @author Uriel
 */
public class InscricoesEncerradasException extends SistemaEventosException {

    private static final long serialVersionUID = 1L;

    /**
     * @param mensagem texto do erro
     */
    public InscricoesEncerradasException(String mensagem) {
        super(mensagem);
    }
}
