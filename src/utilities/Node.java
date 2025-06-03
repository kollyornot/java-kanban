package utilities;

public class Node<Task> {
    private Task value;
    private Node next;
    private Node prev;

    public Node(Task value) {
        this.value = value;
        this.next = null;
    }

    public Node(Task value, Node<Task> next) {
        this.value = value;
        this.next = next;
    }

    public Task getValue() {
        return this.value;
    }

    public Node<Task> getNext() {
        return this.next;
    }

    public Node<Task> getPrev() {
        return this.prev;
    }

    public void setValue(Task value) {
        this.value = value;
    }

    public void setNext(Node<Task> next) {
        this.next = next;
    }

    public void setPrev(Node<Task> prev) {
        this.prev = prev;
    }

    public boolean hasNext() {
        return (this.next != null);
    }
}
