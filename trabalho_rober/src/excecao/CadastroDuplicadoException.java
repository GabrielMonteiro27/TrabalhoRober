package excecao;

/**
 * Lançada ao tentar cadastrar uma entidade que já existe no sistema
 * (por exemplo, um participante ou evento com os mesmos dados de
 * identificação, como CPF ou nome).
 */
public class CadastroDuplicadoException extends SistemaEventosException {

    /**
     * Cria a exceção com a mensagem padrão.
     */
    public CadastroDuplicadoException() {
        super("Já existe um cadastro com esses dados.");
    }

    /**
     * Cria a exceção com uma mensagem personalizada.
     *
     * @param mensagem texto específico sobre o cadastro duplicado
     */
    public CadastroDuplicadoException(String mensagem) {
        super(mensagem);
    }
}
