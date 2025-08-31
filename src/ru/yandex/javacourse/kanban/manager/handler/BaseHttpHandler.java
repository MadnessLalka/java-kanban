package ru.yandex.javacourse.kanban.manager.handler;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static ru.yandex.javacourse.kanban.manager.handler.Stubs.*;

public class BaseHttpHandler {
    protected void sendText(HttpExchange h, String text) throws IOException {
        try {
            byte[] resp = text.getBytes(StandardCharsets.UTF_8);
            h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
            h.sendResponseHeaders(HTTP_200, resp.length);
            h.getResponseBody().write(resp);
        } finally {
            h.close();
        }
    }

    protected void sendNotFound(HttpExchange h, String text) throws IOException {
        try {
            byte[] resp = text.getBytes(StandardCharsets.UTF_8);
            h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
            h.sendResponseHeaders(HTTP_404, resp.length);
            h.getResponseBody().write(resp);
        } finally {
            h.close();
        }
    }

    protected void sendHasOverlaps(HttpExchange h, String text) throws IOException {
        try {
            byte[] resp = text.getBytes(StandardCharsets.UTF_8);
            h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
            h.sendResponseHeaders(HTTP_406, resp.length);
            h.getResponseBody().write(resp);
        } finally {
            h.close();
        }
    }


}
