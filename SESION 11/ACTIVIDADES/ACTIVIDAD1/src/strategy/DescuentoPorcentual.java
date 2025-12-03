package strategy;

public class DescuentoPorcentual implements DescuentoStrategy {

    @Override
    public double aplicarDescuento(double precio, int cantidad, double precioMasBajo) {

        if (cantidad == 2) {
            return (precio * cantidad) * 0.70;
        }

        return precio * cantidad;
    }
}
