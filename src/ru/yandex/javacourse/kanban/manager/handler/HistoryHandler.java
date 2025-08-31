package ru.yandex.javacourse.kanban.manager.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.javacourse.kanban.manager.HistoryManager;
import ru.yandex.javacourse.kanban.manager.handler.exception.HttpHandlerQueryException;

import java.io.IOException;

import static ru.yandex.javacourse.kanban.manager.handler.Stubs.HTTP_500;

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
                    resource = gson.toJson(historyManager.getHistory());
                    System.out.println(resource);
                    sendText(exchange, resource);
                }
            } else {
                System.out.println("Такого запроса нет в списке");
                sendNotFound(exchange, "Такого запроса нет в списке");
            }
        } catch (IOException e) {
            exchange.sendResponseHeaders(HTTP_500,0);
            throw new HttpHandlerQueryException("Ошибка при обращение к EpicHandler", e);
        } finally {
            exchange.close();
        }
    }
}
