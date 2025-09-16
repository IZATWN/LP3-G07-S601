import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class RegistroEstudiantes {

    // Usamos una lista para almacenar los nombres de los estudiantes.
    // Es buena práctica declararla como privada para encapsularla.
    private List<String> estudiantes;

    /**
     * Constructor que inicializa el registro con una lista vacía.
     */
    public RegistroEstudiantes() {
        // ArrayList es una implementación de List que permite un tamaño dinámico.
        this.estudiantes = new ArrayList<>();
        System.out.println("Registro de Estudiantes inicializado.");
    }

    /**
     * Agrega un estudiante a la lista.
     *
     * @param nombre El nombre del estudiante a agregar.
     * @throws IllegalArgumentException si el nombre es nulo o está vacío.
     */
    public void agregarEstudiante(String nombre) {
        // Verificamos si el nombre es nulo o si está en blanco (solo espacios).
        if (nombre == null || nombre.trim().isEmpty()) {
            // Esta es la excepción estándar en Java para argumentos de método inválidos.
            throw new IllegalArgumentException("El nombre del estudiante no puede ser nulo o vacío.");
        }
        this.estudiantes.add(nombre);
        System.out.println("Estudiante '" + nombre + "' agregado exitosamente.");
    }

    /**
     * Busca un estudiante en la lista por su nombre.
     *
     * @param nombre El nombre del estudiante a buscar.
     * @return El nombre del estudiante si se encuentra.
     * @throws NoSuchElementException si el estudiante no se encuentra en la lista.
     */
    public String buscarEstudiante(String nombre) {
        if (!this.estudiantes.contains(nombre)) {
            // Esta es la excepción estándar en Java para cuando un elemento no se encuentra.
            throw new NoSuchElementException("El estudiante '" + nombre + "' no está en el registro.");
        }
        return nombre;
    }
    
    /**
     * Devuelve la lista actual de estudiantes para visualización.
     * @return String con la lista de estudiantes.
     */
    public String toString() {
        return "Lista actual de estudiantes: " + this.estudiantes.toString();
    }


    // --- Código de Invocación y Manejo de Excepciones ---
    public static void main(String[] args) {
        RegistroEstudiantes registro = new RegistroEstudiantes();

        System.out.println("\n--- Intentando agregar estudiantes ---");
        

        try {
            registro.agregarEstudiante("Ana Torres");
            registro.agregarEstudiante("Carlos Gomez");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }

        try {
            System.out.println("\nIntentando agregar un estudiante con nombre nulo...");
            registro.agregarEstudiante(null);
        } catch (IllegalArgumentException e) {
            System.out.println("Error capturado exitosamente: " + e.getMessage());
        }


        try {
            System.out.println("\nIntentando agregar un estudiante con nombre vacío...");
            registro.agregarEstudiante("  ");
        } catch (IllegalArgumentException e) {
            System.out.println("Error capturado exitosamente: " + e.getMessage());
        }

        System.out.println("\n" + registro.toString());

        System.out.println("\n\n--- Intentando buscar estudiantes ---");

        try {
            String estudianteEncontrado = registro.buscarEstudiante("Ana Torres");
            System.out.println("Búsqueda exitosa: Se encontró a '" + estudianteEncontrado + "'.");
        } catch (NoSuchElementException e) {
            System.out.println("Error inesperado: " + e.getMessage());
        }

        try {
            System.out.println("\nBuscando a un estudiante no registrado ('Luis Parra')...");
            registro.buscarEstudiante("Luis Parra");
        } catch (NoSuchElementException e) {
            System.out.println(" Error capturado exitosamente: " + e.getMessage());
        }
    }
}