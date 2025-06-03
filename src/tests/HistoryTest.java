package tests;

import history.InMemoryHistoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task_classes.Task;
import utilities.Status;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HistoryTest {
    private InMemoryHistoryManager historyManager;
    private Task task1;
    private Task task2;
    private Task task3;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
        task1 = new Task("task 1", "description 1", Status.NEW, 1);
        task2 = new Task("task 2", "description 2", Status.NEW, 2);
        task3 = new Task("task 3", "description 3", Status.NEW, 3);
    }

    @Test
    void newHistoryEmpty() {
        List<Task> history = historyManager.getHistory();
        assertTrue(history.isEmpty(), "история должна быть пустой");
    }

    @Test
    void shouldAddTaskToHistory() {
        historyManager.addTaskToHistory(task1);
        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size(), "история должна содержать одну задачу");
        assertEquals(task1, history.get(0), "задача должна соответствовать добавленной");
    }

    @Test
    void shouldRemoveDupicateTask() {
        historyManager.addTaskToHistory(task1);
        historyManager.addTaskToHistory(task2);
        historyManager.addTaskToHistory(task1);
        List<Task> history = historyManager.getHistory();

        assertEquals(2, history.size(), "история должна содержать две уникальные задачи");
        assertEquals(task2, history.get(0), "задача 2 должна быть первой в списке");
        assertEquals(task1, history.get(1), "задача 1 должна быть последней в списке");
    }

    @Test
    void shouldRemoveTaskFromHistory() {
        historyManager.addTaskToHistory(task1);
        historyManager.addTaskToHistory(task2);
        historyManager.addTaskToHistory(task3);
        historyManager.removeTaskFromHistory(task2.getId());
        List<Task> history = historyManager.getHistory();

        assertEquals(2, history.size(), "история должна содержать две уникальные задачи");
        assertEquals(task1, history.get(0), "задача 1 должна быть первой в списке");
        assertEquals(task3, history.get(1), "задача 3 должна быть последней в списке");
    }

    @Test
    void shouldMaintainCorrectOrder() {
        historyManager.addTaskToHistory(task1);
        historyManager.addTaskToHistory(task2);
        historyManager.addTaskToHistory(task3);
        List<Task> history = historyManager.getHistory();

        assertEquals(List.of(task1, task2, task3), history, "история должна сохраняться в порядке добавления задач");
    }

}
