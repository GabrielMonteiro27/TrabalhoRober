package eventos.modelo;

import java.time.LocalDate;
import java.time.LocalDateTime;

/** Evento realizado em um endereço físico. */
public class EventoPresencial extends Evento {
    private final String endereco;

    /**
     * @param codigo              código único
     * @param titulo              título
     * @param descricao           descrição
     * @param dataHora            data e hora
     * @param dataLimiteInscricao prazo de inscrição
     * @param capacidade          vagas
     * @param organizador         responsável
     * @param endereco            local onde acontece
     */
    public EventoPresencial(String codigo, String titulo, String descricao, LocalDateTime dataHora,
                            LocalDate dataLimiteInscricao, int capacidade, Organizador organizador,
                            String endereco) {
        super(codigo, titulo, descricao, dataHora, dataLimiteInscricao, capacidade, organizador);
        this.endereco = endereco;
    }

    @Override public String getInformacoesAcesso() { return "Local: " + endereco; }
    @Override public String getTipo() { return "Presencial"; }
}
