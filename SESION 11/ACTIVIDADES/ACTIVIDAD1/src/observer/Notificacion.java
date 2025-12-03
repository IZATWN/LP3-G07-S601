package observer;

import java.util.ArrayList;
import java.util.List;

public class Notificacion {

    private List<Observer> usuarios = new ArrayList<>();

    public void suscribir(Observer usuario) {
        usuarios.add(usuario);
    }

    public void desuscribir(Observer usuario) {
        usuarios.remove(usuario);
    }

    public void enviarNotificacion(String mensaje) {
        for (Observer u : usuarios) {
            u.update(mensaje);
        }
    }
}
