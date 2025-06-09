package taskinteraction;

import exceptions.ManagerSaveException;
import interfaces.TaskManager;
import taskclasses.Epic;
import taskclasses.SubTask;
import taskclasses.Task;
import utilities.Status;
import utilities.TaskTypes;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    private File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
        if (!file.exists() || file.length() == 0) {
            try (FileWriter fw = new FileWriter(file, StandardCharsets.UTF_8)) {
                fw.write("id,type,name,status,description,epic\n");
            } catch (IOException e) {
                throw new ManagerSaveException("ошибка при прочтении файла");
            }
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);

        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            br.readLine(); // пропускаем заголовок
            String line;
            while ((line = br.readLine()) != null && !line.isBlank()) {
                lines.add(line);
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при загрузке данных с файла");
        }

        // Сначала загружаем TASK и EPIC
        for (String line : lines) {
            Task task = fromString(line);
            switch (task.getTaskType()) {
                case TASK -> manager.addNewTask(task);
                case EPIC -> manager.addNewEpic((Epic) task);
            }
        }

        // Потом SUBTASK
        for (String line : lines) {
            Task task = fromString(line);
            if (task.getTaskType() == TaskTypes.SUBTASK) {
                manager.addNewSubTask((SubTask) task);
            }
        }

        return manager;
    }


    @Override
    public void addNewTask(String name, String description, Status status) {
        super.addNewTask(name, description, status);
        save();
    }

    @Override
    public void addNewEpic(String name, String description, ArrayList<Integer> subTasks) {
        super.addNewEpic(name, description, subTasks);
        save();
    }

    @Override
    public void addNewSubTask(String name, String description, Status status, int epicId) {
        super.addNewSubTask(name, description, status, epicId);
        save();
    }

    @Override
    public void addNewTask(Task task) {
        super.addNewTask(task);
        save();
    }

    @Override
    public void addNewEpic(Epic epic) {
        System.out.println(">>> addNewEpic: id=" + epic.getId() + ", с subTasks=" + epic.getSubTasks());
        super.addNewEpic(epic);
        save();
    }


    @Override
    public void addNewSubTask(SubTask subTask) {
        super.addNewSubTask(subTask);
        Epic epic = getEpicById(subTask.getEpicId());
        if (epic == null) {
            throw new ManagerSaveException("Эпик с ID " + subTask.getEpicId() + " не найден при добавлении подзадачи.");
        }
        System.out.println(">>> addNewSubTask: epicId=" + subTask.getEpicId() + ", epic instance=" + epic);
        System.out.println(">>> subTasks до: " + epic.getSubTasks());
        epic.addSubTask(subTask.getId());
        System.out.println(">>> subTasks после: " + epic.getSubTasks());
        save();
    }


    @Override
    public void updateTask(Task task, Status status) {
        super.updateTask(task, status);
        save();
    }

    @Override
    public void updateTask(Epic epic, Status status) {
        super.updateTask(epic, status);
        save();
    }

    @Override
    public void updateTask(SubTask subTask, Status status) {
        super.updateTask(subTask, status);
        save();
    }

    @Override
    public void deleteTasks() {
        super.deleteTasks();
        save();
    }

    @Override
    public void deleteEpics() {
        super.deleteEpics();
        save();
    }

    @Override
    public void deleteSubTasks() {
        super.deleteSubTasks();
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubTaskById(int id) {
        super.deleteSubTaskById(id);
        save();
    }


    @Override
    public void addSubTask(String name, String description, Status status, Epic epic) {
        super.addSubTask(name, description, status, epic);
        save();
    }

    @Override
    public void removeAllSubTask(Epic epic) {
        super.removeAllSubTask(epic);
        save();
    }

    @Override
    public SubTask addAndGetNewSubTask(String name, String description, Status status, int epicId) {
        SubTask newSubTask = super.addAndGetNewSubTask(name, description, status, epicId);
        save();
        return newSubTask;
    }

    @Override
    public Task addAndGetNewTask(String name, String description, Status status) {
        Task newTask = super.addAndGetNewTask(name, description, status);
        save();
        return newTask;
    }


    @Override
    public Epic addAndGetNewEpic(String name, String description, ArrayList<Integer> subTasks) {
        Epic epic = super.addAndGetNewEpic(name, description, subTasks);
        save();
        return epic;
    }

    public static Task fromString(String str) {
        String[] split = str.split(",");
        int id = Integer.parseInt(split[0]);
        TaskTypes type = TaskTypes.valueOf(split[1]);
        String name = split[2];
        Status status = Status.valueOf(split[3]);
        String description = split[4];

        switch (type) {
            case TASK:
                return new Task(name, description, status, id);
            case EPIC:
                return new Epic(name, description, status, id);
            case SUBTASK:
                int epicId = Integer.parseInt(split[5]);
                return new SubTask(name, description, status, id, epicId);
            default:
                throw new ManagerSaveException("Неизвестный тип задачи: " + type);
        }
    }


    private void save() {
        try (FileWriter fw = new FileWriter(file, StandardCharsets.UTF_8)) {
            fw.write("id,type,name,status,description,epic\n");
            for (Task task : super.taskList()) {
                fw.write(task.toString() + "\n");
            }
            for (Epic epic : super.epicList()) {
                fw.write(epic.toString() + "\n");
            }
            for (SubTask subtask : super.subTaskList()) {
                fw.write(subtask.toString() + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении файла");
        }

    }

}
