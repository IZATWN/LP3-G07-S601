class Numero {
    private double valor;

    public Numero(double valor) {
        setValor(valor); 
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        if (valor < 0) {
            throw new IllegalArgumentException("El valor no puede ser negativo");
        }
        this.valor = valor;
    }
}


public class MainNumero {
    public static void main(String[] args) {
        try {
            Numero num = new Numero(10);
            System.out.println("Valor inicial: " + num.getValor());

            num.setValor(-5); 
        } 
        catch (IllegalArgumentException e) {
            System.out.println("Excepción capturada: " + e.getMessage());
        }
    }
}
