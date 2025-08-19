package tp_01;

import java.util.Scanner;

public class actividad4 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("¿Cuantos numeros desea ingresar? ");
        int cantidad = scanner.nextInt();

        if (cantidad <= 0) {
            System.out.println("La cantidad debe ser mayor que cero.");
            scanner.close();
            return;
        }

        double[] numeros = new double[cantidad];
        double suma = 0;

        for (int i = 0; i < cantidad; i++) {
            System.out.print("Ingrese el numero " + (i + 1) + ": ");
            numeros[i] = scanner.nextDouble();
            suma += numeros[i];
        }

        double promedio = suma / cantidad;
        System.out.println("El promedio es: " + promedio);

        scanner.close();
    }
}
