public class ListaDeProcessos {

    /* Nó interno: permanece privado e invisível fora da classe. */
    private static final class Node {
        Processo value;
        Node next;
        Node(Processo p) { this.value = p; }
    }

    private Node head;
    private Node tail;
    private int size;

    /** Insere no fim (fila). */
    public void addLast(Processo p) {
        Node n = new Node(p);
        if (tail == null) { head = tail = n; }
        else { tail.next = n; tail = n; }
        size++;
    }

    /** Insere no início. */
    public void addFirst(Processo p) {
        Node n = new Node(p);
        if (head == null) { head = tail = n; }
        else { n.next = head; head = n; }
        size++;
    }

    /** Remove e retorna o primeiro; null se vazia. */
    public Processo removeFirst() {
        if (head == null) return null;
        Processo out = head.value;
        head = head.next;
        if (head == null) tail = null;
        size--;
        return out;
    }

    /** Retorna o primeiro sem remover; null se vazia. */
    public Processo peekFirst() {
        return head == null ? null : head.value;
    }

    /** Busca por id; null se não achar. */
    public Processo findById(int id) {
        Node cur = head;
        while (cur != null) {
            if (cur.value.getId() == id) return cur.value;
            cur = cur.next;
        }
        return null;
    }

    /** Remove o primeiro processo com o id informado. */
    public boolean removeById(int id) {
        Node prev = null, cur = head;
        while (cur != null) {
            if (cur.value.getId() == id) {
                if (prev == null) {
                    head = cur.next;
                    if (head == null) tail = null;
                } else {
                    prev.next = cur.next;
                    if (cur == tail) tail = prev;
                }
                size--;
                return true;
            }
            prev = cur; cur = cur.next;
        }
        return false;
    }

    public boolean isEmpty() { return size == 0; }

    public int size() { return size; }

    /** Iterador seguro que NÃO expõe Node em nenhuma assinatura pública. */
    public ProcessoIterator iterator() { return new Itr(head); }

    /** Interface pública e simples para iterar. */
    public interface ProcessoIterator {
        boolean hasNext();
        Processo next();
    }

    /** Implementação privada do iterador (usa Node internamente). */
    private static final class Itr implements ProcessoIterator {
        private Node current;
        private Itr(Node start) { this.current = start; }
        public boolean hasNext() { return current != null; }
        public Processo next() {
            if (current == null) return null;
            Processo p = current.value;
            current = current.next;
            return p;
        }
    }

    /** Alternativa: callback de iteração. */
    public void forEach(ProcessConsumer consumer) {
        Node cur = head;
        while (cur != null) {
            consumer.accept(cur.value);
            cur = cur.next;
        }
    }

    /** Converte para array (sem usar coleções prontas). */
    public Processo[] toArray() {
        Processo[] arr = new Processo[size];
        int i = 0;
        Node cur = head;
        while (cur != null) {
            arr[i++] = cur.value;
            cur = cur.next;
        }
        return arr;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("ListaDeProcessos[");
        Node cur = head;
        while (cur != null) {
            sb.append(cur.value.getId()).append(":").append(cur.value.getNome());
            cur = cur.next;
            if (cur != null) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    /** Interface funcional simples (evita java.util.function). */
    public interface ProcessConsumer {
        void accept(Processo p);
    }
}
