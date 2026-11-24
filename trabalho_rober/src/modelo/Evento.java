package modelo;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import excecao.InscricoesEncerradasException;
import mensagens.Mensagens;
import mensagens.MensagensPrograma;
/** PROVISÓRIO (do Arthur). Só o mínimo que a parte do Heitor usa. */
public class Evento implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String codigo;
    private final String nome;
    private final int vagas;
    private final LocalDate dataLimite;
    private final List<Inscricao> inscricoes = new ArrayList<>();
    public Evento(String codigo, String nome, int vagas, LocalDate dataLimite) {
        this.codigo = codigo; this.nome = nome; this.vagas = vagas; this.dataLimite = dataLimite;
    }
    public String getCodigo() { return codigo; }
    public String getNome() { return nome; }
    public List<Inscricao> getInscricoes() { return inscricoes; }
    public int getVagasDisponiveis() {
        int ocupadas = 0;
        for (Inscricao i : inscricoes) if (i.isAtiva()) ocupadas++;
        return vagas - ocupadas;
    }
    public void aceitaInscricao(LocalDate hoje, String mensagem) throws InscricoesEncerradasException {
        if (hoje.isAfter(dataLimite)) {
            throw new InscricoesEncerradasException(mensagem);
        }
    }
    public void adicionarInscricao(Inscricao i) { inscricoes.add(i); }
}
