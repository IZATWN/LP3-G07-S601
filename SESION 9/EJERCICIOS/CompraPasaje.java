package actividad2;
import javax.swing.*;
import java.awt.*;

public class CompraPasaje extends JFrame {

    private JTextField txtNombre, txtDni, txtFecha;
    private JCheckBox chkAudifonos, chkManta, chkRevistas;
    private JRadioButton rbPrimerPiso, rbSegundoPiso;
    private JComboBox<String> cbOrigen, cbDestino;
    private JList<String> listaCalidad;
    private JButton btnReiniciar, btnMostrar;

    public CompraPasaje() {

        setTitle("Compra de Pasaje");
        setSize(750, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout(10,10));
        JPanel contenedor = new JPanel(new GridLayout(6, 1, 10, 10));
        contenedor.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JPanel pDatos = new JPanel(new GridLayout(3, 2, 10, 5));
        pDatos.setBorder(BorderFactory.createTitledBorder("Datos personales"));

        pDatos.add(new JLabel("Nombre completo:"));
        txtNombre = new JTextField();
        pDatos.add(txtNombre);

        pDatos.add(new JLabel("Documento de identidad:"));
        txtDni = new JTextField();
        pDatos.add(txtDni);

        pDatos.add(new JLabel("Fecha de viaje (dd-mm-aaaa):"));
        txtFecha = new JTextField();
        pDatos.add(txtFecha);

        contenedor.add(pDatos);

        JPanel pServicios = new JPanel(new GridLayout(1, 3));
        pServicios.setBorder(BorderFactory.createTitledBorder("Servicios opcionales"));

        chkAudifonos = new JCheckBox("Audifonos");
        chkManta = new JCheckBox("Manta");
        chkRevistas = new JCheckBox("Revistas");

        pServicios.add(chkAudifonos);
        pServicios.add(chkManta);
        pServicios.add(chkRevistas);

        contenedor.add(pServicios);

        JPanel pPiso = new JPanel(new GridLayout(1, 2));
        pPiso.setBorder(BorderFactory.createTitledBorder("Piso del bus"));

        rbPrimerPiso = new JRadioButton("1er Piso");
        rbSegundoPiso = new JRadioButton("2do Piso");

        ButtonGroup grupoPiso = new ButtonGroup();
        grupoPiso.add(rbPrimerPiso);
        grupoPiso.add(rbSegundoPiso);

        pPiso.add(rbPrimerPiso);
        pPiso.add(rbSegundoPiso);

        contenedor.add(pPiso);

        JPanel pViaje = new JPanel(new GridLayout(2, 2, 10, 5));
        pViaje.setBorder(BorderFactory.createTitledBorder("Ruta de viaje"));

        pViaje.add(new JLabel("Origen:"));
        cbOrigen = new JComboBox<>(new String[]{"Lima", "Arequipa", "Cusco", "Trujillo", "Ica","Nazca"});
        pViaje.add(cbOrigen);

        pViaje.add(new JLabel("Destino:"));
        cbDestino = new JComboBox<>(new String[]{"Lima", "Arequipa", "Cusco", "Trujillo", "Ica"});
        pViaje.add(cbDestino);

        contenedor.add(pViaje);

        JPanel pCalidad = new JPanel(new BorderLayout());
        pCalidad.setBorder(BorderFactory.createTitledBorder("Calidad de servicio"));

        listaCalidad = new JList<>(new String[]{"Economico", "Standard", "VIP"});
        listaCalidad.setVisibleRowCount(3);

        pCalidad.add(new JScrollPane(listaCalidad), BorderLayout.CENTER);

        contenedor.add(pCalidad);

        JPanel pBotones = new JPanel(new FlowLayout());
        btnMostrar = new JButton("Mostrar resumen");
        btnReiniciar = new JButton("Reiniciar formulario");

        pBotones.add(btnMostrar);
        pBotones.add(btnReiniciar);

        add(contenedor, BorderLayout.CENTER);
        add(pBotones, BorderLayout.SOUTH);

        btnMostrar.addActionListener(e -> mostrarResumen());
        btnReiniciar.addActionListener(e -> limpiarCampos());
    }

    private boolean validarDni() {
        String dni = txtDni.getText().trim();

        if (!dni.matches("\\d{8}")) {
            JOptionPane.showMessageDialog(this, 
                "El DNI debe tener exactamente 8 digitos ",
                "Error en DNI",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean validarFecha() {
        String fecha = txtFecha.getText().trim();

        if (!fecha.matches("\\d{2}-\\d{2}-\\d{4}")) {
            JOptionPane.showMessageDialog(this,
                "La fecha debe tener formato (00-00-0000)",
                "Error en fecha",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void mostrarResumen() {

        if (!validarDni()) return;
        if (!validarFecha()) return;

        String servicios = "";
        if (chkAudifonos.isSelected()) servicios += "Audifonos ";
        if (chkManta.isSelected()) servicios += "Manta ";
        if (chkRevistas.isSelected()) servicios += "Revistas ";
        if (servicios.equals("")) servicios = "Ninguno";

        String piso = rbPrimerPiso.isSelected() ? "1er Piso"
                     : rbSegundoPiso.isSelected() ? "2do Piso"
                     : "No seleccionado";

        String calidad = listaCalidad.getSelectedValue();
        if (calidad == null) calidad = "No seleccionado";

        String resumen =
                "----- RESUMEN DE COMPRA -----\n\n" +
                "Nombre: " + txtNombre.getText() + "\n" +
                "DNI: " + txtDni.getText() + "\n" +
                "Fecha de viaje: " + txtFecha.getText() + "\n\n" +
                "Servicios opcionales: " + servicios + "\n" +
                "Piso: " + piso + "\n" +
                "Origen: " + cbOrigen.getSelectedItem() + "\n" +
                "Destino: " + cbDestino.getSelectedItem() + "\n" +
                "Calidad de servicio: " + calidad;

        JOptionPane.showMessageDialog(this, resumen);
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtDni.setText("");
        txtFecha.setText("");
        chkAudifonos.setSelected(false);
        chkManta.setSelected(false);
        chkRevistas.setSelected(false);
        rbPrimerPiso.setSelected(false);
        cbOrigen.setSelectedIndex(0);
        cbDestino.setSelectedIndex(0);
        listaCalidad.clearSelection();
    }

    public static void main(String[] args) {
        new CompraPasaje().setVisible(true);
    }
}
