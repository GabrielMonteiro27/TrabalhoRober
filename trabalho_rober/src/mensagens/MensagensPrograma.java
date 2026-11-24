package mensagens;

import java.util.HashMap;
import java.util.Map;

/**
 * VERSÃO PROVISÓRIA para as mensagens não ficarem espalhadas eu vou colocar todas aqui por que fica masi facil de achar elas( Heitor ).
 * Assim pra usar uma mansagem é só saber o código dela, e se ela for colocada em mais de um lugar nao precisa copiar ela
 */
public class MensagensPrograma {
    static Map<Mensagens, String> mensagens = new HashMap<>();

    public static void carregar() {
        // Evento
        mensagens.put(Mensagens.PRAZO_ACABOU, "O prazo de inscrição terminou.");
        mensagens.put(Mensagens.ERRO_SALVAR, "Erro ao salvar dados: %s");
        mensagens.put(Mensagens.ERRO_CARREGAR, "Erro ao carregar dados: %s");

        // Inscricao
        mensagens.put(Mensagens.NUMERO_INSCRICAO_NEGATIVO, "O número da inscrição deve ser positivo.");
        mensagens.put(Mensagens.PARTICIPANTE_OBRIGATORIO, "O participante é obrigatório.");
        mensagens.put(Mensagens.EVENTO_OBRIGATORIO, "O evento é obrigatório.");
        mensagens.put(Mensagens.NAO_PODE_CANCELAR_PRESENTE, "Não é possível cancelar: a presença já foi registrada.");
        mensagens.put(Mensagens.NAO_PODE_CHECKIN_CANCELADA, "Inscrição cancelada não pode fazer check-in.");
        mensagens.put(Mensagens.PRESENCA_JA_REGISTRADA, "A presença já foi registrada em %s.");
        mensagens.put(Mensagens.INSCRICAO_TEXTO, "Inscrição #%d | %s | %s | inscrito em %s");
        mensagens.put(Mensagens.INSCRICAO_TEXTO_PRESENCA, " | presença em %s");

        // GerenciadorEventos
        mensagens.put(Mensagens.CPF_NAO_ENCONTRADO, "CPF não encontrado: %s");
        mensagens.put(Mensagens.EVENTO_NAO_ENCONTRADO, "Evento não encontrado: %s");
        mensagens.put(Mensagens.SO_PARTICIPANTE_INSCREVE, "Apenas participantes podem se inscrever em eventos.");
        mensagens.put(Mensagens.JA_INSCRITO, "O participante já está inscrito neste evento.");
        mensagens.put(Mensagens.SEM_VAGAS, "Não há vagas disponíveis neste evento.");
        mensagens.put(Mensagens.INSCRICAO_NAO_ENCONTRADA, "Inscrição #%d não encontrada neste evento.");

        // LeitorEntrada
        mensagens.put(Mensagens.CAMPO_VAZIO, "O campo não pode ficar vazio.");
        mensagens.put(Mensagens.NUMERO_INVALIDO, "Digite um número válido.");

        // Menu
        mensagens.put(Mensagens.EVENTO_SEM_INSCRICOES, "Este evento não tem inscrições.");
        mensagens.put(Mensagens.TITULO_INSCRICOES, "\n--- Inscrições do evento ---");
        mensagens.put(Mensagens.PEDIR_CODIGO_EVENTO, "Código do evento: ");
        mensagens.put(Mensagens.PEDIR_NUMERO_CHECKIN, "\nNúmero da inscrição para check-in: ");
        mensagens.put(Mensagens.PRESENCA_OK, "Presença registrada com sucesso.");
        mensagens.put(Mensagens.ERRO, "Erro: %s");

        // TesteHeitor
        mensagens.put(Mensagens.TESTE_INFO, "CPFs: 111 (Ana), 222 (Bruno) | Eventos: E1 (1 vaga), E2 (prazo vencido)");
        mensagens.put(Mensagens.TESTE_MENU, "\n1-Inscrever  2-Cancelar inscrição  3-Check-in  4-Cadastrar Pessoa  5-Cadastrar Evento  6-Listar pessoas  7-Listar Eventos 0-Sair");
        mensagens.put(Mensagens.PEDIR_OPCAO, "Opção: ");
        mensagens.put(Mensagens.PEDIR_CPF, "CPF: ");
        mensagens.put(Mensagens.PEDIR_NOME, "Nome: ");
        mensagens.put(Mensagens.PEDIR_DATA, "Data do Evento (dia/mes/ano): ");
        mensagens.put(Mensagens.PEDIR_CAPACIDADE, "Capacidade de pessoas: ");
        mensagens.put(Mensagens.PEDIR_NUMERO_INSCRICAO, "Número da inscrição: ");
        mensagens.put(Mensagens.INSCRICAO_OK, "OK: %s");
        mensagens.put(Mensagens.INSCRICAO_CANCELADA, "Inscrição cancelada.");
    }

    /**
     * Pega a mensagem pelo código. Se a mensagem tiver %s ou %d é só passar os valores em seguida.
     */
    public static String pegar(Mensagens codigo, Object... valores) {
        if (mensagens.isEmpty()) {
            carregar();
        }
        String texto = mensagens.get(codigo);
        if (valores.length == 0) {
            return texto;
        }
        return String.format(texto, valores);
    }
}
