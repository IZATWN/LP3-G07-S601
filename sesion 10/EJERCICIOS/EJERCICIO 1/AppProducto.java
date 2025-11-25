package actividad2;

import javax.swing.*;

public class AppProducto extends JFrame {
    private JTextField txtNombre;
    private JTextField txtPrecio;
    private JTextField txtStock;
    private JTextField txtCategoria;
    private JButton btnActualizar;
    private JLabel lblResultado;
    private Producto producto;

    public AppProducto() {
        producto = new Producto("ProductoX", 0.0, 0, "General");

        txtNombre = new JTextField(producto.getNombre(), 15);
        txtPrecio = new JTextField(String.valueOf(producto.getPrecio()), 15);
        txtStock = new JTextField(String.valueOf(producto.getCantidadStock()), 15);
        txtCategoria = new JTextField(producto.getCategoria(), 15);
        btnActualizar = new JButton("Actualizar Producto");
        lblResultado = new JLabel();

        btnActualizar.addActionListener(e -> {
            producto.setNombre(txtNombre.getText());
            producto.setPrecio(Double.parseDouble(txtPrecio.getText()));
            producto.setCantidadStock(Integer.parseInt(txtStock.getText()));
            producto.setCategoria(txtCategoria.getText());
            lblResultado.setText("Nombre: " + producto.getNombre() +
                    " Precio: " + producto.getPrecio() +
                    " Stock: " + producto.getCantidadStock() +
                    " Categoria: " + producto.getCategoria());
        });

        setLayout(new java.awt.FlowLayout());
        add(new JLabel("Nombre:"));
        add(txtNombre);
        add(new JLabel("Precio:"));
        add(txtPrecio);
        add(new JLabel("Stock:"));
        add(txtStock);
        add(new JLabel("Categoria:"));
        add(txtCategoria);
        add(btnActualizar);
        add(lblResultado);

        setSize(350, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        new AppProducto();
    }
}

class Producto {
    private String nombre;
    private double precio;
    private int cantidadStock;
    private String categoria;

    public Producto(String n, double p, int c, String cat) {
        nombre = n;
        precio = p;
        cantidadStock = c;
        categoria = cat;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String n) { nombre = n; }
    public double getPrecio() { return precio; }
    public void setPrecio(double p) { precio = p; }
    public int getCantidadStock() { return cantidadStock; }
    public void setCantidadStock(int c) { cantidadStock = c; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String c) { categoria = c; }
}
