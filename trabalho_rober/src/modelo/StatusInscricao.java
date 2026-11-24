package modelo;

/**
 * Situação de uma inscrição em um evento.
 */
public enum StatusInscricao {
    CONFIRMADA("Confirmada"),
    PRESENTE("Presente"),
    CANCELADA("Cancelada");

    private final String descricao;

    StatusInscricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
