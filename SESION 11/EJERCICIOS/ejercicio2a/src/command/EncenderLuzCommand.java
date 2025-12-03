package command;

public class EncenderLuzCommand implements Command {

    private Luz luz;

    public EncenderLuzCommand(Luz luz) {
        this.luz = luz;
    }

    public void execute() {
        luz.encender();
    }
}
