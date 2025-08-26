package ru.yandex.javacourse.kanban.manager.exception;

import java.io.IOException;

public class HttpServerCreateException extends IOException {
    public HttpServerCreateException(String message, Throwable e) {
        super(message, e);
    }
}
