package ru.yandex.javacourse.kanban.manager.httpmanager;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.javacourse.kanban.manager.*;
import ru.yandex.javacourse.kanban.task.Epic;
import ru.yandex.javacourse.kanban.task.SubTask;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.yandex.javacourse.kanban.StubsTest.SERVER_URL;

class HttpEpicManagerTest {
    TaskManager manager = new InMemoryTaskManager();
    HistoryManager historyManager = new InMemoryHistoryManager();
    HttpTaskServer taskServer = new HttpTaskServer(manager, historyManager);
    Gson gson = taskServer.getGson();

    public HttpEpicManagerTest() throws IOException {
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

    @DisplayName("Добавление эпика")
    @Test
    void addEpic_Add_EpicFromHttpServer() throws IOException, InterruptedException {
        // given
        JsonObject jsonEpic = new JsonObject();
        jsonEpic.addProperty("name", "Epic");
        jsonEpic.addProperty("description", "Testing epic 1");

        String epicJson = gson.toJson(jsonEpic);

        //when
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(SERVER_URL + "/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        client.close();

        // then
        assertEquals(201, response.statusCode());
        List<Epic> subTasksFromManager = manager.getAllEpicList();

        assertNotNull(subTasksFromManager, "эпики не возвращаются");
        assertEquals(1, subTasksFromManager.size(), "Некорректное количество эпиков");
        assertEquals("Epic", subTasksFromManager.get(0).getName(), "Некорректное имя эпика");
    }

    @DisplayName("Удаление эпика")
    @Test
    void deleteEpic_Delete_EpicToHttpServer() throws IOException, InterruptedException {
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

        // when
        url = URI.create(SERVER_URL + "/epics/0");
        request = HttpRequest.newBuilder().uri(url).DELETE().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        //then
        List<Epic> tasksFromManager = manager.getAllEpicList();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(0, tasksFromManager.size(), "Некорректное количество задач");
    }

    @DisplayName("Получение эпика по id")
    @Test
    void getEpic_Get_EpicToHttpServerByID() throws IOException, InterruptedException {
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

        // when
        url = URI.create(SERVER_URL + "/epics/0");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        List<Epic> tasksFromManager = manager.getAllEpicList();

        JsonObject jsonObjectTask1 = gson.fromJson(response.body(), JsonObject.class);
        assertEquals(jsonObjectTask1.get("name").getAsString(), tasksFromManager.get(0).getName(), "Некорректное имя подзадачи");
    }

    @DisplayName("Получение подзадач эпика по id эпика")
    @Test
    void getSubTask_Get_SubTaskToHttpServerByEpicID() throws IOException, InterruptedException {
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
        url = URI.create(SERVER_URL + "/subtasks");
        request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(subTaskJson)).build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        // when
        url = URI.create(SERVER_URL + "/epics/0/subtasks");
        HttpRequest request1 = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response1.statusCode());

        List<SubTask> tasksFromManager = manager.getAllSubTaskList();
        System.out.println(response.body());
        JsonArray jsonObjectSubTask1 = gson.fromJson(response1.body(), JsonArray.class);

        assertEquals(jsonObjectSubTask1.get(0).getAsJsonObject().get("name").getAsString(),
                tasksFromManager.get(0).getName(), "Некорректное имя подзадачи");
    }


    @DisplayName("Проверка на исключение эпик не найден")
    @Test
    void getThrow_GetThrow_NotFoundEpic() throws IOException, InterruptedException {
        //given
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(SERVER_URL + "/epics/0");

        //when
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        //then
        assertEquals(404, response.statusCode());
    }

    @DisplayName("Проверка на исключение эпик не найден по id")
    @Test
    void getThrow_GetThrow_NotFoundEpicById() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(SERVER_URL + "/epics/0/subtasks");
        HttpRequest request1 = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response1.statusCode());
    }


}
