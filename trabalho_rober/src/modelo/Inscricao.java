package modelo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import mensagens.Mensagens;
import mensagens.MensagensPrograma;

/**
 * Inscrição de um participante em um evento.
 * Guarda a situação (confirmada, presente ou cancelada) e a data do check-in.
 */
public class Inscricao implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter FORMATO =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final int numero;
    private final Participante participante;
    private final Evento evento;
    private final LocalDateTime dataInscricao;
    private LocalDateTime dataPresenca;
    private StatusInscricao status;

    public Inscricao(int numero, Participante participante, Evento evento) {
        if (numero <= 0) {
            throw new IllegalArgumentException(MensagensPrograma.pegar(Mensagens.NUMERO_INSCRICAO_NEGATIVO));
        }
        if (participante == null) {
            throw new IllegalArgumentException(MensagensPrograma.pegar(Mensagens.PARTICIPANTE_OBRIGATORIO));
        }
        if (evento == null) {
            throw new IllegalArgumentException(MensagensPrograma.pegar(Mensagens.EVENTO_OBRIGATORIO));
        }
        this.numero = numero;
        this.participante = participante;
        this.evento = evento;
        this.dataInscricao = LocalDateTime.now();
        this.status = StatusInscricao.CONFIRMADA;
    }

    /** RN06: ao cancelar, a inscrição deixa de ocupar vaga. */
    public void cancelar() {
        if (status == StatusInscricao.PRESENTE) {
            throw new IllegalStateException(MensagensPrograma.pegar(Mensagens.NAO_PODE_CANCELAR_PRESENTE));
        }
        status = StatusInscricao.CANCELADA;
    }

    /** Check-in: só vale para inscrição confirmada. */
    public void registrarPresenca() {
        if (status == StatusInscricao.CANCELADA) {
            throw new IllegalStateException(MensagensPrograma.pegar(Mensagens.NAO_PODE_CHECKIN_CANCELADA));
        }
        if (status == StatusInscricao.PRESENTE) {
            throw new IllegalStateException(MensagensPrograma.pegar(Mensagens.PRESENCA_JA_REGISTRADA, dataPresenca.format(FORMATO)));
        }
        status = StatusInscricao.PRESENTE;
        dataPresenca = LocalDateTime.now();
    }

    /** Uma inscrição ocupa vaga enquanto não estiver cancelada. */
    public boolean isAtiva() {
        return status != StatusInscricao.CANCELADA;
    }

    public int getNumero() { return numero; }
    public Participante getParticipante() { return participante; }
    public Evento getEvento() { return evento; }
    public LocalDateTime getDataInscricao() { return dataInscricao; }
    public LocalDateTime getDataPresenca() { return dataPresenca; }
    public StatusInscricao getStatus() { return status; }

    // equals/hashCode usam só códigos para não entrar em ciclo com Evento.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Inscricao outra = (Inscricao) o;
        return numero == outra.numero
                && Objects.equals(evento.getCodigo(), outra.evento.getCodigo())
                && Objects.equals(participante.getCpf(), outra.participante.getCpf());
    }

    @Override
    public int hashCode() {
        return Objects.hash(numero, evento.getCodigo(), participante.getCpf());
    }

    @Override
    public String toString() {
        return MensagensPrograma.pegar(Mensagens.INSCRICAO_TEXTO,
                numero, participante.getNome(), status, dataInscricao.format(FORMATO))
                + (dataPresenca != null
                    ? MensagensPrograma.pegar(Mensagens.INSCRICAO_TEXTO_PRESENCA, dataPresenca.format(FORMATO))
                    : "");
    }
}
