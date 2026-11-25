package modelo;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Evento transmitido pela internet, com link e plataforma (Meet, Zoom...).
 *
 * @author Arthur
 */
public class EventoOnline extends Evento {

    private static final long serialVersionUID = 1L;

    private String link;
    private String plataforma;

    /**
     * @param codigo              código gerado pelo sistema
     * @param titulo              título
     * @param descricao           descrição
     * @param dataHora            data e hora
     * @param dataLimiteInscricao último dia para se inscrever
     * @param vagas               quantidade de vagas
     * @param organizador         quem organiza
     * @param link                link de acesso
     * @param plataforma          plataforma, ex.: "Google Meet"
     * @throws IllegalArgumentException se algum dado for inválido
     */
    public EventoOnline(String codigo, String titulo, String descricao, LocalDateTime dataHora,
                        LocalDate dataLimiteInscricao, int vagas, Organizador organizador,
                        String link, String plataforma) {
        super(codigo, titulo, descricao, dataHora, dataLimiteInscricao, vagas, organizador);
        setLink(link);
        setPlataforma(plataforma);
    }

    /** @return "Acesso: link (plataforma)" */
    @Override
    public String getInformacoesAcesso() {
        return "Acesso: " + link + " (" + plataforma + ")";
    }

    /** @return "Online" */
    @Override
    public String getTipo() {
        return "Online";
    }

    /** @return o link */
    public String getLink() {
        return link;
    }

    /**
     * @param link novo link, não pode ficar vazio
     * @throws IllegalArgumentException se estiver vazio
     */
    public void setLink(String link) {
        if (link == null || link.trim().isEmpty()) {
            throw new IllegalArgumentException("O link não pode ficar vazio.");
        }
        this.link = link.trim();
    }

    /** @return a plataforma */
    public String getPlataforma() {
        return plataforma;
    }

    /**
     * @param plataforma nova plataforma, não pode ficar vazia
     * @throws IllegalArgumentException se estiver vazia
     */
    public void setPlataforma(String plataforma) {
        if (plataforma == null || plataforma.trim().isEmpty()) {
            throw new IllegalArgumentException("A plataforma não pode ficar vazia.");
        }
        this.plataforma = plataforma.trim();
    }

    /** @return dados do evento mais o acesso */
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
