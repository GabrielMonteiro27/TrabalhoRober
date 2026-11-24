package excecao;

/**
 * Exceção base do Sistema de Eventos.
 *
 * Todas as exceções de negócio do sistema herdam desta classe. Com isso,
 * o menu consegue tratar qualquer erro do sistema com um único bloco catch:
 *
 * <pre>
 * try {
 *     // operação do sistema
 * } catch (SistemaEventosException e) {
 *     System.out.println("Erro: " + e.getMessage());
 * }
 * </pre>
 *
 * É uma checked exception (extends Exception), então os métodos que a lançam
 * precisam declarar "throws" ou tratá-la com try/catch.
 */
public class SistemaEventosException extends Exception {

    /**
     * Cria a exceção com uma mensagem descritiva.
     *
     * @param mensagem texto exibido ao usuário, recuperado com getMessage()
     */
    public SistemaEventosException(String mensagem) {
        super(mensagem);
    }
}
