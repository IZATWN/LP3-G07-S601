package actividad2;
import java.io.*;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

class Personaje implements Serializable {
    
    private String nombre;
    private int vida;
    private int ataque;
    private int defensa;
    private int alcance;
    
    public static final String SEPARADOR = ";";

    public Personaje(String nombre, int vida, int ataque, int defensa, int alcance) {
        this.nombre = nombre;
        this.vida = validarAtributo("Vida", vida);
        this.ataque = validarAtributo("Ataque", ataque);
        this.defensa = validarAtributo("Defensa", defensa);
        this.alcance = validarAtributo("Alcance", alcance);
    }

    private int validarAtributo(String nombreAttr, int valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException(
                "El atributo '" + nombreAttr + "' debe ser un numero entero mayor que cero. Valor recibido: " + valor
            );
        }
        return valor;
    }

    public boolean modificarAtributo(String atributo, int nuevoValor) {
        try {
            if (atributo.toLowerCase().equals("nombre")) {
                 System.err.println("ERROR: No se puede modificar el nombre de un personaje.");
                 return false;
            }
            
            int valorValidado = validarAtributo(atributo, nuevoValor);
            
            switch (atributo.toLowerCase()) {
                case "vida":
                    this.vida = valorValidado;
                    break;
                case "ataque":
                    this.ataque = valorValidado;
                    break;
                case "defensa":
                    this.defensa = valorValidado;
                    break;
                case "alcance":
                    this.alcance = valorValidado;
                    break;
                default:
                    System.err.println("ERROR: El atributo '" + atributo + "' no es modificable o no existe.");
                    return false;
            }
            System.out.printf("'%s' | Atributo '%s' modificado a %d.%n", this.nombre, atributo, nuevoValor);
            return true;
        } catch (IllegalArgumentException e) {
            System.err.println("ERROR al modificar " + atributo + ": " + e.getMessage());
            return false;
        }
    }

    public String aCadenaArchivo() {
        return this.nombre + SEPARADOR + this.vida + SEPARADOR + this.ataque + SEPARADOR + this.defensa + SEPARADOR + this.alcance;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return String.format("| %-10s | Vida: %2d | Ataque: %2d | Defensa: %2d | Alcance: %2d |", 
                             nombre, vida, ataque, defensa, alcance);
    }
}

class Gestor {
    
    private Map<String, Personaje> personajes;
    private final String nombreArchivo;

    public Gestor(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
        this.personajes = new HashMap<>();
        this.leerPersonajes();
    }
    
    public void leerPersonajes() {
        File file = new File(nombreArchivo); 
        if (!file.exists()) {
            System.out.println("Gestor inicializado. Archivo de datos no encontrado, se iniciara vacio.");
            return;
        }

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String linea = scanner.nextLine();
                String[] datos = linea.split(Personaje.SEPARADOR);
                
                if (datos.length == 5) {
                    try {
                        Personaje p = new Personaje(
                            datos[0], 
                            Integer.parseInt(datos[1]), 
                            Integer.parseInt(datos[2]), 
                            Integer.parseInt(datos[3]), 
                            Integer.parseInt(datos[4])
                        );
                        this.personajes.put(p.getNombre(), p);
                    } catch (NumberFormatException e) {
                        System.err.println("Error de formato de numero al cargar personaje. Linea: " + linea + ". " + e.getMessage());
                    } catch (IllegalArgumentException e) {
                        System.err.println("Error en la validacion de atributos al cargar personaje. Linea: " + linea + ". " + e.getMessage());
                    }
                }
            }
            System.out.println("Gestor inicializado. " + personajes.size() + " personajes cargados.");
        } catch (FileNotFoundException e) {
            System.err.println("Archivo " + nombreArchivo + " no encontrado.");
        }
    }

    private void guardarPersonajes() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(nombreArchivo))) {
            for (Personaje p : this.personajes.values()) {
                writer.println(p.aCadenaArchivo());
            }
            System.out.println("\n--- CAMBIOS GUARDADOS en " + nombreArchivo + " ---");
        } catch (IOException e) {
            System.err.println("E/S al guardar el archivo: " + e.getMessage());
        }
    }

    public boolean anadirPersonaje(String nombre, int vida, int ataque, int defensa, int alcance) {
        String nombreClave = nombre.trim();
        if (nombreClave.isEmpty() || this.personajes.containsKey(nombreClave)) {
            System.err.println("El nombre no puede estar vacio o el personaje '" + nombreClave + "' ya existe.");
            return false;
        }
        
        try {
            Personaje nuevoPersonaje = new Personaje(nombreClave, vida, ataque, defensa, alcance);
            this.personajes.put(nombreClave, nuevoPersonaje);
            guardarPersonajes(); 
            System.out.println("PERSONAJE '" + nombreClave + "' anadido exitosamente.");
            return true;
        } catch (IllegalArgumentException e) {
            System.err.println("erroral crear personaje: " + e.getMessage());
            return false;
        }
    }

    public void mostrarPersonajes() {
        if (this.personajes.isEmpty()) {
            System.out.println("\n--- No hay personajes registrados en el Gestor. ---");
            return;
        }
        
        System.out.println("\n--- LISTA DE PERSONAJES REGISTRADOS ---");
        for (Personaje p : this.personajes.values()) {
            System.out.println(p);
        }
        System.out.println("------------------------------------------");
    }

    public boolean borrarPersonaje(String nombre) {
        String nombreClave = nombre.trim();
        if (this.personajes.containsKey(nombreClave)) {
            this.personajes.remove(nombreClave);
            guardarPersonajes(); 
            System.out.println("PERSONAJE '" + nombreClave + "' borrado exitosamente.");
            return true;
        } else {
            System.err.println("El personaje '" + nombreClave + "' no se encuentra en el Gestor.");
            return false;
        }
    }

    public boolean modificarPersonaje(String nombre, String atributo, int nuevoValor) {
        String nombreClave = nombre.trim();
        if (this.personajes.containsKey(nombreClave)) {
            Personaje p = this.personajes.get(nombreClave);
            if (p.modificarAtributo(atributo, nuevoValor)) {
                guardarPersonajes(); 
                return true;
            }
            return false;
        } else {
            System.err.println("El personaje '" + nombreClave + "' no se encuentra en el Gestor para modificar.");
            return false;
        }
    }
}

public class GestorPersonajes {
    
    public static final String NOMBRE_ARCHIVO = "personajes.txt";
    private static Scanner scanner = new Scanner(System.in);
    
    private static void mostrarMenu() {
        System.out.println("\n===== GESTOR DE PERSONAJES =====");
        System.out.println("1. Mostrar todos los personajes");
        System.out.println("2. Anadir nuevo personaje");
        System.out.println("3. Modificar un personaje");
        System.out.println("4. Borrar un personaje");
        System.out.println("5. Salir y guardar");
        System.out.println("================================");
        System.out.print("Seleccione una opcion: ");
    }
    
    private static void anadir(Gestor gestor) {
        System.out.println("\n--- ANADIR PERSONAJE ---");
        
        System.out.print("Nombre: ");
        scanner.nextLine(); 
        String nombre = scanner.nextLine().trim();

        int vida = leerEntero("Vida", "Ingrese Vida (> 0): ");
        int ataque = leerEntero("Ataque", "Ingrese Ataque (> 0): ");
        int defensa = leerEntero("Defensa", "Ingrese Defensa (> 0): ");
        int alcance = leerEntero("Alcance", "Ingrese Alcance (> 0): ");
        
        gestor.anadirPersonaje(nombre, vida, ataque, defensa, alcance);
    }
    
    private static void modificar(Gestor gestor) {
        System.out.println("\n--- MODIFICAR PERSONAJE ---");
        gestor.mostrarPersonajes();

        System.out.print("Ingrese el NOMBRE del personaje a modificar: ");
        scanner.nextLine(); 
        String nombre = scanner.nextLine().trim();
        
        System.out.print("Ingrese el ATRIBUTO a modificar (vida, ataque, defensa, alcance): ");
        String atributo = scanner.nextLine().trim();
        
        int nuevoValor = leerEntero("Nuevo Valor", "Ingrese el NUEVO VALOR (> 0) para " + atributo + ": ");
        
        gestor.modificarPersonaje(nombre, atributo, nuevoValor);
    }
    
    private static void borrar(Gestor gestor) {
        System.out.println("\n--- BORRAR PERSONAJE ---");
        gestor.mostrarPersonajes();

        System.out.print("Ingrese el NOMBRE del personaje a borrar: ");
        scanner.nextLine(); 
        String nombre = scanner.nextLine().trim();
        
        gestor.borrarPersonaje(nombre);
    }

    private static int leerEntero(String nombreCampo, String mensaje) {
        int valor = -1;
        boolean valido = false;
        while (!valido) {
            try {
                System.out.print(mensaje);
                valor = scanner.nextInt();
                if (valor > 0) {
                    valido = true;
                } else {
                    System.err.println("El valor de " + nombreCampo + " debe ser mayor que cero.");
                }
            } catch (InputMismatchException e) {
                System.err.println("Debe ingresar un numero entero valido.");
                scanner.next(); 
            }
        }
        return valor;
    }

    public static void main(String[] args) {
        Gestor gestor = new Gestor(NOMBRE_ARCHIVO);
        int opcion = 0;
        boolean salir = false;
        
        while (!salir) {
            mostrarMenu();
            
            try {
                opcion = scanner.nextInt();
                
                switch (opcion) {
                    case 1:
                        gestor.mostrarPersonajes();
                        break;
                    case 2:
                        anadir(gestor);
                        break;
                    case 3:
                        modificar(gestor);
                        break;
                    case 4:
                        borrar(gestor);
                        break;
                    case 5:
                        System.out.println("\nSaliendo del Gestor de Personajes. ¡Hasta pronto!");
                        salir = true;
                        break;
                    default:
                        System.err.println("Opcion no valida. Intente de nuevo.");
                }
            } catch (InputMismatchException e) {
                System.err.println("Por favor, ingrese un numero entero para la opcion.");
                scanner.next(); 
            }
        }
        scanner.close(); 
    }
}