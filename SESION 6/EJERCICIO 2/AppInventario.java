package actividad2;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


class Articulo {
    private String nombre;
    private int cantidad;
    private String tipo; 
    private String descripcion;
    private String usuario;

    public Articulo(String nombre, int cantidad, String tipo, String descripcion, String usuario) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.usuario = usuario;
    }

    public String getNombre() { return nombre; }
    public int getCantidad() { return cantidad; }
    public String getTipo() { return tipo; }
    public String getDescripcion() { return descripcion; }
    public String getUsuario() { return usuario; }

    @Override
    public String toString() {
        return "Articulo: " + nombre + " | Cantidad: " + cantidad + " | Tipo: " + tipo;
    }

    public String detalles() {
        return "Nombre: " + nombre +
                "\nCantidad: " + cantidad +
                "\nTipo: " + tipo +
                "\nDescripcion: " + descripcion +
                "\nUsuario: " + usuario;
    }
}


class InventarioModelo {
    private List<Articulo> articulos;

    public InventarioModelo() {
        articulos = new ArrayList<>();
    }

    public void agregarArticulo(Articulo articulo) {
        articulos.add(articulo);
    }

    public void eliminarArticulo(Articulo articulo) {
        articulos.remove(articulo);
    }

    public List<Articulo> obtenerArticulos() {
        return articulos;
    }

    public Articulo buscarArticulo(String nombre) {
        for (Articulo articulo : articulos) {
            if (articulo.getNombre().equalsIgnoreCase(nombre)) {
                return articulo;
            }
        }
        return null;
    }
}


class InventarioVista {
    public void mostrarInventario(List<Articulo> articulos) {
        if (articulos.isEmpty()) {
            System.out.println("Inventario vacio.");
        } else {
            System.out.println("\n--- INVENTARIO ---");
            for (Articulo articulo : articulos) {
                System.out.println(articulo);
            }
        }
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    public void mostrarDetalleArticulo(Articulo articulo) {
        if (articulo != null) {
            System.out.println("\n--- DETALLES DEL ARTICULO ---");
            System.out.println(articulo.detalles());
        } else {
            System.out.println("Articulo no encontrado.");
        }
    }
}


class InventarioControlador {
    private InventarioModelo modelo;
    private InventarioVista vista;

    public InventarioControlador(InventarioModelo modelo, InventarioVista vista) {
        this.modelo = modelo;
        this.vista = vista;
    }

    public void agregarArticulo(Articulo articulo) {
        modelo.agregarArticulo(articulo);
        vista.mostrarMensaje("Articulo agregado correctamente.");
    }

    public void eliminarArticulo(String nombre) {
        Articulo articulo = modelo.buscarArticulo(nombre);
        if (articulo != null) {
            modelo.eliminarArticulo(articulo);
            vista.mostrarMensaje("Articulo eliminado correctamente.");
        } else {
            vista.mostrarMensaje("No se encontro el articulo.");
        }
    }

    public void verInventario() {
        vista.mostrarInventario(modelo.obtenerArticulos());
    }

    public void mostrarDetalles(String nombre) {
        Articulo articulo = modelo.buscarArticulo(nombre);
        vista.mostrarDetalleArticulo(articulo);
    }

    public void buscarArticulo(String nombre) {
        Articulo articulo = modelo.buscarArticulo(nombre);
        if (articulo != null) {
            vista.mostrarMensaje("Articulo encontrado: " + articulo);
        } else {
            vista.mostrarMensaje("Articulo no encontrado.");
        }
    }
}


public class AppInventario {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        InventarioModelo modelo = new InventarioModelo();
        InventarioVista vista = new InventarioVista();
        InventarioControlador controlador = new InventarioControlador(modelo, vista);

        int opcion;
        do {
            System.out.println("\n===== MENU INVENTARIO =====");
            System.out.println("1. Agregar articulo");
            System.out.println("2. Eliminar articulo");
            System.out.println("3. Ver inventario");
            System.out.println("4. Ver detalles de articulo");
            System.out.println("5. Buscar articulo");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opcion: ");
            opcion = sc.nextInt();
            sc.nextLine(); 

            switch (opcion) {
                case 1 -> {
                    System.out.print("Nombre: ");
                    String nombre = sc.nextLine();
                    System.out.print("Cantidad: ");
                    int cantidad = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Tipo: ");
                    String tipo = sc.nextLine();
                    System.out.print("Descripcion: ");
                    String descripcion = sc.nextLine();
                    System.out.print("Usuario: ");
                    String usuario = sc.nextLine();

                    Articulo articulo = new Articulo(nombre, cantidad, tipo, descripcion, usuario);
                    controlador.agregarArticulo(articulo);
                }
                case 2 -> {
                    System.out.print("Nombre del articulo a eliminar: ");
                    String nombreEliminar = sc.nextLine();
                    controlador.eliminarArticulo(nombreEliminar);
                }
                case 3 -> controlador.verInventario();
                case 4 -> {
                    System.out.print("Nombre del articulo: ");
                    String nombreDetalle = sc.nextLine();
                    controlador.mostrarDetalles(nombreDetalle);
                }
                case 5 -> {
                    System.out.print("Nombre del articulo a buscar: ");
                    String nombreBuscar = sc.nextLine();
                    controlador.buscarArticulo(nombreBuscar);
                }
                case 0 -> System.out.println("Saliendo del sistema...");
                default -> System.out.println("Opcion invalida.");
            }
        } while (opcion != 0);

        sc.close();
    }
}