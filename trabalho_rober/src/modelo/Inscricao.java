package eventos.modelo;

import java.time.LocalDate;
import java.util.Objects;

/** Inscrição de um {@link Participante} em um {@link Evento}. */
public class Inscricao {
    private final Participante participante;
    private final LocalDate data;
    private StatusInscricao status = StatusInscricao.CONFIRMADA;

    /**
     * Cria uma inscrição já confirmada.
     *
     * @param participante quem se inscreve
     * @param data         data em que a inscrição foi feita
     */
    public Inscricao(Participante participante, LocalDate data) {
        this.participante = Objects.requireNonNull(participante, "participante");
        this.data = Objects.requireNonNull(data, "data");
    }

    /** @return participante inscrito */
    public Participante getParticipante() { return participante; }

    /** @return data da inscrição */
    public LocalDate getData() { return data; }

    /** @return status atual */
    public StatusInscricao getStatus() { return status; }

    /** @return {@code true} se a inscrição está confirmada */
    public boolean isConfirmada() { return status == StatusInscricao.CONFIRMADA; }

    /** Cancela a inscrição (libera a vaga no evento). */
    public void cancelar() { status = StatusInscricao.CANCELADA; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Inscricao)) return false;
        Inscricao i = (Inscricao) o;
        return participante.equals(i.participante) && data.equals(i.data);
    }

    @Override
    public int hashCode() { return Objects.hash(participante, data); }

    @Override
    public String toString() { return participante + " | " + data + " | " + status; }
}
