package strategy;

public class CalculadoraEnvio {

    private CalculoEnvio strategy;

    public void setStrategy(CalculoEnvio strategy) {
        this.strategy = strategy;
    }

    public double calcularCosto(double peso) {
        return strategy.calcular(peso);
    }
}
