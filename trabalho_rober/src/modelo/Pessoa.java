package modelo;
import java.io.Serializable;
/** PROVISÓRIO (do Arthur). */
public class Pessoa implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String cpf;
    private final String nome;
    public Pessoa(String cpf, String nome) { this.cpf = cpf; this.nome = nome; }
    public String getCpf() { return cpf; }
    public String getNome() { return nome; }
}
