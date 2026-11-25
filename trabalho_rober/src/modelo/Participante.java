package modelo;

/**
 * Pessoa que pode se inscrever nos eventos.
 *
 * Além dos dados de toda pessoa, guarda a instituição de onde ela vem
 * (faculdade, empresa, etc).
 *
 * @author Arthur
 */
public class Participante extends Pessoa {

    private static final long serialVersionUID = 1L;

    private String instituicao;

    /**
     * @param nome        nome completo
     * @param cpf         CPF com 11 dígitos
     * @param email       e-mail no formato nome@dominio.com
     * @param instituicao instituição do participante
     * @throws IllegalArgumentException se algum dado for inválido
     */
    public Participante(String nome, String cpf, String email, String instituicao) {
        super(nome, cpf, email);
        setInstituicao(instituicao);
    }

    /** @return "Participante" */
    @Override
    public String getTipo() {
        return "Participante";
    }

    /** @return a instituição, que é o "detalhe" do participante */
    @Override
    public String getDetalhe() {
        return "Instituição: " + instituicao;
    }

    /** @return a instituição */
    public String getInstituicao() {
        return instituicao;
    }

    /**
     * @param instituicao nova instituição, não pode ficar vazia
     * @throws IllegalArgumentException se estiver vazia
     */
    public void setInstituicao(String instituicao) {
        this.instituicao = textoObrigatorio(instituicao, "Instituição");
    }

    /** @return os dados da pessoa mais a instituição */
    @Override
    public String toString() {
        return super.toString() + " - " + instituicao;
    }

    /**
     * Usa o equals da Pessoa, que compara pelo CPF.
     *
     * @param obj objeto a comparar
     * @return true se tiver o mesmo CPF
     */
    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    /** @return o mesmo hash da Pessoa (pelo CPF) */
    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
