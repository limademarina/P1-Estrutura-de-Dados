public class Scheduler {

    // === Filas por prioridade e bloqueados ===
    private final ListaDeProcessos lista_alta_prioridade = new ListaDeProcessos();
    private final ListaDeProcessos lista_media_prioridade = new ListaDeProcessos();
    private final ListaDeProcessos lista_baixa_prioridade = new ListaDeProcessos();
    private final ListaDeProcessos lista_bloqueados = new ListaDeProcessos();

    // === Estado ===
    private int contador_ciclos_alta_prioridade = 0;
    private int ciclo = 0;

    // === Configurações ===
    private int antiStarvationLimit = 5; // após N execuções de alta, forçar média/baixa
    private boolean verbose = true;      // controla logs

    // === Configuradores ===
    public void setAntiStarvationLimit(int n) {
        if (n > 0) this.antiStarvationLimit = n;
    }

    public void setVerbose(boolean v) {
        this.verbose = v;
    }

    private void log(String s) {
        if (verbose) System.out.println(s);
    }

    public void adicionarProcesso(Processo p) {
        switch (p.getPrioridade()) {
            case 1:
                lista_alta_prioridade.addLast(p);
                break;
            case 2:
                lista_media_prioridade.addLast(p);
                break;
            case 3:
                lista_baixa_prioridade.addLast(p);
                break;
            default:
                throw new IllegalArgumentException("Prioridade inválida: " + p.getPrioridade());
        }
    }

    /**
     * Executa 1 ciclo e imprime logs. Retorna false quando tudo terminou.
     */
    public boolean executarCicloDeCPU() {
        if (tudoVazio()) return false;

        ciclo++;
        log("\n========== CICLO " + ciclo + " ==========");

        // 1) Desbloqueio no início do ciclo (1 processo por ciclo)
        Processo desbloqueado = lista_bloqueados.removeFirst();
        if (desbloqueado != null) {
            log("DESBLOQUEADO: " + resumo(desbloqueado));
            refileirarNoFimDaSuaPrioridade(desbloqueado);
        }

        // 2) Escolha do próximo (anti-inanição ou padrão)
        Processo emExecucao = null;
        boolean antiInanicaoUsada = false;

        if (contador_ciclos_alta_prioridade >= antiStarvationLimit) {
            emExecucao = lista_media_prioridade.removeFirst();
            if (emExecucao == null) emExecucao = lista_baixa_prioridade.removeFirst();
            if (emExecucao != null) {
                antiInanicaoUsada = true;
                contador_ciclos_alta_prioridade = 0;
            }
        }

        if (emExecucao == null) {
            emExecucao = lista_alta_prioridade.removeFirst();
            if (emExecucao == null) {
                emExecucao = lista_media_prioridade.removeFirst();
                if (emExecucao == null) {
                    emExecucao = lista_baixa_prioridade.removeFirst();
                }
            }
        }

        if (emExecucao == null) {
            // só havia bloqueados (já tratados); imprimir estado e sair
            imprimirEstadoListas();
            return !tudoVazio();
        }

        // 3) Bloqueio na primeira solicitação de DISCO
        if (precisaDeDiscoPelaPrimeiraVez(emExecucao)) {
            emExecucao.setJaSolicitouRecurso(true);
            lista_bloqueados.addLast(emExecucao);
            log("BLOQUEADO POR DISCO (1ª vez): " + resumo(emExecucao));
            imprimirEstadoListas();
            return !tudoVazio();
        }

        // 4) Execução (-1 ciclo)
        log("EXECUTANDO: " + resumo(emExecucao) + (antiInanicaoUsada ? " [anti-inanição]" : ""));
        emExecucao.setCiclosNecessarios(emExecucao.getCiclosNecessarios() - 1);

        // Atualiza contador alta
        if (emExecucao.getPrioridade() == 1) contador_ciclos_alta_prioridade++;
        else contador_ciclos_alta_prioridade = 0;

        // 5) Término ou refileirar
        if (emExecucao.getCiclosNecessarios() <= 0) {
            log("TERMINOU: " + resumo(emExecucao));
        } else {
            refileirarNoFimDaSuaPrioridade(emExecucao);
            log("REINSERIDO NO FIM DA FILA P" + emExecucao.getPrioridade() + ": " + resumo(emExecucao));
        }

        imprimirEstadoListas();
        return !tudoVazio();
    }

    // === Auxiliares ===
    private void refileirarNoFimDaSuaPrioridade(Processo p) {
        switch (p.getPrioridade()) {
            case 1:
                lista_alta_prioridade.addLast(p);
                break;
            case 2:
                lista_media_prioridade.addLast(p);
                break;
            case 3:
                lista_baixa_prioridade.addLast(p);
                break;
            default:
                throw new IllegalStateException("Prioridade inválida: " + p.getPrioridade());
        }
    }

    private boolean precisaDeDiscoPelaPrimeiraVez(Processo p) {
        String r = p.getRecursoNecessario();
        return r != null && "DISCO".equalsIgnoreCase(r) && !p.isJaSolicitouRecurso();
    }

    private boolean tudoVazio() {
        return lista_alta_prioridade.isEmpty()
                && lista_media_prioridade.isEmpty()
                && lista_baixa_prioridade.isEmpty()
                && lista_bloqueados.isEmpty();
    }

    private void imprimirEstadoListas() {
        log("Filas após o ciclo:");
        log("  Alta : " + lista_alta_prioridade);
        log("  Média: " + lista_media_prioridade);
        log("  Baixa: " + lista_baixa_prioridade);
        log("  Bloq.: " + lista_bloqueados);
        log("  Contador altas seguidas: " + contador_ciclos_alta_prioridade);
    }

    private String resumo(Processo p) {
        return "#" + p.getId() + " (" + p.getNome() + ", P" + p.getPrioridade()
                + ", ciclos=" + p.getCiclosNecessarios()
                + (p.getRecursoNecessario() != null ? ", recurso=" + p.getRecursoNecessario() : "")
              +")";
    }
}
