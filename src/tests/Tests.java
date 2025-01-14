package tests;

import interfaces.HistoryManager;
import interfaces.TaskManager;
import main.Managers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import taskClasses.Epic;
import taskClasses.SubTask;
import taskClasses.Task;
import utilities.Status;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class Tests {
    Managers managers = new Managers();
    TaskManager taskManager = managers.getDefault();
    HistoryManager historyManager = Managers.getDefaultHistory();

    @Test
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

    @Test
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

    @Test
    void addNewSubTask() {
        SubTask subTask = taskManager.addNewSubTask("Test addNewSubTask", "Test addNewSubTask description", Status.NEW, taskManager.epicList().getFirst().getId());
        final int subTaskId = subTask.getId();

        final Task savedSubTask = taskManager.getSubTaskById(subTaskId);

        assertNotNull(savedSubTask, "Подзадача не найдена.");
        assertEquals(subTask, savedSubTask, "Подзадачи не совпадают.");

        final ArrayList<SubTask> subTasks = taskManager.subTaskList();

        assertNotNull(subTasks, "Подзадачи не возвращаются.");
        assertEquals(1, subTasks.size(), "Неверное количество подзадач.");
        assertEquals(subTask, subTasks.getFirst(), "Подзадачи не совпадают.");
    }
/*
    @Test
    void cannotAddEpicAsSubTask() {
        taskClasses.Epic epic = taskManager.addNewEpic("Test cannotAddEpicAsSubTasks", "Test cannotAddEpicAsSubTask description", new ArrayList<>());
        taskClasses.Epic epic1 = taskManager.addNewEpic("Test cannotAddEpicAsSubTask1", "Test cannotAddEpicAsSubTask1 description", new ArrayList<>());
        taskManager.addSubTask((taskClasses.SubTask) epic, epic1); <-- error bc of the difference between classes

    } */
    //проверьте, что объект Subtask нельзя сделать своим же эпиком: не вижу смысла для проверки, в моем коде
    // айди подзадачи генерируется сам, создавая уникальный айди, который не может совпасть с айди эпика

    //проверьте, что объект taskClasses.Epic нельзя добавить в самого себя в виде подзадачи; - то же самое, я добавила
    //проверку в методе, но я не вижу в ней никакого смысла

    //проверьте, что задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера; - у меня не задаются
    //никакие айди, все генерируются
    @Test
    void fieldsDidNotChange() {
        Task task = taskManager.addNewTask("Test fieldsDidNotChange", "Test fieldsDidNotChange description", Status.NEW);
        final int taskId = task.getId();
        assertEquals("Test fieldsDidNotChange", task.getName(), "название не совпадает");
        assertEquals("Test fieldsDidNotChange description", task.getDescription(), "описание не совпадает");
        Assertions.assertEquals(Status.NEW, task.getStatus(), "статус не совпадает");
        assertEquals(taskId, task.getId(), "айди не совпадает");
    }

    @Test
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

    @Test
    void tasksInManagerCouldBeFound() {
        Task task = taskManager.addNewTask("Test fieldsDidNotChange", "Test fieldsDidNotChange description", Status.NEW);
        Epic epic = taskManager.addNewEpic("Test addNewEpic", "Test addNewEpic description", new ArrayList<>());
        SubTask subTask = taskManager.addNewSubTask("Test addNewSubTask", "Test addNewSubTask description", Status.NEW, taskManager.epicList().getFirst().getId());
        int taskId = subTask.getId();
        int epicId = epic.getId();
        int subTaskId = subTask.getId();

        Assertions.assertEquals(task, taskManager.getTaskById(taskId), "задачи не совпадают");
        Assertions.assertEquals(epic, taskManager.getEpicById(epicId), "эпики не совпадают");
        Assertions.assertEquals(subTask, taskManager.getSubTaskById(subTaskId), "подзадачи не совпадают");

    }
}




