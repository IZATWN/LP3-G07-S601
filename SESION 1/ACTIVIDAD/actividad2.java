package tp_01;

import java.util.Scanner;

public class actividad2 {

    public static boolean esPalindromo(int numero) {
        int original = numero;
        int invertido = 0;

        while (numero > 0) {
            int digito = numero % 10;
            invertido = invertido * 10 + digito;
            numero /= 10;
        }

        return original == invertido;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ingrese un numero entero positivo: ");
        int numero = scanner.nextInt();

        if (numero < 0) {
            System.out.println("Debe ingresar un numero positivo.");
        } else {
            if (esPalindromo(numero)) {
                System.out.println("El numero " + numero + " ES un palindromo.");
            } else {
                System.out.println("El numero " + numero + " NO es un palindromo.");
            }
        }

        scanner.close();
    }
}
