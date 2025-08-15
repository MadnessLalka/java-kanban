package ru.yandex.javacourse.kanban.manager;

import ru.yandex.javacourse.kanban.task.Task;

public class Node<T extends Task> {
    public final int id;
    public final T data;
    public Node<T> next;
    public Node<T> prev;

    public Node(Node<T> prev, T task, Node<T> next) {
        this.id = task.getId();
        this.data = task;
        this.next = next;
        this.prev = prev;
    }
}
