package ru.yandex.javacourse.kanban.manager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.javacourse.kanban.manager.exception.InvalidTaskTypeException;
import ru.yandex.javacourse.kanban.task.Epic;
import ru.yandex.javacourse.kanban.task.SubTask;
import ru.yandex.javacourse.kanban.task.Task;
import ru.yandex.javacourse.kanban.task.TaskStatus;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.javacourse.kanban.manager.Stubs.FILE_HEADER;

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
                inMemoryTaskManager.getNewId(), TaskStatus.IN_PROGRESS, newEpic.getId());

        //when
        String subTaskToString = fileBackedTaskManager.toString(newSubTask);
        SubTask restoredSubTask = (SubTask) FileBackedTaskManager.fromString(subTaskToString);

        //then
        assertEquals(restoredSubTask, newSubTask, "Подзадачи должны быть эквиваленты");
    }

    @DisplayName("Проверка сохранения пустого файл")
    @Test
    void add_isSaved_EmptyFileToDisk() throws IOException {
        //given
        String fileHeader = "id,type,name,status,description,epic";

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
//        Exception exception = assertThrows(NullPointerException.class, () -> {
//            FileBackedTaskManager.loadFromFile(tempFile);
//        });

        //then
        assertEquals(FILE_HEADER+"\n", memory, "В файле должен появится заголовок");
    }

    @DisplayName("Проверка сохранения задач в файл")
    @Test
    void add_isSave_TasksObjectToFileMemory() throws IOException, InvalidTaskTypeException {
        //given
        Task newTask = new Task("Первая задача", "Описание первой задачи",
                inMemoryTaskManager.getNewId(),
                TaskStatus.NEW);
        Epic newEpic = new Epic("Первый эпик", "Описание первого Эпика",
                inMemoryTaskManager.getNewId());
        SubTask newSubTask = new SubTask("Вторая подзадача", "Описание второй подзадачи",
                inMemoryTaskManager.getNewId(), TaskStatus.IN_PROGRESS, newEpic.getId());

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

    @DisplayName("Проверка загрузки задач в при перезапуске программы")
    @Test
    void add_isImport_FromStringToProgram() throws IOException, InvalidTaskTypeException {
        //given
        Task newTask = new Task("Первая задача", "Описание первой задачи",
                inMemoryTaskManager.getNewId(),
                TaskStatus.NEW);
        Epic newEpic = new Epic("Первый эпик", "Описание первого Эпика",
                inMemoryTaskManager.getNewId());
        SubTask newSubTask = new SubTask("Вторая подзадача", "Описание второй подзадачи",
                inMemoryTaskManager.getNewId(), TaskStatus.IN_PROGRESS, newEpic.getId());

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
}
