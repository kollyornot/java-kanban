package taskinteraction;

import exceptions.PrioritizingException;
import history.InMemoryHistoryManager;
import interfaces.TaskManager;
import main.Managers;
import taskclasses.Epic;
import taskclasses.SubTask;
import taskclasses.Task;
import utilities.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private final InMemoryHistoryManager inMemoryHistoryManager = (InMemoryHistoryManager) Managers.getDefaultHistory();
    private final TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    @Override
    public ArrayList<Task> prioritizedTasksList() {
        return new ArrayList<>(prioritizedTasks);
    }

    private boolean hasTimeIntersection(Task task) {
        if (task.getStartTime() != null && task.getEndTime() != null) return prioritizedTasks.stream()
                .anyMatch(current -> isTimeOverlapping(current, task));
        else return false;
    }


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
        epics.values()
                .forEach(this::removeAllSubTask);
        epics.clear();
        subTasks.clear();
    }


    @Override
    public void deleteSubTasks() {
        epics.values()
                .forEach(this::removeAllSubTask);
        subTasks.clear();
    }

    @Override
    public Task getTaskById(int id) {
        return tasks.values().stream()
                .filter(task -> task.getId() == id)
                .findFirst()
                .map(task -> {
                    inMemoryHistoryManager.addTaskToHistory(task);
                    return task;
                })
                .orElse(null);
    }


    @Override
    public Epic getEpicById(int id) {
        return epics.values().stream()
                .filter(epic -> epic.getId() == id)
                .findFirst()
                .map(epic -> {
                    inMemoryHistoryManager.addTaskToHistory(epic);
                    return epic;
                })
                .orElse(null);
    }

    @Override
    public SubTask getSubTaskById(int id) {
        return subTasks.values().stream()
                .filter(subTask -> subTask.getId() == id)
                .findFirst()
                .map(subTask -> {
                    inMemoryHistoryManager.addTaskToHistory(subTask);
                    return subTask;
                })
                .orElse(null);
    }

    @Override
    public ArrayList<Integer> getSubTaskListByEpic(int epicId) {
        Epic epic = getEpicById(epicId);
        return epic.getSubTasks();
    }

    @Override
    public void addNewTask(Task task) {
        if (hasTimeIntersection(task)) {
            throw new PrioritizingException("Пересечение во времени");
        } else {
            tasks.put(task.getId(), task);
            if (task.getStartTime() != null && task.getDuration() != null) {
                prioritizedTasks.add(task);
            }
        }
    }

    @Override
    public void addNewEpic(Epic epic) {
        if (hasTimeIntersection(epic)) {
            throw new PrioritizingException("Пересечение во времени");
        } else {
            boolean areAllSubTasksDone = checkAllSubTasksSameStatus(epic.getSubTasks(), Status.DONE);
            boolean areAllSubTasksNew = checkAllSubTasksSameStatus(epic.getSubTasks(), Status.NEW);


            Status status;
            if (subTasks.isEmpty() || areAllSubTasksNew) {
                status = Status.NEW;
            } else if (areAllSubTasksDone) {
                status = Status.DONE;
            } else {
                status = Status.IN_PROGRESS;
            }
            epics.put(epic.getId(), epic);
            if (epic.getStartTime() != null && epic.getDuration() != null) {
                prioritizedTasks.add(epic);
            }
            epic.setStartTime(getStartTimeForEpic(epic));
            epic.setDuration(getDurationForEpic(epic));
            epic.setEndTime(getEndTimeForEpic(epic));
            epic.setStatus(status);
        }
    }

    @Override
    public void addNewSubTask(SubTask subTask) {
        if (hasTimeIntersection(subTask)) {
            throw new PrioritizingException("Пересечение во времени");
        } else {
            subTasks.put(subTask.getId(), subTask);
            if (subTask.getStartTime() != null && subTask.getDuration() != null) {
                prioritizedTasks.add(subTask);
            }
        }
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
        boolean allSubTasksSameStatus = getEpicById(id).getSubTasks()
                .stream()
                .map(this::getSubTaskById)
                .allMatch(subTask -> subTask.getStatus().equals(status));
        if (status.equals(Status.DONE) && allSubTasksSameStatus) {
            getEpicById(id).setAreAllSubTasksCompleted(true);
            getEpicById(id).setStatus(Status.DONE);
        }
    }

    public boolean checkAllSubTasksSameStatus(ArrayList<Integer> subtasksIds, Status status) {

        return subtasksIds.stream()
                .map(this::getSubTaskById)
                .allMatch(subTask -> subTask.getStatus().equals(status));

    }

    @Override
    public void addSubTask(SubTask subTask, Epic epic) {
        boolean found = epics.values().stream()
                .anyMatch(epicsItem -> epicsItem.getId() == epic.getId());

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

    public boolean isTimeOverlapping(Task t1, Task t2) {
        if (t1.getStartTime() == null || t1.getEndTime() == null ||
                t2.getStartTime() == null || t2.getEndTime() == null) {
            return true;
        }
        return t2.getStartTime().isBefore(t1.getStartTime()) && t1.getEndTime().isBefore(t2.getEndTime()) ||
                t1.getEndTime().isAfter(t2.getStartTime()) && t2.getEndTime().isAfter(t1.getEndTime()) ||
                t1.getEndTime().equals(t2.getEndTime()) && t1.getStartTime().equals(t2.getStartTime()) ||
                t2.getEndTime().isAfter(t1.getStartTime()) && t1.getEndTime().isAfter(t2.getEndTime()) ||
                t1.getStartTime().isBefore(t2.getStartTime()) && t2.getEndTime().isBefore(t1.getEndTime());
    }

    public Duration getDurationForEpic(Epic epic) {
        return epic.getSubTasks().stream()
                .map(this::getSubTaskById)
                .map(SubTask::getDuration)
                .filter(Objects::nonNull)
                .reduce(Duration.ZERO, Duration::plus);
    }

    public LocalDateTime getStartTimeForEpic(Epic epic) {
        return epic.getSubTasks().stream()
                .map(this::getSubTaskById)
                .map(SubTask::getStartTime)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo)
                .orElse(null);
    }

    public LocalDateTime getEndTimeForEpic(Epic epic) {

        return epic.getSubTasks().stream()
                .map(this::getSubTaskById)
                .map(SubTask::getEndTime)
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo)
                .orElse(null);
    }

}


