package tp_01;

import java.util.Scanner;

public class actividad3 {

    
    public static boolean esPerfecto(int numero) {
        int suma = 0;

        
        for (int i = 1; i < numero; i++) {
            if (numero % i == 0) {
                suma += i;
            }
        }

        return suma == numero;
    }

    
    public static void imprimirPerfectosMenoresQue(int n) {
        System.out.println("Numeros perfectos menores que " + n + ":");
        for (int i = 2; i < n; i++) {
            if (esPerfecto(i)) {
                System.out.println(i);
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ingrese un numero entero positivo: ");
        int n = scanner.nextInt();

        if (n <= 1) {
            System.out.println("Debe ingresar un numero mayor a 1.");
        } else {
            imprimirPerfectosMenoresQue(n);
        }

        scanner.close();
    }
}
