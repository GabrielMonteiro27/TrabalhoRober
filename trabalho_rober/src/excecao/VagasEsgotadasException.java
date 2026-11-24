package excecao;

/**
 * Lançada ao tentar inscrever um participante em um evento que já atingiu
 * o limite de vagas.
 */
public class VagasEsgotadasException extends SistemaEventosException {

    /**
     * Cria a exceção com a mensagem padrão.
     */
    public VagasEsgotadasException() {
        super("Não há vagas disponíveis para este evento.");
    }

    /**
     * Cria a exceção com uma mensagem personalizada.
     *
     * @param mensagem texto específico, por exemplo com o nome do evento
     */
    public VagasEsgotadasException(String mensagem) {
        super(mensagem);
    }
}
