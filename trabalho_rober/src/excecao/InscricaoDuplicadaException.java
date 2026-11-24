package excecao;

/**
 * Lançada ao tentar inscrever um participante em um evento no qual ele
 * já está inscrito.
 */
public class InscricaoDuplicadaException extends SistemaEventosException {

    /**
     * Cria a exceção com a mensagem padrão.
     */
    public InscricaoDuplicadaException() {
        super("Este participante já está inscrito neste evento.");
    }

    /**
     * Cria a exceção com uma mensagem personalizada.
     *
     * @param mensagem texto específico sobre a inscrição duplicada
     */
    public InscricaoDuplicadaException(String mensagem) {
        super(mensagem);
    }
}
