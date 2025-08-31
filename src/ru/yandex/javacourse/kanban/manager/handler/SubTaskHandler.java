package ru.yandex.javacourse.kanban.manager.handler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.javacourse.kanban.manager.TaskManager;
import ru.yandex.javacourse.kanban.manager.exception.IntersectionException;
import ru.yandex.javacourse.kanban.manager.exception.NotFoundException;
import ru.yandex.javacourse.kanban.manager.handler.exception.HttpHandlerQueryException;
import ru.yandex.javacourse.kanban.task.SubTask;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static ru.yandex.javacourse.kanban.manager.handler.Stubs.*;

public class SubTaskHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public SubTaskHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            System.out.println("Обработка метода " + method + " /subtasks");

            String path = exchange.getRequestURI().getPath();

            String[] requestString = path.split("/");
            String resource = "";
            switch (method) {
                case "GET" -> {
                    if (requestString.length == 2) {
                        resource = gson.toJson(taskManager.getAllSubTaskList());
                        System.out.println(resource);
                        sendText(exchange, resource);
                    } else if (requestString.length == 3 && !requestString[2].isBlank()) {
                        try {
                            resource = gson.toJson(taskManager.getSubTaskById(Integer.parseInt(requestString[2])));
                            System.out.println(resource);
                            sendText(exchange, resource);
                        } catch (NotFoundException e) {
                            sendNotFound(exchange, e.getLocalizedMessage());
                        }
                    }
                }
                case "POST" -> {
                    String requestJson = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                    JsonObject subTaskObject = JsonParser.parseString(requestJson).getAsJsonObject();

                    if (requestString.length == 2 && !requestJson.contains("id")) {
                        int newId = taskManager.getNewId();
                        subTaskObject.addProperty("id", newId);
                        SubTask newSubTask = gson.fromJson(subTaskObject, SubTask.class);

                        try {
                            taskManager.createSubTask(newSubTask);
                            exchange.sendResponseHeaders(HTTP_201, 0);
                        } catch (IntersectionException e) {
                            sendHasOverlaps(exchange, e.getLocalizedMessage());
                        } finally {
                            exchange.close();
                        }
                    } else if (requestString.length == 2 && requestJson.contains("id")) {
                        SubTask newSubTask = gson.fromJson(subTaskObject, SubTask.class);
                        try {
                            taskManager.updateSubTask(newSubTask);
                            exchange.sendResponseHeaders(HTTP_201, 0);
                        } catch (NotFoundException e) {
                            sendNotFound(exchange, e.getLocalizedMessage());
                        } catch (IntersectionException e) {
                            sendHasOverlaps(exchange, e.getLocalizedMessage());
                        } finally {
                            exchange.close();
                        }
                    }
                }
                case "DELETE" -> {
                    int id = Integer.parseInt(requestString[2]);

                    try (exchange) {
                        taskManager.removeSubTaskById(id);
                        exchange.sendResponseHeaders(HTTP_200, 0);
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
            exchange.sendResponseHeaders(HTTP_500, 0);
            throw new HttpHandlerQueryException("Ошибка при обращение к SubTaskHandler", e);
        } finally {
            exchange.close();
        }
    }
}
