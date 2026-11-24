package servico;

// As opcoes disponiveis do programa, para o menu principal ( Heitor )
public enum OpcoesDisponiveis {

    INSCRICAO(1),
    CANCELAR_INSCRICAO(2),
    CHECKIN(3),
    CADASTRAR_PESSOA(4),
    CADASTRAR_EVENTO(5),
    LISTAR_PESSOAS(6),
    LISTAR_EVENTOS(7),
    SAIR(0);

    private final int valor;

    OpcoesDisponiveis(int valor) {
        this.valor = valor;
    }

    public int getValor() {
        return valor;
    }

    /**
     * Retorna a opção de acordo com o numero inteiro.
     */
    public static OpcoesDisponiveis inteiroParaOpcao(int valor) {
        if (valor == 1) {
            return INSCRICAO;
        }

        if (valor == 2) {
            return CANCELAR_INSCRICAO;
        }

        if (valor == 3) {
            return CHECKIN;
        }

        if (valor == 4) {
            return CADASTRAR_PESSOA;
        }

        if (valor == 5) {
            return CADASTRAR_EVENTO;
        }

        if (valor == 6) {
            return LISTAR_PESSOAS;
        }

        if (valor == 7) {
            return LISTAR_EVENTOS;
        }

        if (valor == 0) {
            return SAIR;
        }

        return null;
    }
}
