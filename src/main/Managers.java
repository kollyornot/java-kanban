package main;

import inMemory.InMemoryHistoryManager;
import inMemory.InMemoryTaskManager;
import interfaces.HistoryManager;
import interfaces.TaskManager;

public class Managers {

    private InMemoryTaskManager inMemoryTaskManager;
    private static InMemoryHistoryManager inMemoryHistoryManager;

    public TaskManager getDefault() {
        return inMemoryTaskManager;
    }

    public static HistoryManager getDefaultHistory() {
        return inMemoryHistoryManager;
    }
}
