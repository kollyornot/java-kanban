package history;

import interfaces.HistoryManager;
import taskclasses.Task;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private class Node<T> {
        private T value;
        private Node next;
        private Node prev;

        public Node(T value) {
            this.value = value;
            this.next = null;
        }

        public Node(T value, Node<T> next) {
            this.value = value;
            this.next = next;
        }

        public T getValue() {
            return this.value;
        }

        public Node<T> getNext() {
            return this.next;
        }

        public Node<T> getPrev() {
            return this.prev;
        }

        public void setValue(T value) {
            this.value = value;
        }

        public void setNext(Node<T> next) {
            this.next = next;
        }

        public void setPrev(Node<T> prev) {
            this.prev = prev;
        }

        public boolean hasNext() {
            return (this.next != null);
        }
    }

    private final Map<Integer, Node<Task>> history = new HashMap<>();
    private Node<Task> last;
    private Node<Task> first;

    @Override
    public List<Task> getHistory() {
        List<Task> historyList = new LinkedList<>();
        Node<Task> pos = first;
        while (pos != null) {
            historyList.add(pos.getValue());
            pos = pos.getNext();
        }
        return historyList;
    }

    @Override
    public void addTaskToHistory(Task task) {
        removeTaskFromHistory(task.getId());
        Node<Task> newNode = new Node<>(task);
        if (last != null) {
            last.setNext(newNode);
            newNode.setPrev(last);
        }
        last = newNode;
        history.put(task.getId(), newNode);
        if (first == null) {
            first = newNode;
        }
    }

    @Override
    public void removeTaskFromHistory(int id) {
        Node<Task> node = history.get(id);
        if (node != null) {
            removeNode(node);
        }
        history.remove(id);
    }

    public void removeNode(Node<Task> node) {
        if (node.getPrev() != null) {
            node.getPrev().setNext(node.getNext());
        }
        if (node.getNext() != null) {
            node.getNext().setPrev(node.getPrev());
        }
        if (node == last) {
            last = node.getPrev();
        }
        if (node == first) {
            first = node.getNext();
        }

    }
}
