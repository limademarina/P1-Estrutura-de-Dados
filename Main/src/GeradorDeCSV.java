import java.io.FileWriter;
import java.io.IOException;

public class GeradorDeCSV {
    public static void main(String[] args) {
        String nomeArquivo = "processos_1000.csv";

        try (FileWriter fw = new FileWriter(nomeArquivo)) {
            // cabeçalho
            fw.write("id,nome,prioridade,ciclos,recurso\n");

            // gera 1000 processos
            for (int i = 1; i <= 1000; i++) {
                int prioridade = ((i - 1) % 3) + 1;   // 1,2,3 em ciclo
                int ciclos = 2 + (i % 8);             // 2..9
                String recurso = (i % 3 == 0) ? "DISCO" : "";

                fw.write(i + ",P" + i + "," + prioridade + "," + ciclos + "," + recurso + "\n");
            }

            System.out.println("Arquivo '" + nomeArquivo + "' gerado com sucesso!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}