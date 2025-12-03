package strategy;

public class EnvioEstandar implements CalculoEnvio {
    public double calcular(double peso) {
        return peso * 5;
    }
}
