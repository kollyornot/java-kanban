package interfaces;

import task_classes.Epic;
import task_classes.SubTask;
import task_classes.Task;
import utilities.Status;

import java.util.ArrayList;

public interface TaskManager {
    ArrayList<Task> taskList();

    ArrayList<Epic> epicList();

    ArrayList<SubTask> subTaskList();

    void deleteTasks();

    void deleteEpics();

    void deleteSubTasks();

    Task getTaskById(int id);

    Epic getEpicById(int id);

    SubTask getSubTaskById(int id);

    ArrayList<Integer> getSubTaskListByEpic(int epicId);

    Task addNewTask(String name, String description, Status status);

    Epic addNewEpic(String name, String description, ArrayList<Integer> subTasks);

    SubTask addNewSubTask(String name, String description, Status status, int epicId);

    Task updateTask(Task task, Status status);

    Epic updateTask(Epic epic, Status status);

    SubTask updateTask(SubTask subTask, Status status);

    void deleteTaskById(int id);

    void deleteEpicById(int id);

    void deleteSubTaskById(int id);

    void addSubTask(String name, String description, Status status, Epic epic);

    void removeAllSubTask(Epic epic);
}
