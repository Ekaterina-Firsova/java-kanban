public class Task {
    public String name;
    public String description;
    public TaskStatus taskStatus;

    public Task(String name, String description) {
        this.name = name;
        //this.taskStatus = taskStatus;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", taskStatus=" + taskStatus +
                '}';
    }
}
