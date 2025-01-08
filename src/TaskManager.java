import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Epic> epics;
    private HashMap<Integer, SubTask> subTasks;
    int counter = this.hashCode();

    public ArrayList<Task> taskList() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> epicList() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<SubTask> subTaskList() {
        return new ArrayList<>(subTasks.values());
    }

    public void DeleteTasks() {
        tasks.clear();
    }

    public void DeleteEpics() {
        for (Epic epic : epics.values()) {
            RemoveAllSubTask(epic);
        }
        epics.clear();
    }

    public void DeleteSubTasks() {
        for (Epic epic : epics.values()) {
            RemoveAllSubTask(epic);
        }
        subTasks.clear();
    }

    public Task getTaskById(int id) {
        for (Task task : tasks.values()) {
            if (task.getId() == id) {
                return task;
            }
        }
        return null;
    }

    public Epic getEpicById(int id) {
        for (Epic epic : epics.values()) {
            if (epic.getId() == id) {
                return epic;
            }
        }
        return null;
    }

    public SubTask getSubTaskById(int id) {
        for (SubTask subTask : subTasks.values()) {
            if (subTask.getId() == id) {
                return subTask;
            }
        }
        return null;
    }

    public ArrayList<Integer> getSubTaskListByEpic(int epicId) {
        Epic epic = getEpicById(epicId);
        return epic.getSubTasks();
    }

    public Task addNewTask(String name, String description, Status status) {
        Task newTask = new Task(name, description, status, ++counter);
        tasks.put(newTask.getId(), newTask);
        return newTask;
    }

    public Epic addNewEpic(String name, String description, ArrayList<Integer> subTasks) {
        boolean areAllSubTasksDone = checkAllSubTasksCompleted(subTasks);
        boolean areAllSubTasksNew = checkAllSubTasksNew(subTasks);
        if (subTasks.isEmpty() || areAllSubTasksNew) {
            Epic newEpic = new Epic(name, description, Status.NEW, ++counter, subTasks);
        } else if (areAllSubTasksDone) {
            Epic newEpic = new Epic(name, description, Status.DONE, ++counter, subTasks);
        }
        Epic newEpic = new Epic(name, description, Status.IN_PROGRESS, ++counter, subTasks);
        epics.put(newEpic.getId(), newEpic);
        return newEpic;
    }

    public SubTask addNewSubTask(String name, String description, Status status, int epicId) {
        SubTask newSubTask = new SubTask(name, description, status, ++counter, epicId);
        subTasks.put(newSubTask.getId(), newSubTask);
        return newSubTask;
    }

    public boolean checkAllSubTasksNew(ArrayList<Integer> subTasks) {
        boolean allSubTasksNew = false;
        for (Integer subTaskid : subTasks) {
            if (getSubTaskById(subTaskid).getStatus().equals(Status.NEW))
                allSubTasksNew = true;
            else {
                allSubTasksNew = false;
                break;
            }
        }
        return allSubTasksNew;
    }

    public boolean checkAllSubTasksCompleted(ArrayList<Integer> subTasks) {
        boolean allSubTasksCompleted = false;
        for (Integer subTaskid : subTasks) {
            if (getSubTaskById(subTaskid).getStatus().equals(Status.DONE))
                allSubTasksCompleted = true;
            else {
                allSubTasksCompleted = false;
                break;
            }
        }
        return allSubTasksCompleted;
    }

    public Task updateTask(Task task, Status status) {
        task.setStatus(status);
        return task;
    }

    public Epic updateTask(Epic epic, Status status) {
        epic.setStatus(status);
        if (status.equals(Status.DONE)) {
            for (Integer subTaskid : epic.getSubTasks()) {
                getSubTaskById(subTaskid).setStatus(Status.DONE);
            }
        }
        return epic;
    }

    public SubTask updateTask(SubTask subTask, Status status) {
        subTask.setStatus(status);
        checkAllSubTasksCompletedByEpicId(subTask.getEpicId());
        return subTask;
    }

    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    public void deleteEpicById(int id) {
        RemoveAllSubTask(getEpicById(id));
        epics.remove(id);
    }

    public void deleteSubTaskById(int id) {
        subTasks.remove(id);
    }

    public void checkAllSubTasksCompletedByEpicId(int id) {
        boolean allSubTasksCompleted = false;
        for (Integer subTaskid : getEpicById(id).getSubTasks()) {
            if (getSubTaskById(subTaskid).getStatus().equals(Status.DONE))
                allSubTasksCompleted = true;
            else {
                allSubTasksCompleted = false;
                break;
            }
        }
        getEpicById(id).setAreAllSubTasksCompleted(allSubTasksCompleted);
        if (allSubTasksCompleted)
            getEpicById(id).setStatus(Status.DONE);
    }

    public void AddSubTask(SubTask subTask, Epic epic) {
        epic.getSubTasks().add(subTask.getId());
        subTask.setEpicId(epic.getId());
        checkAllSubTasksCompletedByEpicId(epic.getId());
    }

    public void RemoveAllSubTask(Epic epic) {
        epic.getSubTasks().clear();
    }
}