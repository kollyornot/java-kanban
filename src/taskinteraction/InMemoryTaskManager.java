package taskinteraction;

import history.InMemoryHistoryManager;
import interfaces.TaskManager;
import main.Managers;
import taskclasses.Epic;
import taskclasses.SubTask;
import taskclasses.Task;
import utilities.Counter;
import utilities.Status;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {

    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private final Counter counter = new Counter();
    private final InMemoryHistoryManager inMemoryHistoryManager = (InMemoryHistoryManager) Managers.getDefaultHistory();

    @Override
    public ArrayList<Task> taskList() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> epicList() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<SubTask> subTaskList() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public void deleteTasks() {
        tasks.clear();
    }

    @Override
    public void deleteEpics() {
        for (Epic epic : epics.values()) {
            removeAllSubTask(epic);
        }
        epics.clear();
        subTasks.clear();
    }

    @Override
    public void deleteSubTasks() {
        for (Epic epic : epics.values()) {
            removeAllSubTask(epic);
        }
        subTasks.clear();
    }

    @Override
    public Task getTaskById(int id) {
        for (Task task : tasks.values()) {
            if (task.getId() == id) {
                inMemoryHistoryManager.addTaskToHistory(task);
                return task;
            }
        }
        return null;
    }

    @Override
    public Epic getEpicById(int id) {
        for (Epic epic : epics.values()) {
            if (epic.getId() == id) {
                inMemoryHistoryManager.addTaskToHistory(epic);
                return epic;
            }
        }
        return null;
    }

    @Override
    public SubTask getSubTaskById(int id) {
        for (SubTask subTask : subTasks.values()) {
            if (subTask.getId() == id) {
                inMemoryHistoryManager.addTaskToHistory(subTask);
                return subTask;
            }
        }
        return null;
    }

    @Override
    public ArrayList<Integer> getSubTaskListByEpic(int epicId) {
        Epic epic = getEpicById(epicId);
        return epic.getSubTasks();
    }

    @Override
    public void addNewTask(String name, String description, Status status) {
        Task newTask = new Task(name, description, status, counter.getId());
        tasks.put(newTask.getId(), newTask);
    }

    public void addNewTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void addNewEpic(String name, String description, ArrayList<Integer> subTasks) {
        Epic epic = createEpicWithCalculatedStatus(name, description, subTasks);
        epics.put(epic.getId(), epic);

    }

    public void addNewEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    public void addEpicDirect(Epic epic) {
        epics.put(epic.getId(), epic);

    }


    @Override
    public void addNewSubTask(String name, String description, Status status, int epicId) {
        SubTask newSubTask = new SubTask(name, description, status, counter.getId(), epicId);
        subTasks.put(newSubTask.getId(), newSubTask);
    }

    public void addNewSubTask(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
    }

    @Override
    public Task addAndGetNewTask(String name, String description, Status status) {
        Task newTask = new Task(name, description, status, counter.getId());
        tasks.put(newTask.getId(), newTask);
        return newTask;
    }


    @Override
    public Epic addAndGetNewEpic(String name, String description, ArrayList<Integer> subTasks) {
        Epic epic = createEpicWithCalculatedStatus(name, description, subTasks);
        epics.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public SubTask addAndGetNewSubTask(String name, String description, Status status, int epicId) {
        SubTask newSubTask = new SubTask(name, description, status, counter.getId(), epicId);
        subTasks.put(newSubTask.getId(), newSubTask);
        return newSubTask;
    }

    @Override
    public void updateTask(Task task, Status status) {
        task.setStatus(status);
    }

    @Override
    public void updateTask(Epic epic, Status status) {
        epic.setStatus(status);
        if (status.equals(Status.DONE)) {
            for (Integer subTaskId : epic.getSubTasks()) {
                getSubTaskById(subTaskId).setStatus(Status.DONE);
            }
        }
    }

    @Override
    public void updateTask(SubTask subTask, Status status) {
        subTask.setStatus(status);
        checkAllSubTasksSameStatus(subTask.getEpicId(), Status.DONE);
    }

    @Override
    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    @Override
    public void deleteEpicById(int id) {
        removeAllSubTask(getEpicById(id));
        epics.remove(id);
    }

    @Override
    public void deleteSubTaskById(int id) {
        subTasks.remove(id);
    }

    public void checkAllSubTasksSameStatus(int id, Status status) {
        boolean allSubTasksSameStatus = false;
        for (Integer subTaskId : getEpicById(id).getSubTasks()) {
            if (getSubTaskById(subTaskId).getStatus().equals(status))
                allSubTasksSameStatus = true;
            else {
                allSubTasksSameStatus = false;
                break;
            }
        }
        if (status.equals(Status.DONE) && allSubTasksSameStatus) {
            getEpicById(id).setAreAllSubTasksCompleted(true);
            getEpicById(id).setStatus(Status.DONE);
        }
    }

    public boolean checkAllSubTasksSameStatus(ArrayList<Integer> subtasksIds, Status status) {
        boolean allSubTasksSameStatus = false;
        for (Integer subTaskId : subtasksIds) {
            if (getSubTaskById(subTaskId).getStatus().equals(status))
                allSubTasksSameStatus = true;
            else {
                allSubTasksSameStatus = false;
                break;
            }
        }
        return allSubTasksSameStatus;
    }

    @Override
    public void addSubTask(String name, String description, Status status, Epic epic) {
        boolean found = false;
        for (Epic epics : epics.values()) {
            if (epic.getId() == epics.getId()) {
                found = true;
                break;
            }
        }
        if (found) {
            SubTask subTask = addAndGetNewSubTask(name, description, status, epic.getId());
            epic.getSubTasks().add(subTask.getId());
            checkAllSubTasksSameStatus(epic.getId(), Status.DONE);
        }
    }

    @Override
    public void removeAllSubTask(Epic epic) {
        epic.getSubTasks().clear();
    }

    private Epic createEpicWithCalculatedStatus(String name, String description, ArrayList<Integer> subTasks) {
        Status status;
        boolean areAllSubTasksDone = checkAllSubTasksSameStatus(subTasks, Status.DONE);
        boolean areAllSubTasksNew = checkAllSubTasksSameStatus(subTasks, Status.NEW);

        if (subTasks.isEmpty() || areAllSubTasksNew) {
            status = Status.NEW;
        } else if (areAllSubTasksDone) {
            status = Status.DONE;
        } else {
            status = Status.IN_PROGRESS;
        }

        return new Epic(name, description, status, counter.getId(), subTasks);
    }
}


