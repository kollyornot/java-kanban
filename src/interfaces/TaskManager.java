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

    void addNewTask(String name, String description, Status status);

    void addNewEpic(String name, String description, ArrayList<Integer> subTasks);

    void addNewSubTask(String name, String description, Status status, int epicId);

    void updateTask(Task task, Status status);

    void updateTask(Epic epic, Status status);

    void updateTask(SubTask subTask, Status status);

    void deleteTaskById(int id);

    void deleteEpicById(int id);

    void deleteSubTaskById(int id);

    void addSubTask(String name, String description, Status status, Epic epic);

    void removeAllSubTask(Epic epic);

    Task addAndGetNewTask(String testAddNewTask, String testAddNewTaskDescription, Status status);

    Epic addAndGetNewEpic(String name, String description, ArrayList<Integer> subTasks);

    SubTask addAndGetNewSubTask(String testAddNewSubTask, String testAddNewSubTaskDescription, Status status, int i);
}
