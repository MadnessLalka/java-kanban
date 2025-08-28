package ru.yandex.javacourse.kanban;

import java.time.format.DateTimeFormatter;

public class StubsTest {
    public static final String FILE_HEADER = "id,type,name,status,description,epic,duration,startTime,endTime";
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy MM dd HH mm");

    private StubsTest() {
    }
}
