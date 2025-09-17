import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        if (args.length == 0) {
            printUsageAndExit();
        }

        String filePath = null;
        boolean quiet = false;
        int antiLimit = -1;

        // parse simples dos argumentos
        for (String a : args) {
            if (a.equalsIgnoreCase("--quiet")) {
                quiet = true;
            } else if (a.startsWith("--anti=")) {
                try {
                    antiLimit = Integer.parseInt(a.substring("--anti=".length()));
                } catch (NumberFormatException ignored) {}
            } else {
                filePath = a; // primeiro argumento não-opcional é o arquivo
            }
        }

        if (filePath == null) {
            printUsageAndExit();
        }

        Scheduler scheduler = new Scheduler();
        scheduler.setVerbose(!quiet);
        if (antiLimit > 0) scheduler.setAntiStarvationLimit(antiLimit);

        long tTotalStart = System.nanoTime();

        carregarDoArquivo(filePath, scheduler);

        long tSchedStart = System.nanoTime();

        while (scheduler.executarCicloDeCPU()) {
            // nada
        }

        long tSchedEnd = System.nanoTime();
        long tTotalEnd = System.nanoTime();

        double msSched = (tSchedEnd - tSchedStart) / 1_000_000.0;
        double msTotal = (tTotalEnd - tTotalStart) / 1_000_000.0;

        System.out.println("\n=== Todos os processos foram finalizados! ===");
        System.out.printf("Duração (apenas escalonamento): %.3f ms%n", msSched);
        System.out.printf("Duração total (leitura + escalonamento): %.3f ms%n", msTotal);
    }

    private static void carregarDoArquivo(String filePath, Scheduler scheduler) {
        boolean isCsv = filePath.toLowerCase().endsWith(".csv");
        boolean skipHeader = isCsv;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                if (skipHeader && line.toLowerCase().startsWith("id,")) continue;

                Processo p = parseLinha(line, isCsv);
                if (p != null) scheduler.adicionarProcesso(p);
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler arquivo '" + filePath + "': " + e.getMessage());
            System.exit(2);
        }
    }

    private static Processo parseLinha(String line, boolean isCsv) {
        try {
            String[] parts;
            if (isCsv) {
                parts = line.split(",", -1); // mantém campos vazios
            } else {
                parts = line.replace(';', ' ').replace(',', ' ').trim().split("\\s+");
            }

            if (parts.length < 4) {
                System.err.println("Linha inválida: " + line);
                return null;
            }

            int id = Integer.parseInt(parts[0]);
            String nome = parts[1];
            int prioridade = Integer.parseInt(parts[2]);
            int ciclos = Integer.parseInt(parts[3]);
            String recurso = (parts.length > 4 && !parts[4].isEmpty()) ? parts[4] : null;

            return (recurso == null)
                    ? new Processo(id, nome, prioridade, ciclos)
                    : new Processo(id, nome, prioridade, ciclos, recurso);

        } catch (Exception e) {
            System.err.println("Falha ao parsear linha: " + line);
            return null;
        }
    }

    private static void printUsageAndExit() {
        System.err.println("Uso: java Main <arquivo.csv|txt> [--quiet] [--anti=N]");
        System.err.println("Exemplos:");
        System.err.println("  java Main processos_1000.csv --quiet");
        System.err.println("  java Main processos.txt --anti=7");
        System.exit(1);
    }
}