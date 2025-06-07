import task.Epic;
import task.SubTask;
import task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class TaskManager {
    private int taskId;

    private HashMap<Integer, Task> taskMap = new HashMap<>();
    private HashMap<Integer, Epic> epicMap = new HashMap<>();
    private HashMap<Integer, SubTask> subTaskMap = new HashMap<>();

    public ArrayList<Task> getAllTaskList() {
        ArrayList<Task> taskList = new ArrayList<>();

        for (Integer key : taskMap.keySet()) {
            taskList.add(taskMap.get(key));
        }

        return taskList;
    }

    public ArrayList<Epic> getAllEpicList() {
        ArrayList<Epic> epicList = new ArrayList<>();

        for (Integer key : epicMap.keySet()) {
            epicList.add(epicMap.get(key));
        }

        return epicList;
    }

    public ArrayList<SubTask> getAllSubTaskList() {
        ArrayList<SubTask> subTaskList = new ArrayList<>();

        for (Integer key : subTaskMap.keySet()) {
            subTaskList.add(subTaskMap.get(key));
        }

        return subTaskList;
    }

    public void removeAllTask() {
        taskMap.clear();
    }

    public void removeAllEpic() {
        epicMap.clear();
    }

    public void removeAllSubTask() {
        subTaskMap.clear();
    }


    public Task getTaskById(int taskId) {
        if (!taskMap.containsKey(taskId)) {
            return null;
        }

        return taskMap.get(taskId);
    }

    public Epic getEpicById(int epicId) {
        if (!epicMap.containsKey(epicId)) {
            return null;
        }

        return epicMap.get(epicId);
    }

    public SubTask getSubTaskById(int subTaskId) {
        if (!subTaskMap.containsKey(subTaskId)) {
            return null;
        }

        return subTaskMap.get(subTaskId);
    }

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
