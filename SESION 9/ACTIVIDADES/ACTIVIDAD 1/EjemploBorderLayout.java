package actividad2;
import javax.swing.*;
import java.awt.*;

public class EjemploBorderLayout {
    public static void main(String[] args) {

        JFrame ventana = new JFrame("BorderLayout - Integrantes: [NOMBRES]");
        ventana.setSize(400, 300);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ventana.setLayout(new BorderLayout());

        ventana.add(new JButton("NORTE"), BorderLayout.NORTH);
        ventana.add(new JButton("SUR"), BorderLayout.SOUTH);
        ventana.add(new JButton("ESTE"), BorderLayout.EAST);
        ventana.add(new JButton("OESTE"), BorderLayout.WEST);
        ventana.add(new JButton("CENTRO"), BorderLayout.CENTER);

        ventana.setVisible(true);
    }
}
