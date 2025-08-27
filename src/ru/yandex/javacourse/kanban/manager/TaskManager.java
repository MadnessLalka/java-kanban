package ru.yandex.javacourse.kanban.manager;

import ru.yandex.javacourse.kanban.manager.exception.IntersectionException;
import ru.yandex.javacourse.kanban.manager.exception.NotFoundException;
import ru.yandex.javacourse.kanban.task.Epic;
import ru.yandex.javacourse.kanban.task.SubTask;
import ru.yandex.javacourse.kanban.task.Task;

import java.util.ArrayList;

public interface TaskManager {
    int getNewId();

    int getIdCounter();

    ArrayList<Task> getAllTaskList();

    ArrayList<Epic> getAllEpicList();

    ArrayList<SubTask> getAllSubTaskList();

    void removeAllTask();

    void removeAllEpic();

    void removeAllSubTask();

    Task getTaskById(int taskId) throws NotFoundException;

    Epic getEpicById(int epicId) throws NotFoundException;

    SubTask getSubTaskById(int subTaskId) throws NotFoundException;

    void createTask(Task task) throws IntersectionException;

    void createEpic(Epic epic)  throws IntersectionException;

    void createSubTask(SubTask subTask)  throws IntersectionException;

    void updateTask(Task task) throws NotFoundException, IntersectionException;

    void updateEpic(Epic epic) throws NotFoundException, IntersectionException;

    void updateSubTask(SubTask subTask) throws NotFoundException, IntersectionException;

    void removeTaskById(int taskId) throws NotFoundException;

    void removeEpicById(int epicId) throws NotFoundException;

    void removeSubTaskById(Integer subTaskId) throws NotFoundException;

    void setHistoryManager(HistoryManager historyManager);

    ArrayList<SubTask> getAllSubTaskByEpic(Task epic);

    @Override
    boolean equals(Object o);

    @Override
    int hashCode();
}
