package command;

public class MainCommandEjemplo {

    public static void main(String[] args) {

        ControlRemoto control = new ControlRemoto();

        Luz luz = new Luz();
        Alarma alarma = new Alarma();
        Puerta puerta = new Puerta();

        control.setCommand(new EncenderLuzCommand(luz)); control.presionar();
        control.setCommand(new ApagarLuzCommand(luz)); control.presionar();

        control.setCommand(new ActivarAlarmaCommand(alarma)); control.presionar();
        control.setCommand(new DesactivarAlarmaCommand(alarma)); control.presionar();

        control.setCommand(new AbrirPuertaCommand(puerta)); control.presionar();
        control.setCommand(new CerrarPuertaCommand(puerta)); control.presionar();
    }
}
