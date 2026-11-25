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
 *
 * @author Anderson
 */
public class Main {

    /**
     * Começa o programa.
     *
     * @param args não usado
     */
    public static void main(String[] args) {
        GerenciadorEventos gerenciador;
        try {
            // O GerenciadorEventos carrega os dados no construtor.
            gerenciador = new GerenciadorEventos(new RepositorioArquivo());
        } catch (SistemaEventosException e) {
            System.out.println("[ERRO] " + e.getMessage());
            System.out.println("Apague ou renomeie o arquivo dados.dat para começar do zero.");
            return;
        }

        Menu menu = new Menu(gerenciador, new LeitorEntrada());
        menu.executar();

        System.out.print("Salvando dados em dados.dat... ");
        try {
            gerenciador.salvar();
            System.out.println("[OK]");
        } catch (SistemaEventosException e) {
            System.out.println("[ERRO] " + e.getMessage());
        }
        System.out.println("Até logo!");
    }
}
