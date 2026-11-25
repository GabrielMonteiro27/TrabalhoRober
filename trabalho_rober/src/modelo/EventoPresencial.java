package modelo;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Evento que acontece em um lugar físico, com endereço e sala.
 *
 * @author Arthur
 */
public class EventoPresencial extends Evento {

    private static final long serialVersionUID = 1L;

    private String endereco;
    private String sala;

    /**
     * @param codigo              código gerado pelo sistema
     * @param titulo              título
     * @param descricao           descrição
     * @param dataHora            data e hora
     * @param dataLimiteInscricao último dia para se inscrever
     * @param vagas               quantidade de vagas
     * @param organizador         quem organiza
     * @param endereco            endereço, ex.: "Av. Principal, 1000"
     * @param sala                sala, ex.: "Auditório B"
     * @throws IllegalArgumentException se algum dado for inválido
     */
    public EventoPresencial(String codigo, String titulo, String descricao, LocalDateTime dataHora,
                            LocalDate dataLimiteInscricao, int vagas, Organizador organizador,
                            String endereco, String sala) {
        super(codigo, titulo, descricao, dataHora, dataLimiteInscricao, vagas, organizador);
        setEndereco(endereco);
        setSala(sala);
    }

    /** @return "Local: endereço - sala" */
    @Override
    public String getInformacoesAcesso() {
        return "Local: " + endereco + " - " + sala;
    }

    /** @return "Presencial" */
    @Override
    public String getTipo() {
        return "Presencial";
    }

    /** @return o endereço */
    public String getEndereco() {
        return endereco;
    }

    /**
     * @param endereco novo endereço, não pode ficar vazio
     * @throws IllegalArgumentException se estiver vazio
     */
    public void setEndereco(String endereco) {
        if (endereco == null || endereco.trim().isEmpty()) {
            throw new IllegalArgumentException("O endereço não pode ficar vazio.");
        }
        this.endereco = endereco.trim();
    }

    /** @return a sala */
    public String getSala() {
        return sala;
    }

    /**
     * @param sala nova sala, não pode ficar vazia
     * @throws IllegalArgumentException se estiver vazia
     */
    public void setSala(String sala) {
        if (sala == null || sala.trim().isEmpty()) {
            throw new IllegalArgumentException("A sala não pode ficar vazia.");
        }
        this.sala = sala.trim();
    }

    /** @return dados do evento mais o local */
    @Override
    public String toString() {
        return super.toString() + " - " + getInformacoesAcesso();
    }

    /**
     * Usa o equals do Evento (compara pelo código).
     *
     * @param obj objeto a comparar
     * @return true se tiver o mesmo código
     */
    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    /** @return o mesmo hash do Evento */
    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
