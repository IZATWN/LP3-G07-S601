package command;

public class ApagarLuzCommand implements Command {

    private Luz luz;

    public ApagarLuzCommand(Luz luz) {
        this.luz = luz;
    }

    public void execute() {
        luz.apagar();
    }
}
