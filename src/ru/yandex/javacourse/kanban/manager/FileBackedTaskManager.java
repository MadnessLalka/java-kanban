package ru.yandex.javacourse.kanban.manager;

import ru.yandex.javacourse.kanban.task.Epic;
import ru.yandex.javacourse.kanban.task.SubTask;
import ru.yandex.javacourse.kanban.task.Task;
import ru.yandex.javacourse.kanban.task.TaskStatus;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static ru.yandex.javacourse.kanban.manager.TaskType.*;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {

    Path taskManagerMemoryFile = null;

    public FileBackedTaskManager() throws IOException {
        Path path = Paths.get("taskManagerMemory.csv");
        if (!Files.exists(path)){
            taskManagerMemoryFile = Files.createFile(path);
            try (Writer fileWriter = new FileWriter(path.toFile(),true)){
                fileWriter.write("id,type,name,status,description,epic");
            }
        }
    }

    private void save(){

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

    public String toString(Task task) throws InvalidTaskTypeException {
        StringBuilder builder = new StringBuilder();

        switch (task.getClass().getSimpleName().toUpperCase()){
            case "TASK" -> builder.append(task.getId())
                    .append(", ")
                    .append(TASK)
                    .append(", ")
                    .append(task.getName())
                    .append(", ")
                    .append(task.getStatus())
                    .append(", ")
                    .append(task.getDescription());
            case "EPIC" -> {
                Epic epic = (Epic) task;
                builder.append(epic.getId())
                        .append(", ")
                        .append(EPIC)
                        .append(", ")
                        .append(epic.getName())
                        .append(", ")
                        .append(epic.getStatus())
                        .append(", ")
                        .append(epic.getDescription());
            }
            case "SUBTASK" -> {
                SubTask subTask = (SubTask) task;
                builder.append(subTask.getId())
                        .append(", ")
                        .append(SUBTASK)
                        .append(", ")
                        .append(subTask.getName())
                        .append(", ")
                        .append(subTask.getStatus())
                        .append(", ")
                        .append(subTask.getDescription())
                        .append(", ")
                        .append(subTask.getEpicId());

            }
            default -> throw new InvalidTaskTypeException("Такова типа задач нет!");
        }

        return builder.toString();
    }
}
