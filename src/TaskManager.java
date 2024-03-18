import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class TaskManager {
    private HashMap<Integer, Task> tasks;
    private static Integer id = 0;

    public TaskManager() {
        tasks = new HashMap<>();
    }

    public static Integer getId() {
        return id;
    }

    public int addTask(Task task, TaskStatus taskStatus) {
        ++id;
        task.taskStatus = taskStatus;
        tasks.put(id, task);

        return id;
    }

    public int addEpic(Epic epic) {
        ++id;
        epic.setStatus(TaskStatus.NEW);
        tasks.put(id, epic);

        return id;
    }

    public int addSubTask(Subtask subtask, TaskStatus taskStatus) {
        if (!isId(subtask.epicId)) return -1; //если эпика нет, то подзадание не добавляем
        if (!isEpic(subtask.epicId)) return -1; //если не эпик, не добавляем
        ++id;
        subtask.taskStatus = taskStatus;
        tasks.put(id, subtask);
        TaskStatus newEpicStatus = changeEpicStatus(subtask.epicId);
        setEpicStatus(newEpicStatus, subtask.epicId);
        return id;
    }

    public boolean isId(int epicId) {
        return tasks.containsKey(epicId);
    }

    public boolean isEpic(int epicId) {
        Object task = tasks.get(epicId);
        return task instanceof Epic;
    }

    private TaskStatus changeEpicStatus(Integer epicId) {
        boolean allDone = true;
        boolean allNew = true;

        for (Object task : tasks.values()) {
            if (task instanceof Subtask) {
                Subtask subTask = (Subtask) task;
                if (!Objects.equals(subTask.epicId, epicId)) continue;

                if (subTask.taskStatus != TaskStatus.DONE) {
                    allDone = false;
                    continue;
                }
                if (subTask.taskStatus != TaskStatus.NEW) {
                    allNew = false;
                    continue;
                }
            }
        }

        TaskStatus status = TaskStatus.NEW;
        if (allDone) {
            status = TaskStatus.DONE;
        }
        if (allNew) {
            status = TaskStatus.NEW;
        }
        if (!allDone && !allNew) {
            status = TaskStatus.IN_PROGRESS;
        }
        return status;
    }

    protected void setEpicStatus(TaskStatus newEpicStatus, Integer epicId) {
        Task epic = tasks.get(epicId);
        epic.taskStatus = newEpicStatus;
    }

    public Object getById(Integer id) {
        return tasks.get(id);
    }

    public void update(Integer id, Object object) {
        if (object instanceof Task) {
            Task newTask = (Task) object;
            Task task = tasks.get(id);

            task.name = newTask.name;
            task.description = newTask.description;
        }
        if (object instanceof Subtask) {
            Subtask newTask = (Subtask) object;
            Subtask task = (Subtask) tasks.get(id);

            if (!isId(newTask.epicId)) return; //если эпика нет, то подзадание не изменяем
            if (!isEpic(newTask.epicId)) return; //если не эпик, не изменяем

            task.name = newTask.name;
            task.description = newTask.description;
            task.epicId = newTask.epicId;
        }
        if (object instanceof Epic) {
            Epic newTask = (Epic) object;
            Epic task = (Epic) tasks.get(id);
            task.name = newTask.name;
            task.description = newTask.description;
        }
    }

    public void changeStatus(Integer id, TaskStatus status) {
        Object object = tasks.get(id);
        if (object instanceof Task) {
            Task task = (Task) object;
            task.taskStatus = status;
        }
        if (object instanceof Subtask) {
            Subtask task = (Subtask) object;
            task.taskStatus = status;
            TaskStatus newEpicStatus = changeEpicStatus(task.epicId);
            setEpicStatus(newEpicStatus, task.epicId);
        }
//!!! добавить для эпика
    }

    public void printAll() {
        for (Integer key : tasks.keySet()) {
            String message;
            Object object = tasks.get(key);

            if (object instanceof Subtask task) {
                message = object.getClass().getSimpleName() + "{" +
                        "id='" + key + '\'' +
                        "name='" + task.name + '\'' +
                        ", description='" + task.description + '\'' +
                        ", taskStatus=" + task.taskStatus +
                        ", epicId=" + task.epicId +
                        '}';
            } else {
                Task task = (Task) object;
                message = object.getClass().getSimpleName() + "{" +
                        "id='" + key + '\'' +
                        "name='" + task.name + '\'' +
                        ", description='" + task.description + '\'' +
                        ", taskStatus=" + task.taskStatus +
                        '}';
            }
            System.out.println(message);
        }
    }

    public void deleteId(Integer id) {
        Object object = tasks.get(id);
        tasks.remove(id);
        if (object instanceof Subtask task) {
            TaskStatus newEpicStatus = changeEpicStatus(task.epicId);
            setEpicStatus(newEpicStatus, task.epicId);
        }
    }

    public HashMap<Integer, Task> getAllTasks() {
        HashMap<Integer, Task> taskList = new HashMap<>();
        //taskList = getData();
        for (Integer key : tasks.keySet()) {
            Object object = tasks.get(key);
            if (object.getClass().getSimpleName().equals("Task")) {
                Task task = (Task) object;
                taskList.put(key, task);
            }
        }
        return taskList;
    }

    public HashMap<Integer, Epic> getAllEpics() {
        HashMap<Integer, Epic> taskList = new HashMap<>();
        for (Integer key : tasks.keySet()) {
            Object object = tasks.get(key);
            if (object.getClass().getSimpleName().equals("Epic")) {
                Epic epic = (Epic) object;
                taskList.put(key, epic);
            }
        }
        return taskList;
    }

    public HashMap<Integer, Subtask> getAllSubTasks() {
        HashMap<Integer, Subtask> taskList = new HashMap<>();
        for (Integer key : tasks.keySet()) {
            Object object = tasks.get(key);
            if (object.getClass().getSimpleName().equals("Subtask")) {
                Subtask subtask = (Subtask) object;
                taskList.put(key, subtask);
            }
        }
        return taskList;
    }

    public HashMap<Integer, Task> getAllTaskManager() {
        HashMap<Integer, Task> taskList = new HashMap<>();
        for (Integer key : tasks.keySet()) {
            Task task = tasks.get(key);
            taskList.put(key, task);
        }
        return taskList;
    }

    public void deleteAllTasks() {
        HashMap<Integer, Task> taskList = new HashMap<>();
        for (Integer key : tasks.keySet()) {
            Object object = tasks.get(key);
            if (object.getClass().getSimpleName().equals("Task")) {
                //Task task = (Task) object;
                taskList.put(key, tasks.get(key));
            }
        }
        for (Integer key : taskList.keySet()) {
            tasks.remove(key, tasks.get(key));
        }
    }

    public void deleteAllEpics() {
        HashMap<Integer, Task> taskList = new HashMap<>();
        for (Integer key : tasks.keySet()) {
            Object object = tasks.get(key);
            if (object.getClass().getSimpleName().equals("Epic")) {
                taskList.put(key, tasks.get(key));
            }
        }
        for (Integer key : taskList.keySet()) {
            tasks.remove(key, tasks.get(key));
        }
    }

    public void deleteAllSubtask() {
        HashMap<Integer, Task> taskList = new HashMap<>();
        for (Integer key : tasks.keySet()) {
            Task task = tasks.get(key);
            if (task.getClass().getSimpleName().equals("Epic")) {
                setEpicStatus(TaskStatus.NEW, key);
            }
            if (task.getClass().getSimpleName().equals("Subtask")) {
                taskList.put(key, tasks.get(key));
            }
        }
        for (Integer key : taskList.keySet()) {
            tasks.remove(key, tasks.get(key));
        }
    }

}
