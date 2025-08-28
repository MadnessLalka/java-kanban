package ru.yandex.javacourse.kanban.manager.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.javacourse.kanban.manager.HistoryManager;
import ru.yandex.javacourse.kanban.manager.TaskManager;
import ru.yandex.javacourse.kanban.manager.handler.adapter.DurationAdapter;
import ru.yandex.javacourse.kanban.manager.handler.adapter.LocalDateAdapter;
import ru.yandex.javacourse.kanban.manager.handler.exception.HttpHandlerQueryException;
import ru.yandex.javacourse.kanban.task.Epic;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {
    private final HistoryManager historyManager;
    private final Gson gson;

    public HistoryHandler(HistoryManager historyManager) {
        this.historyManager = historyManager;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        gsonBuilder.setPrettyPrinting();
        gson = gsonBuilder.create();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            System.out.println("Обработка метода " + method + " /history");

            String path = exchange.getRequestURI().getPath();

            String[] requestString = path.split("/");
            String resource = "";
            if (method.equals("GET")) {
                if (requestString.length == 2) {
                    System.out.println(historyManager.getHistory());
                    resource = gson.toJson(historyManager.getHistory());
                    System.out.println(resource);
                    sendText(exchange, resource);
                }
            } else {
                System.out.println("Такого запроса нет в списке");
                sendNotFound(exchange, "Такого запроса нет в списке");
            }
        } catch (IOException e) {
            throw new HttpHandlerQueryException("Ошибка при обращение к EpicHandler", e);
        }
    }
}
