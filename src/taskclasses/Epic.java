package taskclasses;

import utilities.Status;
import utilities.TaskTypes;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    private boolean areAllSubTasksCompleted;
    private ArrayList<Integer> subTasks = new ArrayList<>();
    protected TaskTypes taskType = TaskTypes.EPIC;
    protected LocalDateTime endTime = null;

    public Epic(String name, String description, Status status,
                 ArrayList<Integer> subTaskIds, Duration duration, LocalDateTime startTime,  LocalDateTime endTime) {
        super(name, description, status, duration, startTime);
        this.areAllSubTasksCompleted = false;
        this.subTasks = subTaskIds;
        this.endTime = endTime;
    }

    public Epic(String name, String description, Status status,
                 ArrayList<Integer> subTaskIds) {
        super(name, description, status);
        this.areAllSubTasksCompleted = false;
        this.subTasks = subTaskIds;
    }

    public Epic(String name, String description, Status status) {
        super(name, description, status);
        this.areAllSubTasksCompleted = false;
        this.subTasks = new ArrayList<>();
    }


    public Epic(String name, String description, Status status, int id, Duration duration, LocalDateTime startTime) {
        super(name, description, status, id, duration, startTime);
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

    public void addSubTask(int id) {
        this.subTasks.add(id);
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
    public LocalDateTime getEndTime() {
        return endTime;
    }

}
