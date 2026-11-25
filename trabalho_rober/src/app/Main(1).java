package app;

import excecao.SistemaEventosException;
import persistencia.RepositorioArquivo;
import servico.GerenciadorEventos;
import visao.LeitorEntrada;
import visao.Menu;

/**
 * Ponto de entrada do Sistema de Gerenciamento de Eventos.
 *
 * Carrega os dados do arquivo (dados.dat), abre o menu principal e salva
 * os dados quando o usuário escolhe "Salvar e sair".
 */
public class Main {

    /**
     * @param args não usado
     */
    public static void main(String[] args) {
        try {
            // O GerenciadorEventos carrega os dados no construtor (RF14).
            GerenciadorEventos gerenciador = new GerenciadorEventos(new RepositorioArquivo());
            Menu menu = new Menu(gerenciador, new LeitorEntrada());

            menu.executar();

            System.out.print("Salvando dados em dados.dat... ");
            gerenciador.salvar();
            System.out.println("[OK]");
            System.out.println("Até logo!");
        } catch (SistemaEventosException | RuntimeException e) {
            System.out.println("[ERRO] " + e.getMessage());
        }
    }
}
