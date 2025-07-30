package ru.yandex.javacourse.kanban.manager;

import ru.yandex.javacourse.kanban.manager.exception.InvalidTaskTypeException;
import ru.yandex.javacourse.kanban.manager.exception.ManagerSaveException;
import ru.yandex.javacourse.kanban.task.Epic;
import ru.yandex.javacourse.kanban.task.SubTask;
import ru.yandex.javacourse.kanban.task.Task;
import ru.yandex.javacourse.kanban.task.TaskStatus;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static ru.yandex.javacourse.kanban.manager.TaskType.*;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    private final Path path;

    public FileBackedTaskManager(Path path) throws ManagerSaveException {
        this.path = path;

        try {
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось создать файл для сохранения", e);
        }

    }

    static FileBackedTaskManager loadFromFile(File file) throws ManagerSaveException {
        try {
            if (Files.readString(file.toPath()).isBlank()) throw new NullPointerException("Файл пуст");
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось создать файл для сохранения", e);
        }

        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file.toPath());
        try (Reader fileReader = new FileReader(file)) {

            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while (bufferedReader.ready()) {
                String line = bufferedReader.readLine();
                if (line.contains("id,type,name,status,description,epic")) continue;

                if (line.contains("TASK") && !line.contains("SUBTASK")) {
                    fileBackedTaskManager.createTask(fromString(line));
                } else if (line.contains("EPIC")) {
                    fileBackedTaskManager.createEpic((Epic) fromString(line));
                } else if (line.contains("SUBTASK")) {
                    fileBackedTaskManager.createSubTask((SubTask) fromString(line));
                }
            }
        } catch (FileNotFoundException e) {
            throw new ManagerSaveException("Файл не найден", e);
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при попытке чтения из файла", e);
        } catch (InvalidTaskTypeException e) {
            System.out.println(e.getMessage());
        }
        return fileBackedTaskManager;
    }


    protected void save() {
        ArrayList<Task> allTasksList = new ArrayList<>();
        allTasksList.addAll(getAllTaskList());
        allTasksList.addAll(getAllEpicList());
        allTasksList.addAll(getAllSubTaskList());

        try (Writer fileWriter = new FileWriter(path.toFile())) {
            fileWriter.write("id,type,name,status,description,epic\n");
            for (Task t : allTasksList) {
                fileWriter.write(toString(t) + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при попытке записать в файл", e);
        } catch (InvalidTaskTypeException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void setHistoryManager(HistoryManager historyManager) {
        super.setHistoryManager(historyManager);
    }

    @Override
    public int getNewId() {
        return super.getNewId();
    }

    @Override
    public int getIdCounter() {
        return super.getIdCounter();
    }

    @Override
    public ArrayList<Task> getAllTaskList() {
        return super.getAllTaskList();
    }

    @Override
    public ArrayList<Epic> getAllEpicList() {
        return super.getAllEpicList();
    }

    @Override
    public ArrayList<SubTask> getAllSubTaskList() {
        return super.getAllSubTaskList();
    }

    @Override
    public void removeAllTask() {
        super.removeAllTask();
        save();
    }

    @Override
    public void removeAllEpic() {
        super.removeAllEpic();
        save();
    }

    @Override
    public void removeAllSubTask() {
        super.removeAllSubTask();
        save();
    }

    @Override
    public Task getTaskById(int taskId) {
        return super.getTaskById(taskId);
    }

    @Override
    public Epic getEpicById(int epicId) {
        return super.getEpicById(epicId);
    }

    @Override
    public SubTask getSubTaskById(int subTaskId) {
        return super.getSubTaskById(subTaskId);
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void createSubTask(SubTask subTask) {
        super.createSubTask(subTask);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void removeTaskById(int taskId) {
        super.removeTaskById(taskId);
        save();
    }

    @Override
    public void removeEpicById(int epicId) {
        super.removeEpicById(epicId);
        save();
    }

    @Override
    public void removeSubTaskById(Integer subTaskId) {
        super.removeSubTaskById(subTaskId);
        save();

    }

    @Override
    public ArrayList<SubTask> getAllSubTaskByEpic(Task epic) {
        return super.getAllSubTaskByEpic(epic);
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    String toString(Task task) throws InvalidTaskTypeException {
        StringBuilder builder = new StringBuilder();

        switch (TaskType.valueOf(task.getClass().getSimpleName().toUpperCase())) {
            case TASK -> builder.append(task.getId())
                    .append(",")
                    .append(TASK)
                    .append(",")
                    .append(task.getName())
                    .append(",")
                    .append(task.getStatus())
                    .append(",")
                    .append(task.getDescription());
            case EPIC -> {
                Epic epic = (Epic) task;
                builder.append(epic.getId())
                        .append(",")
                        .append(EPIC)
                        .append(",")
                        .append(epic.getName())
                        .append(",")
                        .append(epic.getStatus())
                        .append(",")
                        .append(epic.getDescription());
            }
            case SUBTASK -> {
                SubTask subTask = (SubTask) task;
                builder.append(subTask.getId())
                        .append(",")
                        .append(SUBTASK)
                        .append(",")
                        .append(subTask.getName())
                        .append(",")
                        .append(subTask.getStatus())
                        .append(",")
                        .append(subTask.getDescription())
                        .append(",")
                        .append(subTask.getEpicId());

            }
            default -> throw new InvalidTaskTypeException("Такова типа задач нет!");
        }

        return builder.toString();
    }

    public static Task fromString(String value) throws InvalidTaskTypeException {
        List<String> tempStringTaskList = new ArrayList<>(List.of(value.split(",")));

        String name = tempStringTaskList.get(2);
        String description = tempStringTaskList.get(4);
        int id = Integer.parseInt(tempStringTaskList.get(0));

        switch (TaskType.valueOf(tempStringTaskList.get(1))) {
            case TASK -> {
                TaskStatus status = TaskStatus.valueOf(tempStringTaskList.get(3));
                return new Task(name, description, id, status);
            }
            case EPIC -> {
                return new Epic(name, description, id);
            }
            case SUBTASK -> {
                TaskStatus status = TaskStatus.valueOf(tempStringTaskList.get(3));
                int epicId = Integer.parseInt(tempStringTaskList.get(5));
                return new SubTask(name, description, id, status, epicId);
            }
            default -> throw new InvalidTaskTypeException("Такова типа задач нет!");
        }

    }
}
