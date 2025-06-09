package task;

public class Task {
    private String name;
    private String description;
    private int id;
    private TaskStatus status;

    public Task(String name, String description, int id, TaskStatus status) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;
    }

    public Task(Task task, TaskStatus status) {
        this.name = task.name;
        this.description = task.description;
        this.id = task.id;
        this.status = status;
    }

    public String getName() {
        return name;
    }


    public String getDescription() {
        return description;
    }


    public int getId() {
        return id;
    }


    public TaskStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }
}
