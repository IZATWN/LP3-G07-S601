package actividad2;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;

public class AppAudio extends JFrame {

    public AppAudio() {
        JButton btnPlay = new JButton("Reproducir");
        btnPlay.addActionListener(e -> playAudio("C:\\Users\\iza\\Downloads\\audio.wav"));

        add(btnPlay);
        setSize(200,100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void playAudio(String ruta) {
        try {
            File f = new File(ruta);
            AudioInputStream ais = AudioSystem.getAudioInputStream(f);
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            clip.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new AppAudio();
    }
}
