import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    ArrayList<Task> history;

    @Override
    public ArrayList<Task> getHistory() {
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
