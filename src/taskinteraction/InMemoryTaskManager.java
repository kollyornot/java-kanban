package taskinteraction;

import history.InMemoryHistoryManager;
import interfaces.TaskManager;
import main.Managers;
import taskclasses.Epic;
import taskclasses.SubTask;
import taskclasses.Task;
import utilities.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {

    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();
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

    public void addNewTask(String name, String description, Status status) {
        Task newTask = new Task(name, description, status);
        tasks.put(newTask.getId(), newTask);
    }

    @Override
    public void addNewTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void addNewEpic(String name, String description, ArrayList<Integer> subTasks) {
        Epic epic = createEpicWithCalculatedStatus(name, description, subTasks);
        epics.put(epic.getId(), epic);

    }

    @Override
    public void addNewEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }


    public void addNewSubTask(String name, String description, Status status, int epicId) {
        SubTask newSubTask = new SubTask(name, description, status, epicId);
        subTasks.put(newSubTask.getId(), newSubTask);
    }

    @Override
    public void addNewSubTask(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
    }


    public Task addAndGetNewTask(String name, String description, Status status) {
        Task newTask = new Task(name, description, status);
        tasks.put(newTask.getId(), newTask);
        return newTask;
    }



    public Epic addAndGetNewEpic(String name, String description, ArrayList<Integer> subTasks) {
        Epic epic = createEpicWithCalculatedStatus(name, description, subTasks);
        epics.put(epic.getId(), epic);
        return epic;
    }


    public SubTask addAndGetNewSubTask(String name, String description, Status status, int epicId) {
        SubTask newSubTask = new SubTask(name, description, status, epicId);
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
    public void addSubTask(SubTask subTask, Epic epic) {
        boolean found = false;
        for (Epic epics : epics.values()) {
            if (epic.getId() == epics.getId()) {
                found = true;
                break;
            }
        }
        if (found) {
           addNewSubTask(subTask);
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
        LocalDateTime minStartTime = LocalDateTime.MAX;
        LocalDateTime maxEndTime = LocalDateTime.MIN;
        Duration duration = Duration.ZERO;
        for (Integer subTaskId : subTasks) {
            SubTask subTask = getSubTaskById(subTaskId);
            if(subTask.getDuration() != null) {
                Duration subTaskDuration = subTask.getDuration();
                duration = duration.plus(subTaskDuration);
            }
            if(subTask.getStartTime() != null){
                LocalDateTime subTaskStartTime = subTask.getStartTime();
                if(subTaskStartTime.isBefore(minStartTime)) {
                    minStartTime = subTaskStartTime;
                }
            }
            if(subTask.getEndTime() != null){
                LocalDateTime subTaskEndTime = subTask.getEndTime();
                if(subTaskEndTime.isAfter(maxEndTime)) {
                    maxEndTime = subTaskEndTime;
                }

            }
        }
        if(duration != null && minStartTime != LocalDateTime.MAX && maxEndTime != LocalDateTime.MIN) {
            return new Epic(name, description, status, subTasks, duration, minStartTime, maxEndTime);
        }
        else return new Epic(name, description, status, subTasks);
    }
}


