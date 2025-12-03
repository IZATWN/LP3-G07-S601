package strategy;

public class SinDescuento implements DescuentoStrategy {

    @Override
    public double aplicarDescuento(double precio, int cantidad, double precioMasBajo) {
        return precio * cantidad;
    }
}
