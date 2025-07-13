package interfaces;

import taskclasses.Epic;
import taskclasses.SubTask;
import taskclasses.Task;
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

    void addNewTask(Task task);

    void addNewEpic(Epic epic);

    void addNewSubTask(SubTask subTask);

    void updateTask(Task task, Status status);

    void updateTask(Epic epic, Status status);

    void updateTask(SubTask subTask, Status status);

    void deleteTaskById(int id);

    void deleteEpicById(int id);

    void deleteSubTaskById(int id);

    void addSubTask(SubTask subTask, Epic epic);

    void removeAllSubTask(Epic epic);


}
