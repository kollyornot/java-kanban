import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import taskclasses.Epic;
import taskclasses.SubTask;
import taskclasses.Task;
import taskinteraction.FileBackedTaskManager;
import utilities.Status;

import java.io.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedManagerTest {
    private File testFile;

    @BeforeEach
    void setUp() throws IOException {
        testFile = File.createTempFile("test", ".csv");
        try (Writer writer = new FileWriter(testFile, StandardCharsets.UTF_8)) {
            writer.write("id,type,name,status,description,epic\n");
            writer.write("1,TASK,Task1,NEW,task description1,\n");
            writer.write("2,EPIC,Epic1,NEW,epic description,\n");
            writer.write("3,SUBTASK,SubTask1,DONE,subtask description,2\n");
        }
    }

    @AfterEach
    void delete() {
        testFile.delete();
    }

    @Test
    void loadFromFile_shouldRestoreTasksCorrectly() {
        FileBackedTaskManager manager = FileBackedTaskManager.loadFromFile(testFile);

        List<Task> tasks = manager.taskList();
        assertEquals(1, tasks.size(), "количество заданий не совпадает");
        assertEquals("Task1", tasks.getFirst().getName(), "имя задания не совпадает");

        List<Epic> epics = manager.epicList();
        assertEquals(1, epics.size(), "количество эпиков не совпадает");
        Epic epic = epics.getFirst();
        assertEquals("Epic1", epic.getName(), "имя эпика не совпадает");

        List<SubTask> subTasks = manager.subTaskList();
        assertEquals(1, subTasks.size(), "количество подзаданий не совпадает");
        SubTask subTask = subTasks.getFirst();
        assertEquals("SubTask1", subTask.getName(), "имя подзадания не совпадает");
        assertEquals(Status.DONE, subTask.getStatus(), "статус не совпадает");
        assertEquals(2, subTask.getEpicId(), "айди эпика не соответствует");

        // Проверка связи между подзадачей и эпиком
        assertEquals(1, epic.getSubTasks().size(), "количество подзаданий у эпика не соответствует");
        assertEquals(3, epic.getSubTasks().getFirst(), "неверный айди подзадачи у эпика");
    }

    @Test
    void loadFromFile_shouldHandleEmptyFileGracefully() throws IOException {
        File emptyFile = File.createTempFile("empty", ".csv");
        try (Writer writer = new FileWriter(emptyFile, StandardCharsets.UTF_8)) {
            writer.write("id,type,name,status,description,epic\n");
        }

        FileBackedTaskManager manager = FileBackedTaskManager.loadFromFile(emptyFile);

        assertTrue(manager.taskList().isEmpty(), "taskList должен быть пустым");
        assertTrue(manager.epicList().isEmpty(), "epicList должен быть пустым");
        assertTrue(manager.subTaskList().isEmpty(), "subTaskList должен быть пустым");

        emptyFile.delete();
    }

    @Test
    void loadFromFile_shouldLoadAllTaskTypesAndMaintainRelations() throws IOException {
        FileBackedTaskManager manager = FileBackedTaskManager.loadFromFile(testFile);

        Task task = manager.getTaskById(1);
        Epic epic = manager.getEpicById(2);
        SubTask subTask = manager.getSubTaskById(3);

        assertNotNull(task, "ожидалась задача с id 1");
        assertEquals("Task1", task.getName(), "имя задачи не совпадает");

        assertNotNull(epic, "ожидался эпик с id 2");
        assertEquals("Epic1", epic.getName(), "имя эпика не совпадает");

        assertNotNull(subTask, "ожидалась подзадача с id 3");
        assertEquals(2, subTask.getEpicId(), "подзадача должна ссылаться на эпик id 2");

        List<Integer> subIds = epic.getSubTasks();
        assertEquals(1, subIds.size(), "у эпика должен быть один подзадачный id");
        assertEquals(3, subIds.getFirst(), "id подзадачи у эпика должен быть 3");
    }

    @Test
    void deleteAll_shouldClearAllTaskLists() {
        FileBackedTaskManager manager = FileBackedTaskManager.loadFromFile(testFile);

        assertFalse(manager.taskList().isEmpty(), "задачи должны быть загружены");
        assertFalse(manager.epicList().isEmpty(), "эпики должны быть загружены");
        assertFalse(manager.subTaskList().isEmpty(), "подзадачи должны быть загружены");

        manager.deleteTasks();
        manager.deleteEpics();

        assertTrue(manager.taskList().isEmpty(), "все задачи должны быть удалены");
        assertTrue(manager.epicList().isEmpty(), "все эпики должны быть удалены");
        assertTrue(manager.subTaskList().isEmpty(), "все подзадачи должны быть удалены");
    }

    @Test
    void saveAndLoad_shouldPreserveAllDataCorrectly() throws IOException {
        FileBackedTaskManager originalManager = new FileBackedTaskManager(testFile);
        Task task = originalManager.addAndGetNewTask("Task A", "Description A", Status.NEW);
        Epic epic = originalManager.addAndGetNewEpic("Epic B", "Description B", new ArrayList<>());
        SubTask subTask = originalManager.addAndGetNewSubTask("SubTask C", "Description C", Status.IN_PROGRESS, epic.getId());


        originalManager.addNewTask(task);
        originalManager.addNewEpic(epic);
        originalManager.addNewSubTask(subTask);

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(testFile);

        List<Task> tasks = loadedManager.taskList();
        List<Epic> epics = loadedManager.epicList();
        List<SubTask> subTasks = loadedManager.subTaskList();

        assertEquals(1, tasks.size(), "taskList должен содержать одну задачу");
        assertEquals(1, epics.size(), "epicList должен содержать один эпик");
        assertEquals(1, subTasks.size(), "subTaskList должен содержать одну подзадачу");

        Task loadedTask = tasks.getFirst();
        assertEquals(task.getName(), loadedTask.getName(), "имя задачи не совпадает");
        assertEquals(task.getDescription(), loadedTask.getDescription(), "описание задачи не совпадает");
        assertEquals(task.getStatus(), loadedTask.getStatus(), "статус задачи не совпадает");

        Epic loadedEpic = epics.getFirst();
        assertEquals(epic.getName(), loadedEpic.getName(), "имя эпика не совпадает");
        assertEquals(epic.getDescription(), loadedEpic.getDescription(), "описание эпика не совпадает");
        assertEquals(1, loadedEpic.getSubTasks().size(), "у эпика должен быть один сабтаск");

        SubTask loadedSubTask = subTasks.getFirst();
        assertEquals(subTask.getName(), loadedSubTask.getName(), "имя сабтаска не совпадает");
        assertEquals(subTask.getDescription(), loadedSubTask.getDescription(), "описание сабтаска не совпадает");
        assertEquals(subTask.getStatus(), loadedSubTask.getStatus(), "статус сабтаска не совпадает");
        assertEquals(subTask.getEpicId(), loadedSubTask.getEpicId(), "айди эпика в сабтаске не совпадает");
    }


}
