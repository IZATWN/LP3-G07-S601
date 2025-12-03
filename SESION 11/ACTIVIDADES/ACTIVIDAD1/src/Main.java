import observer.*;
import strategy.*;
import command.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // OBSERVER
        System.out.println("===== OBSERVER =====");
        Notificacion notificacion = new Notificacion();

        Usuario u1 = new Usuario("Carlos");
        Usuario u2 = new Usuario("Maria");

        notificacion.suscribir(u1);
        notificacion.suscribir(u2);

        notificacion.enviarNotificacion("Nueva promocion disponible!");
        notificacion.desuscribir(u2);
        notificacion.enviarNotificacion("Actualizacion importante!");

        // STRATEGY
        System.out.println("\n===== STRATEGY =====");
        Scanner sc = new Scanner(System.in);

        Producto p = new Producto("Laptop", 1500);
        CalculadoraDePrecios calc = new CalculadoraDePrecios();

        System.out.println("Seleccione descuento:");
        System.out.println("1. Sin descuento");
        System.out.println("2. Fijo 10%");
        System.out.println("3. 30% por 2 iguales");
        System.out.println("4. 50% al mas barato");

        int op = sc.nextInt();
        int cantidad = 2;

        switch (op) {
            case 1: calc.setStrategy(new SinDescuento()); break;
            case 2: calc.setStrategy(new DescuentoFijo()); break;
            case 3: calc.setStrategy(new DescuentoPorcentual()); break;
            case 4: calc.setStrategy(new DescuentoPorcentualAcumulado()); break;
        }

        double total = calc.calcular(p, cantidad, p.precio);
        System.out.println("Total a pagar: " + total);

        // COMMAND
        System.out.println("\n===== COMMAND =====");

        Televisor tv = new Televisor();
        ControlRemoto control = new ControlRemoto();

        control.setCommand(new EncenderCommand(tv)); control.presionarBoton();
        control.setCommand(new SubirVolumenCommand(tv)); control.presionarBoton();
        control.setCommand(new CambiarCanalCommand(tv)); control.presionarBoton();
        control.setCommand(new ApagarCommand(tv)); control.presionarBoton();
    }
}
