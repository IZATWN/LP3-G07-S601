// Experiencia 04
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Scanner;

class HistorialVacioException extends Exception {
    public HistorialVacioException(String mensaje) {
        super(mensaje);
    }
}

class CuentaBancaria {
    private String numeroCuenta;
    private String titular;
    private double saldo;

    public CuentaBancaria(String numeroCuenta, String titular, double saldoInicial) {
        this.numeroCuenta = numeroCuenta;
        this.titular = titular;
        this.saldo = saldoInicial;
    }

    public double getSaldo() {
        return saldo;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public String getTitular() {
        return titular;
    }
}

class ReporteTransacciones {
    public static void generarReporte(CuentaBancaria cuenta, String archivo)
            throws IOException, HistorialVacioException {
        if (cuenta.getSaldo() == 0) {
            throw new HistorialVacioException("No se puede generar un reporte de una cuenta vacía.");
        }
        try (FileWriter writer = new FileWriter(archivo)) {
            writer.write("=== REPORTE DE CUENTA ===\n");
            writer.write("Cuenta: " + cuenta.getNumeroCuenta() + "\n");
            writer.write("Titular: " + cuenta.getTitular() + "\n");
            writer.write("Saldo: " + cuenta.getSaldo() + "\n");
        }
    }

    public static void leerReporte(String archivo) throws FileNotFoundException {
        try (Scanner sc = new Scanner(new File(archivo))) {
            while (sc.hasNextLine()) {
                System.out.println(sc.nextLine());
            }
        }
    }
}

public class MainPractica04 {
    public static void main(String[] args) {
        try {
            CuentaBancaria c1 = new CuentaBancaria("301", "Luis", 0);
            ReporteTransacciones.generarReporte(c1, "reporte.txt"); // lanza excepción
        } catch (HistorialVacioException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error de archivo: " + e.getMessage());
        }

        try {
            ReporteTransacciones.leerReporte("noExiste.txt"); // lanza excepción
        } catch (FileNotFoundException e) {
            System.out.println("Archivo no encontrado.");
        }
    }
}
