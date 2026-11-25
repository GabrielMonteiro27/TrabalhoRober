package eventos.modelo;

import java.time.LocalDate;
import java.time.LocalDateTime;

/** Evento transmitido pela internet. */
public class EventoOnline extends Evento {
    private final String plataforma;
    private final String link;

    /**
     * @param codigo              código único
     * @param titulo              título
     * @param descricao           descrição
     * @param dataHora            data e hora
     * @param dataLimiteInscricao prazo de inscrição
     * @param capacidade          vagas
     * @param organizador         responsável
     * @param plataforma          Meet, Zoom, Teams...
     * @param link                endereço de acesso
     */
    public EventoOnline(String codigo, String titulo, String descricao, LocalDateTime dataHora,
                        LocalDate dataLimiteInscricao, int capacidade, Organizador organizador,
                        String plataforma, String link) {
        super(codigo, titulo, descricao, dataHora, dataLimiteInscricao, capacidade, organizador);
        this.plataforma = plataforma;
        this.link = link;
    }

    @Override public String getInformacoesAcesso() { return plataforma + ": " + link; }
    @Override public String getTipo() { return "Online"; }
}
