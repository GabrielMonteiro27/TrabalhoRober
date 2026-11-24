package visao;
import java.util.Scanner;
import mensagens.Mensagens;
import mensagens.MensagensPrograma;
/** PROVISÓRIO (do Anderson). */
public class LeitorEntrada {
    private final Scanner sc = new Scanner(System.in);
    private String mensagemCampoVazio = "";
    private String mensagemNumeroInvalido = "";

    public void definirMensagens(String campoVazio, String numeroInvalido) {
        this.mensagemCampoVazio = campoVazio;
        this.mensagemNumeroInvalido = numeroInvalido;
    }

    public String lerTexto(String msg) {
        System.out.print(msg);
        String s = sc.nextLine().trim();
        if (s.isEmpty()) throw new IllegalArgumentException(mensagemCampoVazio);
        return s;
    }
    public int lerInteiro(String msg) {
        try { return Integer.parseInt(lerTexto(msg)); }
        catch (NumberFormatException e) { throw new IllegalArgumentException(mensagemNumeroInvalido); }
    }
}
