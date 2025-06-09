import interfaces.HistoryManager;
import interfaces.TaskManager;
import main.Managers;
import org.junit.jupiter.api.Assertions;
import taskclasses.Epic;
import taskclasses.SubTask;
import taskclasses.Task;
import utilities.Status;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class Test {
    Managers managers = new Managers();
    TaskManager taskManager = managers.getDefault();
    HistoryManager historyManager = Managers.getDefaultHistory();

    @org.junit.jupiter.api.Test
    void addNewTask() {
        Task task = taskManager.addNewTask("Test addNewTask", "Test addNewTask description", Status.NEW);
        final int taskId = task.getId();

        final Task savedTask = taskManager.getTaskById(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final ArrayList<Task> tasks = taskManager.taskList();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.getFirst(), "Задачи не совпадают.");
    }

    @org.junit.jupiter.api.Test
    void addNewEpic() {
        Epic epic = taskManager.addNewEpic("Test addNewEpic", "Test addNewEpic description", new ArrayList<>());
        final int epicId = epic.getId();

        final Task savedEpic = taskManager.getEpicById(epicId);

        assertNotNull(savedEpic, "Эпик не найден.");
        assertEquals(epic, savedEpic, "Эпики не совпадают.");

        final ArrayList<Epic> epics = taskManager.epicList();

        assertNotNull(epics, "Эпики не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(epic, epics.getFirst(), "Эпики не совпадают.");
    }

    @org.junit.jupiter.api.Test
    void addNewSubTask() {
        SubTask subTask = taskManager.addNewSubTask("Test addNewSubTask", "Test addNewSubTask description", Status.NEW, 14);
        final int subTaskId = subTask.getId();

        final Task savedSubTask = taskManager.getSubTaskById(subTaskId);

        assertNotNull(savedSubTask, "Подзадача не найдена.");
        assertEquals(subTask, savedSubTask, "Подзадачи не совпадают.");

        final ArrayList<SubTask> subTasks = taskManager.subTaskList();

        assertNotNull(subTasks, "Подзадачи не возвращаются.");
        assertEquals(1, subTasks.size(), "Неверное количество подзадач.");
        assertEquals(subTask, subTasks.getFirst(), "Подзадачи не совпадают.");
    }

    @org.junit.jupiter.api.Test
    void fieldsDidNotChange() {
        Task task = taskManager.addNewTask("Test fieldsDidNotChange", "Test fieldsDidNotChange description", Status.NEW);
        final int taskId = task.getId();
        assertEquals("Test fieldsDidNotChange", task.getName(), "название не совпадает");
        assertEquals("Test fieldsDidNotChange description", task.getDescription(), "описание не совпадает");
        Assertions.assertEquals(Status.NEW, task.getStatus(), "статус не совпадает");
        assertEquals(taskId, task.getId(), "айди не совпадает");
    }

    @org.junit.jupiter.api.Test
    void taskWasAdded() {
        Task task = taskManager.addNewTask("Test fieldsDidNotChange", "Test fieldsDidNotChange description", Status.NEW);
        historyManager.addTaskToHistory(task);
        ArrayList<Task> history1 = new ArrayList<>();
        history1.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
        assertEquals(history1, history, "Истории не равны");
    }

    @org.junit.jupiter.api.Test
    void tasksInManagerCouldBeFound() {
        Task task = taskManager.addNewTask("Test fieldsDidNotChange", "Test fieldsDidNotChange description", Status.NEW);
        Epic epic = taskManager.addNewEpic("Test addNewEpic", "Test addNewEpic description", new ArrayList<>());
        SubTask subTask = taskManager.addNewSubTask("Test addNewSubTask", "Test addNewSubTask description", Status.NEW, taskManager.epicList().getFirst().getId());
        int taskId = task.getId();
        int epicId = epic.getId();
        int subTaskId = subTask.getId();

        Assertions.assertEquals(task, taskManager.getTaskById(taskId), "задачи не совпадают");
        Assertions.assertEquals(epic, taskManager.getEpicById(epicId), "эпики не совпадают");
        Assertions.assertEquals(subTask, taskManager.getSubTaskById(subTaskId), "подзадачи не совпадают");

    }

    @org.junit.jupiter.api.Test
    void shouldRemoveSubtaskCompletely() {
        Epic epic = taskManager.addNewEpic("epic", "epicDesc", new ArrayList<>());
        SubTask subtask = taskManager.addNewSubTask("subtask", "subtaskDesc", Status.NEW, epic.getId());

        int subtaskId = subtask.getId();
        taskManager.deleteSubTaskById(subtaskId);
        assertTrue(taskManager.getSubTaskById(subtaskId) == null, "подзадача должна быть удалена из менеджера");
        assertFalse(taskManager.subTaskList().contains(subtask), "список подзадач не должен содержать удалённую подзадачу");
    }

    @org.junit.jupiter.api.Test
    void epicShouldNotContainDeletedSubtaskId() {
        Epic epic = taskManager.addNewEpic("epic", "epicDesc", new ArrayList<>());
        SubTask subtask = taskManager.addNewSubTask("subtask", "subtaskDesc", Status.NEW, epic.getId());

        int subtaskId = subtask.getId();
        taskManager.deleteSubTaskById(subtaskId);

        Epic updatedEpic = taskManager.getEpicById(epic.getId());
        assertFalse(updatedEpic.getSubTasks().contains(subtaskId), "эпик не должен содержать ID удалённой подзадачи");
    }
}




