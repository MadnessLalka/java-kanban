package ru.yandex.javacourse.kanban.task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static ru.yandex.javacourse.kanban.Stubs.FORMATTER;

public class Epic extends Task {
    private LocalDateTime endTime;
    private final List<SubTask> subTasksList = new ArrayList<>();

    public Epic(String name, String description, int taskId) {
        super(name, description, taskId);
        setStatus();
        setDuration();
        setStartTime();
        setEndTime();
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
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

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public List<SubTask> getSubTasksList() {
        return subTasksList;
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
            setStatus(TaskStatus.NEW);
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
            setStatus(TaskStatus.DONE);
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
            setStatus(TaskStatus.IN_PROGRESS);
        } else {
            setStatus(TaskStatus.NEW);
        }
    }

    public void setDuration() {
        if (subTasksList.isEmpty()) {
            setDuration(Duration.ofMinutes(0));
            return;
        }

        Duration allSubTaskDuration = Duration.ofMinutes(0);

        for (SubTask st : subTasksList) {
            allSubTaskDuration = allSubTaskDuration.plus(st.getDuration());
        }

        setDuration(Duration.ofMinutes(allSubTaskDuration.toMinutes()));
    }

    public void setStartTime() {
        if (subTasksList.isEmpty()) {
            setStartTime(LocalDateTime.parse(LocalDateTime.now().format(FORMATTER), FORMATTER));
            return;
        }

        if (subTasksList.size() == 1) {
            setStartTime(subTasksList.getFirst().getStartTime());
            return;
        }

        LocalDateTime minStartSubTaskTime = subTasksList.getFirst().getStartTime();

        for (SubTask st : subTasksList) {
            if (st.getStartTime().isBefore(minStartSubTaskTime)) {
                minStartSubTaskTime = st.getStartTime();
            }
        }

        setStartTime(minStartSubTaskTime);

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
    public String toString() {
        return "Epic{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status=" + getStatus() +
                ", duration=" + getDuration() +
                ", startTime=" + getStartTime() +
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