package strategy;

public class CalculadoraDePrecios {

    private DescuentoStrategy strategy;

    public void setStrategy(DescuentoStrategy strategy) {
        this.strategy = strategy;
    }

    public double calcular(Producto p, int cantidad, double precioMasBajo) {
        return strategy.aplicarDescuento(p.precio, cantidad, precioMasBajo);
    }
}
