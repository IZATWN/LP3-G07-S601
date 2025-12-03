package command;

public class AbrirPuertaCommand implements Command {

    private Puerta puerta;

    public AbrirPuertaCommand(Puerta puerta) {
        this.puerta = puerta;
    }

    public void execute() {
        puerta.abrir();
    }
}
