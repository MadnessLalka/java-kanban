package ru.yandex.javacourse.kanban.manager.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.javacourse.kanban.manager.TaskManager;
import ru.yandex.javacourse.kanban.manager.exception.IntersectionException;
import ru.yandex.javacourse.kanban.manager.exception.NotFoundException;
import ru.yandex.javacourse.kanban.manager.handler.exception.HttpHandlerQueryException;
import ru.yandex.javacourse.kanban.task.Epic;

import java.io.IOException;
import java.util.List;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public EpicHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws HttpHandlerQueryException {
        try {
            String method = exchange.getRequestMethod();
            System.out.println("Обработка метода " + method + " /epics");

            String path = exchange.getRequestURI().getPath();

            String[] requestString = path.split("/");
            String resource = "";
            switch (method) {
                case "GET" -> {
                    if (requestString.length == 2) {
                        List<Epic> epicList = taskManager.getAllEpicList();
                        resource = gson.toJson(epicList);
                        System.out.println(resource);
                        sendText(exchange, resource);
                    } else if (requestString.length == 3 && !requestString[2].isBlank()) {
                        try {
                            resource = gson.toJson(taskManager.getEpicById(Integer.parseInt(requestString[2])));
                            System.out.println(resource);
                            sendText(exchange, resource);
                        } catch (NotFoundException e) {
                            sendNotFound(exchange, e.getLocalizedMessage());
                        }
                    } else if (requestString.length == 4 &&
                            !requestString[2].isBlank() &&
                            requestString[3].equals("subtasks")) {
                        try {
                            Epic currentEpic = taskManager.getEpicById(Integer.parseInt(requestString[2]));
                            resource = gson.toJson(taskManager.getAllSubTaskByEpic(currentEpic));
                            System.out.println(resource);
                            sendText(exchange, resource);
                        } catch (NotFoundException e) {
                            sendNotFound(exchange, e.getLocalizedMessage());
                        }
                    }
                }
                case "POST" -> {
                    String[] dataQuery = exchange.getRequestURI().getQuery().split("&");
                    String name = dataQuery[0].split("=")[1];
                    String description = dataQuery[1].split("=")[1];

                    if (requestString.length == 2) {
                        try {
                            taskManager.createEpic(
                                    new Epic(name,
                                            description,
                                            taskManager.getNewId()
                                    )
                            );
                            exchange.sendResponseHeaders(201, 0);
                            exchange.close();
                        } catch (IntersectionException e) {
                            sendHasOverlaps(exchange, e.getLocalizedMessage());
                        }
                    }
                }
                case "DELETE" -> {
                    int id = Integer.parseInt(requestString[2]);

                    try {
                        taskManager.removeEpicById(id);
                        exchange.sendResponseHeaders(200, 0);
                        exchange.close();
                    } catch (NotFoundException e) {
                        sendNotFound(exchange, e.getLocalizedMessage());
                    }
                }
                default -> {
                    System.out.println("Такого запроса нет в списке");
                    sendNotFound(exchange, "Такого запроса нет в списке");
                }
            }
        } catch (IOException e) {
            throw new HttpHandlerQueryException("Ошибка при обращение к EpicHandler", e);
        }
    }
}
