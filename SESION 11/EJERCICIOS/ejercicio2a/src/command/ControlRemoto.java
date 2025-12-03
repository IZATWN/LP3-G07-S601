package command;

public class ControlRemoto {

    private Command command;

    public void setCommand(Command command) {
        this.command = command;
    }

    public void presionar() {
        command.execute();
    }
}
