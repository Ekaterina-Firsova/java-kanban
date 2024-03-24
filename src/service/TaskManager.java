package service;

import model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class TaskManager {
    private final HashMap<Integer, Task> tasks; //Александр, мы интерфейсы еще не проходили. Я не знаю как в Map переделать
    private static Integer id = 0;

    public TaskManager() {
        tasks = new HashMap<>();
    }

    public static Integer getId() {
        return id;
    }

    public Task addTask(Task task, TaskStatus taskStatus) {
        ++id;
        task.setTaskStatus(taskStatus);
        tasks.put(id, task);

        return task;
    }

    public Epic addEpic(Epic epic) {
        ++id;
        epic.setStatus(TaskStatus.NEW);
        tasks.put(id, epic);

        return epic;
    }

    public Subtask addSubTask(Subtask subtask, TaskStatus taskStatus) {
        if (!containsId(subtask.getEpicId())) return null; //если эпика нет, то подзадание не добавляем
        if (!isThisIdEpic(subtask.getEpicId())) return null; //если не эпик, не добавляем
        ++id;
        subtask.setTaskStatus(taskStatus);
        tasks.put(id, subtask);
        TaskStatus newEpicStatus = changeEpicStatus(subtask.getEpicId());
        setEpicStatus(newEpicStatus, subtask.getEpicId());
        return subtask;
    }

    public boolean containsId (int epicId) {
        return tasks.containsKey(epicId);
    }

    public boolean isThisIdEpic (int epicId) {
        Task object = tasks.get(epicId);
        return object.getType().equals(TaskType.EPIC);
    }

    private TaskStatus changeEpicStatus(Integer epicId) {
        boolean allDone = true;
        boolean allNew = true;

        for (Task object : tasks.values()) {
            if (object.getType().equals(TaskType.SUBTASK)) {
                Subtask subTask = (Subtask) object;
                if (!Objects.equals(subTask.getEpicId(), epicId)) continue;

                if (subTask.getTaskStatus() != TaskStatus.DONE) {
                    allDone = false;
                    continue;
                }
                if (subTask.getTaskStatus() != TaskStatus.NEW) {
                    allNew = false;
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
        epic.setTaskStatus(newEpicStatus);
    }

    public void update(Integer id, Task object) {
        TaskType taskType = object.getType();
        switch (taskType) {
            case SUBTASK :
                Subtask newSubTask = (Subtask) object;
                Subtask subTask = (Subtask) tasks.get(id);
                if (!containsId(newSubTask.getEpicId())) return; //если эпика нет, то подзадание не изменяем
                if (!isThisIdEpic(newSubTask.getEpicId())) return; //если новый id не эпик, не изменяем

                subTask.setName(newSubTask.getName());
                subTask.setDescription(newSubTask.getDescription());
                subTask.setEpicId(newSubTask.getEpicId());
                break;
            case EPIC:
                Epic newEpic = (Epic) object;
                Epic epic = (Epic) tasks.get(id);
                epic.setName(newEpic.getName());
                epic.setDescription(newEpic.getDescription());
                break;
            case TASK:
                Task task = tasks.get(id);
                task.setName(object.getName());
                task.setDescription(object.getDescription());
        }
    }

    public void changeStatus(Integer id, TaskStatus status) {
        Task object = tasks.get(id);
        TaskType taskType = object.getType();
        switch (taskType) {
            case EPIC:
                break;
            case SUBTASK:
                Subtask subTask = (Subtask) object;
                subTask.setTaskStatus(status);
                TaskStatus newEpicStatus = changeEpicStatus(subTask.getEpicId());
                setEpicStatus(newEpicStatus, subTask.getEpicId());
                break;
            case TASK:
                object.setTaskStatus(status);
                break;
        }
    }

    public Object getById(Integer id) {
        return tasks.get(id);
    }

    public ArrayList<Task> getAllTasks() {
        return getSpecificType(TaskType.TASK);
    }

    public ArrayList<Task> getAllEpics() {
        return getSpecificType(TaskType.EPIC);
    }

    public ArrayList<Task> getAllSubTasks() {
        return getSpecificType(TaskType.SUBTASK);
    }

    public ArrayList<Task> getAllTaskManager() {
        ArrayList<Task> taskList = new ArrayList<>();
        for (Task object : tasks.values()) {
            taskList.add(object);
        }
        return taskList;
    }

    private ArrayList<Task> getSpecificType(TaskType taskType) {
        ArrayList<Task> taskList = new ArrayList<>();
        for (Task object : tasks.values()) {
            if (object.getType().equals(taskType)) {
                taskList.add(object);
            }
        }
        return taskList;
    }

    public ArrayList<Task> getEpicsSubtasks (Integer epicId){
        ArrayList<Task> taskList = new ArrayList<>();
        for (Task object : tasks.values()) {
            if (object.getType().equals(TaskType.SUBTASK)) {
                if (((Subtask) object).getEpicId().equals(epicId)) {
                    taskList.add(object);
                }
            }
        }
        return taskList;
    }

    public void deleteId(Integer id) {
        Task object = tasks.get(id);
        tasks.remove(id);
        if (object.getType().equals(TaskType.SUBTASK)) {
            Subtask task = (Subtask) object;
            TaskStatus newEpicStatus = changeEpicStatus(task.getEpicId());
            setEpicStatus(newEpicStatus, task.getEpicId());
        }
    }

    public void deleteAllTasks() {
        HashMap<Integer, Task> taskList = new HashMap<>();
        for (Integer key : tasks.keySet()) {
            Task object = tasks.get(key);
            if (object.getType().equals(TaskType.TASK)) {
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
            Task object = tasks.get(key);
            if (object.getType().equals(TaskType.EPIC)) {
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
            if (task.getType().equals(TaskType.EPIC)) {
                setEpicStatus(TaskStatus.NEW, key);
            }
            if (task.getType().equals(TaskType.SUBTASK)) {
                taskList.put(key, tasks.get(key));
            }
        }
        for (Integer key : taskList.keySet()) {
            tasks.remove(key, tasks.get(key));
        }
    }

    public void printAll() {
        for (Integer key : tasks.keySet()) {
            Task object = tasks.get(key);
            TaskType taskType = object.getType();
            String message = taskType+"{" +
                    "id='" + key + '\'' +
                    "name='" + object.getName() + '\'' +
                    ", description='" + object.getDescription() + '\'' +
                    ", taskStatus=" + object.getTaskStatus();

            if (taskType.equals(TaskType.SUBTASK)) {
                    message +=
                            ", epicId=" + ((Subtask) object).getEpicId() +
                            '}';
            }
            System.out.println(message);
        }
    }

}
