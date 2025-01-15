package main;

import history.InMemoryHistoryManager;
import taskInteraction.InMemoryTaskManager;
import interfaces.HistoryManager;
import interfaces.TaskManager;

public class Managers {

    private InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
    private static InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();

    public TaskManager getDefault() {
        return inMemoryTaskManager;
    }

    public static HistoryManager getDefaultHistory() {
        return inMemoryHistoryManager;
    }
}
