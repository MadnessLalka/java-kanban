package ru.yandex.javacourse.kanban.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import ru.yandex.javacourse.kanban.manager.exception.HttpServerCreateException;
import ru.yandex.javacourse.kanban.manager.handler.*;
import ru.yandex.javacourse.kanban.manager.handler.adapter.DurationAdapter;
import ru.yandex.javacourse.kanban.manager.handler.adapter.LocalDateAdapter;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

import static ru.yandex.javacourse.kanban.manager.Stubs.PORT;

public class HttpTaskServer {
    private final TaskManager manager;
    private final Gson gson;

    public TaskManager getManager() {
        return manager;
    }

    public Gson getGson() {
        return gson;
    }

    public HttpTaskServer(TaskManager manager) {
        this.manager = manager;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        gsonBuilder.setPrettyPrinting();
        this.gson = gsonBuilder.create();
    }

    public static void main(String[] args) throws HttpServerCreateException {
        TaskManager manager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();
        manager.setHistoryManager(historyManager);
        HttpTaskServer httpTaskServer = new HttpTaskServer(manager);

        try {
            HttpServer httpServer = HttpServer.create();
            httpServer.bind(new InetSocketAddress(PORT), 0);

            httpServer.createContext("/tasks", new TaskHandler(manager, httpTaskServer.getGson()));
            httpServer.createContext("/subtasks", new SubTaskHandler(manager , httpTaskServer.getGson()));
            httpServer.createContext("/epics", new EpicHandler(manager , httpTaskServer.getGson()));
            httpServer.createContext("/history", new HistoryHandler(historyManager , httpTaskServer.getGson()));
            httpServer.createContext("/prioritized", new PrioritizedHandler(manager , httpTaskServer.getGson()));
            httpServer.start();
            System.out.println("HTTP-сервер запущен на " + PORT + " порту!");

        } catch (IOException e) {
            throw new HttpServerCreateException("Ошибка при попытке запуска сервера. Проверьте порт - " + PORT, e);
        }
    }
}
