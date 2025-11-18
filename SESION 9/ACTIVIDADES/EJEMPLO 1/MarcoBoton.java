package actividad2;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class MarcoBoton extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JButton botonSimple;
    private final JButton botonElegante;

    public MarcoBoton() {
        super("Prueba de Botones (Modular)");

        setLayout(new FlowLayout());

        // Botón simple
        botonSimple = new JButton("Boton simple");
        add(botonSimple);

        // Iconos (debes tener los GIF en la carpeta del proyecto)
        Icon icono1 = new ImageIcon(getClass().getResource("insecto1.gif"));
        Icon icono2 = new ImageIcon(getClass().getResource("insecto2.gif"));

        botonElegante = new JButton("Boton elegante", icono1);
        botonElegante.setRolloverIcon(icono2);
        add(botonElegante);

        // Instancia del manejador de eventos
        ManejadorBoton manejador = new ManejadorBoton();
        botonSimple.addActionListener(manejador);
        botonElegante.addActionListener(manejador);
    }

    // Clase interna: maneja eventos
    private class ManejadorBoton implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent evento) {
            JOptionPane.showMessageDialog(
                MarcoBoton.this,
                "Usted oprimio: " + evento.getActionCommand()
            );
        }
    }
}
