package interfaces;

import task_classes.Task;

import java.util.List;

public interface HistoryManager {
    List<Task> getHistory();

    void addTaskToHistory(Task task);

    void removeTaskFromHistory(int id);
}
