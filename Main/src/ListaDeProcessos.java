public class ListaDeProcessos {

    /* Nó interno: permanece privado e invisível fora da classe. */
    private static final class Node {
        Processo value;
        Node next;

        Node(Processo p) {
            this.value = p;
        }
    }

    private Node head;
    private Node tail;
    private int size;

    /**
     * Insere no fim (fila).
     */
    public void addLast(Processo p) {
        Node n = new Node(p);
        if (tail == null) {
            head = tail = n;
        } else {
            tail.next = n;
            tail = n;
        }
        size++;
    }

    /**
     * Insere no início.
     */
    public void addFirst(Processo p) {
        Node n = new Node(p);
        if (head == null) {
            head = tail = n;
        } else {
            n.next = head;
            head = n;
        }
        size++;
    }

    /**
     * Remove e retorna o primeiro; null se vazia.
     */
    public Processo removeFirst() {
        if (head == null) return null;
        Processo out = head.value;
        head = head.next;
        if (head == null) tail = null;
        size--;
        return out;
    }

    public boolean isEmpty() {
        return size == 0;
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
}
