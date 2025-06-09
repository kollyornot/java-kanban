package taskclasses;

import utilities.Status;
import utilities.TaskTypes;


public class SubTask extends Task {
    private final int epicId;
    protected TaskTypes taskType = TaskTypes.SUBTASK;

    public SubTask(String name, String description, Status status, int id, int epicId) {
        super(name, description, status, id);
        this.epicId = epicId;
        this.taskType = TaskTypes.SUBTASK;
    }

    public int getEpicId() {
        return epicId;
    }

    public TaskTypes getTaskType() {
        return taskType;
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s,%d",
                getId(), getTaskType(), getName(), getStatus(), getDescription(), getEpicId());
    }

}
