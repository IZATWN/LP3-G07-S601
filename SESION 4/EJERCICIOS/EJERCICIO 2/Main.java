
class DivisionPorCeroException extends Exception {
    public DivisionPorCeroException(String mensaje) {
        super(mensaje);
    }
}

class Calculadora {

    public double sumar(double a, double b) {
        return a + b;
    }

    public double restar(double a, double b) {
        return a - b;
    }

    public double multiplicar(double a, double b) {
        return a * b;
    }

    public double dividir(double a, double b) throws DivisionPorCeroException {
        if (b == 0) {
            throw new DivisionPorCeroException("No se puede dividir entre cero");
        }
        return a / b;
    }
}


public class Main {
    public static void main(String[] args) {
        Calculadora calc = new Calculadora();

        try {
            System.out.println("Suma: " + calc.sumar(10, 5));
            System.out.println("Resta: " + calc.restar(10, 5));
            System.out.println("Multiplicación: " + calc.multiplicar(10, 5));
            System.out.println("División: " + calc.dividir(10, 0)); // lanza excepción
        } 
        catch (DivisionPorCeroException e) {
            System.out.println("Error: " + e.getMessage());
        } 
        catch (IllegalArgumentException e) {
            System.out.println("Error de argumento: " + e.getMessage());
        } 
        catch (ArithmeticException e) {
            System.out.println("Error aritmético: " + e.getMessage());
        }
    }
}
