import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Epic> epics;
    private HashMap<Integer, SubTask> subTasks;
    Counter counter = new Counter();
    //я не поняла, о чем вы, и сделала так

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
        Task newTask = new Task(name, description, status, counter.getId());
        tasks.put(newTask.getId(), newTask);
        return newTask;
    }

    public Epic addNewEpic(String name, String description, ArrayList<Integer> subTasks) {
        boolean areAllSubTasksDone = checkAllSubTasksSameStatus(subTasks, Status.DONE);
        boolean areAllSubTasksNew = checkAllSubTasksSameStatus(subTasks, Status.NEW);
        if (subTasks.isEmpty() || areAllSubTasksNew) {
            Epic newEpic = new Epic(name, description, Status.NEW, counter.getId(), subTasks);
        } else if (areAllSubTasksDone) {
            Epic newEpic = new Epic(name, description, Status.DONE, counter.getId(), subTasks);
        }
        Epic newEpic = new Epic(name, description, Status.IN_PROGRESS, counter.getId(), subTasks);
        epics.put(newEpic.getId(), newEpic);
        return newEpic;
    }

    public SubTask addNewSubTask(String name, String description, Status status, int epicId) {
        SubTask newSubTask = new SubTask(name, description, status, counter.getId(), epicId);
        subTasks.put(newSubTask.getId(), newSubTask);
        return newSubTask;
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
        checkAllSubTasksSameStatusById(subTask.getEpicId(), Status.DONE);
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

    public boolean checkAllSubTasksSameStatusById(int id, Status status) {
        boolean allSubTasksSameStatus = false;
        for (Integer subTaskid : getEpicById(id).getSubTasks()) {
            if (getSubTaskById(subTaskid).getStatus().equals(status))
                allSubTasksSameStatus = true;
            else {
                allSubTasksSameStatus = false;
                break;
            }
        }
        if(status.equals(Status.DONE) && allSubTasksSameStatus){
            getEpicById(id).setAreAllSubTasksCompleted(true);
            getEpicById(id).setStatus(Status.DONE);}
        return allSubTasksSameStatus;
    }

    public boolean checkAllSubTasksSameStatus(ArrayList<Integer> subtasksIds, Status status) {
        boolean allSubTasksSameStatus = false;
        for (Integer subTaskid : subtasksIds) {
            if (getSubTaskById(subTaskid).getStatus().equals(status))
                allSubTasksSameStatus = true;
            else {
                allSubTasksSameStatus = false;
                break;
            }
        }
        return allSubTasksSameStatus;
    }

    public void addSubTask(SubTask subTask, Epic epic) {
        epic.getSubTasks().add(subTask.getId());
        subTask.setEpicId(epic.getId());
        checkAllSubTasksSameStatusById(epic.getId(), Status.DONE);
    }

    public void RemoveAllSubTask(Epic epic) {
        epic.getSubTasks().clear();
    }
}