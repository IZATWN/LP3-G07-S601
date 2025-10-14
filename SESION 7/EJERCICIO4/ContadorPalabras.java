package EJERCICIO4;

import javax.swing.*;
import java.io.*;
import java.util.*;

public class ContadorPalabras {

    public static void main(String[] args) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Selecciona el archivo de texto a analizar");

        int opcion = fileChooser.showOpenDialog(null);
        if (opcion != JFileChooser.APPROVE_OPTION) {
            System.out.println("No se seleccionó ningún archivo. Programa terminado.");
            return;
        }

        File archivo = fileChooser.getSelectedFile();

        if (!archivo.exists() || !archivo.canRead()) {
            JOptionPane.showMessageDialog(null, "El archivo no se puede leer o no existe.");
            return;
        }

        try {
            analizarArchivo(archivo);
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        }
    }

    private static void analizarArchivo(File archivo) throws IOException {
        BufferedReader lector = new BufferedReader(new FileReader(archivo));

        int totalLineas = 0;
        int totalPalabras = 0;
        int totalCaracteres = 0;
        Map<String, Integer> frecuenciaPalabras = new HashMap<>();

        String linea;
        while ((linea = lector.readLine()) != null) {
            totalLineas++;
            totalCaracteres += linea.length();

            // Dividir en palabras (solo letras o dígitos)
            String[] palabras = linea.split("\\W+");
            for (String palabra : palabras) {
                if (!palabra.isEmpty()) {
                    totalPalabras++;
                    palabra = palabra.toLowerCase();
                    frecuenciaPalabras.put(palabra, frecuenciaPalabras.getOrDefault(palabra, 0) + 1);
                }
            }
        }
        lector.close();

        // Calcular promedio
        double promedioPalabrasPorLinea = (totalLineas > 0) ? (double) totalPalabras / totalLineas : 0;

        // Mostrar resultados
        System.out.println("===== RESULTADOS DEL ANÁLISIS =====");
        System.out.println("Archivo: " + archivo.getName());
        System.out.println("Total de líneas: " + totalLineas);
        System.out.println("Total de palabras: " + totalPalabras);
        System.out.println("Total de caracteres: " + totalCaracteres);
        System.out.printf("Promedio de palabras por línea: %.2f%n", promedioPalabrasPorLinea);
        System.out.println();

        // Mostrar palabras más frecuentes
        System.out.println("===== PALABRAS MÁS FRECUENTES =====");
        frecuenciaPalabras.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(Map.Entry.comparingByKey()))
                .limit(10)
                .forEach(entry ->
                        System.out.println(entry.getKey() + " → " + entry.getValue() + " veces"));
    }
}
