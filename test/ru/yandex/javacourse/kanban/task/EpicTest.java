package ru.yandex.javacourse.kanban.task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.javacourse.kanban.manager.InMemoryTaskManager;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.yandex.javacourse.kanban.StubsTest.FORMATTER;

public class EpicTest {
    public static InMemoryTaskManager inMemoryTaskManager;

    @BeforeEach
    void beforeEach() {
        inMemoryTaskManager = new InMemoryTaskManager();
    }

    @DisplayName("Проверка статуса эпика без подзадач")
    @Test
    void setStatus_StatusChange_NewEpic() {
        //given
        Epic newEpic = new Epic("Первый эпик", "Описание первого Эпика",
                inMemoryTaskManager.getNewId());

        //when
        assertEquals(TaskStatus.NEW, newEpic.getStatus(), "Статус созданного эпик должен быть NEW");
        newEpic.setStatus();

        //then
        assertEquals(TaskStatus.NEW, newEpic.getStatus(), "Эпик без подзадач должен быть NEW");
    }

    @DisplayName("Статус эпика если подзадача выполняется")
    @Test
    void setStatus_StatusChange_WhenSubTaskToProgress() {
        //given
        Epic newEpic = new Epic("Первый эпик", "Описание первого Эпика",
                inMemoryTaskManager.getNewId());
        SubTask newSubTask = new SubTask("Первая подзадача", "Описание второй подзадачи",
                inMemoryTaskManager.getNewId(), TaskStatus.IN_PROGRESS, newEpic.getId(), Duration.ofMinutes(30),
                LocalDateTime.of(2025, 11, 1, 1, 1));

        //when
        assertEquals(TaskStatus.NEW, newEpic.getStatus(), "Статус созданного эпик должен быть NEW");
        newEpic.addSubTaskToList(newSubTask);
        newEpic.setStatus();

        //then
        assertEquals(TaskStatus.IN_PROGRESS, newEpic.getStatus(),
                "Эпик c подзадачей IN_PROGRESS  должен быть IN_PROGRESS");
    }

    @DisplayName("Статус эпика если подзадача выполнена")
    @Test
    void setStatus_StatusChange_WhenSubTaskToDone() {
        //given
        Epic newEpic = new Epic("Первый эпик", "Описание первого Эпика",
                inMemoryTaskManager.getNewId());
        SubTask newSubTask = new SubTask("Первая подзадача", "Описание второй подзадачи",
                inMemoryTaskManager.getNewId(), TaskStatus.DONE, newEpic.getId(), Duration.ofMinutes(30),
                LocalDateTime.of(2025, 11, 1, 1, 1));

        //when
        assertEquals(TaskStatus.NEW, newEpic.getStatus(), "Статус созданного эпик должен быть NEW");
        newEpic.addSubTaskToList(newSubTask);
        newEpic.setStatus();

        //then
        assertEquals(TaskStatus.DONE, newEpic.getStatus(), "Эпик c подзадачей DONE  должен быть DONE");
    }

    @DisplayName("Статус эпика если удалить подзадачу")
    @Test
    void setStatus_StatusChange_WhenSubTaskRemove() {
        //given
        Epic newEpic = new Epic("Первый эпик", "Описание первого Эпика",
                inMemoryTaskManager.getNewId());
        SubTask newSubTask = new SubTask("Первая подзадача", "Описание второй подзадачи",
                inMemoryTaskManager.getNewId(), TaskStatus.DONE, newEpic.getId(), Duration.ofMinutes(30),
                LocalDateTime.of(2025, 11, 1, 1, 1));

        newEpic.addSubTaskToList(newSubTask);
        newEpic.setStatus();
        assertEquals(TaskStatus.DONE, newEpic.getStatus(), "Эпик c подзадачей DONE  должен быть DONE");

        //when
        newEpic.removeSubTaskToList(newSubTask);
        newEpic.setStatus();

        //then
        assertEquals(TaskStatus.NEW, newEpic.getStatus(), "Статус пустого эпика должен быть NEW");
    }

    @DisplayName("Длительность эпика если удалить подзадачу")
    @Test
    void setDuration_DurationChange_WhenRemoveSubTask() {
        //given
        Epic newEpic = new Epic("Первый эпик", "Описание первого Эпика",
                inMemoryTaskManager.getNewId());
        SubTask newSubTask = new SubTask("Первая подзадача", "Описание второй подзадачи",
                inMemoryTaskManager.getNewId(), TaskStatus.DONE, newEpic.getId(), Duration.ofMinutes(30),
                LocalDateTime.of(2025, 11, 1, 1, 1));
        SubTask newSubTask1 = new SubTask("Вторая подзадача", "Описание второй подзадачи",
                inMemoryTaskManager.getNewId(), TaskStatus.DONE, newEpic.getId(), Duration.ofMinutes(30),
                LocalDateTime.of(2025, 10, 1, 1, 1));

        //when
        newEpic.addSubTaskToList(newSubTask);
        newEpic.addSubTaskToList(newSubTask1);
        newEpic.setDuration();
        assertEquals(Duration.ofMinutes(60), newEpic.getDuration(), "Длительность эпика должен быть 60");

        newEpic.removeSubTaskToList(newSubTask1);
        newEpic.setDuration();

        //then
        assertEquals(Duration.ofMinutes(30), newEpic.getDuration(), "Длительность эпика должен быть 30");
    }

    @DisplayName("Длительность эпика если удалить все подзадачи")
    @Test
    void setDuration_DurationChange_WhenRemoveAllSubTask() {
        //given
        Epic newEpic = new Epic("Первый эпик", "Описание первого Эпика",
                inMemoryTaskManager.getNewId());
        SubTask newSubTask = new SubTask("Первая подзадача", "Описание второй подзадачи",
                inMemoryTaskManager.getNewId(), TaskStatus.DONE, newEpic.getId(), Duration.ofMinutes(30),
                LocalDateTime.of(2025, 11, 1, 1, 1));
        SubTask newSubTask1 = new SubTask("Вторая подзадача", "Описание второй подзадачи",
                inMemoryTaskManager.getNewId(), TaskStatus.DONE, newEpic.getId(), Duration.ofMinutes(30),
                LocalDateTime.of(2025, 10, 1, 1, 1));

        //when
        newEpic.addSubTaskToList(newSubTask);
        newEpic.addSubTaskToList(newSubTask1);
        newEpic.setDuration();
        assertEquals(Duration.ofMinutes(60), newEpic.getDuration(), "Длительность эпика должен быть 60");

        newEpic.clearSubTaskList();
        newEpic.setDuration();

        //then
        assertEquals(Duration.ofMinutes(0), newEpic.getDuration(), "Длительность эпика должен быть 0");
    }

    @DisplayName("Длительность эпика если у него есть подзадачи")
    @Test
    void setDuration_DurationChange_WhenSubTaskExist() {
        //given
        Epic newEpic = new Epic("Первый эпик", "Описание первого Эпика",
                inMemoryTaskManager.getNewId());
        SubTask newSubTask = new SubTask("Первая подзадача", "Описание второй подзадачи",
                inMemoryTaskManager.getNewId(), TaskStatus.DONE, newEpic.getId(), Duration.ofMinutes(30),
                LocalDateTime.of(2025, 11, 1, 1, 1));
        SubTask newSubTask1 = new SubTask("Вторая подзадача", "Описание второй подзадачи",
                inMemoryTaskManager.getNewId(), TaskStatus.DONE, newEpic.getId(), Duration.ofMinutes(30),
                LocalDateTime.of(2025, 10, 1, 1, 1));

        //when
        newEpic.addSubTaskToList(newSubTask);
        newEpic.addSubTaskToList(newSubTask1);
        newEpic.setDuration();

        //then
        assertEquals(Duration.ofMinutes(60), newEpic.getDuration(), "Длительность пустого эпика должен быть 0");
    }

    @DisplayName("Длительность эпика если у него есть подзадачи")
    @Test
    void setDuration_DurationChange_WhenSubTaskNotExist() {
        //given
        Epic newEpic = new Epic("Первый эпик", "Описание первого Эпика",
                inMemoryTaskManager.getNewId());

        //when
        newEpic.setDuration();

        //then
        assertEquals(Duration.ofMinutes(0), newEpic.getDuration(), "Длительность пустого эпика должен быть 0");
    }

    @DisplayName("Назначение границ начала и конца эпика если у него нет подзадач")
    @Test
    void setStartEndTime_StartEndChanges_WhenSubTaskNotExist() {
        //given
        Epic newEpic = new Epic("Первый эпик", "Описание первого Эпика",
                inMemoryTaskManager.getNewId());

        //when
        newEpic.setStartTime();
        newEpic.setEndTime();

        //then
        assertEquals(LocalDateTime.now().format(FORMATTER), newEpic.getStartTime().format(FORMATTER),
                "Начало пустого эпика должен быть равно значению актуальной даты");
        assertEquals(LocalDateTime.now().format(FORMATTER), newEpic.getEndTime().format(FORMATTER),
                "Конец пустого эпика должен быть равен значению актуальной даты");
    }

    @DisplayName("Назначение границ начала и конца эпика если у него есть подзадачи")
    @Test
    void setStartEndTime_StartEndChanges_WhenSubTasksExist() {
        //given
        Epic newEpic = new Epic("Первый эпик", "Описание первого Эпика",
                inMemoryTaskManager.getNewId());
        SubTask newSubTask = new SubTask("Первая подзадача", "Описание второй подзадачи",
                inMemoryTaskManager.getNewId(), TaskStatus.DONE, newEpic.getId(), Duration.ofMinutes(30),
                LocalDateTime.of(2025, 11, 1, 1, 1));
        SubTask newSubTask1 = new SubTask("Вторая подзадача", "Описание второй подзадачи",
                inMemoryTaskManager.getNewId(), TaskStatus.DONE, newEpic.getId(), Duration.ofMinutes(30),
                LocalDateTime.of(2025, 10, 1, 1, 1));

        //when
        newEpic.addSubTaskToList(newSubTask);
        newEpic.addSubTaskToList(newSubTask1);
        newEpic.setStartTime();
        newEpic.setEndTime();

        //then
        assertEquals(LocalDateTime.of(2025, 10, 1, 1, 1).format(FORMATTER),
                newEpic.getStartTime().format(FORMATTER),
                "Начало эпика должен быть равно значению самой ранней подзадачи");
        assertEquals(LocalDateTime.of(2025, 11, 1, 1, 31).format(FORMATTER),
                newEpic.getEndTime().format(FORMATTER),
                "Конец эпика должен быть равен значению самой поздней подзадачи");
    }

    @DisplayName("Назначение границ начала и конца эпика если удалить подзадачу")
    @Test
    void setStartEndTime_StartEndChanges_WhenSubTaskRemove() {
        //given
        Epic newEpic = new Epic("Первый эпик", "Описание первого Эпика",
                inMemoryTaskManager.getNewId());
        SubTask newSubTask = new SubTask("Первая подзадача", "Описание второй подзадачи",
                inMemoryTaskManager.getNewId(), TaskStatus.DONE, newEpic.getId(), Duration.ofMinutes(30),
                LocalDateTime.of(2025, 11, 1, 1, 1));
        SubTask newSubTask1 = new SubTask("Вторая подзадача", "Описание второй подзадачи",
                inMemoryTaskManager.getNewId(), TaskStatus.DONE, newEpic.getId(), Duration.ofMinutes(30),
                LocalDateTime.of(2025, 10, 1, 1, 1));
        SubTask newSubTask2 = new SubTask("Вторая подзадача", "Описание второй подзадачи",
                inMemoryTaskManager.getNewId(), TaskStatus.DONE, newEpic.getId(), Duration.ofMinutes(30),
                LocalDateTime.of(2025, 9, 1, 1, 1));

        //when
        newEpic.addSubTaskToList(newSubTask);
        newEpic.addSubTaskToList(newSubTask1);
        newEpic.addSubTaskToList(newSubTask2);
        newEpic.setStartTime();
        newEpic.setEndTime();

        assertEquals(LocalDateTime.of(2025, 9, 1, 1, 1).format(FORMATTER),
                newEpic.getStartTime().format(FORMATTER),
                "Начало эпика должен быть равно значению самой ранней подзадачи");
        assertEquals(LocalDateTime.of(2025, 11, 1, 1, 31).format(FORMATTER),
                newEpic.getEndTime().format(FORMATTER),
                "Конец эпика должен быть равен значению самой поздней подзадачи");

        newEpic.removeSubTaskToList(newSubTask);
        newEpic.setStartTime();
        newEpic.setEndTime();

        //then
        assertEquals(LocalDateTime.of(2025, 9, 1, 1, 1).format(FORMATTER),
                newEpic.getStartTime().format(FORMATTER),
                "Начало эпика должен быть равно значению самой ранней подзадачи");
        assertEquals(LocalDateTime.of(2025, 10, 1, 1, 31).format(FORMATTER),
                newEpic.getEndTime().format(FORMATTER),
                "Конец эпика должен быть равен значению самой поздней подзадачи");
    }

    @DisplayName("Назначение границ начала и конца эпика если у эпика 1 подзадача")
    @Test
    void setStartEndTime_StartEndChanges_WhenHasOneSubTask() {
        //given
        Epic newEpic = new Epic("Первый эпик", "Описание первого Эпика",
                inMemoryTaskManager.getNewId());
        SubTask newSubTask = new SubTask("Первая подзадача", "Описание второй подзадачи",
                inMemoryTaskManager.getNewId(), TaskStatus.DONE, newEpic.getId(), Duration.ofMinutes(30),
                LocalDateTime.of(2025, 11, 1, 1, 1));
        SubTask newSubTask1 = new SubTask("Вторая подзадача", "Описание второй подзадачи",
                inMemoryTaskManager.getNewId(), TaskStatus.DONE, newEpic.getId(), Duration.ofMinutes(30),
                LocalDateTime.of(2025, 10, 1, 1, 1));

        //when
        newEpic.addSubTaskToList(newSubTask);
        newEpic.addSubTaskToList(newSubTask1);
        newEpic.setStartTime();
        newEpic.setEndTime();

        assertEquals(LocalDateTime.of(2025, 10, 1, 1, 1).format(FORMATTER),
                newEpic.getStartTime().format(FORMATTER),
                "Начало эпика должен быть равно значению самой ранней подзадачи");
        assertEquals(LocalDateTime.of(2025, 11, 1, 1, 31).format(FORMATTER),
                newEpic.getEndTime().format(FORMATTER),
                "Конец эпика должен быть равен значению самой поздней подзадачи");

        newEpic.removeSubTaskToList(newSubTask);
        newEpic.setStartTime();
        newEpic.setEndTime();

        //then
        assertEquals(LocalDateTime.of(2025, 10, 1, 1, 1).format(FORMATTER),
                newEpic.getStartTime().format(FORMATTER),
                "Начало эпика должен быть равно значению самой ранней подзадачи");
        assertEquals(LocalDateTime.of(2025, 10, 1, 1, 31).format(FORMATTER),
                newEpic.getEndTime().format(FORMATTER),
                "Конец эпика должен быть равен значению самой поздней подзадачи");


    }

}