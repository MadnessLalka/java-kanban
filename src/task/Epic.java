package task;

import java.util.ArrayList;

public class Epic extends Task {
    private TaskStatus status;
    private ArrayList<SubTask> subTasksList = new ArrayList<>();

    public Epic(String name, String description, int taskId) {
        super(name, description, taskId);
    }

    @Override
    public TaskStatus getStatus() {
        return status;
    }

    public void addSubTaskToList(SubTask subTask){
        subTasksList.add(subTask);
    }

    public void removeSubTaskToList(SubTask subTask){
        subTasksList.remove(subTask);
    }

    public void setStatus() {
        if(subTasksList.isEmpty()){
            this.status = TaskStatus.NEW;
            return;
        }

        boolean isDone = false;

        for (SubTask st : subTasksList){
            if (st.getStatus() == TaskStatus.DONE){
                isDone = true;
            } else {
                isDone = false;
               break;
            }
        }

        if (isDone){
            this.status = TaskStatus.DONE;
            return;
        }

        boolean isInProgress = false;
        for (SubTask st : subTasksList){
            if (st.getStatus() == TaskStatus.IN_PROGRESS || st.getStatus() == TaskStatus.DONE){
                isInProgress = true;
                break;
            }
        }

        if (isInProgress) {
            this.status = TaskStatus.IN_PROGRESS;
        } else {
            this.status = TaskStatus.NEW;
        }

    }

    public void setSubTasksList(ArrayList<SubTask> subTasksList) {
        this.subTasksList = subTasksList;
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public String getDescription() {
        return super.getDescription();
    }

    @Override
    public int getId() {
        return super.getId();
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + super.getName() + '\'' +
                ", description='" + super.getDescription()+ '\'' +
                ", id=" + super.getId() +
                ", status=" + status +
                ", subTaskList=" + subTasksList +
                '}';
    }



    //    public Epic(Epic epic, TaskStatus status) {
//        super(epic.getName(), epic.getDescription(), epic.getId(), status);
//    }



}
