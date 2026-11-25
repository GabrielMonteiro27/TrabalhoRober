package visao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Scanner;

/**
 * Leitura e validação de toda a entrada do usuário.
 *
 * Toda leitura é feita com nextLine() e a conversão acontece depois.
 * Misturar nextInt() com nextLine() deixa um "\n" no buffer e causa o bug
 * do "pulou a pergunta" (aconteceu com a gente no começo, por isso ficou assim).
 *
 * Quando a entrada é inválida, o método mostra "[ERRO] ..." e repete a
 * pergunta até o usuário digitar um valor válido.
 *
 * @author Anderson
 */
public class LeitorEntrada {

    // STRICT pra não aceitar coisa tipo 31/02. Com STRICT tem que usar "uuuu" no ano e não "yyyy"
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

    /**
     * Mostra uma mensagem de erro no padrão do sistema.
     *
     * @param detalhe o que está errado
     */
    public void mostrarErro(String detalhe) {
        System.out.println(" [ERRO] " + detalhe);
    }

    /**
     * Lê um texto que não pode ser vazio.
     *
     * @param mensagem pergunta mostrada ao usuário
     * @return o texto digitado (sem espaços nas pontas)
     */
    public String lerTexto(String mensagem) {
        while (true) {
            String texto = lerLinha(mensagem);
            if (!texto.isEmpty()) {
                return texto;
            }
            mostrarErro("O campo não pode ficar vazio.");
        }
    }

    /**
     * Lê um inteiro qualquer (sem limites).
     *
     * @param mensagem pergunta mostrada ao usuário
     * @return o número digitado
     */
    public int lerInteiro(String mensagem) {
        return lerInteiro(mensagem, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    /**
     * Lê um inteiro entre min e max (inclusive).
     *
     * @param mensagem pergunta mostrada ao usuário
     * @param min      menor valor aceito
     * @param max      maior valor aceito
     * @return o número digitado
     */
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

    /**
     * Lê um CPF com 11 dígitos. Aceita com ou sem pontos e traço, mas
     * devolve só os números.
     *
     * @param mensagem pergunta mostrada ao usuário
     * @return o CPF só com os 11 dígitos
     */
    public String lerCpf(String mensagem) {
        while (true) {
            String texto = lerLinha(mensagem).replace(".", "").replace("-", "");
            if (texto.matches("\\d{11}")) {
                return texto;
            }
            mostrarErro("O CPF deve ter exatamente 11 dígitos.");
        }
    }

    /**
     * Igual ao lerCpf, mas aceita "0" para o usuário voltar (usado no check-in).
     *
     * @param mensagem pergunta mostrada ao usuário
     * @return o CPF só com os dígitos, ou "0" se o usuário quiser voltar
     */
    public String lerCpfOuZero(String mensagem) {
        while (true) {
            String texto = lerLinha(mensagem).replace(".", "").replace("-", "");
            if (texto.equals("0") || texto.matches("\\d{11}")) {
                return texto;
            }
            mostrarErro("O CPF deve ter exatamente 11 dígitos (ou 0 para voltar).");
        }
    }

    /**
     * Lê um e-mail no formato usuario@dominio.com.
     *
     * @param mensagem pergunta mostrada ao usuário
     * @return o e-mail digitado
     */
    public String lerEmail(String mensagem) {
        while (true) {
            String texto = lerLinha(mensagem);
            if (texto.matches("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$")) {
                return texto;
            }
            mostrarErro("E-mail inválido. Use o formato nome@dominio.com.");
        }
    }

    /**
     * Lê o código de um evento (ex.: EV001) e já devolve em maiúsculas.
     *
     * @param mensagem pergunta mostrada ao usuário
     * @return o código em maiúsculas
     */
    public String lerCodigoEvento(String mensagem) {
        return lerTexto(mensagem).toUpperCase();
    }

    /**
     * Lê uma data no formato dd/MM/aaaa.
     *
     * @param mensagem pergunta mostrada ao usuário (o formato é colocado no final)
     * @return a data digitada
     */
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

    /**
     * Lê data e hora no formato dd/MM/aaaa HH:mm.
     *
     * @param mensagem pergunta mostrada ao usuário (o formato é colocado no final)
     * @return a data e hora digitadas
     */
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

    /**
     * Lê a data-limite de inscrição e confere se ela é até a data do evento.
     * Se não for, mostra o erro e pergunta de novo.
     *
     * @param mensagem   pergunta mostrada ao usuário
     * @param dataEvento data e hora do evento
     * @return a data-limite válida
     */
    public LocalDate lerDataLimite(String mensagem, LocalDateTime dataEvento) {
        while (true) {
            LocalDate data = lerData(mensagem);
            if (!data.isAfter(dataEvento.toLocalDate())) {
                return data;
            }
            mostrarErro("A data-limite deve ser até a data do evento.");
        }
    }

    /**
     * Pergunta sim ou não.
     *
     * @param mensagem pergunta mostrada ao usuário (o "(s/n)" é colocado no final)
     * @return true se a resposta for "s"
     */
    public boolean lerSimNao(String mensagem) {
        while (true) {
            String texto = lerLinha(mensagem + " (s/n): ").toLowerCase();
            if (texto.equals("s") || texto.equals("sim")) {
                return true;
            }
            if (texto.equals("n") || texto.equals("nao") || texto.equals("não")) {
                return false;
            }
            mostrarErro("Digite s ou n.");
        }
    }

    /** @return texto simples, a classe só guarda o scanner */
    @Override
    public String toString() {
        return "LeitorEntrada{entrada=teclado}";
    }

    /**
     * Dois leitores são iguais se usarem o mesmo Scanner.
     *
     * @param obj objeto a comparar
     * @return true se for o mesmo scanner
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof LeitorEntrada)) return false;
        return scanner == ((LeitorEntrada) obj).scanner;
    }

    /** @return hash do scanner */
    @Override
    public int hashCode() {
        return scanner.hashCode();
    }
}
