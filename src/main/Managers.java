package main;

import history.InMemoryHistoryManager;
import taskInteraction.InMemoryTaskManager;
import interfaces.HistoryManager;
import interfaces.TaskManager;

public class Managers {

    private InMemoryTaskManager inMemoryTaskManager;
    private static InMemoryHistoryManager inMemoryHistoryManager;

    public TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
