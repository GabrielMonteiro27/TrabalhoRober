package excecao;

/**
 * Lançada ao tentar cadastrar uma pessoa com um CPF que já existe no
 * sistema.
 *
 * @author Uriel
 */
public class CadastroDuplicadoException extends SistemaEventosException {

    private static final long serialVersionUID = 1L;

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
