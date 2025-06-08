import task.Epic;
import task.SubTask;
import task.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private static int idCounter = 0;
    private HashMap<Integer, Task> taskMap = new HashMap<>();
    private HashMap<Integer, Epic> epicMap = new HashMap<>();
    private HashMap<Integer, SubTask> subTaskMap = new HashMap<>();

    private int getNewId() {
        return idCounter++;
    }

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
        System.out.println("Все задачи удалены");
        taskMap.clear();
    }

    public void removeAllEpic() {
        System.out.println("Все эпики удалены");
        epicMap.clear();
    }

    public void removeAllSubTask() {
        System.out.println("Все подзадачи удалены");
        subTaskMap.clear();
    }


    public Task getTaskById(int taskId) {
        if (!taskMap.containsKey(taskId)) {
            System.out.println("Задачи по такому id нет");
            return null;
        }

        return taskMap.get(taskId);
    }

    public Epic getEpicById(int epicId) {
        if (!epicMap.containsKey(epicId)) {
            System.out.println("Эпика по такому id нет");
            return null;
        }

        return epicMap.get(epicId);
    }

    public SubTask getSubTaskById(int subTaskId) {
        if (!subTaskMap.containsKey(subTaskId)) {
            System.out.println("Подзадачи по такому id нет");
            return null;
        }

        return subTaskMap.get(subTaskId);
    }

    public void createTask(Task task) {
        if (isTaskExist(getAllTaskList(), task)) {
            System.out.println("Такая задача уже поставлена");
            return;
        }

        taskMap.put(getNewId(), task);
        System.out.println("Задача добавлена");
    }

    private Boolean isTaskExist(ArrayList<Task> taskList, Task task) {
        for (Task t : taskList) {
            if (t.equals(task)) {
                return true;
            }
        }
        return false;
    }

    public void createEpic(Epic epic) {
        if (isEpicExist(getAllEpicList(), epic)) {
            System.out.println("Такой эпик уже поставлен");
            return;
        }

        epicMap.put(getNewId(), epic);
        System.out.println("Эпик добавлен");
    }

    private Boolean isEpicExist(ArrayList<Epic> epicList, Epic epic) {
        for (Task e : epicList) {
            if (e.equals(epic)) {
                return true;
            }
        }

        return false;
    }

    public void createSubTask(SubTask subTask) {
        if (isSubTaskExist(getAllSubTaskList(), subTask)) {
            System.out.println("Такая подзадача уже поставлена");
            return;
        }

        subTaskMap.put(getNewId(), subTask);
        System.out.println("Подзадача добавлена");
    }

    private Boolean isSubTaskExist(ArrayList<SubTask> subTaskList, SubTask subTask) {
        for (Task sb : subTaskList) {
            if (sb.equals(subTask)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskManager that = (TaskManager) o;
        return idCounter == that.idCounter;
    }

    @Override
    public int hashCode() {
        return idCounter;
    }
}
