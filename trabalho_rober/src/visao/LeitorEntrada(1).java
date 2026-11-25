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

    /** Cria o leitor usando o teclado (System.in). */
    public LeitorEntrada() {
        this(new Scanner(System.in));
    }

    /**
     * @param scanner Scanner único do sistema
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
            mostrarErro("O campo não pode ficar vazio.");
        }
    }

    /** Lê um inteiro qualquer (sem limites). */
    public int lerInteiro(String mensagem) {
        return lerInteiro(mensagem, Integer.MIN_VALUE, Integer.MAX_VALUE);
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
            } catch (NumberFormatException e) {
                // cai no erro abaixo
            }
            if (min == Integer.MIN_VALUE && max == Integer.MAX_VALUE) {
                mostrarErro("Digite um número inteiro.");
            } else {
                mostrarErro("Digite um número inteiro entre " + min + " e " + max + ".");
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
            mostrarErro("E-mail inválido. Use o formato nome@dominio.com.");
        }
    }

    /** Lê uma data no formato dd/MM/aaaa. */
    public LocalDate lerData(String mensagem) {
        while (true) {
            String texto = lerLinha(mensagem + " (dd/MM/aaaa): ");
            try {
                return LocalDate.parse(texto, FORMATO_DATA);
            } catch (DateTimeParseException e) {
                mostrarErro("Formato inválido. Use dd/MM/aaaa.");
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
                mostrarErro("Formato inválido. Use dd/MM/aaaa HH:mm.");
            }
        }
    }
}
