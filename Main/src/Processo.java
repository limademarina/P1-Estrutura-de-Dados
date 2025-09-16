public class Processo {
    private int id;
    private String nome;
    private int prioridade;          // 1 - Alta, 2 - Média, 3 - Baixa
    private int ciclosNecessarios;
    private String recursoNecessario;
    private boolean jaSolicitouRecurso;

    public Processo(int id, String nome, int prioridade, int ciclosNecessarios, String recursoNecessario) {
        this.id = id;
        this.nome = nome;
        this.prioridade = prioridade;
        this.ciclosNecessarios = ciclosNecessarios;
        this.recursoNecessario = recursoNecessario;
    }

    public Processo(int id, String nome, int prioridade, int ciclosNecessarios) {
        this.id = id;
        this.nome = nome;
        this.prioridade = prioridade;
        this.ciclosNecessarios = ciclosNecessarios;
        this.recursoNecessario = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(int prioridade) {
        this.prioridade = prioridade;
    }

    public int getCiclosNecessarios() {
        return ciclosNecessarios;
    }

    public void setCiclosNecessarios(int ciclosNecessarios) {
        this.ciclosNecessarios = ciclosNecessarios;
    }

    public String getRecursoNecessario() {
        return recursoNecessario;
    }

    public void setRecursoNecessario(String recursoNecessario) {
        this.recursoNecessario = recursoNecessario;
    }

    public boolean isJaSolicitouRecurso() {
        return jaSolicitouRecurso;
    }

    public void setJaSolicitouRecurso(boolean jaSolicitouRecurso) {
        this.jaSolicitouRecurso = jaSolicitouRecurso;
    }

    @Override
    public String toString() {
        return "Processo {" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", prioridade=" + prioridade +
                ", ciclosNecessarios=" + ciclosNecessarios +
                ", recursoNecessario='" + recursoNecessario + '\'' +
                '}';
    }
}