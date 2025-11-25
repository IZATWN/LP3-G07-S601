package actividad2;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class AppGraficos extends JPanel {

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.BLUE);
        g2.drawRect(20, 20, 100, 50);

        g2.setColor(Color.RED);
        g2.fillOval(150, 20, 80, 80);

        AffineTransform t1 = g2.getTransform();
        g2.translate(50, 150);
        g2.rotate(Math.toRadians(45));
        g2.setColor(Color.GREEN);
        g2.fillRect(0, 0, 100, 50);
        g2.setTransform(t1);
    }

    public static void main(String[] args) {
        JFrame f = new JFrame("Graficos");
        f.setSize(400, 300);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new AppGraficos());
        f.setVisible(true);
    }
}
