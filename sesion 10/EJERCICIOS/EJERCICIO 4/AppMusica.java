package actividad2;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;

public class AppMusica extends JFrame {

    private Clip clip;

    public AppMusica() {
        JButton play = new JButton("Reproducir");
        JButton pause = new JButton("Pausar");
        JButton resume = new JButton("Reanudar");

        play.addActionListener(e -> reproducir("C:\\Users\\iza\\Downloads\\musica.wav"));
        pause.addActionListener(e -> {
            if (clip != null && clip.isRunning()) clip.stop();
        });
        resume.addActionListener(e -> {
            if (clip != null) clip.start();
        });

        setLayout(new java.awt.FlowLayout());
        add(play);
        add(pause);
        add(resume);

        setSize(300,150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void reproducir(String ruta) {
        try {
            File f = new File(ruta);
            AudioInputStream ais = AudioSystem.getAudioInputStream(f);
            clip = AudioSystem.getClip();
            clip.open(ais);
            clip.start();
        } catch (Exception ex) { }
    }

    public static void main(String[] args) {
        new AppMusica();
    }
}
