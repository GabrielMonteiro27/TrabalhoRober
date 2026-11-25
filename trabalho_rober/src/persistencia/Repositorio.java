package persistencia;

import excecao.SistemaEventosException;

/**
 * Contrato de persistência do sistema (RF14).
 * <p>
 * Quem usa o repositório (o {@code GerenciadorEventos}) não precisa saber
 * COMO os dados são guardados. Trocar a serialização por JSON, por exemplo,
 * seria uma nova classe que implementa esta interface, sem mudar o resto.
 */
public interface Repositorio {

    /**
     * Grava a base de dados completa.
     *
     * @param dados base com pessoas, eventos e o contador de códigos
     * @throws SistemaEventosException se não for possível gravar os dados
     */
    void salvar(BaseDeDados dados) throws SistemaEventosException;

    /**
     * Carrega a base de dados. Se ainda não houver dados salvos, devolve uma base vazia.
     *
     * @return a base carregada, nunca {@code null}
     * @throws SistemaEventosException se o arquivo existir mas não puder ser lido
     */
    BaseDeDados carregar() throws SistemaEventosException;
}
