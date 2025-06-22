package ru.yandex.javacourse.kanban.manager;

public class Managers   {

    private Managers(){
    }

    public static TaskManager getDefault(){
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
