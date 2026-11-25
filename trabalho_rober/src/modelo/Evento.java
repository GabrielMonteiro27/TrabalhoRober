package eventos.modelo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Classe abstrata que representa um evento com inscrições.
 * A forma de acesso (local físico ou link) e o tipo são definidos pelas subclasses.
 * Eventos são comparados por data/hora ({@link Comparable}) e identificados pelo código.
 */
public abstract class Evento implements Comparable<Evento> {
    private final String codigo;
    private String titulo;
    private String descricao;
    private LocalDateTime dataHora;
    private LocalDate dataLimiteInscricao;
    private int capacidade;
    private StatusEvento status;
    private Organizador organizador;
    private final List<Inscricao> inscricoes = new ArrayList<>();

    /**
     * Cria um evento com status {@link StatusEvento#ABERTO}.
     *
     * @param codigo              código único do evento
     * @param titulo              título
     * @param descricao           descrição
     * @param dataHora            data e hora de realização
     * @param dataLimiteInscricao último dia para se inscrever
     * @param capacidade          número máximo de inscritos confirmados (&gt; 0)
     * @param organizador         responsável
     * @throws IllegalArgumentException se algum dado for inválido
     */
    protected Evento(String codigo, String titulo, String descricao, LocalDateTime dataHora,
                     LocalDate dataLimiteInscricao, int capacidade, Organizador organizador) {
        if (codigo == null || codigo.isBlank()) throw new IllegalArgumentException("Código obrigatório.");
        if (titulo == null || titulo.isBlank()) throw new IllegalArgumentException("Título obrigatório.");
        if (capacidade <= 0) throw new IllegalArgumentException("A capacidade deve ser maior que zero.");
        this.dataHora = Objects.requireNonNull(dataHora, "dataHora");
        this.dataLimiteInscricao = Objects.requireNonNull(dataLimiteInscricao, "dataLimiteInscricao");
        if (dataLimiteInscricao.isAfter(dataHora.toLocalDate())) {
            throw new IllegalArgumentException("O prazo de inscrição não pode ser após o evento.");
        }
        this.codigo = codigo.trim();
        this.titulo = titulo.trim();
        this.descricao = descricao == null ? "" : descricao.trim();
        this.capacidade = capacidade;
        this.organizador = Objects.requireNonNull(organizador, "organizador");
        this.status = StatusEvento.ABERTO;
    }

    /** @return vagas livres = capacidade − inscrições confirmadas */
    public int getVagasDisponiveis() {
        long confirmadas = inscricoes.stream().filter(Inscricao::isConfirmada).count();
        return capacidade - (int) confirmadas;
    }

    /**
     * Indica se o evento aceita novas inscrições na data informada.
     *
     * @param hoje data de referência
     * @return {@code true} se está ABERTO, dentro do prazo e com vagas
     */
    public boolean aceitaInscricao(LocalDate hoje) {
        return status == StatusEvento.ABERTO
                && !hoje.isAfter(dataLimiteInscricao)
                && getVagasDisponiveis() > 0;
    }

    /**
     * Registra uma inscrição no evento.
     *
     * @param i inscrição a adicionar
     * @throws IllegalArgumentException se for nula
     * @throws IllegalStateException    se o evento não estiver aberto, não houver vagas,
     *                                  o prazo tiver passado ou o participante já estiver inscrito
     */
    public void adicionarInscricao(Inscricao i) {
        if (i == null) throw new IllegalArgumentException("Inscrição não pode ser nula.");
        if (status != StatusEvento.ABERTO) {
            throw new IllegalStateException("Evento " + status + ": inscrições indisponíveis.");
        }
        if (i.getData().isAfter(dataLimiteInscricao)) {
            throw new IllegalStateException("Prazo de inscrição encerrado em " + dataLimiteInscricao + ".");
        }
        if (buscarInscricaoConfirmada(i.getParticipante()).isPresent()) {
            throw new IllegalStateException(i.getParticipante().getNome() + " já está inscrito(a).");
        }
        if (getVagasDisponiveis() <= 0) {
            throw new IllegalStateException("Evento lotado (" + capacidade + " vagas).");
        }
        inscricoes.add(i);
    }

    /**
     * Procura a inscrição confirmada de um participante.
     *
     * @param p participante
     * @return a inscrição, ou vazio se não houver inscrição confirmada
     */
    public Optional<Inscricao> buscarInscricaoConfirmada(Participante p) {
        return inscricoes.stream()
                .filter(i -> i.isConfirmada() && i.getParticipante().equals(p))
                .findFirst();
    }

    /** Cancela o evento e todas as suas inscrições confirmadas. */
    public void cancelar() {
        status = StatusEvento.CANCELADO;
        inscricoes.forEach(Inscricao::cancelar);
    }

    /**
     * Ordena eventos pela data/hora de realização.
     *
     * @param outro evento a comparar
     * @return negativo, zero ou positivo conforme a data
     */
    @Override
    public int compareTo(Evento outro) { return dataHora.compareTo(outro.dataHora); }

    /** @return como acessar o evento (endereço ou link) */
    public abstract String getInformacoesAcesso();

    /** @return tipo do evento (ex.: "Presencial") */
    public abstract String getTipo();

    /** @return código */
    public String getCodigo() { return codigo; }
    /** @return título */
    public String getTitulo() { return titulo; }
    /** @return descrição */
    public String getDescricao() { return descricao; }
    /** @return data e hora */
    public LocalDateTime getDataHora() { return dataHora; }
    /** @return prazo final de inscrição */
    public LocalDate getDataLimiteInscricao() { return dataLimiteInscricao; }
    /** @return capacidade */
    public int getCapacidade() { return capacidade; }
    /** @return status */
    public StatusEvento getStatus() { return status; }
    /** @return organizador */
    public Organizador getOrganizador() { return organizador; }
    /** @return visão somente leitura das inscrições */
    public List<Inscricao> getInscricoes() { return List.copyOf(inscricoes); }

    /** Iguais quando têm o mesmo código. */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Evento)) return false;
        return codigo.equals(((Evento) o).codigo);
    }

    @Override
    public int hashCode() { return codigo.hashCode(); }

    /** Representação textual sem a lista de inscrições. */
    @Override
    public String toString() {
        return getTipo() + "{codigo='" + codigo + "', titulo='" + titulo + "', dataHora=" + dataHora
                + ", limiteInscricao=" + dataLimiteInscricao + ", capacidade=" + capacidade
                + ", status=" + status + ", organizador=" + organizador + "}";
    }
}
