package ru.yandex.javacourse.kanban.manager;

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

    Task getTaskById(int taskId);

    Epic getEpicById(int epicId);

    SubTask getSubTaskById(int subTaskId);

    void createTask(Task task);

    void createEpic(Epic epic);

    void createSubTask(SubTask subTask);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubTask(SubTask subTask);

    void removeTaskById(int taskId);

    void removeEpicById(int epicId);

    void removeSubTaskById(Integer subTaskId);

    void setHistoryManager(HistoryManager historyManager);

    ArrayList<SubTask> getAllSubTaskByEpic(Task epic);



    @Override
    boolean equals(Object o);

    @Override
    int hashCode();
}
