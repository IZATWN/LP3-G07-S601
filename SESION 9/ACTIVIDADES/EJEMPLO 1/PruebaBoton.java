package actividad2;
import javax.swing.JFrame;

public class PruebaBoton {

    public static void main(String[] args) {

        MarcoBoton ventana = new MarcoBoton();
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setSize(300, 150);
        ventana.setVisible(true);
    }
}
