package ru.yandex.javacourse.kanban.task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

import static ru.yandex.javacourse.kanban.Stubs.FORMATTER;

public class Epic extends Task {
    private TaskStatus status;
    private Duration duration;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private final ArrayList<SubTask> subTasksList = new ArrayList<>();

    public Epic(String name, String description, int taskId) {
        super(name, description, taskId);
        this.status = TaskStatus.NEW;
        this.duration = Duration.ofMinutes(0);
        this.startTime = LocalDateTime.parse(LocalDateTime.now().format(FORMATTER), FORMATTER);
        this.endTime = LocalDateTime.parse(LocalDateTime.now().format(FORMATTER), FORMATTER);
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

    @Override
    public Duration getDuration() {
        return duration;
    }

    @Override
    public LocalDateTime getStartTime() {
        return startTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
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

    public void setDuration() {
        if (subTasksList.isEmpty()) {
            this.duration = Duration.ofMinutes(0);
            return;
        }

        Duration allSubTaskDuration = Duration.ofMinutes(0);

        for (SubTask st : subTasksList) {
            allSubTaskDuration = allSubTaskDuration.plus(st.getDuration());
        }

        this.duration = Duration.ofMinutes(allSubTaskDuration.toMinutes());
    }

    public void setStartTime() {
        if (subTasksList.isEmpty()) {
            this.startTime = LocalDateTime.parse(LocalDateTime.now().format(FORMATTER), FORMATTER);
            return;
        }

        if (subTasksList.size() == 1) {
            this.startTime = subTasksList.getFirst().getStartTime();
            return;
        }

        LocalDateTime minStartSubTaskTime = subTasksList.getFirst().getStartTime();

        for (SubTask st : subTasksList) {
            if (st.getStartTime().isBefore(minStartSubTaskTime)) {
                minStartSubTaskTime = st.getStartTime();
            }
        }

        this.startTime = minStartSubTaskTime;


    }

    public void setEndTime() {
        if (subTasksList.isEmpty()) {
            this.endTime = LocalDateTime.parse(LocalDateTime.now().format(FORMATTER), FORMATTER);
            return;
        }

        if (subTasksList.size() == 1) {
            this.endTime = subTasksList.getFirst().getStartTime()
                    .plusMinutes(subTasksList.getFirst().getDuration().toMinutes());
            return;
        }

        LocalDateTime maxEndSubTaskTime = subTasksList.getFirst().getEndTime();

        for (SubTask st : subTasksList) {
            if (st.getStartTime().isAfter(maxEndSubTaskTime)) {
                maxEndSubTaskTime = st.getEndTime();
            }
        }

        this.endTime = maxEndSubTaskTime;
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
                ", duration=" + duration +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
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