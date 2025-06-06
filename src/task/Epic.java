package task;

import java.util.List;

public class Epic extends Task {
    private String epicName;
    private String epicDescription;
    private int epicId;
    private TaskStatus epicStatus;
    private List<SubTask> subTaskList;


    public Epic(String name, String description, int taskId, TaskStatus status, String epicName, String epicDescription,
                int epicId, TaskStatus epicStatus, List<SubTask> subTaskList) {
        super(name, description, taskId, status);
        this.epicName = epicName;
        this.epicDescription = epicDescription;
        this.epicId = epicId;
        this.epicStatus = epicStatus;
        this.subTaskList = subTaskList;
    }

    public String getEpicName() {
        return epicName;
    }

    public void setEpicName(String epicName) {
        this.epicName = epicName;
    }

    public String getEpicDescription() {
        return epicDescription;
    }

    public void setEpicDescription(String epicDescription) {
        this.epicDescription = epicDescription;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public TaskStatus getEpicStatus() {
        return epicStatus;
    }

    public void setEpicStatus(TaskStatus epicStatus) {
        this.epicStatus = epicStatus;
    }

    public List<SubTask> getSubTaskList() {
        return subTaskList;
    }

    public void setSubTaskList(List<SubTask> subTaskList) {
        this.subTaskList = subTaskList;
    }
}
