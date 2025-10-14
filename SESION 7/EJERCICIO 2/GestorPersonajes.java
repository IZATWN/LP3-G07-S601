package actividad2;
import java.io.*;
import java.util.*;

class Personaje implements Serializable {
    
    private String nombre;
    private int vida;
    private int ataque;
    private int defensa;
    private int alcance;
    private int nivel; 
    
    public static final String SEPARADOR = ";";

    public Personaje(String nombre, int vida, int ataque, int defensa, int alcance) {
        this(nombre, vida, ataque, defensa, alcance, 1); 
    }
    
    public Personaje(String nombre, int vida, int ataque, int defensa, int alcance, int nivel) {
        this.nombre = nombre;
        this.vida = validarAtributo("Vida", vida);
        this.ataque = validarAtributo("Ataque", ataque);
        this.defensa = validarAtributo("Defensa", defensa);
        this.alcance = validarAtributo("Alcance", alcance);
        this.nivel = validarAtributo("Nivel", nivel);
    }

    private int validarAtributo(String nombreAttr, int valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException(
                "El atributo '" + nombreAttr + "' debe ser un numero entero mayor que cero. Valor recibido: " + valor
            );
        }
        return valor;
    }

    public void subirNivel() {
        this.nivel++;
        this.vida += 2; 
        this.ataque += 1;
        this.defensa += 1;
        this.alcance += 1;
        System.out.printf("'%s' ha subido al Nivel %d! Atributos mejorados.%n", this.nombre, this.nivel);
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
                case "nivel":
                    this.nivel = valorValidado;
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
        return this.nombre + SEPARADOR + this.vida + SEPARADOR + this.ataque + SEPARADOR + this.defensa + SEPARADOR + this.alcance + SEPARADOR + this.nivel;
    }

    public String getNombre() {
        return nombre;
    }
    
    public int getVida() { return vida; }
    public int getAtaque() { return ataque; }
    public int getDefensa() { return defensa; }
    public int getAlcance() { return alcance; }
    public int getNivel() { return nivel; }

    @Override
    public String toString() {
        return String.format("| %-10s | Nivel: %2d | Vida: %2d | Ataque: %2d | Defensa: %2d | Alcance: %2d |", 
                             nombre, nivel, vida, ataque, defensa, alcance);
    }
}

class Gestor {
    
    private Map<String, Personaje> personajes;
    private final String nombreArchivo;

    public Gestor(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
        this.personajes = new HashMap<>();
        this.leerPersonajes();
        
        if (this.personajes.isEmpty()) {
            cargarPersonajesIniciales();
        }
    }
    
    private void cargarPersonajesIniciales() {
        System.out.println("--- Lista de personajes vacia. Cargando personajes iniciales. ---");
        try {
            this.personajes.put("Guerrero", new Personaje("Guerrero", 15, 8, 10, 2, 2));
            this.personajes.put("Arquero", new Personaje("Arquero", 10, 12, 5, 15, 1));
            this.personajes.put("Mago", new Personaje("Mago", 8, 15, 4, 10, 1));
            this.personajes.put("Tanque", new Personaje("Tanque", 25, 5, 15, 1, 3));
            guardarPersonajes();
        } catch (IllegalArgumentException e) {
            System.err.println("ERROR interno al crear personajes iniciales: " + e.getMessage());
        }
    }
    
    public void leerPersonajes() {
        File file = new File(nombreArchivo); 
        if (!file.exists()) {
            System.out.println("Gestor inicializado. Archivo de datos no encontrado.");
            return;
        }

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String linea = scanner.nextLine();
                String[] datos = linea.split(Personaje.SEPARADOR);
                
                if (datos.length >= 5) {
                    try {
                        Personaje p = new Personaje(
                            datos[0], 
                            Integer.parseInt(datos[1]), 
                            Integer.parseInt(datos[2]), 
                            Integer.parseInt(datos[3]), 
                            Integer.parseInt(datos[4]),
                            datos.length > 5 ? Integer.parseInt(datos[5]) : 1 
                        );
                        this.personajes.put(p.getNombre(), p);
                    } 
                    // SOLUCION ERROR CATCH: Separacion de excepciones para maxima compatibilidad
                    catch (NumberFormatException e) {
                        System.err.println("ADVERTENCIA: Error de formato de numero al cargar personaje. Linea: " + linea + ". " + e.getMessage());
                    } catch (IllegalArgumentException e) {
                        System.err.println("ADVERTENCIA: Error en la validacion de atributos al cargar personaje. Linea: " + linea + ". " + e.getMessage());
                    }
                }
            }
            System.out.println("Gestor inicializado. " + personajes.size() + " personajes cargados.");
        } catch (FileNotFoundException e) {
            System.err.println("ADVERTENCIA: Archivo " + nombreArchivo + " no encontrado.");
        }
    }

    private void guardarPersonajes() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(nombreArchivo))) {
            for (Personaje p : this.personajes.values()) {
                writer.println(p.aCadenaArchivo());
            }
            System.out.println("\n--- CAMBIOS GUARDADOS en " + nombreArchivo + " ---");
        } catch (IOException e) {
            System.err.println("ERROR de E/S al guardar el archivo: " + e.getMessage());
        }
    }

    public boolean anadirPersonaje(String nombre, int vida, int ataque, int defensa, int alcance) {
        String nombreClave = nombre.trim();
        if (nombreClave.isEmpty() || this.personajes.containsKey(nombreClave)) {
            System.err.println("ERROR: El nombre no puede estar vacio o el personaje '" + nombreClave + "' ya existe.");
            return false;
        }
        
        try {
            Personaje nuevoPersonaje = new Personaje(nombreClave, vida, ataque, defensa, alcance, 1);
            this.personajes.put(nombreClave, nuevoPersonaje);
            guardarPersonajes(); 
            System.out.println("PERSONAJE '" + nombreClave + "' anadido exitosamente.");
            return true;
        } catch (IllegalArgumentException e) {
            System.err.println("ERROR al crear personaje: " + e.getMessage());
            return false;
        }
    }

    public boolean subirNivelPersonaje(String nombre) {
        String nombreClave = nombre.trim();
        if (this.personajes.containsKey(nombreClave)) {
            Personaje p = this.personajes.get(nombreClave);
            p.subirNivel();
            guardarPersonajes();
            return true;
        } else {
            System.err.println("ERROR: El personaje '" + nombreClave + "' no se encuentra en el Gestor para subir de nivel.");
            return false;
        }
    }

    public void mostrarPersonajesOrdenados(String atributo) {
        if (this.personajes.isEmpty()) {
            System.out.println("\n--- No hay personajes registrados en el Gestor. ---");
            return;
        }

        List<Personaje> lista = new ArrayList<>(this.personajes.values());

        Comparator<Personaje> comparador;
        switch (atributo.toLowerCase()) {
            case "vida":
                comparador = Comparator.comparing(Personaje::getVida).reversed();
                break;
            case "ataque":
                comparador = Comparator.comparing(Personaje::getAtaque).reversed();
                break;
            case "defensa":
                comparador = Comparator.comparing(Personaje::getDefensa).reversed();
                break;
            case "alcance":
                comparador = Comparator.comparing(Personaje::getAlcance).reversed();
                break;
            case "nivel":
                comparador = Comparator.comparing(Personaje::getNivel).reversed();
                break;
            default:
                System.out.println("Atributo de ordenacion no valido. Mostrando por nombre.");
                comparador = Comparator.comparing(Personaje::getNombre);
                break;
        }

        lista.sort(comparador);

        System.out.println("\n--- LISTA DE PERSONAJES ORDENADOS POR " + atributo.toUpperCase() + " ---");
        for (Personaje p : lista) {
            System.out.println(p);
        }
        System.out.println("------------------------------------------");
    }

    public void mostrarPersonajes() {
        mostrarPersonajesOrdenados("nombre");
    }

    public void mostrarEstadisticas() {
        int total = personajes.size();
        if (total == 0) {
            System.out.println("\n--- No hay personajes para calcular estadisticas. ---");
            return;
        }

        double vidaTotal = 0, ataqueTotal = 0, defensaTotal = 0, alcanceTotal = 0, nivelTotal = 0;

        for (Personaje p : this.personajes.values()) {
            vidaTotal += p.getVida();
            ataqueTotal += p.getAtaque();
            defensaTotal += p.getDefensa();
            alcanceTotal += p.getAlcance();
            nivelTotal += p.getNivel();
        }

        System.out.println("\n--- ESTADISTICAS GENERALES ---");
        System.out.println("Total de Personajes: " + total);
        System.out.printf("Nivel Promedio: %.2f%n", nivelTotal / total);
        System.out.printf("Vida Promedio: %.2f%n", vidaTotal / total);
        System.out.printf("Ataque Promedio: %.2f%n", ataqueTotal / total);
        System.out.printf("Defensa Promedio: %.2f%n", defensaTotal / total);
        System.out.printf("Alcance Promedio: %.2f%n", alcanceTotal / total);
        System.out.println("------------------------------");
    }

    public boolean importarPersonajes(String archivoImportar) {
        File file = new File(archivoImportar);
        if (!file.exists()) {
            System.err.println("ERROR: Archivo de importacion no encontrado: " + archivoImportar);
            return false;
        }
        
        int importados = 0;
        try (Scanner scanner = new Scanner(file)) {
            System.out.println("\n--- INICIANDO IMPORTACION DESDE " + archivoImportar + " ---");
            while (scanner.hasNextLine()) {
                String linea = scanner.nextLine();
                String[] datos = linea.split(Personaje.SEPARADOR);
                
                if (datos.length >= 5) {
                    try {
                        String nombre = datos[0];
                        int vida = Integer.parseInt(datos[1]);
                        int ataque = Integer.parseInt(datos[2]);
                        int defensa = Integer.parseInt(datos[3]);
                        int alcance = Integer.parseInt(datos[4]);
                        int nivel = datos.length > 5 ? Integer.parseInt(datos[5]) : 1;
                        
                        if (!this.personajes.containsKey(nombre)) {
                            Personaje p = new Personaje(nombre, vida, ataque, defensa, alcance, nivel);
                            this.personajes.put(nombre, p);
                            importados++;
                        } else {
                            System.out.println("ADVERTENCIA: Personaje '" + nombre + "' ya existe. Omitiendo importacion.");
                        }
                    } 
                    // SOLUCION ERROR CATCH
                    catch (NumberFormatException e) {
                        System.err.println("ERROR al procesar linea de importacion: " + linea + ". " + e.getMessage());
                    } catch (IllegalArgumentException e) {
                        System.err.println("ERROR al procesar linea de importacion: " + linea + ". " + e.getMessage());
                    }
                }
            }
            guardarPersonajes();
            System.out.println("--- IMPORTACION FINALIZADA: " + importados + " personajes anadidos. ---");
            return true;
        } catch (FileNotFoundException e) {
            System.err.println("ERROR: Archivo no encontrado durante la importacion.");
            return false;
        }
    }

    public boolean borrarPersonaje(String nombre) {
        String nombreClave = nombre.trim();
        if (this.personajes.containsKey(nombreClave)) {
            this.personajes.remove(nombreClave);
            guardarPersonajes(); 
            System.out.println("PERSONAJE '" + nombreClave + "' borrado exitosamente.");
            return true;
        } else {
            System.err.println("ERROR: El personaje '" + nombreClave + "' no se encuentra en el Gestor.");
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
            System.err.println("ERROR: El personaje '" + nombreClave + "' no se encuentra en el Gestor para modificar.");
            return false;
        }
    }
}

public class GestorPersonajes {
    
    public static final String NOMBRE_ARCHIVO = "personajes.txt";
    private static Scanner scanner = new Scanner(System.in);
    
    private static void mostrarMenu() {
        System.out.println("\n===== GESTOR DE PERSONAJES =====");
        System.out.println("1. Mostrar todos (por nombre)");
        System.out.println("2. Anadir nuevo personaje");
        System.out.println("3. Modificar un atributo (individual)");
        System.out.println("4. Borrar un personaje");
        System.out.println("5. Subir de NIVEL a un personaje (MEJORA)");
        System.out.println("6. Filtrar/Ordenar por Atributo");
        System.out.println("7. Mostrar ESTADISTICAS promedio");
        System.out.println("8. Importar personajes desde archivo");
        System.out.println("9. Salir y guardar");
        System.out.println("======================================");
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
        System.out.println("\n--- MODIFICAR ATRIBUTO INDIVIDUAL ---");
        gestor.mostrarPersonajes();

        System.out.print("Ingrese el NOMBRE del personaje a modificar: ");
        scanner.nextLine();
        String nombre = scanner.nextLine().trim();
        
        System.out.print("Ingrese el ATRIBUTO a modificar (vida, ataque, defensa, alcance, nivel): ");
        String atributo = scanner.nextLine().trim();
        
        int nuevoValor = leerEntero("Nuevo Valor", "Ingrese el NUEVO VALOR (> 0) para " + atributo + ": ");
        
        gestor.modificarPersonaje(nombre, atributo, nuevoValor);
    }
    
    // SOLUCION ERROR BORRAR: El metodo esta definido correctamente
    private static void borrar(Gestor gestor) { 
        System.out.println("\n--- BORRAR PERSONAJE ---");
        gestor.mostrarPersonajes();

        System.out.print("Ingrese el NOMBRE del personaje a borrar: ");
        scanner.nextLine(); 
        String nombre = scanner.nextLine().trim();
        
        gestor.borrarPersonaje(nombre);
    }

    private static void subirNivel(Gestor gestor) {
        System.out.println("\n--- SUBIR DE NIVEL ---");
        gestor.mostrarPersonajes();
        
        System.out.print("Ingrese el NOMBRE del personaje a mejorar: ");
        scanner.nextLine();
        String nombre = scanner.nextLine().trim();
        
        gestor.subirNivelPersonaje(nombre);
    }

    private static void filtrar(Gestor gestor) {
        System.out.println("\n--- FILTRAR/ORDENAR PERSONAJES ---");
        System.out.println("Ordenar por: [vida, ataque, defensa, alcance, nivel]");
        System.out.print("Ingrese el atributo para ordenar: ");
        scanner.nextLine();
        String atributo = scanner.nextLine().trim();
        
        gestor.mostrarPersonajesOrdenados(atributo);
    }
    
    private static void importar(Gestor gestor) {
        System.out.println("\n--- IMPORTAR PERSONAJES ---");
        System.out.print("Ingrese el nombre del archivo de importacion (ej: 'import.txt'): ");
        scanner.nextLine(); 
        String archivo = scanner.nextLine().trim();
        
        gestor.importarPersonajes(archivo);
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
                    System.err.println("ERROR: El valor de " + nombreCampo + " debe ser mayor que cero.");
                }
            } catch (InputMismatchException e) {
                System.err.println("ERROR: Debe ingresar un numero entero valido.");
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
                        subirNivel(gestor);
                        break;
                    case 6:
                        filtrar(gestor);
                        break;
                    case 7:
                        gestor.mostrarEstadisticas();
                        break;
                    case 8:
                        importar(gestor);
                        break;
                    case 9:
                        System.out.println("\nSaliendo del Gestor de Personajes.");
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