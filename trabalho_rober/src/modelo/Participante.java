package eventos.modelo;

import java.util.Objects;

/** Pessoa que pode se inscrever em um {@link Evento}. */
public class Participante {
    private final String nome;
    private final String email;

    /**
     * @param nome  nome do participante
     * @param email e-mail (identifica o participante)
     */
    public Participante(String nome, String email) {
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
        if (!(o instanceof Participante)) return false;
        return email.equalsIgnoreCase(((Participante) o).email);
    }

    @Override
    public int hashCode() { return email.toLowerCase().hashCode(); }

    @Override
    public String toString() { return nome + " <" + email + ">"; }
}
