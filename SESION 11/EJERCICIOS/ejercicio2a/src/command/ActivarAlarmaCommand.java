package command;

public class ActivarAlarmaCommand implements Command {

    private Alarma alarma;

    public ActivarAlarmaCommand(Alarma alarma) {
        this.alarma = alarma;
    }

    public void execute() {
        alarma.activar();
    }
}
