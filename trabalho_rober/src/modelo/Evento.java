package modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Evento cadastrado no sistema.
 * É abstrata porque o evento sempre é presencial ou online. Cada subclasse
 * diz do seu jeito como o evento é acessado ({@link #getInformacoesAcesso()})
 * e qual é o seu tipo ({@link #getTipo()}).
 * Cada evento guarda a lista das suas inscrições (composição), e por isso
 * ele mesmo consegue calcular as vagas livres, os presentes e a taxa de
 * presença.
 * Implementa Comparable para a lista de eventos sair em ordem de data.
 *
 * @author Arthur
 */
public abstract class Evento implements Comparable<Evento>, Serializable {

    private static final long serialVersionUID = 1L;

    /** Formato usado para mostrar data e hora na tela, ex.: 10/12/2026 19:00 */
    private static final DateTimeFormatter FORMATO_DATA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    /** Formato usado para mostrar só a data, ex.: 08/12/2026 */
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final String codigo;
    private String titulo;
    private String descricao;
    private LocalDateTime dataHora;
    private LocalDate dataLimiteInscricao;
    private int vagas;
    private StatusEvento status;
    private Organizador organizador;
    private List<Inscricao> inscricoes = new ArrayList<>();

    /**
     * Cria um evento já com status ABERTO.
     *
     * @param codigo              código gerado pelo sistema (EV001, EV002...)
     * @param titulo              título do evento
     * @param descricao           descrição (pode ficar vazia)
     * @param dataHora            data e hora em que o evento acontece
     * @param dataLimiteInscricao último dia para se inscrever
     * @param vagas               quantidade de vagas, maior que zero
     * @param organizador         quem organiza o evento
     * @throws IllegalArgumentException se algum dado for inválido
     */
    protected Evento(String codigo, String titulo, String descricao, LocalDateTime dataHora,
                     LocalDate dataLimiteInscricao, int vagas, Organizador organizador) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("O código do evento é obrigatório.");
        }
        if (organizador == null) {
            throw new IllegalArgumentException("O evento precisa de um organizador.");
        }
        this.codigo = codigo.trim().toUpperCase();
        setTitulo(titulo);
        setDescricao(descricao);
        alterarDatas(dataHora, dataLimiteInscricao);
        setVagas(vagas);
        this.organizador = organizador;
        this.status = StatusEvento.ABERTO;
    }

    // ----------------------------- inscrições -----------------------------

    /**
     * Conta quantas inscrições estão CONFIRMADAS (as canceladas não contam).
     *
     * @return número de inscrições confirmadas
     */
    public int getQuantidadeConfirmadas() {
        int contador = 0;
        for (Inscricao i : inscricoes) {
            if (i.isConfirmada()) {
                contador++;
            }
        }
        return contador;
    }

    /**
     * Vagas livres = total de vagas menos as inscrições confirmadas.
     * Por isso cancelar uma inscrição já libera a vaga.
     *
     * @return quantidade de vagas que ainda sobram
     */
    public int getVagasDisponiveis() {
        return vagas - getQuantidadeConfirmadas();
    }

    /**
     * Conta quantos inscritos confirmados fizeram check-in.
     *
     * @return número de presentes
     */
    public int getQuantidadePresentes() {
        int contador = 0;
        for (Inscricao i : inscricoes) {
            if (i.isConfirmada() && i.isPresente()) {
                contador++;
            }
        }
        return contador;
    }

    /**
     * Taxa de presença em porcentagem (presentes / confirmados * 100).
     *
     * @return a taxa de 0 a 100, ou 0 se ninguém estiver inscrito
     */
    public double getTaxaPresenca() {
        int confirmadas = getQuantidadeConfirmadas();
        if (confirmadas == 0) {
            return 0; // pra não dividir por zero
        }
        return getQuantidadePresentes() * 100.0 / confirmadas;
    }

    /**
     * Diz se o evento ainda aceita inscrições na data informada:
     * precisa estar ABERTO e a data-limite não pode ter passado.
     *
     * @param hoje data de hoje
     * @return true se ainda dá pra se inscrever
     */
    public boolean inscricoesAbertas(LocalDate hoje) {
        return status == StatusEvento.ABERTO && !hoje.isAfter(dataLimiteInscricao);
    }

    /**
     * Procura a inscrição de um participante neste evento, confirmada ou
     * cancelada. Usado para não duplicar inscrição.
     *
     * @param participante quem procurar
     * @return a inscrição, ou um Optional vazio se ele nunca se inscreveu
     */
    public Optional<Inscricao> buscarInscricao(Participante participante) {
        for (Inscricao i : inscricoes) {
            if (i.getParticipante().equals(participante)) {
                return Optional.of(i);
            }
        }
        return Optional.empty();
    }

    /**
     * Procura a inscrição CONFIRMADA de um CPF neste evento (usado no check-in).
     *
     * @param cpf CPF com 11 dígitos
     * @return a inscrição confirmada, ou um Optional vazio
     */
    public Optional<Inscricao> buscarInscricaoConfirmada(String cpf) {
        for (Inscricao i : inscricoes) {
            if (i.isConfirmada() && i.getParticipante().getCpf().equals(cpf)) {
                return Optional.of(i);
            }
        }
        return Optional.empty();
    }

    /**
     * Adiciona uma inscrição nova na lista. As regras (vagas, prazo,
     * duplicada) são conferidas antes, no GerenciadorEventos.
     *
     * @param inscricao inscrição a adicionar
     * @throws IllegalArgumentException se a inscrição for nula ou for de outro evento
     */
    public void adicionarInscricao(Inscricao inscricao) {
        if (inscricao == null) {
            throw new IllegalArgumentException("A inscrição não pode ser nula.");
        }
        if (!this.equals(inscricao.getEvento())) {
            throw new IllegalArgumentException("Essa inscrição é de outro evento.");
        }
        inscricoes.add(inscricao);
    }

    /**
     * Devolve só as inscrições confirmadas.
     *
     * @return lista nova com as inscrições confirmadas
     */
    public List<Inscricao> getInscricoesConfirmadas() {
        List<Inscricao> confirmadas = new ArrayList<>();
        for (Inscricao i : inscricoes) {
            if (i.isConfirmada()) {
                confirmadas.add(i);
            }
        }
        return confirmadas;
    }

    /**
     * Cancela o evento e todas as inscrições dele.
     */
    public void cancelar() {
        status = StatusEvento.CANCELADO;
        for (Inscricao i : inscricoes) {
            i.cancelar();
        }
    }

    /**
     * Se o evento estava ABERTO e a data dele já passou, ele passa a ser
     * REALIZADO.
     *
     * @param agora data e hora atual
     */
    public void atualizarStatus(LocalDateTime agora) {
        if (status == StatusEvento.ABERTO && dataHora.isBefore(agora)) {
            status = StatusEvento.REALIZADO;
        }
    }

    // ------------------------------ abstratos ------------------------------

    /**
     * Como chegar no evento. Cada tipo responde de um jeito (polimorfismo):
     * endereço e sala no presencial, link e plataforma no online.
     *
     * @return texto com as informações de acesso
     */
    public abstract String getInformacoesAcesso();

    /**
     * @return "Presencial" ou "Online"
     */
    public abstract String getTipo();

    // --------------------------- getters e setters ---------------------------

    /** @return o código, ex.: EV001 */
    public String getCodigo() {
        return codigo;
    }

    /** @return o título */
    public String getTitulo() {
        return titulo;
    }

    /**
     * @param titulo novo título, não pode ficar vazio
     * @throws IllegalArgumentException se estiver vazio
     */
    public void setTitulo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("O título não pode ficar vazio.");
        }
        this.titulo = titulo.trim();
    }

    /** @return a descrição */
    public String getDescricao() {
        return descricao;
    }

    /** @param descricao nova descrição (se vier null fica vazia) */
    public void setDescricao(String descricao) {
        if (descricao == null) {
            this.descricao = "";
        } else {
            this.descricao = descricao.trim();
        }
    }

    /** @return data e hora do evento */
    public LocalDateTime getDataHora() {
        return dataHora;
    }

    /** @return data e hora no formato dd/MM/yyyy HH:mm */
    public String getDataHoraFormatada() {
        return dataHora.format(FORMATO_DATA_HORA);
    }

    /** @return só a data do evento no formato dd/MM/yyyy */
    public String getDataFormatada() {
        return dataHora.format(FORMATO_DATA);
    }

    /** @return o último dia para se inscrever */
    public LocalDate getDataLimiteInscricao() {
        return dataLimiteInscricao;
    }

    /** @return a data-limite no formato dd/MM/yyyy */
    public String getDataLimiteFormatada() {
        return dataLimiteInscricao.format(FORMATO_DATA);
    }

    /**
     * Muda a data do evento e a data-limite juntas. Fica num método só porque
     * uma depende da outra: a data-limite tem que ser até a data do evento.
     *
     * @param dataHora            nova data e hora do evento
     * @param dataLimiteInscricao nova data-limite de inscrição
     * @throws IllegalArgumentException se alguma for nula ou se a data-limite for depois do evento
     */
    public void alterarDatas(LocalDateTime dataHora, LocalDate dataLimiteInscricao) {
        if (dataHora == null || dataLimiteInscricao == null) {
            throw new IllegalArgumentException("As datas são obrigatórias.");
        }
        if (dataLimiteInscricao.isAfter(dataHora.toLocalDate())) {
            throw new IllegalArgumentException("A data-limite deve ser até a data do evento.");
        }
        this.dataHora = dataHora;
        this.dataLimiteInscricao = dataLimiteInscricao;
    }

    /** @return total de vagas */
    public int getVagas() {
        return vagas;
    }

    /**
     * @param vagas novo total de vagas
     * @throws IllegalArgumentException se for zero/negativo ou menor que os inscritos confirmados
     */
    public void setVagas(int vagas) {
        if (vagas <= 0) {
            throw new IllegalArgumentException("O número de vagas deve ser maior que zero.");
        }
        if (vagas < getQuantidadeConfirmadas()) {
            throw new IllegalArgumentException("O evento já tem " + getQuantidadeConfirmadas()
                    + " inscritos, não dá pra deixar menos vagas que isso.");
        }
        this.vagas = vagas;
    }

    /** @return o status (ABERTO, CANCELADO ou REALIZADO) */
    public StatusEvento getStatus() {
        return status;
    }

    /** @return quem organiza o evento */
    public Organizador getOrganizador() {
        return organizador;
    }

    /**
     * @param organizador novo organizador
     * @throws IllegalArgumentException se for nulo
     */
    public void setOrganizador(Organizador organizador) {
        if (organizador == null) {
            throw new IllegalArgumentException("O evento precisa de um organizador.");
        }
        this.organizador = organizador;
    }

    /** @return cópia da lista de inscrições (pra ninguém mexer na lista de fora) */
    public List<Inscricao> getInscricoes() {
        return new ArrayList<>(inscricoes);
    }

    // ------------------------ compareTo, equals, toString ------------------------

    /**
     * Compara pela data e hora, assim o Collections.sort deixa os eventos em
     * ordem de data.
     *
     * @param outro evento a comparar
     * @return negativo se este vem antes, positivo se vem depois, 0 se for igual
     */
    @Override
    public int compareTo(Evento outro) {
        return this.dataHora.compareTo(outro.dataHora);
    }

    /**
     * Dois eventos são iguais se tiverem o mesmo código.
     *
     * @param obj objeto a comparar
     * @return true se tiver o mesmo código
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Evento)) return false;
        Evento outro = (Evento) obj;
        return codigo.equals(outro.codigo);
    }

    /** @return hash pelo código, igual ao equals */
    @Override
    public int hashCode() {
        return codigo.hashCode();
    }

    /**
     * ATENÇÃO: aqui não pode imprimir a lista de inscrições, porque o
     * toString da Inscricao imprime o evento. Um chamando o outro dava
     * StackOverflowError.
     *
     * @return código, título, data, tipo e status
     */
    @Override
    public String toString() {
        return codigo + " - " + titulo + " (" + getDataHoraFormatada() + ") - " + getTipo() + " - " + status;
    }
}
