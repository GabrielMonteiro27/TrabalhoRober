package modelo;

/**
 * Pessoa responsável por organizar um ou mais eventos.
 *
 * @author Arthur
 */
public class Organizador extends Pessoa {

    private static final long serialVersionUID = 1L;

    private String cargo;

    /**
     * @param nome  nome completo
     * @param cpf   CPF com 11 dígitos
     * @param email e-mail no formato nome@dominio.com
     * @param cargo cargo na organização, ex.: "Coordenador de Extensão"
     * @throws IllegalArgumentException se algum dado for inválido
     */
    public Organizador(String nome, String cpf, String email, String cargo) {
        super(nome, cpf, email);
        setCargo(cargo);
    }

    /** @return "Organizador" */
    @Override
    public String getTipo() {
        return "Organizador";
    }

    /** @return o cargo, que é o "detalhe" do organizador */
    @Override
    public String getDetalhe() {
        return "Cargo: " + cargo;
    }

    /** @return o cargo */
    public String getCargo() {
        return cargo;
    }

    /**
     * @param cargo novo cargo, não pode ficar vazio
     * @throws IllegalArgumentException se estiver vazio
     */
    public void setCargo(String cargo) {
        this.cargo = textoObrigatorio(cargo, "Cargo");
    }

    /** @return os dados da pessoa mais o cargo */
    @Override
    public String toString() {
        return super.toString() + " - " + cargo;
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
