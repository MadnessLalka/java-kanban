package ru.yandex.javacourse.kanban;

import org.junit.jupiter.api.*;
import ru.yandex.javacourse.kanban.manager.*;
import ru.yandex.javacourse.kanban.task.Epic;
import ru.yandex.javacourse.kanban.task.SubTask;
import ru.yandex.javacourse.kanban.task.Task;
import ru.yandex.javacourse.kanban.task.TaskStatus;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

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


    @DisplayName("Добавление разных типов задач в историю")
    @Test
    void historyManager_Saved_DifferentTypeItem() {
        //given
        Task task = taskManager.getTaskById(0);
        SubTask subTask = taskManager.getSubTaskById(2);

        //when
        historyManager.add(task);
        historyManager.add(subTask);

        //then
        assertEquals(2, historyManager.getHistory().size(), "Больше чем 2 :" +
                historyManager.getHistory().size());
    }


    @DisplayName("Проверка размерности истории просмотров")
    @Test
    void historyManager_Saved_TenItems() {
        //given
        Task firstTask = taskManager.getTaskById(0);
        Task secondTask = taskManager.getTaskById(4);
        SubTask firstSubTask = taskManager.getSubTaskById(2);

        //when
        historyManager.add(firstTask);
        historyManager.add(firstSubTask);
        historyManager.add(firstSubTask);
        historyManager.add(firstSubTask);
        historyManager.add(firstSubTask);
        historyManager.add(firstSubTask);
        historyManager.add(firstSubTask);
        historyManager.add(firstSubTask);
        historyManager.add(firstSubTask);
        historyManager.add(firstSubTask);
        historyManager.add(firstSubTask);
        historyManager.add(secondTask);

        //then
        assertEquals(10, historyManager.getHistory().size(), "Больше чем 10 :" +
                historyManager.getHistory().size());

    }

    @DisplayName("Проверка одинаковых задач")
    @Test
    void taskManager_Compare_EqualsTasks() {
        //given
        Task task = taskManager.getTaskById(0);
        Task taskClone = taskManager.getTaskById(0);

        //then
        assertEquals(task, taskClone, "Задачи с одинаковым ID - должны быть эквиваленты");
    }

    @DisplayName("Проверка одинаковых эпиков")
    @Test
    void taskManager_Compare_EqualsEpics() {
        //given
        Epic epic = taskManager.getEpicById(1);
        Epic epicClone = taskManager.getEpicById(1);

        //then
        assertEquals(epic, epicClone, "Эпики с одинаковым ID - должны быть эквиваленты");
    }


    @DisplayName("Проверка одинаковых подзадач")
    @Test
    void taskManager_Compare_EqualsSubTasks() {
        //given
        SubTask subTask = taskManager.getSubTaskById(2);
        SubTask subTaskClone = taskManager.getSubTaskById(2);

        //then
        assertEquals(subTask, subTaskClone, "Подзадачи с одинаковым ID - должны быть эквиваленты");
    }

    @DisplayName("Проверка одинаковых менеджеров задач")
    @Test
    void taskManager_Compare_EqualsTaskManagers() {
        //given
        TaskManager taskManager = Managers.getDefault();

        //then
        assertEquals(taskManager, taskManager, "Менеджеры задач должен быть эквиваленты");
    }

    @DisplayName("Проверка одинаковых менеджеров истории")
    @Test
    void taskManager_Compare_EqualsHistoryManagers() {
        //given
        HistoryManager historyManager = Managers.getDefaultHistory();

        //then
        assertEquals(historyManager, historyManager, "Менеджер истории должен быть эквиваленты");
    }


    @DisplayName("Добавление задач разных типов")
    @Test
    void taskManager_Added_TasksAnotherTypes() {
        //given
        int countAllTasks = taskManager.getAllTaskList().size();
        int countAllEpics = taskManager.getAllEpicList().size();
        int countAllSubTask = taskManager.getAllSubTaskList().size();

        //then
        assertEquals(2, countAllTasks);
        assertEquals(1, countAllEpics);
        assertEquals(2, countAllSubTask);
    }

    @DisplayName("Поиск задач разных типов по id")
    @Test
    void isManagerAddedTasksAnotherTypesAndFindThemByIds() {
        //given
        String firstTaskName = taskManager.getTaskById(0).getName();
        String firstEpicName = taskManager.getEpicById(1).getName();
        String firstSubTaskName = taskManager.getSubTaskById(2).getName();

        //then
        assertEquals("Первая задача", firstTaskName);
        assertEquals("Первый эпик", firstEpicName);
        assertEquals("Первая подзадача", firstSubTaskName);
    }

    @DisplayName("Проверка создания задач с одинаковым id")
    @Test
    void taskManager_Create_TaskWithEqualId() {
        //given
        int idEqualTask = 8;
        Task firstEqualTask = new Task("Тестовая1", "Тест1", idEqualTask,
                TaskStatus.NEW);
        Task secondEqualTask = new Task("Тестовая2", "Тест2", idEqualTask,
                TaskStatus.NEW);

        //when
        taskManager.createTask(firstEqualTask);
        taskManager.createTask(secondEqualTask);

        //then
        Assertions.assertEquals("Тестовая1", taskManager.getTaskById(idEqualTask).getName());
        Assertions.assertNotEquals("Тестовая2", taskManager.getTaskById(idEqualTask).getName());
    }

    @DisplayName("Проверка создания эпиков с одинаковым id")
    @Test
    void taskManager_Create_EpicWithEqualId() {
        //given
        int idEqualEpic = 8;
        Epic firstEqualEpic= new Epic("Тестовая1", "Тест1", idEqualEpic);
        Epic secondEqualEpic = new Epic("Тестовая2", "Тест2", idEqualEpic);

        //when
        taskManager.createEpic(firstEqualEpic);
        taskManager.createEpic(secondEqualEpic);

        //then
        Assertions.assertEquals("Тестовая1", taskManager.getEpicById(idEqualEpic).getName());
        Assertions.assertNotEquals("Тестовая2", taskManager.getEpicById(idEqualEpic).getName());
    }

    @DisplayName("Проверка создания подзадач с одинаковым id")
    @Test
    void taskManager_Create_SubTaskWithEqualId() {
        //given
        int idEqualSubTask = 8;
        int idEpic = 1;
        SubTask firstEqualSubTask= new SubTask("Тестовая1", "Тест1", idEqualSubTask, TaskStatus.NEW, idEpic);
        SubTask secondEqualSubTask= new SubTask("Тестовая2", "Тест2", idEqualSubTask, TaskStatus.NEW, idEpic);

        //when
        taskManager.createSubTask(firstEqualSubTask);
        taskManager.createSubTask(secondEqualSubTask);

        //then
        Assertions.assertEquals("Тестовая1", taskManager.getSubTaskById(idEqualSubTask).getName());
        Assertions.assertNotEquals("Тестовая2", taskManager.getSubTaskById(idEqualSubTask).getName());
    }

    @DisplayName("Проверка обновления задач")
    @Test
    void taskManager_Update_Task(){
        //given
        int taskId = 7;
        Task testTask = new Task("Тестовая1", "Тест1", taskId,
                TaskStatus.NEW);
        Task testTaskUpdated = new Task("Изменена", "Тест1", taskId,
                TaskStatus.NEW);

        //when
        taskManager.createTask(testTask);
        taskManager.updateTask(testTaskUpdated);
        Task currentTask = taskManager.getTaskById(taskId);

        //then
        assertNotEquals("Тестовая1", currentTask.getName());
        assertEquals("Изменена", currentTask.getName());
        assertEquals("Тест1", currentTask.getDescription());
        assertEquals(taskId, currentTask.getId());
        assertEquals(TaskStatus.NEW, currentTask.getStatus());

    }

    @DisplayName("Проверка обновления эпиков")
    @Test
    void taskManager_Update_Epic(){
        //given
        int epicId = 7;
        Epic testEpic = new Epic("Тестовая1", "Тест1", epicId);
        Epic testEpicUpdated = new Epic("Изменена", "Тест1", epicId);

        //when
        taskManager.createEpic(testEpic);
        taskManager.updateEpic(testEpicUpdated);
        Epic currentEpic = taskManager.getEpicById(epicId);

        //then
        assertNotEquals("Тестовая1", currentEpic.getName());
        assertEquals("Изменена", currentEpic.getName());
        assertEquals("Тест1", currentEpic.getDescription());
        assertEquals(epicId, currentEpic.getId());
    }

    @DisplayName("Проверка обновления подзадач")
    @Test
    void taskManager_Update_SubTask(){
        //given
        int subTaskId = 7;
        int epicId = 1;
        SubTask testSubTask = new SubTask("Тестовая1", "Тест1", subTaskId, TaskStatus.NEW, epicId);
        SubTask testSubTaskUpdated = new SubTask("Изменена", "Тест1", subTaskId, TaskStatus.NEW, epicId);

        //when
        taskManager.createSubTask(testSubTask);
        taskManager.updateSubTask(testSubTaskUpdated);
        SubTask currentSubTask = taskManager.getSubTaskById(subTaskId);

        //then
        assertNotEquals("Тестовая1", currentSubTask.getName());
        assertEquals("Изменена", currentSubTask.getName());
        assertEquals("Тест1", currentSubTask.getDescription());
        assertEquals(subTaskId, currentSubTask.getId());
        assertEquals(TaskStatus.NEW, currentSubTask.getStatus());
        assertEquals(epicId, currentSubTask.getEpicId());
    }

    @DisplayName("Сохранение прошлых версий задач в историю")
    @Test
    void historyManager_Saved_PastVersionTask() {
        //given
        int taskId = 0;
        ArrayList<Task> testedTasksList = new ArrayList<>();
        Task testTask = taskManager.getTaskById(taskId);
        Task updatedTask = new Task("Тестовая", "Описание первой задачи", taskId, TaskStatus.NEW);

        //when
        testedTasksList.add(testTask);
        historyManager.add(testTask);
        taskManager.updateTask(updatedTask);
        testedTasksList.add(updatedTask);
        historyManager.add(taskManager.getTaskById(taskId));
        System.out.println(historyManager.getHistory());
        System.out.println(testedTasksList);

        //then
        assertEquals(historyManager.getHistory().getFirst(), testTask);
        assertEquals(testedTasksList, historyManager.getHistory());
    }

    @DisplayName("Сохранение прошлых версий эпиков в историю")
    @Test
    void historyManager_Saved_PastVersionEpic() {
        //given
        int epicId = 1;
        ArrayList<Epic> testedEpicList = new ArrayList<>();
        Epic testEpic = taskManager.getEpicById(epicId);
        Epic updatedEpic = new Epic("Тестовая", "Описание первой задачи", epicId);

        //when
        testedEpicList.add(testEpic);
        historyManager.add(testEpic);
        taskManager.updateEpic(updatedEpic);
        testedEpicList.add(updatedEpic);
        historyManager.add(taskManager.getEpicById(epicId));
        System.out.println(historyManager.getHistory());
        System.out.println(testedEpicList);

        //then
        assertEquals(historyManager.getHistory().getFirst(), testEpic);
        assertEquals(testedEpicList, historyManager.getHistory());
    }

    @DisplayName("Сохранение прошлых версий подзадач в историю")
    @Test
    void historyManager_Saved_PastVersionSubTask() {
        //given
        int subTaskId = 2;
        int epicId = 1;
        ArrayList<SubTask> testedSubTaskList = new ArrayList<>();
        SubTask testSubTask = taskManager.getSubTaskById(subTaskId);
        SubTask updatedSubTask = new SubTask("Тестовая1", "Тест1", subTaskId, TaskStatus.NEW, epicId);

        //when
        testedSubTaskList.add(testSubTask);
        historyManager.add(testSubTask);
        taskManager.updateSubTask(updatedSubTask);
        testedSubTaskList.add(updatedSubTask);
        historyManager.add(taskManager.getSubTaskById(subTaskId));
        System.out.println(historyManager.getHistory());
        System.out.println(testedSubTaskList);

        //then
        assertEquals(historyManager.getHistory().getFirst(), testSubTask);
        assertEquals(testedSubTaskList, historyManager.getHistory());
    }
}