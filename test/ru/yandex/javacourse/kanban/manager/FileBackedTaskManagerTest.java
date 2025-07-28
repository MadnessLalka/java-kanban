package ru.yandex.javacourse.kanban.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.javacourse.kanban.task.Epic;
import ru.yandex.javacourse.kanban.task.SubTask;
import ru.yandex.javacourse.kanban.task.Task;
import ru.yandex.javacourse.kanban.task.TaskStatus;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTaskManagerTest {
    public static FileBackedTaskManager fileBackedTaskManager;
    public static InMemoryTaskManager inMemoryTaskManager;

    @BeforeEach
    void beforeEach() throws IOException {
        fileBackedTaskManager = new FileBackedTaskManager();
        inMemoryTaskManager = new InMemoryTaskManager();
    }

    @DisplayName("Проверка вывода задачи из объекта в текстовое поле")
    @Test
    void get_Get_StringLineFromTaskObject() throws InvalidTaskTypeException {
        //given
        Task newTask = new Task("Первая задача", "Описание первой задачи",
                inMemoryTaskManager.getNewId(),
                TaskStatus.NEW);

        //when
        String taskToString = fileBackedTaskManager.toString(newTask);

        //then
        assertEquals(("0,TASK,Первая задача,NEW,Описание первой задачи"), taskToString,
                "Текстовое описание должно быть эквивалентно");
    }

    @DisplayName("Проверка вывода эпика из объекта в текстовое поле")
    @Test
    void get_Get_StringLineFromEpicObject() throws InvalidTaskTypeException {
        //given
        Epic newEpic = new Epic("Первый эпик", "Описание первого Эпика",
                inMemoryTaskManager.getNewId());

        //when
        String epicToString = fileBackedTaskManager.toString(newEpic);

        //then
        assertEquals(("0,EPIC,Первый эпик,NEW,Описание первого Эпика"), epicToString,
                "Текстовое описание должно быть эквивалентно");
    }

    @DisplayName("Проверка вывода подзадачи из объекта в текстовое поле")
    @Test
    void get_Get_StringLineFromSubTaskObject() throws InvalidTaskTypeException {
        //given
        Epic newEpic = new Epic("Первый эпик", "Описание первого Эпика",
                inMemoryTaskManager.getNewId());
        SubTask newSubTask = new SubTask("Вторая подзадача", "Описание второй подзадачи",
                inMemoryTaskManager.getNewId(), TaskStatus.IN_PROGRESS, newEpic.getId());

        //when
        String subTaskToString = fileBackedTaskManager.toString(newSubTask);

        //then
        assertEquals(("1,SUBTASK,Вторая подзадача,IN_PROGRESS,Описание второй подзадачи,0"), subTaskToString,
                "Текстовое описание должно быть эквивалентно");
    }

    @DisplayName("Проверка получения задачи из текстового поле")
    @Test
    void get_Get_TaskFromStringLine() throws InvalidTaskTypeException {
        //given
        Task newTask = new Task("Первая задача", "Описание первой задачи",
                inMemoryTaskManager.getNewId(),
                TaskStatus.NEW);

        //when
        String taskToString = fileBackedTaskManager.toString(newTask);
        Task restoredTask = fileBackedTaskManager.fromString(taskToString);

        //then
        assertEquals(restoredTask, newTask, "Задачи должны быть эквиваленты");
    }

    @DisplayName("Проверка получения эпика из текстового поле")
    @Test
    void get_Get_EpicFromStringLine() throws InvalidTaskTypeException {
        //given
        Epic newEpic = new Epic("Первый эпик", "Описание первого Эпика",
                inMemoryTaskManager.getNewId());

        //when
        String epicToString = fileBackedTaskManager.toString(newEpic);
        Epic restoredEpic = (Epic) fileBackedTaskManager.fromString(epicToString);

        //then
        assertEquals(restoredEpic, newEpic, "Эпики должны быть эквиваленты");
    }

    @DisplayName("Проверка получения подзадачи из текстового поле")
    @Test
    void get_Get_SubTaskFromStringLine() throws InvalidTaskTypeException {
        //given
        Epic newEpic = new Epic("Первый эпик", "Описание первого Эпика",
                inMemoryTaskManager.getNewId());
        SubTask newSubTask = new SubTask("Вторая подзадача", "Описание второй подзадачи",
                inMemoryTaskManager.getNewId(), TaskStatus.IN_PROGRESS, newEpic.getId());

        //when
        String subTaskToString = fileBackedTaskManager.toString(newSubTask);
        SubTask restoredSubTask = (SubTask) fileBackedTaskManager.fromString(subTaskToString);

        //then
        assertEquals(restoredSubTask, newSubTask, "Подзадачи должны быть эквиваленты");
    }
}
