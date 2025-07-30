package ru.yandex.javacourse.kanban.manager.exception;

public class InvalidTaskTypeException extends Exception {
    public InvalidTaskTypeException(String message) {
        super(message);
    }
}
