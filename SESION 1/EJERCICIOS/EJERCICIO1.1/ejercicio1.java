package tp_01;

public class ejercicio1 {

    public static int sumarElementos(int[] arreglo) {
        int suma = 0;
        for (int num : arreglo) {
            suma += num;
        }
        return suma;
    }

    public static void main(String[] args) {
        int[] numeros = {1, 2, 3, 4, 5};
        System.out.println("Suma: " + sumarElementos(numeros));
    }
}
