package strategy;

public class EnvioExpress implements CalculoEnvio {
    public double calcular(double peso) {
        return peso * 8 + 10;
    }
}
