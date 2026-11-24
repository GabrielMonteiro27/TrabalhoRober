package visao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Scanner;

/**
 * Leitura e validação de toda a entrada do usuário (RN08).
 *
 * Toda leitura é feita com nextLine() e a conversão acontece depois.
 * Misturar nextInt() com nextLine() deixa um "\n" no buffer e causa o bug
 * do "pulou a pergunta".
 *
 * Quando a entrada é inválida, o método mostra "[ERRO] ..." e repete a
 * pergunta até o usuário digitar um valor válido.
 */
public class LeitorEntrada {

    private static final DateTimeFormatter FORMATO_DATA =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    private static final DateTimeFormatter FORMATO_DATA_HORA =
            DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm").withResolverStyle(ResolverStyle.STRICT);

    private final Scanner scanner;

    /**
     * @param scanner Scanner único do sistema (criado na Main com System.in)
     */
    public LeitorEntrada(Scanner scanner) {
        this.scanner = scanner;
    }

    /** Único ponto de leitura: mostra a pergunta e lê a linha inteira. */
    private String lerLinha(String mensagem) {
        System.out.print(mensagem);
        return scanner.nextLine().trim();
    }

    private void mostrarErro(String detalhe) {
        System.out.println("[ERRO] " + detalhe);
    }

    /** Lê um texto que não pode ser vazio. */
    public String lerTexto(String mensagem) {
        while (true) {
            String texto = lerLinha(mensagem);
            if (!texto.isEmpty()) {
                return texto;
            }
            mostrarErro("O texto não pode ser vazio.");
        }
    }

    /** Lê um inteiro entre min e max (inclusive). */
    public int lerInteiro(String mensagem, int min, int max) {
        while (true) {
            String texto = lerLinha(mensagem);
            try {
                int numero = Integer.parseInt(texto);
                if (numero >= min && numero <= max) {
                    return numero;
                }
                mostrarErro("Digite um número entre " + min + " e " + max + ".");
            } catch (NumberFormatException e) {
                mostrarErro("Digite um número inteiro válido.");
            }
        }
    }

    /** Lê um CPF com exatamente 11 dígitos (somente números). */
    public String lerCpf(String mensagem) {
        while (true) {
            String texto = lerLinha(mensagem);
            if (texto.matches("\\d{11}")) {
                return texto;
            }
            mostrarErro("O CPF deve ter exatamente 11 dígitos, somente números.");
        }
    }

    /** Lê um e-mail no formato usuario@dominio.com. */
    public String lerEmail(String mensagem) {
        while (true) {
            String texto = lerLinha(mensagem);
            if (texto.matches("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$")) {
                return texto;
            }
            mostrarErro("E-mail inválido. Exemplo: nome@dominio.com");
        }
    }

    /** Lê uma data no formato dd/MM/aaaa. */
    public LocalDate lerData(String mensagem) {
        while (true) {
            String texto = lerLinha(mensagem + " (dd/MM/aaaa): ");
            try {
                return LocalDate.parse(texto, FORMATO_DATA);
            } catch (DateTimeParseException e) {
                mostrarErro("Data inválida. Use o formato dd/MM/aaaa.");
            }
        }
    }

    /** Lê data e hora no formato dd/MM/aaaa HH:mm. */
    public LocalDateTime lerDataHora(String mensagem) {
        while (true) {
            String texto = lerLinha(mensagem + " (dd/MM/aaaa HH:mm): ");
            try {
                return LocalDateTime.parse(texto, FORMATO_DATA_HORA);
            } catch (DateTimeParseException e) {
                mostrarErro("Data e hora inválidas. Use o formato dd/MM/aaaa HH:mm.");
            }
        }
    }
}
