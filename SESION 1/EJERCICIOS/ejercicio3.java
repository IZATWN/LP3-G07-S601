package tp_01;

import java.util.Random;

public class ejercicio3 {

    public static void main(String[] args) {
        int[] frecuencias = new int[6]; 
        Random random = new Random();
        for (int i = 0; i < 20000; i++) {
            int lanzamiento = random.nextInt(6); 
            frecuencias[lanzamiento]++;
        }
        System.out.println("Frecuencia de cada cara del dado:");
        for (int i = 0; i < frecuencias.length; i++) {
            System.out.println("Cara " + (i + 1) + ": " + frecuencias[i] + " veces");
        }
    }
}
