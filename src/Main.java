import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import service.TaskManager;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        System.out.println("Поехали!");

        int objectId =-1;

        TaskManager manager = new TaskManager();

        Task task = new Task("Покупка", "Купить молока");
        Task newTask = manager.addTask(task, TaskStatus.IN_PROGRESS);

        Task task2 = new Task("Покупка", "Купить хлеб");
        Task newTask2 = manager.addTask(task2, TaskStatus.IN_PROGRESS);

        Epic epic = new Epic("Переезд", "Организуем переезд офиса");
        Epic newEpic = manager.addEpic(epic);
        Epic epic1 = new Epic("Расстановка", "Организуем переезд офиса");
        Epic newEpic1 = manager.addEpic(epic1);

        Subtask subTask = new Subtask("Вять стулья", "Вять стулья", 3);
        Subtask newSubTask = manager.addSubTask(subTask, TaskStatus.NEW);
        Subtask subTask1 = new Subtask("Вять столы", "", 3);
        Subtask newSubTask1 = manager.addSubTask(subTask1, TaskStatus.NEW);
        Subtask subTask3 = new Subtask("Вять шкафы", "Шкафы брать только из опенспейса", 3);
        Subtask newSubTask3 = manager.addSubTask(subTask3, TaskStatus.NEW);

        System.out.println(manager.getById(4));

        Subtask subTask2 = new Subtask("Вять столы", "надо забрать все столы, включая подсобку", 4);
        manager.update(5, subTask2);
        Epic epic3 = new Epic("Переезд", "Организуем переезд офиса2");
        manager.update(3, epic3);

        manager.changeStatus(4, TaskStatus.DONE);
        manager.changeStatus(5, TaskStatus.DONE);

        ArrayList<Object> EpicsSubtasks = manager.getEpicsSubtasks(3);

        manager.deleteId(5);

        ArrayList<Object> allTaskManager = manager.getAllTaskManager();
        ArrayList<Object>allTasks = manager.getAllTasks();
        ArrayList<Object> allEpics = manager.getAllEpics();
        ArrayList<Object> allSubtasks = manager.getAllSubTasks();

        manager.deleteAllTasks();
        manager.deleteAllEpics();
        manager.deleteAllSubtask();

        manager.printAll();
    }
}
