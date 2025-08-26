package ru.yandex.javacourse.kanban.manager;

import com.sun.net.httpserver.HttpServer;
import ru.yandex.javacourse.kanban.manager.exception.HttpServerCreateException;
import ru.yandex.javacourse.kanban.manager.handler.*;

import java.io.IOException;
import java.net.InetSocketAddress;

import static ru.yandex.javacourse.kanban.manager.Stubs.PORT;

public class HttpTaskServer {

    public static void main(String[] args) throws HttpServerCreateException {


        try {
            TaskManager taskManager = Managers.getDefault();

            HttpServer httpServer = HttpServer.create();
            httpServer.bind(new InetSocketAddress(PORT), 0);

            httpServer.createContext("/tasks", new TaskHandler(taskManager));
            httpServer.createContext("/subtasks", new SubTaskHandler());
            httpServer.createContext("/epics", new EpicHandler());
            httpServer.createContext("/history", new HistoryHandler());
            httpServer.createContext("/prioritized", new PrioritizedHandler());
            httpServer.start();
            System.out.println("HTTP-сервер запущен на " + PORT + " порту!");

        } catch (IOException e){
            throw new HttpServerCreateException("Ошибка при попытке запуска сервера. Проверьте порт - " + PORT, e);
        }

    }
}
