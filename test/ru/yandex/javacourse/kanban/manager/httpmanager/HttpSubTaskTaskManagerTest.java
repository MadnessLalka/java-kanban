package ru.yandex.javacourse.kanban.manager.httpmanager;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.javacourse.kanban.manager.HttpTaskServer;
import ru.yandex.javacourse.kanban.manager.InMemoryTaskManager;
import ru.yandex.javacourse.kanban.manager.TaskManager;
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

class HttpSubTaskTaskManagerTest {
    TaskManager manager = new InMemoryTaskManager();
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = taskServer.getGson();

    public HttpSubTaskTaskManagerTest() throws IOException {
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

    @DisplayName("Добавление подзадачи")
    @Test
    void addSubTask_Add_SubTaskTaskFromHttpServer() throws IOException, InterruptedException {
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
        client.close();

        // then
        assertEquals(201, response.statusCode());
        List<SubTask> subTasksFromManager = manager.getAllSubTaskList();

        assertNotNull(subTasksFromManager, "подзадачи не возвращаются");
        assertEquals(1, subTasksFromManager.size(), "Некорректное количество подзадач");
        assertEquals("Test 2", subTasksFromManager.get(0).getName(), "Некорректное имя подзадачи");
    }

    @DisplayName("Обновление подзадачи")
    @Test
    void updateSubTask_Update_SubTaskTaskToHttpServer() throws IOException, InterruptedException {
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

        JsonObject jsonObjectUpdatedSubTask = new JsonObject();
        jsonObjectUpdatedSubTask.addProperty("name", "Test Обновлено");
        jsonObjectUpdatedSubTask.addProperty("description", "Testing task 2");
        jsonObjectUpdatedSubTask.addProperty("id", "1");
        jsonObjectUpdatedSubTask.addProperty("status", "NEW");
        jsonObjectUpdatedSubTask.addProperty("epicId", "0");
        jsonObjectUpdatedSubTask.addProperty("duration", "PT29M");
        jsonObjectUpdatedSubTask.addProperty("startTime", "2025 03 01 01 01");

        String updateSubTaskJson = gson.toJson(jsonObjectUpdatedSubTask);
        url = URI.create(SERVER_URL + "/subtasks");
        request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(updateSubTaskJson)).build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        client.close();

        // then
        assertEquals(201, response.statusCode());
        List<SubTask> subTasksFromManager = manager.getAllSubTaskList();

        assertNotNull(subTasksFromManager, "подзадачи не возвращаются");
        assertEquals(1, subTasksFromManager.size(), "Некорректное количество подзадач");
        assertEquals("Test Обновлено", subTasksFromManager.get(0).getName(), "Некорректное имя подзадачи");
    }
    @DisplayName("Удаление подзадачи")
    @Test
    void deleteSubTask_Delete_SubTaskToHttpServer() throws IOException, InterruptedException {
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
        url = URI.create(SERVER_URL + "/subtasks/1");
        request = HttpRequest.newBuilder().uri(url).DELETE().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        //then
        List<SubTask> tasksFromManager = manager.getAllSubTaskList();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(0, tasksFromManager.size(), "Некорректное количество задач");
    }

    @DisplayName("Получение подзадачи по id")
    @Test
    void getSubTask_Get_SubTaskToHttpServerByID() throws IOException, InterruptedException {
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
        url = URI.create(SERVER_URL + "/subtasks/1");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        List<SubTask> tasksFromManager = manager.getAllSubTaskList();

        JsonObject jsonObjectTask1 = gson.fromJson(response.body(), JsonObject.class);
        assertEquals(jsonObjectTask1.get("name").getAsString(), tasksFromManager.get(0).getName(), "Некорректное имя подзадачи");
    }

    @DisplayName("Проверка на исключение подзадача не найдена")
    @Test
    void getThrow_GetThrow_NotFoundSubTask() throws IOException, InterruptedException {
        //given
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(SERVER_URL + "/subtasks/0");

        //when
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        //then
        assertEquals(404, response.statusCode());
    }

    @DisplayName("Проверка на исключение подзадача пересекается с существующей по времени")
    @Test
    void getThrow_GetThrow_IntersectionTask() throws IOException, InterruptedException {
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

        url = URI.create(SERVER_URL + "/subtasks");
        request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(subTaskJson)).build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(406, response.statusCode());
    }

}
