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
        assertEquals(2, subTask.getEpicId(),"айди эпика не соответствует");

        // Проверка связи между подзадачей и эпиком
        assertEquals(1, epic.getSubTasks().size(), "количество подзаданий у эпика не соответствует");
        assertEquals(3, epic.getSubTasks().getFirst(), "неверный айди подзадачи у эпика");
    }
}
