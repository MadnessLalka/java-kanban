package task;

public class SubTask extends Task {
    private final Epic epicId;
    private String subTaskName;
    private String subTaskDescription;
    private int subTaskId;
    private TaskStatus subTaskStatus;


    public SubTask(String name, String description, int taskId, TaskStatus status, Epic epicId, String subTaskName,
                   String subTaskDescription, int subTaskId, TaskStatus subTaskStatus) {
        super(name, description, taskId, status);
        this.epicId = epicId;
        this.subTaskName = subTaskName;
        this.subTaskDescription = subTaskDescription;
        this.subTaskId = subTaskId;
        this.subTaskStatus = subTaskStatus;
    }

    public Epic getEpicId() {
        return epicId;
    }

    public String getSubTaskName() {
        return subTaskName;
    }

    public void setSubTaskName(String subTaskName) {
        this.subTaskName = subTaskName;
    }

    public String getSubTaskDescription() {
        return subTaskDescription;
    }

    public void setSubTaskDescription(String subTaskDescription) {
        this.subTaskDescription = subTaskDescription;
    }

    public int getSubTaskId() {
        return subTaskId;
    }

    public void setSubTaskId(int subTaskId) {
        this.subTaskId = subTaskId;
    }

    public TaskStatus getSubTaskStatus() {
        return subTaskStatus;
    }

    public void setSubTaskStatus(TaskStatus subTaskStatus) {
        this.subTaskStatus = subTaskStatus;
    }
}
