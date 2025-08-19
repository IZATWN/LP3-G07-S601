package tp_01;
import java.util.Scanner;

public class ejercicio2	{

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int[] arreglo = new int[10];
        int suma = 0;

        System.out.println("Ingrese 10 numeros en orden creciente:");

        for (int i = 0; i < 10; ) {
            System.out.print("Numero " + (i + 1) + ": ");
            int numero = scanner.nextInt();

            if (i == 0 || numero > arreglo[i - 1]) {
                arreglo[i] = numero;
                suma += numero;
                i++;
            } else {
                System.out.println("El numero debe ser mayor que el anterior");
            }
        }

        System.out.print("Arreglo final: ");
        for (int num : arreglo) {
            System.out.print(num + " ");
        }

        System.out.println("\nSuma total: " + suma);
        scanner.close();
    }
}
