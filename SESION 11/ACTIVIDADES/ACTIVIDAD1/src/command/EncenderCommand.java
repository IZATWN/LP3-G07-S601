package command;

public class EncenderCommand implements Command {

    private Televisor tv;

    public EncenderCommand(Televisor tv) {
        this.tv = tv;
    }

    @Override
    public void execute() {
        tv.encender();
    }
}
