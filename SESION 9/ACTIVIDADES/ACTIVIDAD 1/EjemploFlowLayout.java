package actividad2;
import javax.swing.*;
import java.awt.*;

public class EjemploFlowLayout {
    public static void main(String[] args) {

        JFrame ventana = new JFrame("FlowLayout - Integrantes: [NOMBRES]");
        ventana.setSize(400, 200);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ventana.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));

        ventana.add(new JButton("Botón 1"));
        ventana.add(new JButton("Botón 2"));
        ventana.add(new JButton("Botón 3"));
        ventana.add(new JButton("Botón 4"));
        ventana.add(new JButton("Botón 5"));

        ventana.setVisible(true);
    }
}
