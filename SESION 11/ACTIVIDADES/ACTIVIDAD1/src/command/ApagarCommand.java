package command;

public class ApagarCommand implements Command {

    private Televisor tv;

    public ApagarCommand(Televisor tv) {
        this.tv = tv;
    }

    @Override
    public void execute() {
        tv.apagar();
    }
}
