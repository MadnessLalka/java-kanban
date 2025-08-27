package ru.yandex.javacourse.kanban.manager;

import ru.yandex.javacourse.kanban.manager.exception.IntersectionException;
import ru.yandex.javacourse.kanban.manager.exception.NotFoundException;
import ru.yandex.javacourse.kanban.task.Epic;
import ru.yandex.javacourse.kanban.task.SubTask;
import ru.yandex.javacourse.kanban.task.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private int idCounter = 0;

    private HistoryManager historyManager = new InMemoryHistoryManager();

    private final HashMap<Integer, Task> taskMap = new HashMap<>();
    private final HashMap<Integer, Epic> epicMap = new HashMap<>();
    private final HashMap<Integer, SubTask> subTaskMap = new HashMap<>();

    private final TreeSet<Task> prioritizedTasks = new TreeSet<>(new Comparator<Task>() {
        @Override
        public int compare(Task o1, Task o2) {
            int timeComparison = o1.getStartTime().compareTo(o2.getStartTime());
            if (timeComparison != 0) {
                return timeComparison;
            }

            return Integer.compare(o1.getId(), o2.getId());
        }
    });

    public void setHistoryManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

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
        return new ArrayList<>(taskMap.values());
    }

    @Override
    public ArrayList<Epic> getAllEpicList() {
        return new ArrayList<>(epicMap.values());
    }

    @Override
    public ArrayList<SubTask> getAllSubTaskList() {
        return new ArrayList<>(subTaskMap.values());
    }

    @Override
    public void removeAllTask() {
        System.out.println("Все задачи удалены");
        taskMap.values().forEach(task -> {
            historyManager.remove(task.getId());
            prioritizedTasks.remove(task);
        });
        taskMap.clear();
    }

    @Override
    public void removeAllEpic() {
        System.out.println("Все эпики удалены");

        for (Epic epic : epicMap.values()) {
            getAllSubTaskByEpic(epic).forEach(subTask -> {
                historyManager.remove(subTask.getId());
                prioritizedTasks.remove(subTask);
            });

            epic.clearSubTaskList();
            historyManager.remove(epic.getId());
            prioritizedTasks.remove(epic);
        }

        subTaskMap.clear();
        epicMap.clear();
    }

    @Override
    public void removeAllSubTask() {
        System.out.println("Все подзадачи удалены");

        for (Epic epic : epicMap.values()) {
            getAllSubTaskByEpic(epic).forEach(subTask -> {
                historyManager.remove(subTask.getId());
                prioritizedTasks.remove(subTask);
            });

            epic.clearSubTaskList();
            epic.setStatus();
            epic.setDuration();
            epic.setStartTime();
            epic.setEndTime();
        }

        subTaskMap.clear();
    }

    @Override
    public Task getTaskById(int taskId) throws NotFoundException {
        if (!isTaskExist(taskMap.get(taskId))) {
            System.out.println("Задачи по id " + taskId + " - нет в списке");
            throw new NotFoundException("Задачи по id " + taskId + " - нет в списке");
        }

        return taskMap.get(taskId);
    }

    @Override
    public Epic getEpicById(int epicId) {
        if (!isEpicExist(epicMap.get(epicId))) {
            System.out.println("Эпика по такому id " + epicId + " нет");
            throw new NotFoundException("Эпика по id " + epicId + " - нет в списке");
        }

        return epicMap.get(epicId);
    }

    @Override
    public SubTask getSubTaskById(int subTaskId) {
        if (!isSubTaskExist(subTaskMap.get(subTaskId))) {
            System.out.println("Подзадачи по такому id " + subTaskId + " нет");
            throw new NotFoundException("Подзадачи по id " + subTaskId + " - нет в списке");
        }

        return subTaskMap.get(subTaskId);
    }

    @Override
    public void createTask(Task task) {
        if (isTaskExist(taskMap.get(task.getId()))) {
            System.out.println("Такая задача " + task + " уже поставлена");
            return;
        }

        if (isTaskIntersectToAllTaskToTime(task)) {
            System.out.println("Задача " + task + " пересекается по времени с другими задачами из списка");
            throw new IntersectionException("Задача " + task.getId() + " пересекается по времени с другими задачами из списка");
        }

        taskMap.put(task.getId(), task);
        prioritizedTasks.add(task);
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

        if (isTaskIntersectToAllTaskToTime(epic)) {
            System.out.println("Эпик " + epic + " пересекаются по времени с другими задачами из списка");
            throw new IntersectionException("Эпик " + epic + " пересекается по времени с другими задачами из списка");
        }

        epic.setStatus();
        epic.setDuration();
        epic.setStartTime();
        epic.setEndTime();

        epicMap.put(epic.getId(), epic);
        prioritizedTasks.add(epic);
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

        if (isTaskIntersectToAllTaskToTime(subTask)) {
            System.out.println("Подзадача " + subTask + " пересекаются по времени с другими задачами из списка");
            throw new IntersectionException("Эпик " + subTask + " пересекается по времени с другими задачами из списка");
        }

        Epic currentEpic = getEpicById(subTask.getEpicId());
        currentEpic.addSubTaskToList(subTask);

        subTaskMap.put(subTask.getId(), subTask);
        prioritizedTasks.add(subTask);
        System.out.println("Подзадача добавлена");

        epicStatusChangerBySubTaskId(subTask.getEpicId());
        epicTimeChangerBySubTaskId(subTask.getEpicId());
    }

    private void epicStatusChangerBySubTaskId(int currentEpicId) {
        if (isEpicExist(epicMap.get(currentEpicId))) {
            Epic currentEpic = epicMap.get(currentEpicId);
            currentEpic.setStatus();
            System.out.println("Статус Эпика '" + currentEpic.getName() + "' был изменён на '" +
                    currentEpic.getStatus() + "'");
        }
    }

    private void epicTimeChangerBySubTaskId(int currentEpicId) {
        if (isEpicExist(epicMap.get(currentEpicId))) {
            Epic currentEpic = epicMap.get(currentEpicId);
            currentEpic.setDuration();
            currentEpic.setStartTime();
            currentEpic.setEndTime();

            System.out.println("Протяженность исполнения Эпика '" + currentEpic.getName() + "' было изменёно на '" +
                    currentEpic.getDuration() + "'");
            System.out.println("Дата начала Эпика '" + currentEpic.getName() + "' была изменёна на '" +
                    currentEpic.getStartTime() + "'");
            System.out.println("Дата конца исполнения Эпика '" + currentEpic.getName() + "' была изменёна на '" +
                    currentEpic.getEndTime() + "'");
        }
    }

    private Boolean isSubTaskExist(SubTask subTask) {
        return getAllSubTaskList().contains(subTask);
    }

    @Override
    public void updateTask(Task task) {
        if (!isTaskExist(taskMap.get(task.getId()))) {
            System.out.println("Такой задачи " + task + " нет в списках");
            throw new NotFoundException("Такой задачи " + task + " нет в списках");
        }

//        if (isTaskIntersectToAllTaskToTime(task)) {
//            System.out.println("Задача " + task + " пересекаются по времени с другими задачами из списка");
//            throw new IntersectionException("Задача " + task + " пересекаются по времени с другими задачами из списка");
//        }

        System.out.println("Задача обновлена");
        prioritizedTasks.remove(taskMap.get(task.getId()));
        prioritizedTasks.add(task);
        taskMap.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic) {
        if (!isEpicExist(epicMap.get(epic.getId()))) {
            System.out.println("Такого эпика " + epic + "нет в списках");
            throw new NotFoundException("Такого эпика " + epic + " нет в списках");
        }

//        if (isTaskIntersectToAllTaskToTime(epic)) {
//            System.out.println("Эпик " + epic + " пересекается по времени с другими задачами из списка");
//            throw new IntersectionException("Эпик " + epic + " пересекается по времени с другими задачами из списка");
//        }

        epic.setStatus();
        epic.setDuration();
        epic.setStartTime();
        epic.setEndTime();

        System.out.println("Эпик обновлен");
        prioritizedTasks.remove(epicMap.get(epic.getId()));
        prioritizedTasks.add(epic);
        epicMap.put(epic.getId(), epic);

    }

    @Override
    public void updateSubTask(SubTask subTask) {
        if (!isSubTaskExist(subTaskMap.get(subTask.getId()))) {
            System.out.println("Такой подзадачи '" + subTask.getName() + "' нет в списках");
            throw new NotFoundException("Такой подзадачи '" + subTask.getName() + "' нет в списках");
        }

//        if (isTaskIntersectToAllTaskToTime(subTask)) {
//            System.out.println("Подзадача " + subTask + " пересекается по времени с другими задачами из списка");
//            throw new IntersectionException("Подзадача " + subTask + " пересекается по времени с другими задачами из списка");
//        }

        Epic currentEpic = getEpicById(subTask.getEpicId());
        SubTask oldSubTask = getSubTaskById(subTask.getId());

        currentEpic.removeSubTaskToList(oldSubTask);
        currentEpic.addSubTaskToList(subTask);

        System.out.println("Подзадача обновлена");
        prioritizedTasks.remove(subTaskMap.get(subTask.getId()));
        prioritizedTasks.add(subTask);
        subTaskMap.put(subTask.getId(), subTask);


        epicStatusChangerBySubTaskId(subTask.getEpicId());
        epicTimeChangerBySubTaskId(subTask.getEpicId());
    }

    @Override
    public void removeTaskById(int taskId) {
        if (!isTaskExist(taskMap.get(taskId))) {
            System.out.println("Задачи с таким номер " + taskId + " нет в списке");
            throw new NotFoundException("Задачи с таким номер " + taskId + " нет в списке");
        }

        System.out.println("Задание удалено");
        historyManager.remove(taskId);
        prioritizedTasks.remove(getTaskById(taskId));
        taskMap.remove(taskId);

    }

    @Override
    public void removeEpicById(int epicId) {
        if (!isEpicExist(epicMap.get(epicId))) {
            System.out.println("Эпика с таким номером " + epicId + " нет в списке");
            throw new NotFoundException("Эпика с таким номером " + epicId + " нет в списке");
        }

        Epic currentEpic = epicMap.get(epicId);

        getAllSubTaskByEpic(currentEpic).forEach(subTask -> {
            subTaskMap.remove(subTask.getId());
            historyManager.remove(subTask.getId());
        });

        historyManager.remove(epicId);
        prioritizedTasks.remove(getEpicById(epicId));
        epicMap.remove(epicId);

        System.out.println("Эпик со всеми подзадачами был удалён");
    }

    @Override
    public void removeSubTaskById(Integer subTaskId) {
        if (!isSubTaskExist(subTaskMap.get(subTaskId))) {
            System.out.println("Подзадачи с таким номером " + subTaskId + " нет в списке");
            throw new NotFoundException("Подзадачи с таким номером " + subTaskId + " нет в списке");
        }

        SubTask currentSubTask = subTaskMap.get(subTaskId);

        Epic currentEpic = getEpicById(currentSubTask.getEpicId());
        currentEpic.removeSubTaskToList(subTaskMap.get(subTaskId));

        System.out.println("Подзадача удалена");
        historyManager.remove(subTaskId);
        prioritizedTasks.remove(getSubTaskById(subTaskId));
        subTaskMap.remove(subTaskId);

        epicStatusChangerBySubTaskId(currentEpic.getId());
        epicTimeChangerBySubTaskId(currentEpic.getId());
    }

    @Override
    public ArrayList<SubTask> getAllSubTaskByEpic(Task epic) {
        ArrayList<SubTask> subTaskListByEpic = new ArrayList<>(
                getAllSubTaskList().stream()
                        .filter(subTask -> Objects.equals(subTask.getEpicId(), epic.getId()))
                        .toList());

        if (subTaskListByEpic.isEmpty()) {
            System.out.println("В этом эпики таких подзадач нет");
            throw new NotFoundException("В этом эпики таких подзадач нет");
        }

        return subTaskListByEpic;
    }


    public TreeSet<Task> getPrioritizedTasks() {
        return new TreeSet<>(prioritizedTasks);
    }

    public boolean isTasksIntersectToTime(Task t1, Task t2) {
        return t1.getStartTime().isBefore(t2.getEndTime()) && t2.getStartTime().isBefore(t1.getEndTime());
    }

    public boolean isTaskIntersectToAllTaskToTime(Task newTask) {
        return getPrioritizedTasks().stream().anyMatch((task) -> isTasksIntersectToTime(newTask, task));
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
