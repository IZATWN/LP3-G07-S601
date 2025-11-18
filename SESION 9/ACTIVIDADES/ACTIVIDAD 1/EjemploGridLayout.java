package actividad2;
import javax.swing.*;
import java.awt.*;

public class EjemploGridLayout {
    public static void main(String[] args) {

        JFrame ventana = new JFrame("GridLayout - Integrantes: [NOMBRES]");
        ventana.setSize(400, 300);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        
        ventana.setLayout(new GridLayout(2, 3, 10, 10));

        ventana.add(new JButton("1"));
        ventana.add(new JButton("2"));
        ventana.add(new JButton("3"));
        ventana.add(new JButton("4"));
        ventana.add(new JButton("5"));
        ventana.add(new JButton("6"));

        ventana.setVisible(true);
    }
}
