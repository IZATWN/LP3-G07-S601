package command;

public class CerrarPuertaCommand implements Command {

    private Puerta puerta;

    public CerrarPuertaCommand(Puerta puerta) {
        this.puerta = puerta;
    }

    public void execute() {
        puerta.cerrar();
    }
}
