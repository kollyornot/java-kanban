import java.util.ArrayList;

public class Epic extends Task {
    private boolean areAllSubTasksCompleted;
    private ArrayList<SubTask> subTasks;

    public Epic(String name, String description, Status status,
                int id, ArrayList<SubTask> subTasks) {
        super(name, description, status, id);
        this.areAllSubTasksCompleted = false;
        this.subTasks = subTasks;
    }

    public boolean isAreAllSubTasksCompleted() {return areAllSubTasksCompleted;}
    public void setAreAllSubTasksCompleted(boolean areAllSubTasksCompleted) {this.areAllSubTasksCompleted = areAllSubTasksCompleted;}
    public ArrayList<SubTask> getSubTasks() {return subTasks;}
    public void setSubTasks(ArrayList<SubTask> subTasks) {this.subTasks = subTasks;}

    @Override
    public String toString() {
        return "Epic{" +
                ", name='" + name + '\'' +
                ", id=" + id +
                ", description='" + description + '\'' +
                ", subTasks=" + subTasks +
                "areAllSubTasksCompleted=" + areAllSubTasksCompleted +
                ", status=" + status +
                '}';
    }

    public void checkAllSubTasksCompleted(){
        boolean allSubTasksCompleted = false;
        for(SubTask subTask : subTasks){
            if(subTask.getStatus().equals(Status.DONE))
                allSubTasksCompleted = true;
            else {
                allSubTasksCompleted = false;
                break;
            }
        }
        this.areAllSubTasksCompleted = allSubTasksCompleted;
    }

    public void AddSubTask(SubTask subTask){
        subTasks.add(subTask);
        subTask.setEpicId(id);
        checkAllSubTasksCompleted();
    }

    public void RemoveAllSubTask(){
        subTasks.clear();
    }
}
