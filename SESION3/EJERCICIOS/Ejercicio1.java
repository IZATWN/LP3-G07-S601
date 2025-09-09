import java.util.Scanner;

class Empleado {
    private String nombre;
    private double salario;
    private String departamento;

    public Empleado(String nombre, double salario, String departamento) {
        this.nombre = nombre;
        this.salario = salario;
        this.departamento = departamento;
    }

    public String getNombre() {
        return nombre;
    }

    public double getSalario() {
        return salario;
    }

    public String getDepartamento() {
        return departamento;
    }
}

class CalculadoraPago {
    public double calcularPagoMensual(Empleado empleado) {
        return empleado.getSalario() / 12;
    }
}

public class Ejercicio1 {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
			System.out.print("Ingrese nombre del empleado: ");
			String nombre = sc.nextLine();

			System.out.print("Ingrese salario anual: ");
			double salario = sc.nextDouble();
			sc.nextLine(); 

			System.out.print("Ingrese departamento: ");
			String departamento = sc.nextLine();

			Empleado empleado = new Empleado(nombre, salario, departamento);
			CalculadoraPago calculadora = new CalculadoraPago();

			double pagoMensual = calculadora.calcularPagoMensual(empleado);
			System.out.println("\n--- Datos del Empleado ---");
			System.out.println("Nombre: " + empleado.getNombre());
			System.out.println("Departamento: " + empleado.getDepartamento());
			System.out.println("Pago mensual: " + pagoMensual);
		}
    }
}
