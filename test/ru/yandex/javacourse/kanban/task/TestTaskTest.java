package ru.yandex.javacourse.kanban.task;

import java.time.Duration;
import java.time.LocalDateTime;

public class TestTaskTest extends Task {

    public TestTaskTest(String name, String description, int id, TaskStatus status, Duration duration, LocalDateTime startTime) {
        super(name, description, id, status, duration, startTime);
    }

    protected TestTaskTest(String name, String description, int id) {
        super(name, description, id);
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public LocalDateTime getEndTime() {
        return super.getEndTime();
    }

    @Override
    public Duration getDuration() {
        return super.getDuration();
    }

    @Override
    public LocalDateTime getStartTime() {
        return super.getStartTime();
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
    public TaskStatus getStatus() {
        return super.getStatus();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}