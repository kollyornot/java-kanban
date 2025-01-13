import java.util.ArrayList;

public interface HistoryManager {
    ArrayList<Task> getHistory();

    void addTaskToHistory(Task task);
}
