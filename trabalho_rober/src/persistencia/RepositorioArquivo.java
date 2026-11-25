package persistencia;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class RepositorioArquivo implements Repositorio {

    private static final String ARQUIVO_PADRAO = "dados.dat";

    private final File arquivo;

    public RepositorioArquivo() {
        this(ARQUIVO_PADRAO);
    }

    public RepositorioArquivo(String caminho) {
        this.arquivo = new File(caminho);
    }

    @Override
    public void salvar(BaseDeDados base) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(arquivo))) {
            out.writeObject(base); // um unico writeObject com tudo dentro
        } catch (IOException e) {
            throw new RepositorioException("Erro ao salvar os dados em " + arquivo.getName(), e);
        }
    }

    @Override
    public BaseDeDados carregar() {
        if (!arquivo.exists()) {
            return new BaseDeDados(); 
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(arquivo))) {
            return (BaseDeDados) in.readObject();
        } catch (IOException e) {
            throw new RepositorioException("Erro ao ler o arquivo " + arquivo.getName(), e);
        } catch (ClassNotFoundException e) {
            throw new RepositorioException("Classe do modelo nao encontrada ao carregar os dados", e);
        }
    }
}
