package taskClasses;

import utilities.Status;

import java.util.ArrayList;

public class Epic extends Task {
    private boolean areAllSubTasksCompleted;
    private ArrayList<Integer> subTasks;

    public Epic(String name, String description, Status status,
                int id, ArrayList<Integer> subTasks) {
        super(name, description, status, id);
        this.areAllSubTasksCompleted = false;
        this.subTasks = subTasks;
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

    public void setSubTasks(ArrayList<Integer> subTasks) {
        this.subTasks = subTasks;
    }

    @Override
    public String toString() {
        return "taskClasses.Epic{" +
                ", name='" + name + '\'' +
                ", id=" + id +
                ", description='" + description + '\'' +
                ", subTasks=" + subTasks +
                "areAllSubTasksCompleted=" + areAllSubTasksCompleted +
                ", status=" + status +
                '}';
    }
}
