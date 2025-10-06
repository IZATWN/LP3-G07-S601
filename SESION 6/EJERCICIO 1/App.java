package actividad2;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

class Producto {
    private int id;
    private String nombre;
    private double precio;

    public Producto(int id, String nombre, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }

    @Override
    public String toString() {
        return id + " - " + nombre + " (S/" + precio + ")";
    }
}

class Carrito {
    private List<Producto> productos = new ArrayList<>();

    public void agregarProducto(Producto p) {
        productos.add(p);
    }

    public void eliminarProducto(int id) {
        productos.removeIf(p -> p.getId() == id);
    }

    public double calcularTotal(double descuento, double envio) {
        double total = productos.stream().mapToDouble(Producto::getPrecio).sum();
        total -= total * descuento; 
        total += envio; 
        return total;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void vaciar() {
        productos.clear();
    }

    public boolean estaVacio() {
        return productos.isEmpty();
    }
}

class Compra {
    private List<Producto> productos;
    private double total;

    public Compra(List<Producto> productos, double total) {
        this.productos = productos;
        this.total = total;
    }

    @Override
    public String toString() {
        return "Compra: " + productos.size() + " productos, Total: S/" + total;
    }
}




class VistaConsola {
    private Scanner sc = new Scanner(System.in);

    public int mostrarMenu() {
        System.out.println("\n===== MENU PRINCIPAL =====");
        System.out.println("1. Listar productos");
        System.out.println("2. Agregar producto al carrito");
        System.out.println("3. Ver carrito");
        System.out.println("4. Eliminar producto del carrito");
        System.out.println("5. Realizar compra");
        System.out.println("6. Ver historial de compras");
        System.out.println("0. Salir");
        System.out.print("Seleccione una opcion: ");
        return sc.nextInt();
    }

    public int pedirIdProducto() {
        System.out.print("Ingrese el ID del producto: ");
        return sc.nextInt();
    }

    public void mostrarProductos(List<Producto> productos) {
        if (productos.isEmpty()) {
            System.out.println("No hay productos registrados.");
        } else {
            productos.forEach(System.out::println);
        }
    }

    public void mostrarCarrito(Carrito carrito) {
        if (carrito.getProductos().isEmpty()) {
            System.out.println("El carrito esta vacio.");
        } else {
            System.out.println("Carrito:");
            carrito.getProductos().forEach(System.out::println);
        }
    }

    public double pedirDescuento() {
        System.out.print("Ingrese descuento (ej: 0.10 para 10%): ");
        return sc.nextDouble();
    }

    public double pedirEnvio() {
        System.out.print("Ingrese costo de envio en soles: ");
        return sc.nextDouble();
    }

    public void mostrarCompra(Compra compra) {
        System.out.println("Compra realizada con exito: " + compra);
    }

    public void mostrarHistorial(List<Compra> historial) {
        if (historial.isEmpty()) {
            System.out.println("No hay compras realizadas.");
        } else {
            historial.forEach(System.out::println);
        }
    }

    public void mostrarMensaje(String msg) {
        System.out.println(msg);
    }
}




class Controlador {
    private List<Producto> productos;
    private Carrito carrito;
    private List<Compra> historial;
    private VistaConsola vista;

    public Controlador(List<Producto> productos, VistaConsola vista) {
        this.productos = productos;
        this.vista = vista;
        this.carrito = new Carrito();
        this.historial = new ArrayList<>();
    }

    public void iniciar() {
        int opcion;
        do {
            opcion = vista.mostrarMenu();
            switch (opcion) {
                case 1 -> vista.mostrarProductos(productos);
                case 2 -> agregarProducto();
                case 3 -> vista.mostrarCarrito(carrito);
                case 4 -> eliminarProducto();
                case 5 -> realizarCompra();
                case 6 -> vista.mostrarHistorial(historial);
                case 0 -> vista.mostrarMensaje("Saliendo del sistema...");
                default -> vista.mostrarMensaje("Opcion no valida.");
            }
        } while (opcion != 0);
    }

    private void agregarProducto() {
        int id = vista.pedirIdProducto();
        productos.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .ifPresentOrElse(carrito::agregarProducto,
                        () -> vista.mostrarMensaje("Producto no encontrado."));
    }

    private void eliminarProducto() {
        int id = vista.pedirIdProducto();
        carrito.eliminarProducto(id);
        vista.mostrarMensaje("Producto eliminado si existia.");
    }

    private void realizarCompra() {
        if (carrito.estaVacio()) {
            vista.mostrarMensaje("El carrito esta vacio.");
            return;
        }
        double descuento = vista.pedirDescuento();
        double envio = vista.pedirEnvio();
        double total = carrito.calcularTotal(descuento, envio);

        Compra compra = new Compra(new ArrayList<>(carrito.getProductos()), total);
        historial.add(compra);
        vista.mostrarCompra(compra);
        carrito.vaciar();
    }
}




public class App {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<Producto> productos = new ArrayList<>();

        
        System.out.print("Cuantos productos desea registrar? ");
        int n = sc.nextInt();
        sc.nextLine();

        for (int i = 0; i < n; i++) {
            System.out.println("Producto " + (i + 1) + ":");
            System.out.print("ID: ");
            int id = sc.nextInt();
            sc.nextLine(); 
            System.out.print("Nombre: ");
            String nombre = sc.nextLine();
            System.out.print("Precio (S/): ");
            double precio = sc.nextDouble();
            sc.nextLine();
            productos.add(new Producto(id, nombre, precio));
        }

       
        VistaConsola vista = new VistaConsola();
        Controlador controlador = new Controlador(productos, vista);

        
        controlador.iniciar();
    }
}
