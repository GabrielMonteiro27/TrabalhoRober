package excecao;

/**
 * Lançada quando o participante já tem uma inscrição confirmada no
 * evento.
 *
 * @author Uriel
 */
public class InscricaoDuplicadaException extends SistemaEventosException {

    private static final long serialVersionUID = 1L;

    /**
     * @param mensagem texto do erro, ex.: "Ana Souza já está inscrito(a) neste evento."
     */
    public InscricaoDuplicadaException(String mensagem) {
        super(mensagem);
    }
}
