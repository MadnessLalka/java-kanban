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
    private final HttpServer httpServer;


    public HttpServer getHttpServer() {
        return httpServer;
    }

    public TaskManager getManager() {
        return manager;
    }

    public Gson getGson() {
        return gson;
    }

    public HttpTaskServer(TaskManager manager, HistoryManager historyManager) throws IOException {
        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);
        this.manager = manager;

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        gsonBuilder.setPrettyPrinting();
        this.gson = gsonBuilder.create();

        httpServer.createContext("/tasks", new TaskHandler(manager, gson));
        httpServer.createContext("/subtasks", new SubTaskHandler(manager, gson));
        httpServer.createContext("/epics", new EpicHandler(manager, gson));
        httpServer.createContext("/history", new HistoryHandler(historyManager, gson));
        httpServer.createContext("/prioritized", new PrioritizedHandler(manager, gson));
    }

    public void start() {
        getHttpServer().start();
    }

    public void stop() {
        getHttpServer().stop(0);
    }

    public static void main(String[] args) throws HttpServerCreateException {
        TaskManager manager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();

        try {
            HttpTaskServer httpTaskServer = new HttpTaskServer(manager, historyManager);
            httpTaskServer.start();
            System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
            httpTaskServer.stop();
        } catch (IOException e) {
            throw new HttpServerCreateException("Ошибка при попытке запуска сервера. Проверьте порт - " + PORT, e);
        }
    }
}
