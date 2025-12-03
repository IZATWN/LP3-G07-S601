package command;

public class DesactivarAlarmaCommand implements Command {

    private Alarma alarma;

    public DesactivarAlarmaCommand(Alarma alarma) {
        this.alarma = alarma;
    }

    public void execute() {
        alarma.desactivar();
    }
}
