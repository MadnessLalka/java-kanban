package task;

import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<SubTask> subTasksList = new ArrayList<>();


    public Epic(String name, String description, int taskId, TaskStatus status) {
        super(name, description, taskId, status);
    }

    public ArrayList<SubTask> getSubTasksList() {
        return subTasksList;
    }

    public void setSubTasksList(ArrayList<SubTask> subTasksList) {
        this.subTasksList = subTasksList;
    }
}
