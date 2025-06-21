package ru.yandex.javacourse.kanban;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.javacourse.kanban.manager.InMemoryTaskManager;
import ru.yandex.javacourse.kanban.manager.TaskManager;
import ru.yandex.javacourse.kanban.task.Epic;
import ru.yandex.javacourse.kanban.task.SubTask;
import ru.yandex.javacourse.kanban.task.Task;
import ru.yandex.javacourse.kanban.task.TaskStatus;

class MainTest {
    public static TaskManager taskManager = new InMemoryTaskManager();

    @BeforeEach
    void beforeEach() {
        Task firstTask = new Task("Первая задача", "Описание первой задачи", taskManager.getNewId(),
                TaskStatus.NEW);
        taskManager.createTask(firstTask);

        Epic firstEpic = new Epic("Первый эпик", "Описание первого Эпика", taskManager.getNewId());
        taskManager.createEpic(firstEpic);

        SubTask firstSubTask = new SubTask("Первая подзадача", "Описание первой подзадачи",
                taskManager.getNewId(), TaskStatus.DONE, firstEpic.getId());
        SubTask secondSubTask = new SubTask("Вторая подзадача", "Описание второй подзадачи",
                taskManager.getNewId(), TaskStatus.IN_PROGRESS, firstEpic.getId());

        taskManager.createSubTask(firstSubTask);
        taskManager.createSubTask(secondSubTask);

        Task task2 = new Task("Первая задача", "Описание первой задачи", taskManager.getNewId(),
                TaskStatus.NEW);
        taskManager.createTask(task2);

    }

    @AfterEach
    void afterEach() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    void isHistoryListSaveAnotherTypeItem(){
        taskManager.getTaskById(0);
        taskManager.getSubTaskById(2);

        Assertions.assertTrue(2 == taskManager.getHistory().size(), "Больше чем 2 :" +
                taskManager.getHistory().size());

    }

    @Test
    void shouldRemoveFirstItemWhenMore10ItemsToHistoryList(){
        taskManager.getTaskById(0);
        taskManager.getSubTaskById(2);
        taskManager.getSubTaskById(2);
        taskManager.getSubTaskById(2);
        taskManager.getSubTaskById(2);
        taskManager.getSubTaskById(2);
        taskManager.getSubTaskById(2);
        taskManager.getSubTaskById(2);
        taskManager.getSubTaskById(2);
        taskManager.getSubTaskById(2);
        taskManager.getTaskById(4);
        System.out.println(taskManager.getHistory());

        Assertions.assertTrue(10 == taskManager.getHistory().size(), "Больше чем 10 :" +
                taskManager.getHistory().size());

    }
}