package inMemory;

import interfaces.HistoryManager;
import taskClasses.Task;

import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private List<Task> history;

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
