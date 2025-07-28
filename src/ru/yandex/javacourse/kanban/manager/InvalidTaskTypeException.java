package ru.yandex.javacourse.kanban.manager;

public class InvalidTaskTypeException extends Exception {
    public InvalidTaskTypeException(String message) {
        super(message);
    }
}
