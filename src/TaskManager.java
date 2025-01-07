import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Epic> epics;
    private HashMap<Integer, SubTask> subTasks;
    static int counter = 0;

    public ArrayList<String> taskList(){
        ArrayList<String> taskList = new ArrayList<>();
        for(Task task : tasks.values()){
            taskList.add(task.getDescription());
        }
        return taskList;
    }
    public ArrayList<String> epicList(){
        ArrayList<String> epicList = new ArrayList<>();
        for(Epic epic : epics.values()){
            epicList.add(epic.getDescription());
        }
        return epicList;
    }
    public ArrayList<String> subTaskList(){
        ArrayList<String> subTaskList = new ArrayList<>();
        for(SubTask subTask : subTasks.values()){
            subTaskList.add(subTask.getDescription());
        }
        return subTaskList;
    }

    public void DeleteTasks(){
        tasks.clear();
    }
    public void DeleteEpics(){
        for(Epic epic : epics.values()){
            epic.RemoveAllSubTask();
        }
        epics.clear();
    }
    public void DeleteSubTasks(){
        subTasks.clear();
    }

    public Task getTaskById(int id){
        for(Task task : tasks.values()){
            if(task.getId() == id){
                return task;
            }
        }
        return null;
    }
    public Epic getEpicById(int id){
        for(Epic epic : epics.values()){
            if(epic.getId() == id){
                return epic;
            }
        }
        return null;
    }
    public SubTask getSubTaskById(int id){
        for(SubTask subTask : subTasks.values()){
            if(subTask.getId() == id){
                return subTask;
            }
        }
        return null;
    }

    public ArrayList<SubTask> getSubTaskListByEpic(int epicId){
        Epic epic = getEpicById(epicId);
        return epic.getSubTasks();
    }

    public Task addNewTask(String name, String description, Status status){
        Task newTask = new Task(name, description, status, ++counter);
        tasks.put(newTask.getId(), newTask);
        return newTask;
    }
    public Epic addNewEpic(String name, String description, ArrayList<SubTask> subTasks){
        boolean areAllSubTasksDone = checkAllSubTasksCompleted(subTasks);
        boolean areAllSubTasksNew = checkAllSubTasksNew(subTasks);
        if(subTasks.isEmpty() || areAllSubTasksNew){
            Epic newEpic = new Epic(name, description, Status.NEW, ++counter, subTasks);
        }
        else if(areAllSubTasksDone){
            Epic newEpic = new Epic(name, description, Status.DONE, ++counter, subTasks);
        }
        Epic newEpic = new Epic(name, description, Status.IN_PROGRESS, ++counter, subTasks);
        epics.put(newEpic.getId(), newEpic);
        return newEpic;
    }
    public SubTask addNewSubTask(String name, String description, Status status, int epicId){
        SubTask newSubTask = new SubTask(name, description, status, ++counter, epicId);
        subTasks.put(newSubTask.getId(), newSubTask);
        return newSubTask;
    }
    public boolean checkAllSubTasksNew(ArrayList<SubTask> subTasks){
        boolean allSubTasksNew = false;
        for(SubTask subTask : subTasks){
            if(subTask.getStatus().equals(Status.NEW))
                allSubTasksNew = true;
            else {
                allSubTasksNew = false;
                break;
            }
        }
        return allSubTasksNew;
    }

    public boolean checkAllSubTasksCompleted(ArrayList<SubTask> subTasks){
        boolean allSubTasksCompleted = false;
        for(SubTask subTask : subTasks){
            if(subTask.getStatus().equals(Status.DONE))
                allSubTasksCompleted = true;
            else {
                allSubTasksCompleted = false;
                break;
            }
        }
        return allSubTasksCompleted;
    }

    public Task updateTask(Task task, Status status){
        task.setStatus(status);
        return task;
    }
    public Epic updateTask(Epic epic, Status status){
        epic.setStatus(status);
        if (status.equals(Status.DONE)) {
            for(SubTask subTask : epic.getSubTasks()){
                subTask.setStatus(Status.DONE);
            }
        }
        return epic;
    }
    public SubTask updateTask(SubTask subTask, Status status){
        subTask.setStatus(status);
        return subTask;
    }

    public void deleteTaskById(int id){
        tasks.remove(id);
    }
    public void deleteEpicById(int id){
        getEpicById(id).RemoveAllSubTask();
        epics.remove(id);
    }
    public void deleteSubTaskById(int id){
        subTasks.remove(id);
    }
}