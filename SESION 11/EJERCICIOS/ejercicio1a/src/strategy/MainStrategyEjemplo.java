package strategy;

public class MainStrategyEjemplo {

    public static void main(String[] args) {

        CalculadoraEnvio calc = new CalculadoraEnvio();
        double peso = 10; // 10 kg

        calc.setStrategy(new EnvioEstandar());
        System.out.println("Estandar: " + calc.calcularCosto(peso));

        calc.setStrategy(new EnvioExpress());
        System.out.println("Express: " + calc.calcularCosto(peso));

        calc.setStrategy(new EnvioInternacional());
        System.out.println("Internacional: " + calc.calcularCosto(peso));

        calc.setStrategy(new EnvioEconomico());
        System.out.println("Economico: " + calc.calcularCosto(peso));
    }
}
