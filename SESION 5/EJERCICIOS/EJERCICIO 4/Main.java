package actividad2;
import java.util.ArrayList;
import java.util.Scanner;

class Par<F, S> {
    private F primero;
    private S segundo;

    public Par(F primero, S segundo) {
        this.primero = primero;
        this.segundo = segundo;
    }

    public F getPrimero() {
        return primero;
    }

    public void setPrimero(F primero) {
        this.primero = primero;
    }

    public S getSegundo() {
        return segundo;
    }

    public void setSegundo(S segundo) {
        this.segundo = segundo;
    }

    @Override
    public String toString() {
        return "(Primero: " + primero + ", Segundo: " + segundo + ")";
    }
}

class Contenedor<F, S> {
    private ArrayList<Par<F, S>> listaPares;

    public Contenedor() {
        listaPares = new ArrayList<>();
    }

    
    public void agregarPar(F primero, S segundo) {
        listaPares.add(new Par<>(primero, segundo));
    }

    public Par<F, S> obtenerPar(int indice) {
        if (indice >= 0 && indice < listaPares.size()) {
            return listaPares.get(indice);
        } else {
            return null; 
        }
    }

    public ArrayList<Par<F, S>> obtenerTodosLosPares() {
        return listaPares;
    }

    public void mostrarPares() {
        if (listaPares.isEmpty()) {
            System.out.println("El contenedor esta vacio.");
        } else {
            for (Par<F, S> par : listaPares) {
                System.out.println(par);
            }
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Contenedor<String, Integer> contenedor = new Contenedor<>();

        System.out.print("¿Cuantos pares desea ingresar? ");
        int cantidad = sc.nextInt();
        sc.nextLine(); 

        for (int i = 0; i < cantidad; i++) {
            System.out.println("\nPar #" + (i + 1));

            System.out.print("Ingrese el primer valor (String): ");
            String primero = sc.nextLine();

            System.out.print("Ingrese el segundo valor (Integer): ");
            int segundo = sc.nextInt();
            sc.nextLine(); 

            contenedor.agregarPar(primero, segundo);
        }

        System.out.println("\nTodos los pares en el contenedor:");
        contenedor.mostrarPares();

        System.out.print("\nIngrese el indice del par que desea ver (0 a " + (cantidad - 1) + "): ");
        int indice = sc.nextInt();
        Par<String, Integer> parSeleccionado = contenedor.obtenerPar(indice);

        if (parSeleccionado != null) {
            System.out.println("Par en la posicion " + indice + ": " + parSeleccionado);
        } else {
            System.out.println("Indice invalido.");
        }

        sc.close();
    }
}