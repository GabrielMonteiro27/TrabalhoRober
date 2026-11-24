package excecao;

/**
 * Lançada ao tentar realizar uma inscrição em um evento cujo período de
 * inscrições já foi encerrado (por exemplo, o evento já aconteceu ou
 * passou do prazo limite).
 */
public class InscricoesEncerradasException extends SistemaEventosException {

    /**
     * Cria a exceção com a mensagem padrão.
     */
    public InscricoesEncerradasException() {
        super("As inscrições para este evento estão encerradas.");
    }

    /**
     * Cria a exceção com uma mensagem personalizada.
     *
     * @param mensagem texto específico sobre o encerramento das inscrições
     */
    public InscricoesEncerradasException(String mensagem) {
        super(mensagem);
    }
}
