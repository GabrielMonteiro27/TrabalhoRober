package visao;

import java.util.List;

import excecao.SistemaEventosException;
import mensagens.Mensagens;
import mensagens.MensagensPrograma;
import modelo.Inscricao;
import servico.GerenciadorEventos;

/** VERSÃO PROVISÓRIA: contém só a seção Check-in (parte do Heitor). */
public class Menu {

    private final GerenciadorEventos gerenciador;
    private final LeitorEntrada leitor;

    public Menu(GerenciadorEventos gerenciador, LeitorEntrada leitor) {
        this.gerenciador = gerenciador;
        this.leitor = leitor;
    }

    // ------------------------------ Check-in ------------------------------

    public void menuCheckIn() {
        try {
            String codigo = leitor.lerTexto(MensagensPrograma.pegar(Mensagens.PEDIR_CODIGO_EVENTO));
            List<Inscricao> inscricoes = gerenciador.listarInscricoes(codigo);

            if (inscricoes.isEmpty()) {
                System.out.println(MensagensPrograma.pegar(Mensagens.EVENTO_SEM_INSCRICOES));
                return;
            }

            System.out.println(MensagensPrograma.pegar(Mensagens.TITULO_INSCRICOES));
            for (Inscricao i : inscricoes) {
                System.out.println(i);
            }

            int numero = leitor.lerInteiro(MensagensPrograma.pegar(Mensagens.PEDIR_NUMERO_CHECKIN));
            gerenciador.registrarPresenca(codigo, numero);
            System.out.println(MensagensPrograma.pegar(Mensagens.PRESENCA_OK));
        } catch (SistemaEventosException | IllegalArgumentException | IllegalStateException e) {
            System.out.println(MensagensPrograma.pegar(Mensagens.ERRO, e.getMessage()));
        }
    }
}
