package ru.yandex.javacourse.kanban.manager;

import java.time.format.DateTimeFormatter;

class Stubs {
    static final String FILE_HEADER = "id,type,name,status,description,epic,duration,startTime,endTime";
    static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy MM dd HH mm");

    private Stubs() {
    }
}
