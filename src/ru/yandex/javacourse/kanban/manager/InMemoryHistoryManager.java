package ru.yandex.javacourse.kanban.manager;

import ru.yandex.javacourse.kanban.task.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private Node<Task> head;
    private Node<Task> tail;

    private final HashMap<Integer, Node<Task>> historyMap = new LinkedHashMap<>();

    @Override
    public ArrayList<Task> getHistory() {

        ArrayList<Task> historyList = new ArrayList<>(getTasks());

        if (historyList.isEmpty()) {
            System.out.println("Список просмотров пуст");
            return historyList;
        }

        return historyList;
    }

    @Override
    public void add(Task task) {
        removeNode(task);
        linkLast(task);
    }

    @Override
    public void remove(int id) {
        Node<Task> node = historyMap.remove(id);

        if (node == null) return;

        Node<Task> prev = node.prev;
        Node<Task> next = node.next;

        if (prev != null) {
            prev.next = next;
        } else {
            head = next;
        }

        if (next != null) {
            next.prev = prev;
        } else {
            tail = prev;
        }

        System.out.println("Узел с id " + id + " был удалён из истории просмотров");
    }

    private void linkLast(Task task) {
        Node<Task> newNode = new Node<>(tail, task, null);

        if (tail != null) {
            tail.next = newNode;
        } else {
            head = newNode;
        }

        tail = newNode;
        historyMap.put(task.getId(), newNode);
    }

    private List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        Node<Task> current = head;

        while (current != null) {
            tasks.add(current.data);
            current = current.next;
        }

        return tasks;
    }

    public void removeNode(Task task) {

        remove(task.getId());
    }
}
