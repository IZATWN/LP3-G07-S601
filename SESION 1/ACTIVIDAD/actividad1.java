package tp_01;

import java.util.Scanner;

public class actividad1 {

    
    public static boolean esPrimo(int numero) {
        if (numero <= 1) {
            return false;
        }

        for (int i = 2; i <= Math.sqrt(numero); i++) {
            if (numero % i == 0) {
                return false;
            }
        }

        return true;
    }

    
    public static void imprimirPrimosHasta(int n) {
        System.out.println("Numeros primos entre 1 y " + n + ":");
        for (int i = 1; i <= n; i++) {
            if (esPrimo(i)) {
                System.out.print(i + " ");
            }
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ingrese un numero entero positivo: ");
        int n = scanner.nextInt();

        if (n < 1) {
            System.out.println("Debe ingresar un numero mayor o igual a 1.");
        } else {
            imprimirPrimosHasta(n);
        }

        scanner.close();
    }
}
