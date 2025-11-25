package actividad2;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;

public class AppEfectos extends JFrame {

    public AppEfectos() {
        JButton aplausos = new JButton("Aplausos");
        JButton campana = new JButton("Campana");
        JButton explosion = new JButton("Explosion");

        aplausos.addActionListener(e -> play("C:\\Users\\iza\\Downloads\\aplausos.wav"));
        campana.addActionListener(e -> play("C:\\Users\\iza\\Downloads\\campana.wav"));
        explosion.addActionListener(e -> play("C:\\Users\\iza\\Downloads\\explosion.wav"));

        setLayout(new java.awt.FlowLayout());
        add(aplausos);
        add(campana);
        add(explosion);

        setSize(300,150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void play(String ruta) {
        try {
            File f = new File(ruta);
            AudioInputStream ais = AudioSystem.getAudioInputStream(f);
            Clip c = AudioSystem.getClip();
            c.open(ais);
            c.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new AppEfectos();
    }
}
