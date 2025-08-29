package ru.yandex.javacourse.kanban;

import java.time.format.DateTimeFormatter;

import static ru.yandex.javacourse.kanban.manager.Stubs.PORT;

public class StubsTest {
    public static final String FILE_HEADER = "id,type,name,status,description,epic,duration,startTime,endTime";
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy MM dd HH mm");
    public static final String SERVER_URL = "http://localhost:"+ PORT;

    private StubsTest() {
    }
}
