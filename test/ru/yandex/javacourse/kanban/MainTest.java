package ru.yandex.javacourse.kanban;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.javacourse.kanban.manager.*;
import ru.yandex.javacourse.kanban.task.Epic;
import ru.yandex.javacourse.kanban.task.SubTask;
import ru.yandex.javacourse.kanban.task.Task;
import ru.yandex.javacourse.kanban.task.TaskStatus;

import java.util.ArrayList;

class MainTest {
    public static TaskManager taskManager = Managers.getDefault();
    public static HistoryManager historyManager = Managers.getDefaultHistory();

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
        taskManager = Managers.getDefault();
        historyManager = Managers.getDefaultHistory();
    }

    @Test
    void isHistoryListSaveAnotherTypeItem() {
        historyManager.add(taskManager.getTaskById(0));
        historyManager.add(taskManager.getSubTaskById(2));

        Assertions.assertEquals(2, historyManager.getHistory().size(), "Больше чем 2 :" +
                historyManager.getHistory().size());

    }

    @Test
    void shouldRemoveFirstItemWhenMore10ItemsToHistoryList() {
        historyManager.add(taskManager.getTaskById(0));
        historyManager.add(taskManager.getSubTaskById(2));
        historyManager.add(taskManager.getSubTaskById(2));
        historyManager.add(taskManager.getSubTaskById(2));
        historyManager.add(taskManager.getSubTaskById(2));
        historyManager.add(taskManager.getSubTaskById(2));
        historyManager.add(taskManager.getSubTaskById(2));
        historyManager.add(taskManager.getSubTaskById(2));
        historyManager.add(taskManager.getSubTaskById(2));
        historyManager.add(taskManager.getSubTaskById(2));
        historyManager.add(taskManager.getSubTaskById(2));
        historyManager.add(taskManager.getTaskById(4));

        Assertions.assertEquals(10, historyManager.getHistory().size(), "Больше чем 10 :" +
                historyManager.getHistory().size());

    }

    @Test
    void isTaskEqualItselfToItself() {
        Assertions.assertEquals(taskManager.getTaskById(0), taskManager.getTaskById(0),
                "Задачи с одинаковым ID - должны быть эквиваленты");
    }

    @Test
    void isEpicEqualItselfToItself() {
        Assertions.assertEquals(taskManager.getEpicById(1), taskManager.getEpicById(1),
                "Эпики с одинаковым ID - должны быть эквиваленты");
    }

    @Test
    void isSubTaskEqualItselfToItself() {
        Assertions.assertEquals(taskManager.getSubTaskById(2), taskManager.getSubTaskById(2),
                "Подзадачи с одинаковым ID - должны быть эквиваленты");
    }

    @Test
    void isManagerClassReturnNewInstance() {
        Assertions.assertEquals(new InMemoryTaskManager(), Managers.getDefault(),
                "Менеджер задач должен быть проинициализирован");
        Assertions.assertEquals(new InMemoryHistoryManager(), Managers.getDefaultHistory(),
                "Менеджер просмотренной истории должен быть проинициализирован");
    }

    @Test
    void isManagerAddedTasksAnotherTypesAndFindThemByIds() {
        Assertions.assertEquals(2, taskManager.getAllTaskList().size());
        Assertions.assertEquals(1, taskManager.getAllEpicList().size());
        Assertions.assertEquals(2, taskManager.getAllSubTaskList().size());
        Assertions.assertEquals("Первая задача", taskManager.getTaskById(0).getName());
        Assertions.assertEquals("Первый эпик", taskManager.getEpicById(1).getName());
        Assertions.assertEquals("Первая подзадача", taskManager.getSubTaskById(2).getName());
    }

    @Test
    void isTasksWithGeneratedIdsAndUpdatedIdsDontConflict() {
        Task testTask = new Task("Тестовая", "Тест", 0,
                TaskStatus.NEW);
        taskManager.createTask(testTask);
        Assertions.assertNotEquals("Тестовая", taskManager.getTaskById(0).getName());

        Epic testEpic = new Epic("Тестовая", "Тест", 1);
        taskManager.createEpic(testEpic);
        Assertions.assertNotEquals("Тестовая", taskManager.getEpicById(1).getName());

        SubTask testSubTask = new SubTask("Тестовая", "Тест",
                2, TaskStatus.DONE, 1);
        taskManager.createSubTask(testSubTask);
        Assertions.assertNotEquals("Тестовая", taskManager.getSubTaskById(2).getName());
    }

    @Test
    void isExistedTaskNotChangedFromAllLinesAfterTryChangeThisTask() {
        Task testTask = new Task("Тестовая", "Описание первой задачи", 0,
                TaskStatus.NEW);
        taskManager.createTask(testTask);

        Assertions.assertNotEquals(testTask.getName(), taskManager.getTaskById(0).getName());
    }

    @Test
    void isHistoryManagerSavedPastVersionTask() {
        ArrayList<Task> testedTasksList = new ArrayList<>();
        Task testTask = taskManager.getTaskById(0);
        testedTasksList.add(testTask);

        historyManager.add(testTask);
        Assertions.assertEquals(historyManager.getHistory().getFirst(), testTask);

        Task updatedTask = new Task("Тестовая", "Описание первой задачи", 0,
                TaskStatus.NEW);

        taskManager.updateTask(updatedTask);
        testedTasksList.add(updatedTask);
        historyManager.add(taskManager.getTaskById(0));

        System.out.println(historyManager.getHistory());
        System.out.println(testedTasksList);

        Assertions.assertEquals(testedTasksList, historyManager.getHistory());
    }
}