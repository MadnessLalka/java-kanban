package ru.yandex.javacourse.kanban.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.javacourse.kanban.task.Epic;
import ru.yandex.javacourse.kanban.task.SubTask;
import ru.yandex.javacourse.kanban.task.Task;
import ru.yandex.javacourse.kanban.task.TaskStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryTaskManagerTest {
    public static InMemoryTaskManager inMemoryTaskManager;
    public static HistoryManager historyManager;

    @BeforeEach
    void beforeEach() {
        inMemoryTaskManager = new InMemoryTaskManager();
        historyManager = Managers.getDefaultHistory();
    }

    @DisplayName("Получение нового ID")
    @Test
    void getNewId_Create_Id() {
        //given
        int expectedId = 1;

        //when
        inMemoryTaskManager.getNewId();
        int currentId = inMemoryTaskManager.getIdCounter();

        //then
        assertEquals(expectedId, currentId);
    }

    @DisplayName("Генерация двух ID")
    @Test
    void getNewId_Create_SecondId() {
        //given
        int expectedId = 2;

        //when
        inMemoryTaskManager.getNewId();
        inMemoryTaskManager.getNewId();
        int currentId = inMemoryTaskManager.getIdCounter();

        //then
        assertEquals(expectedId, currentId);
    }

    @DisplayName("Добавление задачи")
    @Test
    void createTask_Add_NewTask() {
        //given
        int realCountTasks = 1;
        Task newTask = new Task("Первая задача", "Описание первой задачи",
                inMemoryTaskManager.getNewId(),
                TaskStatus.NEW);

        //when
        inMemoryTaskManager.createTask(newTask);

        //then
        assertEquals(realCountTasks, inMemoryTaskManager.getAllTaskList().size(), "Количество задача равно 1");
    }

//

    @DisplayName("Добавление эпика")
    @Test
    void createEpic_Add_NewEpic() {
        //given
        int realCountEpic = 1;
        Epic newEpic = new Epic("Первый эпик", "Описание первого Эпика", inMemoryTaskManager.getNewId());

        //when
        inMemoryTaskManager.createEpic(newEpic);

        //then
        assertEquals(realCountEpic, inMemoryTaskManager.getAllEpicList().size(), "Количество задач равно 1");
    }

    @DisplayName("Добавление подзадачи")
    @Test
    void createSubTask_Add_NewSubTask() {
        //given
        int realCountSubTask = 1;

        Epic newEpic = new Epic("Первый эпик", "Описание первого Эпика", inMemoryTaskManager.getNewId());
        SubTask newSubTask = new SubTask("Вторая подзадача", "Описание второй подзадачи",
                inMemoryTaskManager.getNewId(), TaskStatus.IN_PROGRESS, newEpic.getId());

        //when
        inMemoryTaskManager.createEpic(newEpic);
        inMemoryTaskManager.createSubTask(newSubTask);

        //then
        assertEquals(realCountSubTask, inMemoryTaskManager.getAllSubTaskList().size(), "Количество задача равно 1");
    }

    @DisplayName("Удаление всех задач")
    @Test
    void removeAllTask_Clear_AllTasks() {
        //given
        int countTasks = 3;
        int countTaskAfterRemove = 0;

        Task newTask1 = new Task("Первая задача", "Описание первой задачи",
                inMemoryTaskManager.getNewId(),
                TaskStatus.NEW);
        Task newTask2 = new Task("Вторя задача", "Описание первой задачи",
                inMemoryTaskManager.getNewId(),
                TaskStatus.NEW);
        Task newTask3 = new Task("Третья задача", "Описание первой задачи",
                inMemoryTaskManager.getNewId(),
                TaskStatus.NEW);

        //when
        inMemoryTaskManager.createTask(newTask1);
        inMemoryTaskManager.createTask(newTask2);
        inMemoryTaskManager.createTask(newTask3);

        //then
        assertEquals(countTasks, inMemoryTaskManager.getAllTaskList().size(), "Было добавлено 3 задачи");
        inMemoryTaskManager.removeAllTask();
        assertEquals(countTaskAfterRemove, inMemoryTaskManager.getAllTaskList().size(),
                "Все задачи должны быть удалены");
    }

    @DisplayName("Удаление всех эпиков")
    @Test
    void removeAllEpic_Clear_AllEpics() {
        //given
        int countEpics = 3;
        int countEpicsAfterRemove = 0;

        Epic newEpic1 = new Epic("Первый эпик", "Описание первого Эпика", inMemoryTaskManager.getNewId());
        Epic newEpic2 = new Epic("Второй эпик", "Описание первого Эпика", inMemoryTaskManager.getNewId());
        Epic newEpic3 = new Epic("Третий эпик", "Описание первого Эпика", inMemoryTaskManager.getNewId());

        //when
        inMemoryTaskManager.createEpic(newEpic1);
        inMemoryTaskManager.createEpic(newEpic2);
        inMemoryTaskManager.createEpic(newEpic3);

        //then
        assertEquals(countEpics, inMemoryTaskManager.getAllEpicList().size(), "Было добавлено 3 эпика");
        inMemoryTaskManager.removeAllEpic();
        assertEquals(countEpicsAfterRemove, inMemoryTaskManager.getAllEpicList().size(),
                "Все эпики должны быть удалены");
    }

    @DisplayName("Удаление всех подзадач")
    @Test
    void removeAllSubTask_Clear_AllSubTasks() {
        //given
        int countSubTasks = 3;
        int countEpics = 1;
        int countSuBTasksAfterRemove = 0;

        Epic newEpic1 = new Epic("Первый эпик", "Описание первого Эпика",
                inMemoryTaskManager.getNewId());
        SubTask newSubTask1 = new SubTask("Первая подзадача", "Описание первой подзадачи",
                inMemoryTaskManager.getNewId(), TaskStatus.DONE, newEpic1.getId());
        SubTask newSubTask2 = new SubTask("Вторая подзадача", "Описание первой подзадачи",
                inMemoryTaskManager.getNewId(), TaskStatus.DONE, newEpic1.getId());
        SubTask newSubTask3 = new SubTask("Третья подзадача", "Описание первой подзадачи",
                inMemoryTaskManager.getNewId(), TaskStatus.DONE, newEpic1.getId());

        //when
        inMemoryTaskManager.createEpic(newEpic1);
        inMemoryTaskManager.createSubTask(newSubTask1);
        inMemoryTaskManager.createSubTask(newSubTask2);
        inMemoryTaskManager.createSubTask(newSubTask3);

        //then
        assertEquals(countSubTasks, inMemoryTaskManager.getAllSubTaskList().size(),
                "Было добавлено 3 подзадачи");
        inMemoryTaskManager.removeAllSubTask();
        assertEquals(countSuBTasksAfterRemove, inMemoryTaskManager.getAllSubTaskList().size(),
                "Все подзадачи должны быть удалены");
        assertEquals(countEpics, inMemoryTaskManager.getAllEpicList().size(),
                "Эпик не должен быть удалён если удалить его подзадачи");
    }

    @DisplayName("Удаление подзадач если удалить эпик")
    @Test
    void removeEpicById_Remove_AllSubTaskByEpic(){
        //given
        int countSubTasks = 3;
        int countEpics = 1;
        int countSuBTasksAfterRemove = 0;
        int countEpicsAfterRemove = 0;

        Epic newEpic1 = new Epic("Первый эпик", "Описание первого Эпика",
                inMemoryTaskManager.getNewId());
        SubTask newSubTask1 = new SubTask("Первая подзадача", "Описание первой подзадачи",
                inMemoryTaskManager.getNewId(), TaskStatus.DONE, newEpic1.getId());
        SubTask newSubTask2 = new SubTask("Вторая подзадача", "Описание первой подзадачи",
                inMemoryTaskManager.getNewId(), TaskStatus.DONE, newEpic1.getId());
        SubTask newSubTask3 = new SubTask("Третья подзадача", "Описание первой подзадачи",
                inMemoryTaskManager.getNewId(), TaskStatus.DONE, newEpic1.getId());

        //when
        inMemoryTaskManager.createEpic(newEpic1);
        inMemoryTaskManager.createSubTask(newSubTask1);
        inMemoryTaskManager.createSubTask(newSubTask2);
        inMemoryTaskManager.createSubTask(newSubTask3);
        assertEquals(countSubTasks, inMemoryTaskManager.getAllSubTaskList().size(),
                "Было добавлено 3 подзадачи");
        assertEquals(countEpics, inMemoryTaskManager.getAllEpicList().size(), "Было добавлен 1 эпик");
        inMemoryTaskManager.removeEpicById(newEpic1.getId());

        //then
        assertEquals(countSuBTasksAfterRemove, inMemoryTaskManager.getAllSubTaskList().size(),
                "Все подзадачи должны быть удалены");
        assertEquals(countEpicsAfterRemove, inMemoryTaskManager.getAllEpicList().size(),
                "Эпик должен был быть удалён");
    }

    @DisplayName("Удаление задачи по id")
    @Test
    void removeTaskById_Remove_TaskById(){
        //given
        int countTasks = 3;
        int countTaskAfterRemove = 2;

        Task newTask1 = new Task("Первая задача", "Описание первой задачи",
                inMemoryTaskManager.getNewId(),
                TaskStatus.NEW);
        Task newTask2 = new Task("Вторя задача", "Описание первой задачи",
                inMemoryTaskManager.getNewId(),
                TaskStatus.NEW);
        Task newTask3 = new Task("Третья задача", "Описание первой задачи",
                inMemoryTaskManager.getNewId(),
                TaskStatus.NEW);

        //when
        inMemoryTaskManager.createTask(newTask1);
        inMemoryTaskManager.createTask(newTask2);
        inMemoryTaskManager.createTask(newTask3);
        assertEquals(countTasks, inMemoryTaskManager.getAllTaskList().size(), "Было добавлено 3 задачи");
        inMemoryTaskManager.removeTaskById(newTask2.getId());

        //then
        assertEquals(countTaskAfterRemove, inMemoryTaskManager.getAllTaskList().size(), "Должно быть 2 задачи");
    }

    @DisplayName("Удаление подзадачи по id")
    @Test
    void removeSubTaskById_Remove_SubTaskById(){
        //given
        int countSubTasks = 3;
        int countEpics = 1;
        int countSuBTasksAfterRemove = 2;

        Epic newEpic1 = new Epic("Первый эпик", "Описание первого Эпика",
                inMemoryTaskManager.getNewId());
        SubTask newSubTask1 = new SubTask("Первая подзадача", "Описание первой подзадачи",
                inMemoryTaskManager.getNewId(), TaskStatus.DONE, newEpic1.getId());
        SubTask newSubTask2 = new SubTask("Вторая подзадача", "Описание первой подзадачи",
                inMemoryTaskManager.getNewId(), TaskStatus.DONE, newEpic1.getId());
        SubTask newSubTask3 = new SubTask("Третья подзадача", "Описание первой подзадачи",
                inMemoryTaskManager.getNewId(), TaskStatus.DONE, newEpic1.getId());

        //when
        inMemoryTaskManager.createEpic(newEpic1);
        inMemoryTaskManager.createSubTask(newSubTask1);
        inMemoryTaskManager.createSubTask(newSubTask2);
        inMemoryTaskManager.createSubTask(newSubTask3);

        assertEquals(countSubTasks, inMemoryTaskManager.getAllSubTaskList().size(),
                "Было добавлено 3 подзадачи");
        assertEquals(countEpics, inMemoryTaskManager.getAllEpicList().size(), "Было добавлен 1 эпик");
        inMemoryTaskManager.removeSubTaskById(newSubTask2.getId());

        //then
        assertEquals(countSuBTasksAfterRemove, inMemoryTaskManager.getAllSubTaskList().size(), "Должно быть 2 подзадачи");
    }

    @DisplayName("Проверка синхронного удаление всех задач из истории")
    @Test
    void removeAllTask_Clear_AllTasksFromHistory() {
        //given
        int countTasks = 3;
        int countTaskAfterRemove = 0;

        Task newTask1 = new Task("Первая задача", "Описание первой задачи",
                inMemoryTaskManager.getNewId(),
                TaskStatus.NEW);
        Task newTask2 = new Task("Вторя задача", "Описание первой задачи",
                inMemoryTaskManager.getNewId(),
                TaskStatus.NEW);
        Task newTask3 = new Task("Третья задача", "Описание первой задачи",
                inMemoryTaskManager.getNewId(),
                TaskStatus.NEW);

        //when
        inMemoryTaskManager.createTask(newTask1);
        inMemoryTaskManager.createTask(newTask2);
        inMemoryTaskManager.createTask(newTask3);
        historyManager.add(newTask1);
        historyManager.add(newTask2);
        historyManager.add(newTask3);

        assertEquals(countTasks, inMemoryTaskManager.getAllTaskList().size(), "Было добавлено 3 задачи");

        inMemoryTaskManager.setHistoryManager(historyManager);
        assertEquals(countTasks, historyManager.getHistory().size(), "Было добавлено 3 задачи в историю");

        //then
        inMemoryTaskManager.removeAllTask();
        assertEquals(countTaskAfterRemove, inMemoryTaskManager.getAllTaskList().size(),
                "Все задачи должны быть удалены");
        assertEquals(countTaskAfterRemove, historyManager.getHistory().size(),
                "Все задачи должны быть удалены из истории");
    }

    @DisplayName("Проверка синхронного удаление всех эпиков из истории")
    @Test
    void removeAllEpic_Clear_AllEpicsFromHistory() {
        //given
        int countTasks = 3;
        int countTaskAfterRemove = 0;

        Epic newEpic1 = new Epic("Первый эпик", "Описание первого Эпика",
                inMemoryTaskManager.getNewId());
        Epic newEpic2 = new Epic("Первый эпик", "Описание первого Эпика",
                inMemoryTaskManager.getNewId());
        Epic newEpic3 = new Epic("Первый эпик", "Описание первого Эпика",
                inMemoryTaskManager.getNewId());

        //when
        inMemoryTaskManager.createEpic(newEpic1);
        inMemoryTaskManager.createEpic(newEpic2);
        inMemoryTaskManager.createEpic(newEpic3);
        historyManager.add(newEpic1);
        historyManager.add(newEpic2);
        historyManager.add(newEpic3);

        assertEquals(countTasks, inMemoryTaskManager.getAllEpicList().size(), "Было добавлено 3 эпика");

        inMemoryTaskManager.setHistoryManager(historyManager);
        assertEquals(countTasks, historyManager.getHistory().size(), "Было добавлено 3 эпика в историю");

        //then
        inMemoryTaskManager.removeAllEpic();
        assertEquals(countTaskAfterRemove, inMemoryTaskManager.getAllTaskList().size(),
                "Все эпики должны быть удалены");
        assertEquals(countTaskAfterRemove, historyManager.getHistory().size(),
                "Все эпики должны быть удалены из истории");
    }

    @DisplayName("Проверка синхронного удаление всех эпиков из истории")
    @Test
    void removeAllSubTask_Clear_AllSubTasksFromHistory() {
        //given
        int countTasks = 3;
        int countTaskAfterRemove = 0;

        Epic newEpic1 = new Epic("Первый эпик", "Описание первого Эпика",
                inMemoryTaskManager.getNewId());
        SubTask newSubTask1 = new SubTask("Первая подзадача", "Описание первой подзадачи",
                inMemoryTaskManager.getNewId(), TaskStatus.DONE, newEpic1.getId());
        SubTask newSubTask2 = new SubTask("Первая подзадача", "Описание первой подзадачи",
                inMemoryTaskManager.getNewId(), TaskStatus.DONE, newEpic1.getId());
        SubTask newSubTask3 = new SubTask("Первая подзадача", "Описание первой подзадачи",
                inMemoryTaskManager.getNewId(), TaskStatus.DONE, newEpic1.getId());

        //when
        inMemoryTaskManager.createEpic(newEpic1);
        inMemoryTaskManager.createSubTask(newSubTask1);
        inMemoryTaskManager.createSubTask(newSubTask2);
        inMemoryTaskManager.createSubTask(newSubTask3);
        historyManager.add(newSubTask1);
        historyManager.add(newSubTask2);
        historyManager.add(newSubTask3);

        assertEquals(countTasks, inMemoryTaskManager.getAllSubTaskList().size(), "Было добавлено 3 подзадачи");

        inMemoryTaskManager.setHistoryManager(historyManager);
        assertEquals(countTasks, historyManager.getHistory().size(), "Было добавлено 3 подзадачи в историю");

        //then
        inMemoryTaskManager.removeAllSubTask();
        assertEquals(countTaskAfterRemove, inMemoryTaskManager.getAllSubTaskList().size(),
                "Все подзадачи должны быть удалены");
        assertEquals(countTaskAfterRemove, historyManager.getHistory().size(),
                "Все подзадачи должны быть удалены из истории");
    }
}


