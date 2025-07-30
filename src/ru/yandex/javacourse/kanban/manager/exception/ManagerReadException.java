package ru.yandex.javacourse.kanban.manager.exception;

public class ManagerReadException extends RuntimeException {
    public ManagerReadException(String message, Throwable cause) {
        super(message, cause);
    }
}
