package service;

import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;

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
        if (!isId(subtask.getEpicId())) return null; //если эпика нет, то подзадание не добавляем
        if (!isEpic(subtask.getEpicId())) return null; //если не эпик, не добавляем
        ++id;
        subtask.setTaskStatus(taskStatus);
        tasks.put(id, subtask);
        TaskStatus newEpicStatus = changeEpicStatus(subtask.getEpicId());
        setEpicStatus(newEpicStatus, subtask.getEpicId());
        return subtask;
    }

    public boolean isId(int epicId) {
        return tasks.containsKey(epicId);
    }

    public boolean isEpic(int epicId) {
        Object object = tasks.get(epicId);
        return object instanceof Epic;
    }

    private TaskStatus changeEpicStatus(Integer epicId) {
        boolean allDone = true;
        boolean allNew = true;

        for (Object object : tasks.values()) {
            if (object instanceof Subtask subTask) {
                if (!Objects.equals(subTask.getEpicId(), epicId)) continue;

                if (subTask.getTaskStatus() != TaskStatus.DONE) {
                    allDone = false;
                    continue;
                }
                if (subTask.getTaskStatus() != TaskStatus.NEW) {
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
        epic.setTaskStatus(newEpicStatus);
    }

    public void update(Integer id, Object object) {
        if (object instanceof Subtask) {
            Subtask newTask = (Subtask) object;
            Subtask task = (Subtask) tasks.get(id);

            if (!isId(newTask.getEpicId())) return; //если эпика нет, то подзадание не изменяем
            if (!isEpic(newTask.getEpicId())) return; //если новый id не эпик, не изменяем

            task.setName(newTask.getName());
            task.setDescription(newTask.getDescription());
            task.setEpicId(newTask.getEpicId());
        } else if (object instanceof Epic) {
            Epic newTask = (Epic) object;
            Epic task = (Epic) tasks.get(id);
            task.setName(newTask.getName());
            task.setDescription(newTask.getDescription());
        } else if (object instanceof Task) {
            Task newTask = (Task) object;
            Task task = tasks.get(id);

            task.setName(newTask.getName());
            task.setDescription(newTask.getDescription());
        }
    }

    public void changeStatus(Integer id, TaskStatus status) {
        Object object = tasks.get(id);
        if (object instanceof Epic) {
            return;
        }
        if (object instanceof Subtask) {
            Subtask task = (Subtask) object;
            task.setTaskStatus(status);
            TaskStatus newEpicStatus = changeEpicStatus(task.getEpicId());
            setEpicStatus(newEpicStatus, task.getEpicId());
        } else if (object instanceof Task) {
            Task task = (Task) object;
            task.setTaskStatus(status);
        }
    }

    public Object getById(Integer id) {
        return tasks.get(id);
    }

    public ArrayList<Object> getAllTasks() {
        ArrayList<Object> taskList = new ArrayList<>();
        for (Object object : tasks.values()) {
            if (object.getClass().equals(Task.class)) {
                taskList.add(object);
            }
        }
        return taskList;
    }

    public ArrayList<Object> getAllEpics() {
        ArrayList<Object> taskList = new ArrayList<>();
        for (Object object : tasks.values()) {
            if (object instanceof Epic) {
                taskList.add(object);
            }
        }
        return taskList;
    }

    public ArrayList<Object> getAllSubTasks() {
        ArrayList<Object> taskList = new ArrayList<>();
        for (Object object : tasks.values()) {
            if (object instanceof Subtask) {
                taskList.add(object);
            }
        }
        return taskList;
    }

    public ArrayList<Object> getAllTaskManager() {
        ArrayList<Object> taskList = new ArrayList<>();
        for (Object object : tasks.values()) {
            taskList.add(object);
        }
        return taskList;
    }

    public ArrayList<Object> getEpicsSubtasks (Integer epicId){
        ArrayList<Object> taskList = new ArrayList<>();
        for (Object object : tasks.values()) {
            if (object instanceof Subtask) {
                if (((Subtask) object).getEpicId().equals(epicId)) {
                    taskList.add(object);
                }
            }
        }
        return taskList;
    }

    public void deleteId(Integer id) {
        Object object = tasks.get(id);
        tasks.remove(id);
        if (object instanceof Subtask task) {
            TaskStatus newEpicStatus = changeEpicStatus(task.getEpicId());
            setEpicStatus(newEpicStatus, task.getEpicId());
        }
    }

    public void deleteAllTasks() {
        HashMap<Integer, Task> taskList = new HashMap<>();
        for (Integer key : tasks.keySet()) {
            Object object = tasks.get(key);
            if (object.getClass().equals(Task.class)) {
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
            if (object instanceof Epic) {
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
            if (task instanceof Epic) {
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
    public void printAll() {
        for (Integer key : tasks.keySet()) {
            String message;
            Object object = tasks.get(key);

            if (object instanceof Subtask task) {
                message = object.getClass().getSimpleName() + "{" +
                        "id='" + key + '\'' +
                        "name='" + task.getName() + '\'' +
                        ", description='" + task.getDescription() + '\'' +
                        ", taskStatus=" + task.getTaskStatus() +
                        ", epicId=" + task.getEpicId() +
                        '}';
            } else {
                Task task = (Task) object;
                message = object.getClass().getSimpleName() + "{" +
                        "id='" + key + '\'' +
                        "name='" + task.getName() + '\'' +
                        ", description='" + task.getDescription() + '\'' +
                        ", taskStatus=" + task.getTaskStatus() +
                        '}';
            }
            System.out.println(message);
        }
    }
}
