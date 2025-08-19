package tp_01;

import java.util.Scanner;

public class ejercicio4 {

    public static double menor(double a, double b, double c) {
        double menor = a;

        if (b < menor) {
            menor = b;
        }
        if (c < menor) {
            menor = c;
        }

        return menor;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ingrese el primer numero decimal: ");
        double num1 = scanner.nextDouble();

        System.out.print("Ingrese el segundo numero decimal: ");
        double num2 = scanner.nextDouble();

        System.out.print("Ingrese el tercer numero decimal: ");
        double num3 = scanner.nextDouble();

        double resultado = menor(num1, num2, num3);
        System.out.println("El menor numero es: " + resultado);

        scanner.close();
    }
}
