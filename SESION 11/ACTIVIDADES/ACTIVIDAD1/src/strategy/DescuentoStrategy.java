package strategy;

public interface DescuentoStrategy {
    double aplicarDescuento(double precio, int cantidad, double precioMasBajo);
}
