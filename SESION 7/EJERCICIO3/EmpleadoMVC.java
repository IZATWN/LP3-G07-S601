package EJERCICIO3;

import java.io.*;
import java.util.*;

// CLASE PRINCIPAL

public class EmpleadoMVC {

    private static final String ARCHIVO = "empleados.dat";
    private static final Scanner sc = new Scanner(System.in);


    // MODELO

    static class Empleado implements Serializable {
        private int numero;
        private String nombre;
        private double sueldo;

        public Empleado(int numero, String nombre, double sueldo) {
            this.numero = numero;
            this.nombre = nombre;
            this.sueldo = sueldo;
        }

        public int getNumero() { return numero; }
        public String getNombre() { return nombre; }
        public double getSueldo() { return sueldo; }

        public void setNombre(String nombre) { this.nombre = nombre; }
        public void setSueldo(double sueldo) { this.sueldo = sueldo; }

        @Override
        public String toString() {
            return String.format("N°: %d | Nombre: %s | Sueldo: %.2f", numero, nombre, sueldo);
        }
    }

    // CONTROLADOR

    static class EmpleadoController {

        // Leer lista de empleados desde archivo
        @SuppressWarnings("unchecked")
        public List<Empleado> leerEmpleados() {
            List<Empleado> empleados = new ArrayList<>();
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARCHIVO))) {
                empleados = (List<Empleado>) ois.readObject();
            } catch (FileNotFoundException e) {
                // Si no existe, devolvemos lista vacía
            } catch (IOException | ClassNotFoundException e) {
                System.out.println(" Error leyendo archivo: " + e.getMessage());
            }
            return empleados;
        }

        // Guardar lista de empleados en archivo
        public void guardarEmpleados(List<Empleado> empleados) {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO))) {
                oos.writeObject(empleados);
            } catch (IOException e) {
                System.out.println(" Error guardando archivo: " + e.getMessage());
            }
        }

        // Listar empleados
        public void listarEmpleados() {
            List<Empleado> lista = leerEmpleados();
            if (lista.isEmpty()) {
                System.out.println("\nNo hay empleados registrados.\n");
            } else {
                System.out.println("\n=== LISTA DE EMPLEADOS ===");
                for (Empleado e : lista) {
                    System.out.println(e);
                }
                System.out.println("==========================\n");
            }
        }

        // Agregar nuevo empleado
        public void agregarEmpleado(Empleado nuevo) {
            List<Empleado> lista = leerEmpleados();
            boolean existe = lista.stream().anyMatch(e -> e.getNumero() == nuevo.getNumero());
            if (existe) {
                System.out.println(" Ya existe un empleado con ese número.");
                return;
            }
            lista.add(nuevo);
            guardarEmpleados(lista);
            System.out.println(" Empleado agregado correctamente.");
        }

        // Buscar empleado por número
        public Empleado buscarEmpleado(int numero) {
            List<Empleado> lista = leerEmpleados();
            return lista.stream().filter(e -> e.getNumero() == numero).findFirst().orElse(null);
        }

        // Eliminar empleado
        public void eliminarEmpleado(int numero) {
            List<Empleado> lista = leerEmpleados();
            boolean eliminado = lista.removeIf(e -> e.getNumero() == numero);
            if (eliminado) {
                guardarEmpleados(lista);
                System.out.println(" Empleado eliminado correctamente.");
            } else {
                System.out.println(" Empleado no encontrado.");
            }
        }
    }

    // VISTA / MENÚ
    public static void main(String[] args) {
        EmpleadoController controlador = new EmpleadoController();
        int opcion;

        do {
            System.out.println("\n===== GESTION DE EMPLEADOS =====");
            System.out.println("1. Listar todos los empleados");
            System.out.println("2. Agregar un nuevo empleado");
            System.out.println("3. Buscar empleado por número");
            System.out.println("4. Eliminar empleado por número");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = leerEntero();

            switch (opcion) {
                case 1:
                    controlador.listarEmpleados();
                    break;
                case 2:
                    agregarEmpleado(controlador);
                    break;
                case 3:
                    buscarEmpleado(controlador);
                    break;
                case 4:
                    eliminarEmpleado(controlador);
                    break;
                case 5:
                    System.out.println( "Saliendo del programa...");
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        } while (opcion != 5);
    }

    // FUNCIONES AUXILIARES DE VISTA
    private static void agregarEmpleado(EmpleadoController controlador) {
        System.out.print("Ingrese número: ");
        int numero = leerEntero();
        System.out.print("Ingrese nombre: ");
        String nombre = sc.nextLine();
        System.out.print("Ingrese sueldo: ");
        double sueldo = leerDouble();

        Empleado nuevo = new Empleado(numero, nombre, sueldo);
        controlador.agregarEmpleado(nuevo);
    }

    private static void buscarEmpleado(EmpleadoController controlador) {
        System.out.print("Ingrese número de empleado: ");
        int numero = leerEntero();
        Empleado e = controlador.buscarEmpleado(numero);
        if (e != null) {
            System.out.println("\nEmpleado encontrado:\n" + e + "\n");
        } else {
            System.out.println(" No existe un empleado con ese número.");
        }
    }

    private static void eliminarEmpleado(EmpleadoController controlador) {
        System.out.print("Ingrese número de empleado a eliminar: ");
        int numero = leerEntero();
        controlador.eliminarEmpleado(numero);
    }

    // Métodos seguros para leer números
    private static int leerEntero() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Valor inválido. Intente de nuevo: ");
            }
        }
    }

    private static double leerDouble() {
        while (true) {
            try {
                return Double.parseDouble(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Valor inválido. Intente de nuevo: ");
            }
        }
    }
}


