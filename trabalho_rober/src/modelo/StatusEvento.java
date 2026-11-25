package modelo;

/**
 * Situação de um {@link Evento}.
 *
 * @author Arthur
 */
public enum StatusEvento {
    /** Evento ativo e (dentro do prazo) aceitando inscrições. */
    ABERTO,
    /** Evento cancelado; todas as inscrições são canceladas junto. */
    CANCELADO,
    /** A data do evento já passou. */
    REALIZADO
}
