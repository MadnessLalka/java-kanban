package ru.yandex.javacourse.kanban;

import ru.yandex.javacourse.kanban.manager.*;
import ru.yandex.javacourse.kanban.task.Epic;
import ru.yandex.javacourse.kanban.task.SubTask;
import ru.yandex.javacourse.kanban.task.Task;
import ru.yandex.javacourse.kanban.task.TaskStatus;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();

        Task firstTask = new Task("Первая задача", "Описание первой задачи", taskManager.getNewId(),
                TaskStatus.NEW);
        Task secondTask = new Task("Вторая задача", "Описание второй задачи", taskManager.getNewId(),
                TaskStatus.NEW);

        taskManager.createTask(firstTask);
        taskManager.createTask(secondTask);

        Epic firstEpic = new Epic("Первый эпик", "Описание первого Эпика", taskManager.getNewId());
        taskManager.createEpic(firstEpic);

        SubTask firstSubTask = new SubTask("Первая подзадача", "Описание первой подзадачи",
                taskManager.getNewId(), TaskStatus.DONE, firstEpic.getId());
        SubTask secondSubTask = new SubTask("Вторая подзадача", "Описание второй подзадачи",
                taskManager.getNewId(), TaskStatus.IN_PROGRESS, firstEpic.getId());

        taskManager.createSubTask(firstSubTask);
        taskManager.createSubTask(secondSubTask);

        SubTask thirdSubTask = new SubTask("Третья подзадача", "Описание третий подзадачи первого эпика",
                taskManager.getNewId(), TaskStatus.NEW, firstEpic.getId());

        taskManager.createSubTask(thirdSubTask);

        Epic secondEpic = new Epic("Второй эпик", "Описание второго Эпика", taskManager.getNewId());
        taskManager.createEpic(secondEpic);

        taskManager.setHistoryManager(historyManager);

        historyManager.add(firstTask);
        historyManager.add(firstTask);
        historyManager.add(secondTask);
        historyManager.add(firstEpic);
        historyManager.add(firstTask);
        historyManager.add(secondTask);
        historyManager.add(firstTask);
        historyManager.add(firstEpic);
        historyManager.add(secondEpic);
        historyManager.add(firstEpic);

        System.out.println("История просмотров");
        System.out.println(historyManager.getHistory());
        System.out.println();

        taskManager.removeTaskById(firstTask.getId());
        taskManager.removeEpicById(firstEpic.getId());

        System.out.println();
        System.out.println("История просмотров");
        System.out.println(historyManager.getHistory());
        System.out.println();

        System.out.println(taskManager.getAllTaskList());
        System.out.println(taskManager.getAllEpicList());
        System.out.println(taskManager.getAllSubTaskList());
        System.out.println();

    }
}
