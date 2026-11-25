package modelo;

/**
 * Situação de uma {@link Inscricao}.
 *
 * @author Heitor
 */
public enum StatusInscricao {
    /** Ocupa uma vaga do evento. */
    CONFIRMADA,
    /** Não ocupa vaga (foi cancelada pelo participante ou o evento foi cancelado). */
    CANCELADA
}
