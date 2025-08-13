package ru.yandex.javacourse.kanban.task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class SubTask extends Task {
    private final int epicId;

    public SubTask(String name, String description, int taskId, TaskStatus status, int epicId,
                   Duration duration, LocalDateTime startTime) {
        super(name, description, taskId, status, duration, startTime);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "name='" + getName() + '\'' +
                ", Description='" + getDescription() + '\'' +
                ", Id=" + getId() +
                ", Status=" + getStatus() +
                ", EpicId=" + getEpicId() +
                ", Duration=" + getDuration() +
                ", StartTime=" + getStartTime() +
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
