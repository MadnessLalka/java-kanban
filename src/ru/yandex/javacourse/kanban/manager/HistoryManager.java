package ru.yandex.javacourse.kanban.manager;

import ru.yandex.javacourse.kanban.task.Task;

import java.util.ArrayList;

public interface HistoryManager {
    void add(Task task);

    void remove(int id);

    ArrayList<Task> getHistory();
}
