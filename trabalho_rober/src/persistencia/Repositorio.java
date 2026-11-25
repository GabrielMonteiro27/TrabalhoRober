package persistencia;

public interface Repositorio {

    void salvar(BaseDeDados base);

    BaseDeDados carregar();
}
