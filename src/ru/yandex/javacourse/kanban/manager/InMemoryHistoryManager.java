package ru.yandex.javacourse.kanban.manager;

import ru.yandex.javacourse.kanban.task.Task;

import java.util.ArrayList;
import java.util.Objects;

public class InMemoryHistoryManager implements HistoryManager {

    private static final int VIEW_LIST_HISTORY_SIZE = 10;
    private final ArrayList<Task> itemViewHistoryList = new ArrayList<>(VIEW_LIST_HISTORY_SIZE);

    @Override
    public ArrayList<Task> getHistory() {
        if (itemViewHistoryList.isEmpty()) {
            System.out.println("Список просмотров пуст");
            return itemViewHistoryList;
        }

        return itemViewHistoryList;
    }

    @Override
    public void add(Task task) {
        historyViewListCleaner();
        itemViewHistoryList.add(task);
    }

    private void historyViewListCleaner() {
        if (isHistoryViewListFull()) {
            itemViewHistoryList.removeFirst();
        }
    }

    private Boolean isHistoryViewListFull() {
        return itemViewHistoryList.size() >= VIEW_LIST_HISTORY_SIZE;
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
