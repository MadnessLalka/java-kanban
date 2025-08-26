package ru.yandex.javacourse.kanban.manager.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.javacourse.kanban.manager.TaskManager;
import ru.yandex.javacourse.kanban.manager.handler.exception.HttpHandlerQueryException;

import java.io.IOException;
import java.io.OutputStream;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;

    public TaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws HttpHandlerQueryException {
        try (OutputStream os = exchange.getResponseBody()) {
            String method = exchange.getRequestMethod();
            System.out.println("Обработка метода " + method + " /tasks");

            String path = exchange.getRequestURI().getPath();

            String[] requestString = path.split("/");
            String resource = "";
            switch (method) {
                case "GET" -> {
                    if (requestString.length == 2) {
//                        resource = String.valueOf(taskManager.getAllTaskList());
                        sendText(exchange, String.valueOf(taskManager.getAllTaskList()));
                    }
                }
            }


//            exchange.sendResponseHeaders(200, 0);
//            os.write(resource.getBytes());

        } catch (IOException e) {
            throw new HttpHandlerQueryException("Ошибка при обращение к TaskHandler", e);
        }
    }
}
