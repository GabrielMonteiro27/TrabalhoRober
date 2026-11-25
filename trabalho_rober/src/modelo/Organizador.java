package modelo;

import java.util.Objects;

/** Pessoa responsável por um {@link Evento}. */
public class Organizador {
    private final String nome;
    private final String email;

    /**
     * @param nome  nome do organizador
     * @param email e-mail de contato
     */
    public Organizador(String nome, String email) {
        this.nome = Objects.requireNonNull(nome, "nome");
        this.email = Objects.requireNonNull(email, "email");
    }

    /** @return nome */
    public String getNome() { return nome; }

    /** @return e-mail */
    public String getEmail() { return email; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Organizador)) return false;
        return email.equalsIgnoreCase(((Organizador) o).email);
    }

    @Override
    public int hashCode() { return email.toLowerCase().hashCode(); }

    @Override
    public String toString() { return nome + " <" + email + ">"; }
}
