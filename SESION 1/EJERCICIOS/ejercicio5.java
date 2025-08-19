package tp_01;

import java.util.Scanner;

public class ejercicio5 {

    public static double calcularCargo(int horas) {
        double cargo = 3.0;

        if (horas <= 1) {
            return cargo;
        } else {
            cargo += (horas - 1) * 0.5;
        }
        if (cargo > 12.0) {
            cargo = 12.0;
        }

        return cargo;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ingrese el numero de horas de estacionamiento: ");
        int horas = scanner.nextInt();

        if (horas <= 0) {
            System.out.println("Las horas deben ser mayores a cero.");
        } else {
            double total = calcularCargo(horas);
            System.out.println("El cargo por " + horas + " hora(s) es: S/" + total);
        }

        scanner.close();
    }
}
