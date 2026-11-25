package excecao;

/**
 * Exceção "mãe" de todas as exceções do sistema.
 *
 * Como todas as outras herdam dela, o menu consegue pegar qualquer erro de
 * regra de negócio com um catch só.
 *
 * @author Uriel
 */
public class SistemaEventosException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Cria a exceção com uma mensagem que vai ser mostrada pro usuário.
     *
     * @param mensagem texto explicando o erro
     */
    public SistemaEventosException(String mensagem) {
        super(mensagem);
    }

    /**
     * @return o nome da exceção e a mensagem, ex.: "VagasEsgotadasException: Não há vagas..."
     */
    @Override
    public String toString() {
        return getClass().getSimpleName() + ": " + getMessage();
    }

    /**
     * Duas exceções são iguais se forem do mesmo tipo e tiverem a mesma mensagem.
     *
     * @param obj objeto a comparar
     * @return true se forem iguais
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SistemaEventosException outra = (SistemaEventosException) obj;
        if (getMessage() == null) {
            return outra.getMessage() == null;
        }
        return getMessage().equals(outra.getMessage());
    }

    /**
     * @return hash calculado pela mensagem
     */
    @Override
    public int hashCode() {
        if (getMessage() == null) {
            return 0;
        }
        return getMessage().hashCode();
    }
}
