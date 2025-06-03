package Main;

import History.InMemoryHistoryManager;
import TaskInteraction.InMemoryTaskManager;
import Interfaces.HistoryManager;
import Interfaces.TaskManager;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
