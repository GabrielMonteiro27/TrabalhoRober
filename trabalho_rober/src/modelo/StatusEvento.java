package modelo;

/** Situação de um {@link Evento}. */
public enum StatusEvento {
    /** Evento ativo e (dentro do prazo) aceitando inscrições. */
    ABERTO,
    /** Evento cancelado; todas as inscrições são canceladas junto. */
    CANCELADO,
    /** Evento já realizado. */
    REALIZADO
}
