package excecao;

/**
 * Lançada quando o evento não tem mais vagas.
 *
 * @author Uriel
 */
public class VagasEsgotadasException extends SistemaEventosException {

    private static final long serialVersionUID = 1L;

    /**
     * @param mensagem texto do erro
     */
    public VagasEsgotadasException(String mensagem) {
        super(mensagem);
    }
}
