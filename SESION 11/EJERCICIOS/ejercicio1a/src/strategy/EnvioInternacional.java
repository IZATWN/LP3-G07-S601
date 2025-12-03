package strategy;

public class EnvioInternacional implements CalculoEnvio {
    public double calcular(double peso) {
        return peso * 15 + 25;
    }
}
