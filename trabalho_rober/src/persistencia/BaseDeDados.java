package persistencia;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import modelo.Evento;
import modelo.Pessoa;


public class BaseDeDados implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Map<String, Pessoa> pessoas = new HashMap<>();

    private final List<Evento> eventos = new ArrayList<>();

    private int proximoCodigoEvento = 1;

    public Map<String, Pessoa> getPessoas() {
        return pessoas;
    }

    public List<Evento> getEventos() {
        return eventos;
    }

    public int gerarCodigoEvento() {
        return proximoCodigoEvento++;
    }
}
