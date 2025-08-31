package ru.yandex.javacourse.kanban.manager.httpmanager;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.javacourse.kanban.manager.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.yandex.javacourse.kanban.StubsTest.SERVER_URL;

class HttpHistoryTaskManagerTest {
    TaskManager manager = new InMemoryTaskManager();
    HistoryManager historyManager = new InMemoryHistoryManager();
    HttpTaskServer taskServer = new HttpTaskServer(manager, historyManager);
    Gson gson = taskServer.getGson();

    public HttpHistoryTaskManagerTest() throws IOException {
    }

    @BeforeEach
    void setUp() {
        manager.removeAllTask();
        manager.removeAllSubTask();
        manager.removeAllEpic();

        taskServer.start();
    }

    @AfterEach
    void shutDown() {
        taskServer.stop();
    }

    @DisplayName("Получение истории")
    @Test
    void getTask_Get_PrioritizedTaskFromHttpServer() throws IOException, InterruptedException {
        // given
        JsonObject jsonEpic = new JsonObject();
        jsonEpic.addProperty("name", "Epic");
        jsonEpic.addProperty("description", "Testing epic 1");

        String epicJson = gson.toJson(jsonEpic);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(SERVER_URL + "/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        JsonObject jsonObjectSubTask = new JsonObject();
        jsonObjectSubTask.addProperty("name", "Test 2");
        jsonObjectSubTask.addProperty("description", "Testing task 2");
        jsonObjectSubTask.addProperty("status", "NEW");
        jsonObjectSubTask.addProperty("epicId", "0");
        jsonObjectSubTask.addProperty("duration", "PT29M");
        jsonObjectSubTask.addProperty("startTime", "2025 01 01 01 01");

        String subTaskJson = gson.toJson(jsonObjectSubTask);
        //when

        url = URI.create(SERVER_URL + "/subtasks");
        request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(subTaskJson)).build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        historyManager.add(manager.getEpicById(0));
        historyManager.add(manager.getSubTaskById(1));

        url = URI.create(SERVER_URL + "/history");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        client.close();

        // then
        JsonArray jsonArray = gson.fromJson(response.body(), JsonArray.class);
        assertEquals(2, jsonArray.size(), "Некорректное количество подзадач");
    }
}
