package actividad2;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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

    public void reducirCantidad() {
        if (cantidad > 0) cantidad--;
    }

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


class Jugador {
    private String nombre;
    private int salud;
    private int nivel;
    private InventarioModelo inventario;
    private Articulo equipado; 

    public Jugador(String nombre) {
        this.nombre = nombre;
        this.salud = 100;
        this.nivel = 1;
        this.inventario = new InventarioModelo();
    }

    public String getNombre() { return nombre; }
    public int getSalud() { return salud; }
    public int getNivel() { return nivel; }
    public InventarioModelo getInventario() { return inventario; }
    public Articulo getEquipado() { return equipado; }

    public void equipar(Articulo articulo) {
        if (articulo != null) {
            equipado = articulo;
        }
    }

    public void recibirDanio(int danio) {
        salud -= danio;
        if (salud < 0) salud = 0;
    }

    public void usarArticulo(String nombreArticulo) {
        Articulo articulo = inventario.buscarArticulo(nombreArticulo);
        if (articulo != null && articulo.getTipo().equalsIgnoreCase("pocion") && articulo.getCantidad() > 0) {
            salud += 20;
            articulo.reducirCantidad();
            System.out.println(nombre + " uso una pocion y recupero salud. Salud actual: " + salud);
        } else {
            System.out.println("No puedes usar ese articulo.");
        }
    }

    public int atacar() {
        if (equipado != null && equipado.getTipo().equalsIgnoreCase("arma")) {
            return 20; 
        }
        return 10; 
    }
}


class Enemigo {
    private String nombre;
    private int salud;
    private int nivel;
    private String tipo;

    public Enemigo(String nombre, int nivel, String tipo) {
        this.nombre = nombre;
        this.salud = 50 + nivel * 10;
        this.nivel = nivel;
        this.tipo = tipo;
    }

    public String getNombre() { return nombre; }
    public int getSalud() { return salud; }
    public int getNivel() { return nivel; }
    public String getTipo() { return tipo; }

    public void recibirDanio(int danio) {
        salud -= danio;
        if (salud < 0) salud = 0;
    }

    public int atacar() {
        return 5 + nivel * 2;
    }

    public boolean estaVivo() {
        return salud > 0;
    }
}


class CombateVista {
    public void mostrarEstado(Jugador jugador, List<Enemigo> enemigos) {
        System.out.println("\n===== ESTADO DEL COMBATE =====");
        System.out.println("Jugador: " + jugador.getNombre() + " | Salud: " + jugador.getSalud());
        for (Enemigo enemigo : enemigos) {
            System.out.println("Enemigo: " + enemigo.getNombre() + " | Salud: " + enemigo.getSalud());
        }
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }
}


class CombateControlador {
    private Jugador jugador;
    private List<Enemigo> enemigos;
    private CombateVista vista;
    private Random random = new Random();

    public CombateControlador(Jugador jugador, List<Enemigo> enemigos, CombateVista vista) {
        this.jugador = jugador;
        this.enemigos = enemigos;
        this.vista = vista;
    }

    public void iniciarCombate() {
        Scanner sc = new Scanner(System.in);

        while (jugador.getSalud() > 0 && enemigos.stream().anyMatch(Enemigo::estaVivo)) {
            vista.mostrarEstado(jugador, enemigos);
            System.out.println("\n1. Atacar");
            System.out.println("2. Usar pocion");
            System.out.print("Elige una accion: ");
            int opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {
                case 1 -> {
                    Enemigo enemigo = enemigos.stream().filter(Enemigo::estaVivo).findFirst().orElse(null);
                    if (enemigo != null) {
                        int danio = jugador.atacar();
                        enemigo.recibirDanio(danio);
                        vista.mostrarMensaje(jugador.getNombre() + " ataco a " + enemigo.getNombre() + " causando " + danio + " de danio.");
                    }
                }
                case 2 -> {
                    jugador.usarArticulo("pocion");
                }
                default -> vista.mostrarMensaje("Accion no valida.");
            }

            for (Enemigo enemigo : enemigos) {
                if (enemigo.estaVivo()) {
                    if (random.nextBoolean()) {
                        int danio = enemigo.atacar();
                        jugador.recibirDanio(danio);
                        vista.mostrarMensaje(enemigo.getNombre() + " ataco a " + jugador.getNombre() + " causando " + danio + " de danio.");
                    } else {
                        vista.mostrarMensaje(enemigo.getNombre() + " no hizo nada.");
                    }
                }
            }
        }

        if (jugador.getSalud() > 0) {
            vista.mostrarMensaje("\nHas ganado el combate!");
        } else {
            vista.mostrarMensaje("\nHas sido derrotado...");
        }
    }
}


public class AppJuego {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

       
        System.out.print("Ingresa el nombre de tu jugador: ");
        Jugador jugador = new Jugador(sc.nextLine());

       
        System.out.print("Cuantos articulos quieres agregar al inventario?: ");
        int cantidadArticulos = sc.nextInt();
        sc.nextLine();

        for (int i = 0; i < cantidadArticulos; i++) {
            System.out.println("=== Articulo " + (i + 1) + " ===");
            System.out.print("Nombre: ");
            String nombreArticulo = sc.nextLine();
            System.out.print("Cantidad: ");
            int cantidad = sc.nextInt();
            sc.nextLine();
            System.out.print("Tipo (arma/pocion): ");
            String tipo = sc.nextLine();
            System.out.print("Descripcion: ");
            String descripcion = sc.nextLine();

            Articulo articulo = new Articulo(nombreArticulo, cantidad, tipo, descripcion, jugador.getNombre());
            jugador.getInventario().agregarArticulo(articulo);

            if (tipo.equalsIgnoreCase("arma")) {
                jugador.equipar(articulo);
            }
        }

       
        System.out.print("Cuantos enemigos deseas crear?: ");
        int numEnemigos = sc.nextInt();
        sc.nextLine();
        List<Enemigo> enemigos = new ArrayList<>();

        for (int i = 0; i < numEnemigos; i++) {
            System.out.println("=== Enemigo " + (i + 1) + " ===");
            System.out.print("Nombre: ");
            String nombreEnemigo = sc.nextLine();
            System.out.print("Nivel: ");
            int nivel = sc.nextInt();
            sc.nextLine();
            System.out.print("Tipo: ");
            String tipo = sc.nextLine();

            enemigos.add(new Enemigo(nombreEnemigo, nivel, tipo));
        }

        
        CombateVista vista = new CombateVista();

        
        CombateControlador combate = new CombateControlador(jugador, enemigos, vista);

        
        combate.iniciarCombate();
    }
}
