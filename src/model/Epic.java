package model;

public class Epic extends Task{
    public Epic(String name, String description) {
        super(name, description);
    }

    public TaskStatus getStatus() {
        return super.getTaskStatus();
    }

    public void setStatus(TaskStatus status) {
        super.setTaskStatus(status);
    }
}
