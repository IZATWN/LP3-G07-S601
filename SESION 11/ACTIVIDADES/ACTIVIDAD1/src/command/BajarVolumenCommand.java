package command;

public class BajarVolumenCommand implements Command {

    private Televisor tv;

    public BajarVolumenCommand(Televisor tv) {
        this.tv = tv;
    }

    @Override
    public void execute() {
        tv.bajarVolumen();
    }
}
