package History;

import Interfaces.HistoryManager;
import TaskClasses.Task;
import Utilities.Node;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
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
