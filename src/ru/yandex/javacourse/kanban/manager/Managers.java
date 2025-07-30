package ru.yandex.javacourse.kanban.manager;

import java.nio.file.Path;

public class Managers {

    private Managers() {
    }

    public static TaskManager getDefault() {
        Path path = Path.of("taskManagerMemory.csv");
        return FileBackedTaskManager.loadFromFile(path.toFile());
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
