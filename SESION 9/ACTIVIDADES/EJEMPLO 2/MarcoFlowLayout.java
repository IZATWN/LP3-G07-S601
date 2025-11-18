package actividad2;

import java.awt.FlowLayout;
import java.awt.Container;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.JButton;

public class MarcoFlowLayout extends JFrame {

    private final JButton botonIzquierda;
    private final JButton botonCentro;
    private final JButton botonDerecha;

    private final FlowLayout esquema;
    private final Container contenedor;

    public MarcoFlowLayout() {

        super("Demostracion de FlowLayout");

        esquema = new FlowLayout();
        contenedor = getContentPane();
        setLayout(esquema);

        // Boton Izquierda
        botonIzquierda = new JButton("Izquierda");
        add(botonIzquierda);
        botonIzquierda.addActionListener(
            new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    esquema.setAlignment(FlowLayout.LEFT);
                    esquema.layoutContainer(contenedor);
                }
            }
        );

        // Boton Centro
        botonCentro = new JButton("Centro");
        add(botonCentro);
        botonCentro.addActionListener(
            new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    esquema.setAlignment(FlowLayout.CENTER);
                    esquema.layoutContainer(contenedor);
                }
            }
        );

        // Boton Derecha
        botonDerecha = new JButton("Derecha");
        add(botonDerecha);
        botonDerecha.addActionListener(
            new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    esquema.setAlignment(FlowLayout.RIGHT);
                    esquema.layoutContainer(contenedor);
                }
            }
        );
    }
}
