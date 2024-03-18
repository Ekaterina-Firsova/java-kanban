import java.util.HashMap;

public class Main {
//https://practicum.yandex.ru/trainer/java-developer/lesson/f7e719dd-ded6-4181-bf88-31f0b5842c29/
    public static void main(String[] args) {

        System.out.println("Поехали!");

        int objectId =-1;

        TaskManager manager = new TaskManager();

        Task task = new Task("Покупка", "Купить молока");
        objectId = manager.addTask(task, TaskStatus.IN_PROGRESS);

        Task task2 = new Task("Покупка", "Купить хлеб");
        objectId = manager.addTask(task2, TaskStatus.IN_PROGRESS);

        Epic epic = new Epic("Переезд", "Организуем переезд офиса");
        objectId = manager.addEpic(epic);
        Epic epic1 = new Epic("Расстановка", "Организуем переезд офиса");
        objectId = manager.addEpic(epic1);

        Subtask subTask = new Subtask("Вять стулья", "Вять стулья", 3);
        objectId = manager.addSubTask(subTask, TaskStatus.NEW);
        Subtask subTask1 = new Subtask("Вять столы", "", 3);
        objectId = manager.addSubTask(subTask1, TaskStatus.NEW);
        Subtask subTask3 = new Subtask("Вять шкафы", "Шкафы брать только из опенспейса", 3);
        objectId = manager.addSubTask(subTask3, TaskStatus.NEW);

        System.out.println(manager.getById(4));

        Subtask subTask2 = new Subtask("Вять столы", "надо забрать все столы, включая подсобку", 4);
        manager.update(5, subTask2);
        Epic epic3 = new Epic("Переезд", "Организуем переезд офиса2");
        manager.update(3, epic3);

        manager.changeStatus(4, TaskStatus.DONE);
        manager.changeStatus(5, TaskStatus.DONE);

        manager.deleteId(5);

        HashMap<Integer, Task> allTaskManager = manager.getAllTaskManager();
        HashMap<Integer, Task> allTasks = manager.getAllTasks();
        HashMap<Integer, Epic> allEpics = manager.getAllEpics();
        HashMap<Integer, Subtask> allSubtasks = manager.getAllSubTasks();

        //manager.deleteAllTasks();
        //manager.deleteAllEpics();
        //manager.deleteAllSubtask();

        //manager.getAllEpics();
        manager.printAll();
    }
}
