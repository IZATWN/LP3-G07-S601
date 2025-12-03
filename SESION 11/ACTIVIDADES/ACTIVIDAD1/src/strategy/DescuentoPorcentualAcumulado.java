package strategy;

public class DescuentoPorcentualAcumulado implements DescuentoStrategy {

    @Override
    public double aplicarDescuento(double precio, int cantidad, double precioMasBajo) {

        if (cantidad >= 3) {
            double total = precio * cantidad;
            total -= precioMasBajo * 0.50;
            return total;
        }

        return precio * cantidad;
    }
}
