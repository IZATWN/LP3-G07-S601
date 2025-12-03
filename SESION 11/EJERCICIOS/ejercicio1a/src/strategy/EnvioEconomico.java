package strategy;

public class EnvioEconomico implements CalculoEnvio {
    public double calcular(double peso) {
        return peso * 3;
    }
}
