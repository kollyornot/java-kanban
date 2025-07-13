package taskclasses;

import utilities.Status;
import utilities.TaskTypes;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import utilities.Counter;


public class Task {
    protected String name;
    protected String description;
    protected Status status;
    protected final int id;
    protected TaskTypes taskType = TaskTypes.TASK;
    protected Duration duration = null;
    protected LocalDateTime startTime = null;
    Counter counter = new Counter();

    public Task(String name, String description, Status status, Duration duration, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.id = counter.getId();
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(String name, String description, Status status, int id, Duration duration, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.id = id;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.id = counter.getId();
    }
    public Task(String name, String description, Status status, int id) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.id = id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getEndTime(){

        if(startTime == null || duration == null) {
            return null;
        }
        else return  startTime.plus(duration);
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        String startTime;
        long duration;
        if(getStartTime() == null) {
            startTime = "null";
        }
        else startTime = getStartTime().format(formatter);
        if(getDuration() == null) {
            duration = 0;
        }
        else duration = getDuration().toMinutes();

        return String.format("%d,%s,%s,%s,%s,%s,%d",
                getId(), getTaskType(), getName(), getStatus(), getDescription(), startTime, duration);
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
