package actividad2;
import javax.swing.*;

public class AppBinding extends JFrame {
    private JTextField txtNombre;
    private JTextField txtEdad;
    private JButton btnActualizar;
    private JLabel lblResultado;
    private Persona persona;

    public AppBinding() {
        persona = new Persona("Juan", 20);

        txtNombre = new JTextField(persona.getNombre(), 15);
        txtEdad = new JTextField(String.valueOf(persona.getEdad()), 15);
        btnActualizar = new JButton("Actualizar");
        lblResultado = new JLabel();

        btnActualizar.addActionListener(e -> {
            persona.setNombre(txtNombre.getText());
            persona.setEdad(Integer.parseInt(txtEdad.getText()));
            lblResultado.setText("Nombre: " + persona.getNombre() + " Edad: " + persona.getEdad());
        });

        setLayout(new java.awt.FlowLayout());
        add(new JLabel("Nombre:"));
        add(txtNombre);
        add(new JLabel("Edad:"));
        add(txtEdad);
        add(btnActualizar);
        add(lblResultado);

        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        new AppBinding();
    }
}

class Persona {
    private String nombre;
    private int edad;

    public Persona(String n, int e) {
        nombre = n;
        edad = e;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String n) { nombre = n; }
    public int getEdad() { return edad; }
    public void setEdad(int e) { edad = e; }
}
