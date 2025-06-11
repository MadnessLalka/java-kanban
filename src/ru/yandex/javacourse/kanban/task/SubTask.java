package ru.yandex.javacourse.kanban.task;

import java.util.Objects;

public class SubTask extends Task {
    private final int epicId;

    public SubTask(String name, String description, int taskId, TaskStatus status, int epicId) {
        super(name, description, taskId, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status=" + getStatus() +
                ", EpicId=" + getEpicId() +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubTask subTask = (SubTask) o;
        return super.getId() == subTask.getId() && Objects.equals(super.getId(), subTask.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.getId());
    }

}
