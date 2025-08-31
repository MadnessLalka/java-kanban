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
import ru.yandex.javacourse.kanban.task.Task;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static ru.yandex.javacourse.kanban.manager.handler.Stubs.*;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public TaskHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();
            System.out.println("Обработка метода " + method + " /tasks");

            String[] requestString = path.split("/");
            String resource = "";
            switch (method) {
                case "GET" -> {
                    if (requestString.length == 2) {
                        resource = gson.toJson(taskManager.getAllTaskList());
                        System.out.println(resource);
                        sendText(exchange, resource);
                    } else if (requestString.length == 3 && !requestString[2].isBlank()) {
                        try {
                            resource = gson.toJson(taskManager.getTaskById(Integer.parseInt(requestString[2])));
                            System.out.println(resource);
                            sendText(exchange, resource);
                        } catch (NotFoundException e) {
                            sendNotFound(exchange, e.getLocalizedMessage());
                        }
                    }
                }
                case "POST" -> {
                    String requestJson = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                    JsonObject taskObject = JsonParser.parseString(requestJson).getAsJsonObject();

                    if (requestString.length == 2 && !requestJson.contains("id")) {
                        int newId = taskManager.getNewId();
                        taskObject.remove("id");
                        taskObject.addProperty("id", newId);
                        Task newTask = gson.fromJson(taskObject, Task.class);

                        try {
                            taskManager.createTask(newTask);
                            exchange.sendResponseHeaders(HTTP_201, 0);
                        } catch (IntersectionException e) {
                            sendHasOverlaps(exchange, e.getLocalizedMessage());
                        } finally {
                            exchange.close();
                        }
                    } else if (requestString.length == 2 && requestJson.contains("id")) {
                        Task newTask = gson.fromJson(taskObject, Task.class);
                        try {
                            taskManager.updateTask(newTask);
                            exchange.sendResponseHeaders(HTTP_201, 0);
                        } catch (NotFoundException e) {
                            sendNotFound(exchange, e.getLocalizedMessage());
                        } catch (IntersectionException e) {
                            sendHasOverlaps(exchange, e.getLocalizedMessage());
                        }
                    }
                }
                case "DELETE" -> {
                    int id = Integer.parseInt(requestString[2]);

                    try {
                        taskManager.removeTaskById(id);
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
            throw new HttpHandlerQueryException("Ошибка при обращение к TaskHandler", e);
        } finally {
            exchange.close();
        }
    }
}
