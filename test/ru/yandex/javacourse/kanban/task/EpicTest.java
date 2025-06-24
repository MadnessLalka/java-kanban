package ru.yandex.javacourse.kanban.task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.javacourse.kanban.manager.InMemoryTaskManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EpicTest {
    public static InMemoryTaskManager inMemoryTaskManager;

    @BeforeEach
    void beforeEach(){
        inMemoryTaskManager = new InMemoryTaskManager();
    }

    @DisplayName("Проверка статуса эпика без подзадач")
    @Test
    void setStatus_StatusChange_NewEpic(){
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
    void setStatus_StatusChange_WhenSubTaskToProgress(){
        //given
        Epic newEpic = new Epic("Первый эпик", "Описание первого Эпика",
                inMemoryTaskManager.getNewId());
        SubTask newSubTask = new SubTask("Первая подзадача", "Описание второй подзадачи",
                inMemoryTaskManager.getNewId(), TaskStatus.IN_PROGRESS, newEpic.getId());

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
    void setStatus_StatusChange_WhenSubTaskToDone(){
        //given
        Epic newEpic = new Epic("Первый эпик", "Описание первого Эпика",
                inMemoryTaskManager.getNewId());
        SubTask newSubTask = new SubTask("Первая подзадача", "Описание второй подзадачи",
                inMemoryTaskManager.getNewId(), TaskStatus.DONE, newEpic.getId());

        //when
        assertEquals(TaskStatus.NEW, newEpic.getStatus(), "Статус созданного эпик должен быть NEW");
        newEpic.addSubTaskToList(newSubTask);
        newEpic.setStatus();

        //then
        assertEquals(TaskStatus.DONE, newEpic.getStatus(), "Эпик c подзадачей DONE  должен быть DONE");
    }

    @DisplayName("Статус эпика если удалить подзадачу")
    @Test
    void setStatus_StatusChange_WhenSubTaskRemove(){
        //given
        Epic newEpic = new Epic("Первый эпик", "Описание первого Эпика",
                inMemoryTaskManager.getNewId());
        SubTask newSubTask = new SubTask("Первая подзадача", "Описание второй подзадачи",
                inMemoryTaskManager.getNewId(), TaskStatus.DONE, newEpic.getId());

        newEpic.addSubTaskToList(newSubTask);
        newEpic.setStatus();
        assertEquals(TaskStatus.DONE, newEpic.getStatus(), "Эпик c подзадачей DONE  должен быть DONE");

        //when
        newEpic.removeSubTaskToList(newSubTask);
        newEpic.setStatus();

        //then
        assertEquals(TaskStatus.NEW, newEpic.getStatus(), "Статус пустого эпика должен быть NEW");
    }
}