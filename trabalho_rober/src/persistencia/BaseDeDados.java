package persistencia;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import modelo.Evento;
import modelo.Pessoa;

/**
 * Reúne TUDO que precisa ser persistido: o mapa de pessoas, a lista de eventos
 * e o contador usado por {@link #gerarCodigoEvento()}.
 *
 * Como a base inteira é gravada em um único {@code writeObject}, o mesmo
 * participante, que aparece no mapa de pessoas e nas inscrições de vários
 * eventos, continua sendo UM só objeto depois de recarregar.
 *
 * @author Guilherme
 */
public class BaseDeDados implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Chave = CPF só com os 11 dígitos ({@link Pessoa#getCpf()}). */
    private final Map<String, Pessoa> pessoas = new HashMap<>();

    private final List<Evento> eventos = new ArrayList<>();

    private int proximoCodigoEvento = 1;

    /** @return o mapa de pessoas, com o CPF (11 dígitos) como chave */
    public Map<String, Pessoa> getPessoas() {
        return pessoas;
    }

    /** @return a lista de eventos cadastrados */
    public List<Evento> getEventos() {
        return eventos;
    }

    /**
     * Gera o próximo código de evento no formato EV001, EV002... O contador
     * também é gravado no arquivo, então a numeração continua depois de reabrir.
     *
     * @return o novo código de evento
     */
    public String gerarCodigoEvento() {
        return String.format("EV%03d", proximoCodigoEvento++);
    }

    /**
     * Resumo com as quantidades, sem listar os objetos.
     *
     * @return por exemplo {@code BaseDeDados{pessoas=12, eventos=3, proximoCodigoEvento=4}}
     */
    @Override
    public String toString() {
        return "BaseDeDados{pessoas=" + pessoas.size() + ", eventos=" + eventos.size()
                + ", proximoCodigoEvento=" + proximoCodigoEvento + "}";
    }

    /**
     * Duas bases são iguais quando têm as mesmas pessoas, os mesmos eventos
     * e o mesmo contador de códigos.
     *
     * @param obj objeto a comparar
     * @return {@code true} se o conteúdo for igual
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof BaseDeDados)) return false;
        BaseDeDados outra = (BaseDeDados) obj;
        return proximoCodigoEvento == outra.proximoCodigoEvento
                && pessoas.equals(outra.pessoas)
                && eventos.equals(outra.eventos);
    }

    /**
     * Calculado com os mesmos campos de {@link #equals(Object)}.
     *
     * @return código hash da base
     */
    @Override
    public int hashCode() {
        return Objects.hash(pessoas, eventos, proximoCodigoEvento);
    }
}
