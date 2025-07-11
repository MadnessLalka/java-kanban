package ru.yandex.javacourse.kanban.manager;

public class Node<T> {
    private int id;
    public final T data;
    public final Node<T> next;
    public final Node<T> prev;


    public Node(T data) {
        id++;
        this.data = data;
        this.next = null;
        this.prev = null;
    }

    public Integer getId() {
        return id;
    }

}
