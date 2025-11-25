package actividad2;

import javax.swing.*;
import java.awt.*;

public class AppTemperaturas extends JFrame {

    private JTextField[] campos = new JTextField[7];
    private int[] temps = new int[7];
    private GraficoPanel panel;

    public AppTemperaturas() {
        setLayout(new BorderLayout());

        JPanel entrada = new JPanel();
        entrada.setLayout(new GridLayout(7, 2));

        String[] dias = {"Lunes","Martes","Miercoles","Jueves","Viernes","Sabado","Domingo"};

        for (int i = 0; i < 7; i++) {
            entrada.add(new JLabel(dias[i]));
            campos[i] = new JTextField();
            entrada.add(campos[i]);
        }

        JButton btn = new JButton("Mostrar Grafico");
        btn.addActionListener(e -> {
            for (int i = 0; i < 7; i++) {
                temps[i] = Integer.parseInt(campos[i].getText());
            }
            panel.repaint();
        });

        panel = new GraficoPanel();

        add(entrada, BorderLayout.WEST);
        add(panel, BorderLayout.CENTER);
        add(btn, BorderLayout.SOUTH);

        setSize(700, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    class GraficoPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            int x = 50;
            int step = 80;

            for (int i = 0; i < 6; i++) {
                int x1 = x + i * step;
                int y1 = getHeight() - temps[i];
                int x2 = x + (i + 1) * step;
                int y2 = getHeight() - temps[i + 1];
                g.drawLine(x1, y1, x2, y2);
                g.fillOval(x1 - 3, y1 - 3, 6, 6);
            }
        }
    }

    public static void main(String[] args) {
        new AppTemperaturas();
    }
}
