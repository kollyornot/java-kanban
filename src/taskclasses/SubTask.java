package taskclasses;

import utilities.Status;
import utilities.TaskTypes;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class SubTask extends Task {
    private final int epicId;
    protected TaskTypes taskType = TaskTypes.SUBTASK;

    public SubTask(String name, String description, Status status, int epicId, Duration duration, LocalDateTime startTime) {
        super(name, description, status, duration, startTime);
        this.epicId = epicId;
    }

    public SubTask(String name, String description, Status status, int id, int epicId, Duration duration, LocalDateTime startTime) {
        super(name, description, status, id, duration, startTime);
        this.epicId = epicId;
    }

    public SubTask(String name, String description, Status status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public SubTask(String name, String description, Status status, int id, int epicId) {
        super(name, description, status, id);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public TaskTypes getTaskType() {
        return taskType;
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

        return String.format("%d,%s,%s,%s,%s,%s,%d,%d",
                getId(), getTaskType(), getName(), getStatus(), getDescription(), startTime, duration, getEpicId());
    }

}
