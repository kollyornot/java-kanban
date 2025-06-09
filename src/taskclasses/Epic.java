package taskclasses;

import utilities.Status;
import utilities.TaskTypes;

import java.util.ArrayList;

public class Epic extends Task {
    private boolean areAllSubTasksCompleted;
    private ArrayList<Integer> subTasks;
    protected TaskTypes taskType = TaskTypes.EPIC;

    public Epic(String name, String description, Status status,
                int id, ArrayList<Integer> subTasks) {
        super(name, description, status, id);
        this.areAllSubTasksCompleted = false;
        this.subTasks = subTasks;
    }

    public Epic(String name, String description, Status status, int id) {
        super(name, description, status, id);
        this.areAllSubTasksCompleted = false;
        this.subTasks = new ArrayList<>();
    }

    public boolean areAllSubTasksCompleted() {
        return areAllSubTasksCompleted;
    }

    public void setAreAllSubTasksCompleted(boolean areAllSubTasksCompleted) {
        this.areAllSubTasksCompleted = areAllSubTasksCompleted;
    }

    public ArrayList<Integer> getSubTasks() {
        return subTasks;
    }

    public TaskTypes getTaskType() {
        return taskType;
    }

    public void addSubTask(int id){
        this.subTasks.add(id);
    }

    public void setSubTasks(ArrayList<Integer> subTasks) {
        this.subTasks = subTasks;
    }

}
