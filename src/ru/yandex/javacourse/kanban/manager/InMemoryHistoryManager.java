package ru.yandex.javacourse.kanban.manager;

import ru.yandex.javacourse.kanban.task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;

public class InMemoryHistoryManager implements HistoryManager {

    private final ArrayList<Task> itemViewHistoryList = new ArrayList<>();
    private final LinkedList<Task> pullViewTasks = new LinkedList<>();
    private final HashMap<Integer, LinkedList<Task>> historyMap = new HashMap<>();

    @Override
    public ArrayList<Task> getHistory() {
        if (pullViewTasks.isEmpty()) {
            System.out.println("Список просмотров пуст");
            itemViewHistoryList.clear();
            return itemViewHistoryList;
        }

//        itemViewHistoryList.addAll(pullViewTasks);
        return itemViewHistoryList;
    }

    @Override
    public void add(Task task) {
        historyViewListCleaner();
        pullViewTasks.add(Node<Task>);
    }

    @Override
    public void remove(int id) {

    }

    private void historyViewListCleaner() {

    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        InMemoryHistoryManager that = (InMemoryHistoryManager) o;
        return Objects.equals(itemViewHistoryList, that.itemViewHistoryList);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(itemViewHistoryList);
    }
}
