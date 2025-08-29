package ru.yandex.javacourse.kanban.manager;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.javacourse.kanban.task.Task;

import javax.sound.midi.Soundbank;
import java.io.IOException;
import java.net.Socket;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.yandex.javacourse.kanban.StubsTest.SERVER_URL;

class HttpTaskServerTest {
    // создаём экземпляр InMemoryTaskManager
    TaskManager manager = new InMemoryTaskManager();
    // передаём его в качестве аргумента в конструктор HttpTaskServer
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = taskServer.getGson();

    public HttpTaskServerTest() throws IOException {
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

    @DisplayName("Добавление задачи")
    @Test
    void addTask_Add_TaskFromHttpServer() throws IOException, InterruptedException {
        // given
        JsonObject jsonObjectTask = new JsonObject();
        jsonObjectTask.addProperty("name", "Test 2");
        jsonObjectTask.addProperty("description", "Testing task 2");
        jsonObjectTask.addProperty("status", "NEW");
        jsonObjectTask.addProperty("duration", "PT29M");
        jsonObjectTask.addProperty("startTime", "2025 01 01 01 01");

        // when
        String taskJson = gson.toJson(jsonObjectTask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(SERVER_URL + "/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        client.close();

        // then
        assertEquals(201, response.statusCode());
        List<Task> tasksFromManager = manager.getAllTaskList();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 2", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    @DisplayName("Обновление задачи")
    @Test
    void updateTask_Update_TaskToHttpServer() throws IOException, InterruptedException {
        // given
        JsonObject jsonObjectTask = new JsonObject();
        jsonObjectTask.addProperty("name", "Test 2");
        jsonObjectTask.addProperty("description", "Testing task 2");
        jsonObjectTask.addProperty("status", "NEW");
        jsonObjectTask.addProperty("duration", "PT29M");
        jsonObjectTask.addProperty("startTime", "2025 01 01 01 01");

        JsonObject updatedJsonObjectTask = new JsonObject();
        updatedJsonObjectTask.addProperty("name", "Test 2 обновлённый");
        updatedJsonObjectTask.addProperty("description", "Testing task 2");
        updatedJsonObjectTask.addProperty("status", "NEW");
        updatedJsonObjectTask.addProperty("id", "0");
        updatedJsonObjectTask.addProperty("duration", "PT29M");
        updatedJsonObjectTask.addProperty("startTime", "2025 04 01 01 01");

        String taskJson = gson.toJson(jsonObjectTask);
        String taskJsonUpdated = gson.toJson(updatedJsonObjectTask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(SERVER_URL + "/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());


        //when
        request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJsonUpdated)).build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        //then
        List<Task> tasksFromManager = manager.getAllTaskList();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 2 обновлённый", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    @DisplayName("Удаление задачи")
    @Test
    void deleteTask_Delete_TaskToHttpServer() throws IOException, InterruptedException {
        // given
        JsonObject jsonObjectTask = new JsonObject();
        jsonObjectTask.addProperty("name", "Test 2");
        jsonObjectTask.addProperty("description", "Testing task 2");
        jsonObjectTask.addProperty("status", "NEW");
        jsonObjectTask.addProperty("duration", "PT29M");
        jsonObjectTask.addProperty("startTime", "2025 01 01 01 01");

        String taskJson = gson.toJson(jsonObjectTask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(SERVER_URL + "/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());


        // when
        url = URI.create(SERVER_URL + "/tasks/0");
        request = HttpRequest.newBuilder().uri(url).DELETE().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());


        List<Task> tasksFromManager = manager.getAllTaskList();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(0, tasksFromManager.size(), "Некорректное количество задач");
    }

    @DisplayName("Получение задачи по id")
    @Test
    void getTask_Get_TaskToHttpServerByID() throws IOException, InterruptedException {
        // given
        JsonObject jsonObjectTask = new JsonObject();
        jsonObjectTask.addProperty("name", "Test 2");
        jsonObjectTask.addProperty("description", "Testing task 2");
        jsonObjectTask.addProperty("status", "NEW");
        jsonObjectTask.addProperty("duration", "PT29M");
        jsonObjectTask.addProperty("startTime", "2025 01 01 01 01");

        String taskJson = gson.toJson(jsonObjectTask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(SERVER_URL + "/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        url = URI.create(SERVER_URL + "/tasks");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        JsonObject jsonObjectTask1 = gson.toJson(response.body());

        // when
        url = URI.create(SERVER_URL + "/tasks/0");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        JsonObject jsonObjectTask2 = gson.fromJson(response.body(), JsonObject.class);

        assertEquals(jsonObjectTask1, jsonObjectTask2, "Объекты должны быть идентичны");
    }

}
