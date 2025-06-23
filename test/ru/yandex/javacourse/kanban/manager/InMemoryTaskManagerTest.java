package ru.yandex.javacourse.kanban.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryTaskManagerTest{
    public static InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
//    public static int idCounter;

    @BeforeEach
    void beforeEach() {
        inMemoryTaskManager.


    }

//    @AfterEach
//    void afterEach() {
//        inMemoryTaskManager = new InMemoryTaskManager();
//    }

    @DisplayName("Получение нового ID")
    @Test
    void getNewId_Create_Id() {
        //given
        int expectedId = 1;

        //when
        int currentId = inMemoryTaskManager.getNewId();
        System.out.println(currentId);
        //then
        assertEquals(expectedId, currentId);
    }

}


