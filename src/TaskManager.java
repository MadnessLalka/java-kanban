import java.util.Objects;

public class TaskManager {
    private int taskId;



    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TaskManager that = (TaskManager) o;
        return taskId == that.taskId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(taskId);
    }
}
