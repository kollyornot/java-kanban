package history;

import interfaces.HistoryManager;
import taskClasses.Task;

import java.util.LinkedList;
import java.util.List;
//я не совсем поняла, в чем разница между использованием списка, и связанного списка

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> history = new LinkedList<>();

    @Override
    public List<Task> getHistory() {
        return this.history;
    }

    @Override
    public void addTaskToHistory(Task task) {
        if (history.size() == 10) {
            history.removeFirst();
        }
        history.add(task);
    }
}
