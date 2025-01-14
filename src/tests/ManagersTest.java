package tests;

import interfaces.HistoryManager;
import interfaces.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import main.Managers;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {
    private Managers managers;

    @BeforeEach
    void setUp() {
        managers = new Managers();
    }

    @Test
    void getDefaultShouldReturnInitializedTaskManager() {
        TaskManager taskManager = managers.getDefault();
        assertNotNull(taskManager, "менеджер задач должен быть инициализирован");
    }

    @Test
    void getDefaultHistoryShouldReturnInitializedHistoryManager() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        assertNotNull(historyManager, "менеджер истории должен быть инициализирован");
    }
}
