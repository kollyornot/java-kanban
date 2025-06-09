package taskclasses;

import utilities.Status;
import utilities.TaskTypes;

import java.util.ArrayList;
import java.util.Objects;


public class Task {
    protected String name;
    protected String description;
    protected Status status;
    protected final int id;
    protected TaskTypes taskType = TaskTypes.TASK;

    public Task(String name, String description, Status status, int id) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.id = ++id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public TaskTypes getTaskType() {
        return taskType;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        String id, taskType, name, status, description;
        return "%s,%s,%s,%s,%s,";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(name, task.name) &&
                Objects.equals(description, task.description) && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, status, id);
    }


}
