package ru.yandex.javacourse.kanban.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.javacourse.kanban.task.Epic;
import ru.yandex.javacourse.kanban.task.Task;
import ru.yandex.javacourse.kanban.task.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryHistoryManagerTest {

    public static InMemoryHistoryManager inMemoryHistoryManager;
    public static InMemoryTaskManager inMemoryTaskManager;

    @BeforeEach
    void beforeEach() {
        inMemoryTaskManager = new InMemoryTaskManager();
        inMemoryHistoryManager = new InMemoryHistoryManager();
    }

    @DisplayName("Проверка пустого списка просмотров")
    @Test
    void getHistory_isEmpty_EmptyHistoryList() {
        //given
        boolean isHistoryEmpty = inMemoryHistoryManager.getHistory().isEmpty();

        //then
        assertTrue(isHistoryEmpty);
    }

    @DisplayName("Добавление заявки")
    @Test
    void add_Add_TaskToHistoryList() {
        //given
        int realHistorySize = 1;
        Task newTask = new Task("Первая задача", "Описание первой задачи",
                inMemoryTaskManager.getNewId(),
                TaskStatus.NEW,
                Duration.of(30, ChronoUnit.MINUTES),
                LocalDateTime.of(2025, 12, 1, 1, 1));

        //when
        inMemoryHistoryManager.add(newTask);

        //then
        assertEquals(realHistorySize, inMemoryHistoryManager.getHistory().size());
    }

    @DisplayName("Добавление эпика")
    @Test
    void add_Add_EpicToHistoryList() {
        //given
        int realHistorySize = 1;
        Epic newEpic = new Epic("Первый эпик", "Описание первого Эпика", inMemoryTaskManager.getNewId());

        //when
        inMemoryHistoryManager.add(newEpic);

        //then
        assertEquals(realHistorySize, inMemoryHistoryManager.getHistory().size());
    }

    @DisplayName("Добавление разных типов задач")
    @Test
    void add_Add_EpicAndTaskToHistory() {
        //given
        int realSizeHistory = 2;
        Task newTask = new Task("Первая задача", "Описание первой задачи",
                inMemoryTaskManager.getNewId(),
                TaskStatus.NEW,
                Duration.of(30, ChronoUnit.MINUTES),
                LocalDateTime.of(2025, 12, 1, 1, 1));

        Epic newEpic = new Epic("Первый эпик", "Описание первого Эпика", inMemoryTaskManager.getNewId());

        //when
        inMemoryHistoryManager.add(newTask);
        inMemoryHistoryManager.add(newEpic);

        //then
        assertEquals(realSizeHistory, inMemoryHistoryManager.getHistory().size());
    }

    @DisplayName("Проверка очерёдности задач при удаление дубликатов")
    @Test
    void add_isCorrect_CorrectQueueTasks() {
        //given
        int realSizeHistory = 3;
        Task newTask = new Task("Первая задача", "Описание первой задачи",
                inMemoryTaskManager.getNewId(),
                TaskStatus.NEW,
                Duration.of(30, ChronoUnit.MINUTES),
                LocalDateTime.of(2025, 12, 1, 1, 1));

        Epic newEpic = new Epic("Первый эпик", "Описание первого Эпика", inMemoryTaskManager.getNewId());
        Task newTask2 = new Task("Вторая задача", "Описание второй задачи",
                inMemoryTaskManager.getNewId(),
                TaskStatus.NEW,
                Duration.of(30, ChronoUnit.MINUTES),
                LocalDateTime.of(2025, 12, 1, 1, 1));

        //when
        inMemoryHistoryManager.add(newTask);
        inMemoryHistoryManager.add(newEpic);
        inMemoryHistoryManager.add(newTask2);
        inMemoryHistoryManager.add(newTask);
        inMemoryHistoryManager.add(newEpic);
        inMemoryHistoryManager.add(newTask);


        //then
        assertEquals(realSizeHistory, inMemoryHistoryManager.getHistory().size());
        assertSame(inMemoryHistoryManager.getHistory().getFirst(), newTask2);
        assertSame(inMemoryHistoryManager.getHistory().get(1), newEpic);
        assertSame(inMemoryHistoryManager.getHistory().getLast(), newTask);
    }
}
