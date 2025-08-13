package ru.yandex.javacourse.kanban.task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

import static java.util.Collections.min;

public class Epic extends Task {
    private TaskStatus status;
    private Duration duration;
    private LocalDateTime starTime;
    private LocalDateTime endTime;

    private final ArrayList<SubTask> subTasksList = new ArrayList<>();

    public Epic(String name, String description, int taskId) {
        super(name, description, taskId);
        this.status = TaskStatus.NEW;
        this.duration = Duration.ofMinutes(0);
        this.starTime = null;
        this.endTime = null;
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

    public LocalDateTime getStarTime() {
        return starTime;
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

    public void setDuration(){
        if (subTasksList.isEmpty()) {
            this.duration = Duration.ofMinutes(0);
            return;
        }

        Duration allSubTaskDuration = null;

        for (SubTask st : subTasksList) {
            allSubTaskDuration.plusMinutes(st.getDuration().toMinutes());
        }

        this.duration = allSubTaskDuration;
    }

    public void setStarAndEndTime(){
        if (subTasksList.isEmpty()) {
            this.starTime = null;
            this.endTime = null;
            return;
        }

        LocalDateTime minStartSubTaskTime = subTasksList.getFirst().getStartTime();
        LocalDateTime maxStartSubTaskTime = subTasksList.getFirst().getEndTime();

        for (SubTask st : subTasksList) {
            if(st.getStartTime().isBefore(minStartSubTaskTime)){
                minStartSubTaskTime = st.getStartTime();
            }

            if(st.getEndTime().isAfter(maxStartSubTaskTime)){
                maxStartSubTaskTime = st.getEndTime();
            }
        }

        this.starTime = minStartSubTaskTime;
        this.endTime = maxStartSubTaskTime;

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
                ", starTime=" + starTime +
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