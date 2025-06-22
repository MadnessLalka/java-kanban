package ru.yandex.javacourse.kanban.task;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private TaskStatus status;
    private final ArrayList<SubTask> subTasksList = new ArrayList<>();

    public Epic(String name, String description, int taskId) {
        super(name, description, taskId);
        this.status = TaskStatus.NEW;
    }

    @Override
    public TaskStatus getStatus() {
        return status;
    }

    public void addSubTaskToList(SubTask subTask) {
        subTasksList.add(subTask);
    }

    public void removeSubTaskToList(SubTask subTask) {
        subTasksList.remove(subTask);
    }

    public void clearSubTaskList() {
        subTasksList.clear();
    }

    public void setStatus() {
        if (subTasksList.isEmpty()) {
            this.status = TaskStatus.NEW;
            return;
        }

        boolean isDone = false;

        for (SubTask st : subTasksList) {
            if (st.getStatus() == TaskStatus.DONE) {
                isDone = true;
            } else {
                isDone = false;
                break;
            }
        }

        if (isDone) {
            this.status = TaskStatus.DONE;
            return;
        }

        boolean isInProgress = false;
        for (SubTask st : subTasksList) {
            if (st.getStatus() == TaskStatus.IN_PROGRESS || st.getStatus() == TaskStatus.DONE) {
                isInProgress = true;
                break;
            }
        }

        if (isInProgress) {
            this.status = TaskStatus.IN_PROGRESS;
        } else {
            this.status = TaskStatus.NEW;
        }
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public String getDescription() {
        return super.getDescription();
    }

    @Override
    public int getId() {
        return super.getId();
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + super.getName() + '\'' +
                ", description='" + super.getDescription() + '\'' +
                ", id=" + super.getId() +
                ", status=" + status +
                ", subTaskList=" + subTasksList +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Epic epic = (Epic) o;
        return super.getId() == epic.getId() && Objects.equals(super.getId(), epic.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.getId());
    }
}