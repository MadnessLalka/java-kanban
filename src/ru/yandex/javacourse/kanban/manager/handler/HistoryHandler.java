package ru.yandex.javacourse.kanban.manager.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.javacourse.kanban.manager.HistoryManager;
import ru.yandex.javacourse.kanban.manager.handler.exception.HttpHandlerQueryException;

import java.io.IOException;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {
    private final HistoryManager historyManager;
    private final Gson gson;

    public HistoryHandler(HistoryManager historyManager, Gson gson) {
        this.historyManager = historyManager;
        this.gson = gson;
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
