package command;

public class ControlRemoto {

    private Command command;

    public void setCommand(Command c) {
        this.command = c;
    }

    public void presionarBoton() {
        command.execute();
    }
}
