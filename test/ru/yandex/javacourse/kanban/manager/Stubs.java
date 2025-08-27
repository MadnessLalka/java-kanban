package ru.yandex.javacourse.kanban.manager;

import java.time.format.DateTimeFormatter;

class Stubs {
    static final String FILE_HEADER_TEST = "id,type,name,status,description,epic,duration,startTime,endTime";
    static final DateTimeFormatter FORMATTER_TEST = DateTimeFormatter.ofPattern("yyyy MM dd HH mm");

    private Stubs() {
    }
}
