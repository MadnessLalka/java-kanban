package ru.yandex.javacourse.kanban.manager;

import ru.yandex.javacourse.kanban.task.Epic;
import ru.yandex.javacourse.kanban.task.SubTask;
import ru.yandex.javacourse.kanban.task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class InMemoryTaskManager implements TaskManager {
    private int idCounter = 0;

    private final HashMap<Integer, Task> taskMap = new HashMap<>();
    private final HashMap<Integer, Epic> epicMap = new HashMap<>();
    private final HashMap<Integer, SubTask> subTaskMap = new HashMap<>();

    @Override
    public int getNewId() {
        return idCounter++;
    }

    @Override
    public int getIdCounter() {
        return idCounter;
    }

    @Override
    public ArrayList<Task> getAllTaskList() {
        ArrayList<Task> taskList = new ArrayList<>();

        for (Integer key : taskMap.keySet()) {
            taskList.add(taskMap.get(key));
        }

        return taskList;
    }

    @Override
    public ArrayList<Epic> getAllEpicList() {
        ArrayList<Epic> epicList = new ArrayList<>();

        for (Integer key : epicMap.keySet()) {
            epicList.add(epicMap.get(key));
        }

        return epicList;
    }

    @Override
    public ArrayList<SubTask> getAllSubTaskList() {
        ArrayList<SubTask> subTaskList = new ArrayList<>();

        for (Integer key : subTaskMap.keySet()) {
            subTaskList.add(subTaskMap.get(key));
        }

        return subTaskList;
    }

    @Override
    public void removeAllTask() {
        System.out.println("Все задачи удалены");
        taskMap.clear();
    }

    @Override
    public void removeAllEpic() {
        System.out.println("Все эпики удалены");

        for (Epic epic : epicMap.values()) {
            epic.clearSubTaskList();
        }

        subTaskMap.clear();
        epicMap.clear();
    }

    @Override
    public void removeAllSubTask() {
        System.out.println("Все подзадачи удалены");

        for (Epic epic : epicMap.values()) {
            epic.clearSubTaskList();
            epic.setStatus();
        }

        subTaskMap.clear();
    }

    @Override
    public Task getTaskById(int taskId) {
        if (!isTaskExist(taskMap.get(taskId))) {
            System.out.println("Задачи по id " + taskId + " - нет в списке");
        }

        return taskMap.get(taskId);
    }

    @Override
    public Epic getEpicById(int epicId) {
        if (!isEpicExist(epicMap.get(epicId))) {
            System.out.println("Эпика по такому id " + epicId + " нет");
        }

        return epicMap.get(epicId);
    }

    @Override
    public SubTask getSubTaskById(int subTaskId) {
        if (!isSubTaskExist(subTaskMap.get(subTaskId))) {
            System.out.println("Подзадачи по такому id " + subTaskId + " нет");

        }

        return subTaskMap.get(subTaskId);
    }

    @Override
    public void createTask(Task task) {
        if (isTaskExist(taskMap.get(task.getId()))) {
            System.out.println("Такая задача " + task + " уже поставлена");
            return;
        }

        taskMap.put(task.getId(), task);
        System.out.println("Задача добавлена");
    }

    private Boolean isTaskExist(Task task) {
        return getAllTaskList().contains(task);
    }

    @Override
    public void createEpic(Epic epic) {
        if (isEpicExist(epicMap.get(epic.getId()))) {
            System.out.println("Такой эпик " + epic + " уже поставлен");
            return;
        }

        epic.setStatus();

        epicMap.put(epic.getId(), epic);
        System.out.println("Эпик добавлен");
    }

    private Boolean isEpicExist(Epic epic) {
        return getAllEpicList().contains(epic);
    }

    @Override
    public void createSubTask(SubTask subTask) {
        if (isSubTaskExist(subTaskMap.get(subTask.getId()))) {
            System.out.println("Такая подзадача " + subTask + " уже поставлена");
            return;
        }

        Epic currentEpic = getEpicById(subTask.getEpicId());
        currentEpic.addSubTaskToList(subTask);

        subTaskMap.put(subTask.getId(), subTask);
        System.out.println("Подзадача добавлена");

        epicStatusChangerBySubTaskId(subTask.getEpicId());
    }

    private void epicStatusChangerBySubTaskId(int currentEpicId) {
        if (isEpicExist(epicMap.get(currentEpicId))) {
            Epic currentEpic = epicMap.get(currentEpicId);
            currentEpic.setStatus();
            System.out.println("Статус Эпика '" + currentEpic.getName() + "' был изменён на '" +
                    currentEpic.getStatus() + "'");
        }
    }

    private Boolean isSubTaskExist(SubTask subTask) {
        return getAllSubTaskList().contains(subTask);
    }

    @Override
    public void updateTask(Task task) {
        if (!isTaskExist(taskMap.get(task.getId()))) {
            System.out.println("Такой задачи " + task + " нет в списках");
            return;
        }

        System.out.println("Задача обновлена");
        taskMap.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic) {
        if (!isEpicExist(epicMap.get(epic.getId()))) {
            System.out.println("Такого эпика " + epic + "нет в списках");
            return;
        }

        epic.setStatus();

        System.out.println("Эпик обновлен");
        epicMap.put(epic.getId(), epic);
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        if (!isSubTaskExist(subTaskMap.get(subTask.getId()))) {
            System.out.println("Такой подзадачи '" + subTask.getName() + "' нет в списках");
            return;
        }

        Epic currentEpic = getEpicById(subTask.getEpicId());
        SubTask oldSubTask = getSubTaskById(subTask.getId());

        currentEpic.removeSubTaskToList(oldSubTask);
        currentEpic.addSubTaskToList(subTask);

        System.out.println("Подзадача обновлена");
        subTaskMap.put(subTask.getId(), subTask);

        epicStatusChangerBySubTaskId(subTask.getEpicId());
    }

    @Override
    public void removeTaskById(int taskId) {
        if (!isTaskExist(taskMap.get(taskId))) {
            System.out.println("Задачи с таким номер " + taskId + " нет в списке");
            return;
        }

        System.out.println("Задание удалено");
        taskMap.remove(taskId);
    }

    @Override
    public void removeEpicById(int epicId) {
        if (!isEpicExist(epicMap.get(epicId))) {
            System.out.println("Эпика с таким номером " + epicId + " нет в списке");
            return;
        }

        Epic currentEpic = epicMap.get(epicId);
        ArrayList<SubTask> subTasksCurrentEpic = getAllSubTaskByEpic(currentEpic);

        for (SubTask st : subTasksCurrentEpic) {
            subTaskMap.remove(st.getId());
        }

        epicMap.remove(epicId);

        System.out.println("Эпик со всеми подзадачами был удалён");

    }

    @Override
    public void removeSubTaskById(Integer subTaskId) {
        if (!isSubTaskExist(subTaskMap.get(subTaskId))) {
            System.out.println("Подзадачи с таким номером " + subTaskId + " нет в списке");
            return;
        }

        SubTask currentSubTask = subTaskMap.get(subTaskId);

        Epic currentEpic = getEpicById(currentSubTask.getEpicId());
        currentEpic.removeSubTaskToList(subTaskMap.get(subTaskId));

        System.out.println("Подзадача удалена");
        subTaskMap.remove(subTaskId);

        epicStatusChangerBySubTaskId(currentEpic.getId());
    }

    @Override
    public ArrayList<SubTask> getAllSubTaskByEpic(Task epic) {
        ArrayList<SubTask> allSubTaskList = getAllSubTaskList();
        ArrayList<SubTask> subTaskListByEpic = new ArrayList<>();

        for (SubTask st : allSubTaskList) {
            if (Objects.equals(st.getEpicId(), epic.getId())) {
                subTaskListByEpic.add(st);
            }
        }

        if (subTaskListByEpic.isEmpty()) {
            System.out.println("В этом эпики таких подзадач нет");
            return subTaskListByEpic;
        }

        return subTaskListByEpic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InMemoryTaskManager that = (InMemoryTaskManager) o;
        return idCounter == that.idCounter;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idCounter);
    }

}
