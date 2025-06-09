import task.Epic;
import task.SubTask;
import task.Task;
import task.TaskStatus;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = new TaskManager();

        Task firstTask = new Task("Первая задача", "Описание первой задачи", taskManager.getNewId(), TaskStatus.NEW);
        Task secondTask = new Task("Вторая задача", "Описание второй задачи", taskManager.getNewId(), TaskStatus.NEW);

        taskManager.createTask(firstTask);
        taskManager.createTask(secondTask);

        Epic firstEpic = new Epic("Первый эпик", "Описание первого Эпика", taskManager.getNewId());
        SubTask firstSubTask = new SubTask("Первая подзадача", "Описание первой подзадачи",
                taskManager.getNewId(), TaskStatus.NEW, firstEpic.getId());
        SubTask secondSubTask = new SubTask("Вторая подзадача", "Описание второй подзадачи",
                taskManager.getNewId(), TaskStatus.NEW, firstEpic.getId());

        taskManager.createEpic(firstEpic);
        taskManager.createSubTask(firstSubTask);
        taskManager.createSubTask(secondSubTask);

        Epic secondEpic = new Epic("Второй эпик", "Описание второго Эпика", taskManager.getNewId());
        SubTask thirdSubTask = new SubTask("Третья подзадача", "Описание третьей подзадачи",
                taskManager.getNewId(), TaskStatus.NEW, secondEpic.getId());

        taskManager.createEpic(secondEpic);
        taskManager.createSubTask(thirdSubTask);

        System.out.println(taskManager.getAllTaskList());
        System.out.println(taskManager.getAllEpicList());
        System.out.println(taskManager.getAllSubTaskList());
        System.out.println();

        taskManager.updateTaskStatus(firstTask, TaskStatus.IN_PROGRESS);
        taskManager.updateTaskStatus(secondTask, TaskStatus.DONE);

        taskManager.updateSubTaskStatus(firstSubTask, TaskStatus.DONE);
        taskManager.updateSubTaskStatus(secondSubTask, TaskStatus.NEW);

        taskManager.updateSubTaskStatus(thirdSubTask, TaskStatus.DONE);

        System.out.println(taskManager.getAllTaskList());
        System.out.println(taskManager.getAllEpicList());
        System.out.println(taskManager.getAllSubTaskList());
        System.out.println();

        taskManager.removeTaskById(firstTask.getId());
        taskManager.removeEpicById(firstEpic.getId());
        taskManager.getEpicById(firstEpic.getId());

        System.out.println(taskManager.getAllTaskList());
        System.out.println(taskManager.getAllEpicList());
        System.out.println(taskManager.getAllSubTaskList());
        System.out.println();



    }
}
