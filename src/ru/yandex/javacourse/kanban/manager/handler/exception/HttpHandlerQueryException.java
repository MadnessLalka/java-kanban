package ru.yandex.javacourse.kanban.manager.handler.exception;

import java.io.IOException;

public class HttpHandlerQueryException extends IOException {
    public HttpHandlerQueryException(String message, Throwable e) {
        super(message, e);
    }
}
