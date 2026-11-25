package modelo;

import java.io.Serializable;
import java.util.Objects;

/**
 * Pessoa cadastrada no sistema.
 *
 * É abstrata porque só existem participantes e organizadores, nunca uma
 * "pessoa" genérica. As subclasses respondem a {@link #getTipo()} e
 * {@link #getDetalhe()} cada uma do seu jeito (polimorfismo).
 *
 * O CPF identifica a pessoa: é usado em {@link #equals(Object)} e
 * {@link #hashCode()} e não pode ser alterado depois do cadastro.
 *
 * @author Arthur
 */
public abstract class Pessoa implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Formato simples de e-mail: algo@dominio.extensao, sem espaços. */
    private static final String REGEX_EMAIL = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$";

    private String nome;
    private final String cpf;
    private String email;

    /**
     * @param nome  nome completo, não pode ficar vazio
     * @param cpf   CPF com 11 dígitos; pontos, traço e espaços são ignorados
     * @param email e-mail no formato nome@dominio.com
     * @throws IllegalArgumentException se algum dado for inválido
     */
    protected Pessoa(String nome, String cpf, String email) {
        this.cpf = normalizarCpf(cpf);
        setNome(nome);
        setEmail(email);
    }

    /**
     * Remove pontos, traço e espaços do CPF e confere se sobram 11 dígitos.
     *
     * @param cpf CPF digitado, com ou sem formatação
     * @return o CPF só com os 11 dígitos
     * @throws IllegalArgumentException se não houver exatamente 11 dígitos
     */
    public static String normalizarCpf(String cpf) {
        String digitos = cpf == null ? "" : cpf.replaceAll("[.\\-\\s]", "");
        if (!digitos.matches("\\d{11}")) {
            throw new IllegalArgumentException("CPF inválido: digite os 11 dígitos.");
        }
        return digitos;
    }

    /**
     * Formata um CPF de 11 dígitos como 000.000.000-00.
     *
     * @param cpf CPF com 11 dígitos
     * @return o CPF formatado
     */
    public static String formatarCpf(String cpf) {
        String d = normalizarCpf(cpf);
        return d.substring(0, 3) + "." + d.substring(3, 6) + "." + d.substring(6, 9) + "-" + d.substring(9);
    }

    /**
     * Confere se um e-mail tem o formato nome@dominio.extensao.
     *
     * @param email e-mail a conferir
     * @return {@code true} se o formato for válido
     */
    public static boolean emailValido(String email) {
        return email != null && email.trim().matches(REGEX_EMAIL);
    }

    /**
     * Garante que um texto obrigatório foi preenchido.
     *
     * @param valor texto informado
     * @param campo nome do campo, usado na mensagem de erro
     * @return o texto sem espaços nas pontas
     * @throws IllegalArgumentException se o texto estiver vazio
     */
    protected static String textoObrigatorio(String valor, String campo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(campo + " não pode ficar vazio.");
        }
        return valor.trim();
    }

    /**
     * Tipo da pessoa, mostrado nas listagens. Cada subclasse responde do seu
     * jeito (polimorfismo).
     *
     * @return "Participante" ou "Organizador"
     */
    public abstract String getTipo();

    /**
     * Informação específica de cada tipo de pessoa, mostrada nas listagens:
     * a instituição do participante ou o cargo do organizador (polimorfismo).
     *
     * @return o detalhe do tipo de pessoa
     */
    public abstract String getDetalhe();

    /** @return o nome completo */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome novo nome, não pode ficar vazio
     * @throws IllegalArgumentException se o nome estiver vazio
     */
    public void setNome(String nome) {
        this.nome = textoObrigatorio(nome, "Nome");
    }

    /** @return o CPF só com os 11 dígitos */
    public String getCpf() {
        return cpf;
    }

    /** @return o CPF no formato 000.000.000-00 */
    public String getCpfFormatado() {
        return formatarCpf(cpf);
    }

    /** @return o e-mail */
    public String getEmail() {
        return email;
    }

    /**
     * @param email novo e-mail, no formato nome@dominio.com
     * @throws IllegalArgumentException se o formato for inválido
     */
    public void setEmail(String email) {
        if (!emailValido(email)) {
            throw new IllegalArgumentException("E-mail inválido: use o formato nome@dominio.com.");
        }
        this.email = email.trim();
    }

    /**
     * @return tipo, nome, CPF formatado e e-mail, por exemplo
     *         {@code Participante: Ana Souza (CPF 987.654.321-00, ana@email.com)}
     */
    @Override
    public String toString() {
        return getTipo() + ": " + nome + " (CPF " + getCpfFormatado() + ", " + email + ")";
    }

    /**
     * Duas pessoas são iguais quando têm o mesmo CPF, seja qual for o tipo.
     * Isso garante que não existam duas pessoas com o mesmo CPF.
     *
     * @param obj objeto a comparar
     * @return {@code true} se for uma pessoa com o mesmo CPF
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Pessoa)) return false;
        Pessoa outra = (Pessoa) obj;
        return cpf.equals(outra.cpf);
    }

    /**
     * Calculado só pelo CPF, coerente com {@link #equals(Object)}.
     *
     * @return código hash da pessoa
     */
    @Override
    public int hashCode() {
        return Objects.hash(cpf);
    }
}
