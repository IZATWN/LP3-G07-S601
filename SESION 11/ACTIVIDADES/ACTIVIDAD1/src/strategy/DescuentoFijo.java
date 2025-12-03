package strategy;

public class DescuentoFijo implements DescuentoStrategy {

    @Override
    public double aplicarDescuento(double precio, int cantidad, double precioMasBajo) {
        return (precio * cantidad) * 0.90;
    }
}
