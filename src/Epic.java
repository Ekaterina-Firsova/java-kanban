public class Epic extends Task{
    //private TaskStatus status;
    public Epic(String name, String description) {
        super(name, description);
    }


    public TaskStatus getStatus() {
        return super.taskStatus;
    }

    public void setStatus(TaskStatus status) {
        super.taskStatus = status;
    }
}
