import task.Epic;
import task.SubTask;
import task.Task;
import task.TaskStatus;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = new TaskManager();

        Task firstTask = new Task("Первая задача", "Описание первой задачи", taskManager.getNewId(),
                TaskStatus.NEW);
        Task secondTask = new Task("Вторая задача", "Описание второй задачи", taskManager.getNewId(),
                TaskStatus.NEW);

        taskManager.createTask(firstTask);
        taskManager.createTask(secondTask);

        Epic firstEpic = new Epic("Первый эпик", "Описание первого Эпика", taskManager.getNewId());
        taskManager.createEpic(firstEpic);

        SubTask firstSubTask = new SubTask("Первая подзадача", "Описание первой подзадачи",
                taskManager.getNewId(), TaskStatus.DONE, firstEpic.getId());
        SubTask secondSubTask = new SubTask("Вторая подзадача", "Описание второй подзадачи",
                taskManager.getNewId(), TaskStatus.IN_PROGRESS, firstEpic.getId());

        taskManager.createSubTask(firstSubTask);
        taskManager.createSubTask(secondSubTask);

        Epic secondEpic = new Epic("Второй эпик", "Описание второго Эпика", taskManager.getNewId());
        taskManager.createEpic(secondEpic);

        SubTask thirdSubTask = new SubTask("Первая подзадача", "Описание первой подзадачи второго эпика",
                taskManager.getNewId(), TaskStatus.NEW, secondEpic.getId());

        taskManager.createSubTask(thirdSubTask);

        System.out.println(taskManager.getAllTaskList());
        System.out.println(taskManager.getAllEpicList());
        System.out.println(taskManager.getAllSubTaskList());
        System.out.println();

        taskManager.updateTask(new Task("Первая задача", "Описание изменено", firstTask.getId(),
                TaskStatus.DONE));

        taskManager.updateEpic(new Epic("Название изменено", "sdsd", 2));
        taskManager.updateSubTask(new SubTask("Первая подзадача изменена", "Описание первой подзадачи новое",
                firstSubTask.getId(), TaskStatus.DONE, firstEpic.getId()));

        System.out.println(taskManager.getAllTaskList());
        System.out.println(taskManager.getAllEpicList());
        System.out.println(taskManager.getAllSubTaskList());
        System.out.println();

        taskManager.removeTaskById(firstTask.getId());
        taskManager.removeEpicById(firstEpic.getId());

        System.out.println(taskManager.getAllTaskList());
        System.out.println(taskManager.getAllEpicList());
        System.out.println(taskManager.getAllSubTaskList());
        System.out.println();

    }
}
