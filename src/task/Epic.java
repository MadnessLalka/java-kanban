package task;

public class Epic extends Task {
    public Epic(String name, String description, int taskId) {
        super(name, description, taskId, TaskStatus.NEW);
    }

    public Epic(Epic epic, TaskStatus status) {
        super(epic.getName(), epic.getDescription(), epic.getId(), status);
    }



}
