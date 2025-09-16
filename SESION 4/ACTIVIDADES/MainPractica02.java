// Experiencia 02

class CuentaNoEncontradaException extends Exception {
    public CuentaNoEncontradaException(String mensaje) {
        super(mensaje);
    }
}

class SaldoNoCeroException extends Exception {
    public SaldoNoCeroException(String mensaje) {
        super(mensaje);
    }
}

class CuentaBancaria {
    private String numeroCuenta;
    private String titular;
    private double saldo;

    public CuentaBancaria(String numeroCuenta, String titular, double saldoInicial) {
        if (saldoInicial < 0) {
            throw new IllegalArgumentException("El saldo inicial no puede ser negativo.");
        }
        this.numeroCuenta = numeroCuenta;
        this.titular = titular;
        this.saldo = saldoInicial;
    }

    public void depositar(double monto) {
        saldo += monto;
    }

    public void retirar(double monto) throws SaldoInsuficienteException {
        if (monto > saldo) {
            throw new SaldoInsuficienteException("Saldo insuficiente.");
        }
        saldo -= monto;
    }

    public void transferir(CuentaBancaria destino, double monto)
            throws CuentaNoEncontradaException, SaldoInsuficienteException {
        if (destino == null) {
            throw new CuentaNoEncontradaException("La cuenta destino no existe.");
        }
        this.retirar(monto);
        destino.depositar(monto);
    }

    public void cerrarCuenta() throws SaldoNoCeroException {
        if (saldo != 0) {
            throw new SaldoNoCeroException("No se puede cerrar la cuenta con saldo distinto de cero.");
        }
        System.out.println("Cuenta cerrada con éxito.");
    }
}

public class MainPractica02 {
    public static void main(String[] args) {
        try {
            CuentaBancaria c1 = new CuentaBancaria("101", "Pedro", 300);
            CuentaBancaria c2 = new CuentaBancaria("102", "Ana", 500);

            c1.transferir(c2, 100);
            System.out.println("Transferencia realizada.");

            c1.cerrarCuenta(); // lanza excepción
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
