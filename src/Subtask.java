public class Subtask extends Task{
    public Integer epicId = -1;

    public Subtask(String name, String description, Integer epicId) {
        super(name, description);
        this.epicId = epicId;
    }

}
