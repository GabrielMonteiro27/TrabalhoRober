package modelo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Inscrição de um participante em um evento.
 *
 * A inscrição nunca é apagada: quando cancela, o status vira CANCELADA e a
 * vaga é liberada. Se a pessoa se inscrever de novo no mesmo evento,
 * a MESMA inscrição volta a ficar CONFIRMADA com {@link #reativar()}, assim
 * nao fica duplicada.
 *
 * @author Heitor
 */
public class Inscricao implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private Participante participante;
    private Evento evento;
    private LocalDateTime dataInscricao;
    private StatusInscricao status;
    private boolean presente;
    private LocalDateTime dataHoraPresenca;

    /**
     * Cria uma inscrição já CONFIRMADA, com a data e hora de agora.
     *
     * @param participante quem está se inscrevendo
     * @param evento       em qual evento
     * @throws IllegalArgumentException se o participante ou o evento forem nulos
     */
    public Inscricao(Participante participante, Evento evento) {
        if (participante == null) {
            throw new IllegalArgumentException("O participante é obrigatório.");
        }
        if (evento == null) {
            throw new IllegalArgumentException("O evento é obrigatório.");
        }
        this.participante = participante;
        this.evento = evento;
        this.dataInscricao = LocalDateTime.now();
        this.status = StatusInscricao.CONFIRMADA;
        this.presente = false;
    }

    /**
     * Cancela a inscrição. A vaga é liberada sozinha porque o evento só
     * conta as confirmadas.
     */
    public void cancelar() {
        status = StatusInscricao.CANCELADA;
    }

    /**
     * Volta uma inscrição cancelada para CONFIRMADA (quando a pessoa se
     * inscreve de novo). A data da inscrição é atualizada e a presença zerada.
     */
    public void reativar() {
        status = StatusInscricao.CONFIRMADA;
        dataInscricao = LocalDateTime.now();
        presente = false;
        dataHoraPresenca = null;
    }

    /**
     * Registra a presença do participante no evento (check-in).
     *
     * @throws IllegalStateException se a inscrição estiver cancelada ou a presença já tiver sido registrada
     */
    public void registrarPresenca() {
        if (status == StatusInscricao.CANCELADA) {
            throw new IllegalStateException("Inscrição cancelada não pode fazer check-in.");
        }
        if (presente) {
            throw new IllegalStateException("A presença já foi registrada em "
                    + dataHoraPresenca.format(FORMATO) + ".");
        }
        presente = true;
        dataHoraPresenca = LocalDateTime.now();
    }

    /** @return true se o status for CONFIRMADA */
    public boolean isConfirmada() {
        return status == StatusInscricao.CONFIRMADA;
    }

    /** @return true se já fez check-in */
    public boolean isPresente() {
        return presente;
    }

    /** @return o participante */
    public Participante getParticipante() {
        return participante;
    }

    /** @return o evento */
    public Evento getEvento() {
        return evento;
    }

    /** @return quando a inscrição foi feita */
    public LocalDateTime getDataInscricao() {
        return dataInscricao;
    }

    /** @return o status (CONFIRMADA ou CANCELADA) */
    public StatusInscricao getStatus() {
        return status;
    }

    /** @return quando fez o check-in, ou null se ainda não fez */
    public LocalDateTime getDataHoraPresenca() {
        return dataHoraPresenca;
    }

    /**
     * Duas inscrições são iguais se forem do mesmo participante no mesmo evento.
     *
     * @param obj objeto a comparar
     * @return true se for o mesmo participante e o mesmo evento
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Inscricao)) return false;
        Inscricao outra = (Inscricao) obj;
        return participante.equals(outra.participante) && evento.equals(outra.evento);
    }

    /** @return hash calculado com participante e evento */
    @Override
    public int hashCode() {
        return 31 * participante.hashCode() + evento.hashCode();
    }

    /**
     * Imprime o nome do participante, o evento e a situação.
     * (o toString do Evento NÃO imprime as inscrições, senão dava loop infinito)
     *
     * @return texto da inscrição
     */
    @Override
    public String toString() {
        String texto = participante.getNome() + " (CPF " + participante.getCpfFormatado() + ") | "
                + evento.getCodigo() + " - " + evento.getTitulo() + " | " + status
                + " | inscrito em " + dataInscricao.format(FORMATO);
        if (presente) {
            texto = texto + " | presença em " + dataHoraPresenca.format(FORMATO);
        }
        return texto;
    }
}
