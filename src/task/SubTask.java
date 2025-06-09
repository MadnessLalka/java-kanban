package task;

public class SubTask extends Task {
    private int epicId;

    public SubTask(String name, String description, int taskId, TaskStatus status, int epicId) {
        super(name, description, taskId, status);
        this.epicId = epicId;
    }

    public SubTask(SubTask subTask, TaskStatus status) {
        super(subTask.getName(), subTask.getDescription(), subTask.getId(), status);
        this.epicId = subTask.epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status=" + getStatus() +
                ", EpicId=" + getEpicId() +
                '}';
    }
}
