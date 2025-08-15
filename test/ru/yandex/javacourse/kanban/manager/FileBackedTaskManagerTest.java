package ru.yandex.javacourse.kanban.manager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.javacourse.kanban.manager.exception.InvalidTaskTypeException;
import ru.yandex.javacourse.kanban.manager.exception.ManagerReadException;
import ru.yandex.javacourse.kanban.manager.exception.ManagerSaveException;
import ru.yandex.javacourse.kanban.task.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.javacourse.kanban.manager.Stubs.FILE_HEADER;
import static ru.yandex.javacourse.kanban.manager.Stubs.FORMATTER;

public class FileBackedTaskManagerTest {
    public static FileBackedTaskManager fileBackedTaskManager;
    public static InMemoryTaskManager inMemoryTaskManager;
    public static File tempFile;

    @BeforeEach
    void beforeEach() throws IOException {
        tempFile = File.createTempFile("TempFileMem", ".csv");
        fileBackedTaskManager = new FileBackedTaskManager(tempFile.toPath());
        inMemoryTaskManager = new InMemoryTaskManager();

    }

    @AfterEach
    void afterEach() {
        tempFile.deleteOnExit();
    }

    @DisplayName("Проверка вывода задачи из объекта в текстовое поле")
    @Test
    void get_Get_StringLineFromTaskObject() throws InvalidTaskTypeException {
        //given
        Task newTask = new Task("Первая задача", "Описание первой задачи",
                inMemoryTaskManager.getNewId(),
                TaskStatus.NEW,
                Duration.of(30, ChronoUnit.MINUTES),
                LocalDateTime.of(2025, 12, 1, 1, 1));

        //when
        String taskToString = fileBackedTaskManager.toString(newTask);

        //then
        assertEquals(("0,TASK,Первая задача,NEW,Описание первой задачи,30,2025 12 01 01 01"), taskToString,
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
        assertEquals(("0,EPIC,Первый эпик,NEW,Описание первого Эпика,0,"
                        + LocalDateTime.now().format(FORMATTER) + ","
                        + LocalDateTime.now().format(FORMATTER)), epicToString,
                "Текстовое описание должно быть эквивалентно");
    }

    @DisplayName("Проверка вывода подзадачи из объекта в текстовое поле")
    @Test
    void get_Get_StringLineFromSubTaskObject() throws InvalidTaskTypeException {
        //given
        Epic newEpic = new Epic("Первый эпик", "Описание первого Эпика",
                inMemoryTaskManager.getNewId());
        SubTask newSubTask = new SubTask("Вторая подзадача", "Описание второй подзадачи",
                inMemoryTaskManager.getNewId(), TaskStatus.IN_PROGRESS, newEpic.getId(),
                Duration.of(30, ChronoUnit.MINUTES),
                LocalDateTime.of(2025, 12, 1, 1, 1));

        //when
        String subTaskToString = fileBackedTaskManager.toString(newSubTask);

        //then
        assertEquals(("1,SUBTASK,Вторая подзадача,IN_PROGRESS,Описание второй подзадачи,0,30,2025 12 01 01 01"),
                subTaskToString,
                "Текстовое описание должно быть эквивалентно");
    }

    @DisplayName("Проверка получения задачи из текстового поле")
    @Test
    void get_Get_TaskFromStringLine() throws InvalidTaskTypeException {
        //given
        Task newTask = new Task("Первая задача", "Описание первой задачи",
                inMemoryTaskManager.getNewId(),
                TaskStatus.NEW,
                Duration.of(30, ChronoUnit.MINUTES),
                LocalDateTime.of(2025, 12, 1, 1, 1));

        //when
        String taskToString = fileBackedTaskManager.toString(newTask);
        Task restoredTask = FileBackedTaskManager.fromString(taskToString);

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
        Epic restoredEpic = (Epic) FileBackedTaskManager.fromString(epicToString);

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
                inMemoryTaskManager.getNewId(), TaskStatus.IN_PROGRESS, newEpic.getId(),
                Duration.of(30, ChronoUnit.MINUTES),
                LocalDateTime.of(2025, 12, 1, 1, 1));

        //when
        String subTaskToString = fileBackedTaskManager.toString(newSubTask);
        SubTask restoredSubTask = (SubTask) FileBackedTaskManager.fromString(subTaskToString);

        //then
        assertEquals(restoredSubTask, newSubTask, "Подзадачи должны быть эквиваленты");
    }

    @DisplayName("Проверка срабатывания исключения IllegalArgumentException")
    @Test
    void get_Get_IllegalArgumentException() {
        //given

        TestTaskTest newTaskTest = new TestTaskTest("Первая задача", "Описание первой задачи",
                inMemoryTaskManager.getNewId(),
                TaskStatus.NEW,
                Duration.of(30, ChronoUnit.MINUTES),
                LocalDateTime.of(2025, 12, 1, 1, 1));

        //then
        assertThrows(IllegalArgumentException.class, () -> {
            fileBackedTaskManager.createTask(newTaskTest);
        }, "Такой тип задачи не добавлен TaskType - должно быть исключение ");
    }

    @DisplayName("Проверка срабатывания исключения ManagerSaveException")
    @Test
    void get_Get_ManagerSaveException() {

        //when
        tempFile.setReadOnly();

        //then
        assertThrows(ManagerSaveException.class, () -> {
            FileBackedTaskManager.loadFromFile(tempFile);
        }, "Сохранение в данный файл невозможно");
    }

    @DisplayName("Проверка срабатывания исключения ManagerReadException")
    @Test
    void get_Get_ManagerReadException() {

        //when
        tempFile.setReadable(false, true);

        //then
        assertThrows(ManagerReadException.class, () -> {
            FileBackedTaskManager.loadFromFile(tempFile);
        }, "Чтение данного файла невозможно");
    }

    @DisplayName("Проверка сохранения пустого файл")
    @Test
    void add_isSaved_EmptyFileToDisk() throws IOException {
        //given
        String fileHeader = "id,type,name,status,description,epic,duration,startTime,endTime";

        //when
        fileBackedTaskManager.save();

        //then
        assertEquals(fileHeader, Files.readString(tempFile.toPath()).trim(),
                "Количество символов должно быть одинаковое");
    }

    @DisplayName("Проверка загрузки пустого файл")
    @Test
    void add_isImport_EmptyFileToMemory() throws IOException {
        //when

        FileBackedTaskManager.loadFromFile(tempFile);
        String memory = Files.readString(tempFile.toPath());

        //then
        assertEquals(FILE_HEADER + "\n", memory, "В файле должен появится заголовок");
    }

    @DisplayName("Проверка сохранения задач в файл")
    @Test
    void add_isSave_TasksObjectToFileMemory() throws IOException, InvalidTaskTypeException {
        //given
        Task newTask = new Task("Первая задача", "Описание первой задачи",
                inMemoryTaskManager.getNewId(),
                TaskStatus.NEW,
                Duration.of(30, ChronoUnit.MINUTES),
                LocalDateTime.of(2025, 12, 1, 1, 1));
        Epic newEpic = new Epic("Первый эпик", "Описание первого Эпика",
                inMemoryTaskManager.getNewId());
        SubTask newSubTask = new SubTask("Вторая подзадача", "Описание второй подзадачи",
                inMemoryTaskManager.getNewId(), TaskStatus.IN_PROGRESS, newEpic.getId(),
                Duration.of(30, ChronoUnit.MINUTES),
                LocalDateTime.of(2025, 11, 1, 1, 1));

        //when
        fileBackedTaskManager.createTask(newTask);
        fileBackedTaskManager.createEpic(newEpic);
        fileBackedTaskManager.createSubTask(newSubTask);

        String tasksLine = FILE_HEADER + "\n" +
                fileBackedTaskManager.toString(newTask) + "\n" +
                fileBackedTaskManager.toString(newEpic) + "\n" +
                fileBackedTaskManager.toString(newSubTask) + "\n";

        String memory = Files.readString(tempFile.toPath());

        //then
        assertEquals(tasksLine, memory,
                "Строки должны быть эквивалентны");
    }

    @DisplayName("Проверка загрузки задач при перезапуске программы")
    @Test
    void add_isImport_FromStringToProgram() throws IOException, InvalidTaskTypeException {
        //given
        Task newTask = new Task("Первая задача", "Описание первой задачи",
                inMemoryTaskManager.getNewId(),
                TaskStatus.NEW,
                Duration.of(30, ChronoUnit.MINUTES),
                LocalDateTime.of(2025, 12, 1, 1, 1));

        Epic newEpic = new Epic("Первый эпик", "Описание первого Эпика",
                inMemoryTaskManager.getNewId());

        SubTask newSubTask = new SubTask("Вторая подзадача", "Описание второй подзадачи",
                inMemoryTaskManager.getNewId(), TaskStatus.IN_PROGRESS, newEpic.getId(),
                Duration.of(30, ChronoUnit.MINUTES),
                LocalDateTime.of(2024, 12, 1, 1, 1));

        Writer fileWriter = new FileWriter(tempFile, true);
        fileWriter.write(FILE_HEADER + "\n");
        fileWriter.write(fileBackedTaskManager.toString(newTask) + "\n");
        fileWriter.write(fileBackedTaskManager.toString(newEpic) + "\n");
        fileWriter.write(fileBackedTaskManager.toString(newSubTask) + "\n");
        fileWriter.close();

        //when
        FileBackedTaskManager fileBackedTaskManager1 = FileBackedTaskManager.loadFromFile(tempFile);

        //then
        assertEquals(newTask, fileBackedTaskManager1.getTaskById(0),
                "Заявки должны быть эквивалентны");
        assertEquals(newEpic, fileBackedTaskManager1.getEpicById(1),
                "Эпики должны быть эквивалентны");
        assertEquals(newSubTask, fileBackedTaskManager1.getSubTaskById(2),
                "Подзадачи должны быть эквивалентны");
    }

    @DisplayName("Вывод отсортированного списка всех задач")
    @Test
    void get_GetAllSortedTasksObject_FromAllList() {
        //given
        Task newTask = new Task("Первая задача", "Описание первой задачи",
                inMemoryTaskManager.getNewId(),
                TaskStatus.NEW,
                Duration.of(30, ChronoUnit.MINUTES),
                LocalDateTime.of(2025, 12, 1, 1, 1));

        Epic newEpic = new Epic("Первый эпик", "Описание первого Эпика",
                inMemoryTaskManager.getNewId());

        SubTask newSubTask = new SubTask("Первая подзадача", "Описание первой подзадачи",
                inMemoryTaskManager.getNewId(), TaskStatus.IN_PROGRESS, newEpic.getId(),
                Duration.of(30, ChronoUnit.MINUTES),
                LocalDateTime.of(2024, 11, 1, 1, 1));

        SubTask newSubTask1 = new SubTask("Вторая подзадача", "Описание второй подзадачи",
                inMemoryTaskManager.getNewId(), TaskStatus.NEW, newEpic.getId(),
                Duration.of(40, ChronoUnit.MINUTES),
                LocalDateTime.of(2023, 10, 1, 1, 1));

        //when
        fileBackedTaskManager.createTask(newTask);
        fileBackedTaskManager.createEpic(newEpic);
        fileBackedTaskManager.createSubTask(newSubTask);
        fileBackedTaskManager.createSubTask(newSubTask1);

        LinkedList<Task> trueOrderTaskList = new LinkedList<>(List.of(newEpic, newSubTask1, newSubTask, newTask));
        LinkedList<Task> true2OrderTaskList = new LinkedList<>(List.copyOf(fileBackedTaskManager.getPrioritizedTasks()));

        //then
        assertEquals(trueOrderTaskList, true2OrderTaskList,
                "Списки должны быть эквивалентны");

    }

    @DisplayName("Проверка пересечения задач")
    @Test
    void get_isIntersect_TasksToTime() {
        //given
        Task newTask = new Task("Первая задача", "Описание первой задачи",
                inMemoryTaskManager.getNewId(),
                TaskStatus.NEW,
                Duration.of(30, ChronoUnit.MINUTES),
                LocalDateTime.of(2025, 12, 1, 1, 1));
        Task newTask1 = new Task("Первая задача", "Описание первой задачи",
                inMemoryTaskManager.getNewId(),
                TaskStatus.NEW,
                Duration.of(28, ChronoUnit.MINUTES),
                LocalDateTime.of(2025, 12, 1, 1, 1));

        //when
        fileBackedTaskManager.createTask(newTask);
        fileBackedTaskManager.createTask(newTask1);

        //then
        assertTrue(fileBackedTaskManager.isTasksIntersectToTime(newTask, newTask1), "Время выполнение заявок должно пересекаться");
    }

    @DisplayName("Проверка параллельности задач")
    @Test
    void get_isNotIntersect_TasksToTime() {
        //given
        Task newTask = new Task("Первая задача", "Описание первой задачи",
                inMemoryTaskManager.getNewId(),
                TaskStatus.NEW,
                Duration.of(30, ChronoUnit.MINUTES),
                LocalDateTime.of(2025, 12, 1, 1, 1));
        Task newTask1 = new Task("Первая задача", "Описание первой задачи",
                inMemoryTaskManager.getNewId(),
                TaskStatus.NEW,
                Duration.of(28, ChronoUnit.MINUTES),
                LocalDateTime.of(2024, 12, 1, 1, 1));

        //when
        fileBackedTaskManager.createTask(newTask);
        fileBackedTaskManager.createTask(newTask1);

        //then
        assertFalse(fileBackedTaskManager.isTasksIntersectToTime(newTask, newTask1), "Время выполнение заявок не должно пересекаться");
    }

    @DisplayName("Проверка задач на пересечение в списках с задачами")
    @Test
    void get_isNewTask_IntersectToAllTasks() {
        //given
        Task newTask = new Task("Первая задача", "Описание первой задачи",
                inMemoryTaskManager.getNewId(),
                TaskStatus.NEW,
                Duration.of(30, ChronoUnit.MINUTES),
                LocalDateTime.of(2025, 12, 1, 1, 1));

        Epic newEpic = new Epic("Первый эпик", "Описание первого Эпика",
                inMemoryTaskManager.getNewId());

        SubTask newSubTask = new SubTask("Первая подзадача", "Описание первой подзадачи",
                inMemoryTaskManager.getNewId(), TaskStatus.IN_PROGRESS, newEpic.getId(),
                Duration.of(30, ChronoUnit.MINUTES),
                LocalDateTime.of(2024, 11, 1, 1, 1));

        SubTask newSubTask1 = new SubTask("Вторая подзадача", "Описание второй подзадачи",
                inMemoryTaskManager.getNewId(), TaskStatus.NEW, newEpic.getId(),
                Duration.of(40, ChronoUnit.MINUTES),
                LocalDateTime.of(2023, 10, 1, 1, 1));

        SubTask newSubTask2 = new SubTask("Вторая подзадача(Копия)", "Описание второй подзадачи(копия)",
                inMemoryTaskManager.getNewId(), TaskStatus.NEW, newEpic.getId(),
                Duration.of(40, ChronoUnit.MINUTES),
                LocalDateTime.of(2023, 10, 1, 1, 1));

        //when
        fileBackedTaskManager.createTask(newTask);
        fileBackedTaskManager.createEpic(newEpic);
        fileBackedTaskManager.createSubTask(newSubTask);
        fileBackedTaskManager.createSubTask(newSubTask1);

        boolean isNewTaskIntersectToExistTasks = fileBackedTaskManager.isTaskIntersectToAllTaskToTime(newSubTask2);

        //then
        assertTrue(isNewTaskIntersectToExistTasks,
                "Пересечение существует");

    }

    @DisplayName("Проверка задач на не пересечение в списках с задачами")
    @Test
    void get_isNewTask_NotIntersectToAllTasks() {
        //given
        Task newTask = new Task("Первая задача", "Описание первой задачи",
                inMemoryTaskManager.getNewId(),
                TaskStatus.NEW,
                Duration.of(30, ChronoUnit.MINUTES),
                LocalDateTime.of(2025, 12, 1, 1, 1));

        Epic newEpic = new Epic("Первый эпик", "Описание первого Эпика",
                inMemoryTaskManager.getNewId());

        SubTask newSubTask = new SubTask("Первая подзадача", "Описание первой подзадачи",
                inMemoryTaskManager.getNewId(), TaskStatus.IN_PROGRESS, newEpic.getId(),
                Duration.of(30, ChronoUnit.MINUTES),
                LocalDateTime.of(2024, 11, 1, 1, 1));

        SubTask newSubTask1 = new SubTask("Вторая подзадача", "Описание второй подзадачи",
                inMemoryTaskManager.getNewId(), TaskStatus.NEW, newEpic.getId(),
                Duration.of(40, ChronoUnit.MINUTES),
                LocalDateTime.of(2023, 10, 1, 1, 1));

        SubTask newSubTask2 = new SubTask("Вторая подзадача(Копия)", "Описание второй подзадачи(копия)",
                inMemoryTaskManager.getNewId(), TaskStatus.NEW, newEpic.getId(),
                Duration.of(40, ChronoUnit.MINUTES),
                LocalDateTime.of(2026, 10, 1, 1, 1));

        //when
        fileBackedTaskManager.createTask(newTask);
        fileBackedTaskManager.createEpic(newEpic);
        fileBackedTaskManager.createSubTask(newSubTask);
        fileBackedTaskManager.createSubTask(newSubTask1);

        boolean isNewTaskNotIntersectToExistTasks = fileBackedTaskManager.isTaskIntersectToAllTaskToTime(newSubTask2);

        //then
        assertFalse(isNewTaskNotIntersectToExistTasks,
                "Задача не должна пересекать другие");

    }

    @DisplayName("Попытка добавить подзадачу с пересечением по времени")
    @Test
    void add_NewSubTask_ToIntersectToAllTasks() {
        //given
        Task newTask = new Task("Первая задача", "Описание первой задачи",
                inMemoryTaskManager.getNewId(),
                TaskStatus.NEW,
                Duration.of(30, ChronoUnit.MINUTES),
                LocalDateTime.of(2025, 12, 1, 1, 1));

        Epic newEpic = new Epic("Первый эпик", "Описание первого Эпика",
                inMemoryTaskManager.getNewId());

        SubTask newSubTask = new SubTask("Первая подзадача", "Описание первой подзадачи",
                inMemoryTaskManager.getNewId(), TaskStatus.IN_PROGRESS, newEpic.getId(),
                Duration.of(30, ChronoUnit.MINUTES),
                LocalDateTime.of(2024, 11, 1, 1, 1));

        SubTask newSubTask1 = new SubTask("Вторая подзадача", "Описание второй подзадачи",
                inMemoryTaskManager.getNewId(), TaskStatus.NEW, newEpic.getId(),
                Duration.of(40, ChronoUnit.MINUTES),
                LocalDateTime.of(2023, 10, 1, 1, 1));

        SubTask newSubTask2 = new SubTask("Вторая подзадача(Копия)", "Описание второй подзадачи(копия)",
                inMemoryTaskManager.getNewId(), TaskStatus.NEW, newEpic.getId(),
                Duration.of(40, ChronoUnit.MINUTES),
                LocalDateTime.of(2023, 10, 1, 1, 1));

        //when
        fileBackedTaskManager.createTask(newTask);
        fileBackedTaskManager.createEpic(newEpic);
        fileBackedTaskManager.createSubTask(newSubTask);
        fileBackedTaskManager.createSubTask(newSubTask1);

        fileBackedTaskManager.createSubTask(newSubTask2);

        //then
        assertNull(fileBackedTaskManager.getSubTaskById(newSubTask2.getId()));
    }

}
