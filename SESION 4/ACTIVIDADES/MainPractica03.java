

class LimiteCreditoExcedidoException extends Exception {
    public LimiteCreditoExcedidoException(String mensaje) {
        super(mensaje);
    }
}

class CuentaBancaria {
    protected double saldo;
    private String numeroCuenta;

    public CuentaBancaria(String numeroCuenta, double saldoInicial) {
        this.numeroCuenta = numeroCuenta;
        this.saldo = saldoInicial;
    }

    public void retirar(double monto) throws SaldoInsuficienteException {
        if (monto > saldo) {
            throw new SaldoInsuficienteException("Saldo insuficiente.");
        }
        saldo -= monto;
    }

    public void depositar(double monto) {
        saldo += monto;
    }

    public double getSaldo() {
        return saldo;
    }
}

class CuentaCredito extends CuentaBancaria {
    private double limiteCredito;

    public CuentaCredito(String numeroCuenta, double saldoInicial, double limiteCredito) {
        super(numeroCuenta, saldoInicial);
        this.limiteCredito = limiteCredito;
    }

    @Override
    public void retirar(double monto) throws LimiteCreditoExcedidoException {
        if (monto > saldo + limiteCredito) {
            throw new LimiteCreditoExcedidoException("Límite de crédito excedido.");
        }
        saldo -= monto;
    }
}

public class MainPractica03 {
    public static void main(String[] args) {
        try {
            CuentaCredito cc = new CuentaCredito("201", 100, 300);
            cc.retirar(350);
            System.out.println("Retiro exitoso. Saldo: " + cc.getSaldo());

            cc.retirar(200); // lanza excepción
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
