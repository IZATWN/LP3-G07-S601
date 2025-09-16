import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

class LeerEntrada {
    private Reader stream;

    public LeerEntrada(InputStream fuente) {
        stream = new InputStreamReader(fuente);
    }

    public int getChar() {
        try {
            return this.stream.read();
        } catch (java.io.IOException e) {
            return -1;
        }
    }
}

public class ExcepcionesSinThrow {
    private LeerEntrada lector;
    private int ultimoInt;
    private char ultimoCaracter;
    private boolean salir = false;
    private static final char EXIT_CHAR = 'q';

    public ExcepcionesSinThrow() {
        lector = new LeerEntrada(System.in);
    }

    public void procesar() {
        int r = lector.getChar();
        ultimoInt = r;

        if (r == -1) {
            System.out.println("Carácter de salida (EOF o error de E/S).");
            salir = true;
        } else {
            char c = (char) r;
            ultimoCaracter = c;

            if (c == EXIT_CHAR || c == Character.toUpperCase(EXIT_CHAR)) {
                System.out.println("Carácter de salida detectado: " + c);
                salir = true;
            } else if (Character.isWhitespace(c)) {
                System.out.println("Simulación: blanco detectado -> código: " + r);
            } else if (Character.isDigit(c)) {
                System.out.println("Simulación: número detectado -> " + c);
            } else {
                String vocales = "aeiouAEIOU";
                if (vocales.indexOf(c) >= 0) {
                    System.out.println("Simulación: vocal detectada -> " + c);
                } else {
                    System.out.println("Carácter leído (otro): '" + c + "'");
                }
            }
        }
    }

    public static void main(String[] args) {
        ExcepcionesSinThrow app = new ExcepcionesSinThrow();
        System.out.println("Escribe caracteres. Pulsa '" + EXIT_CHAR + "' (o '" + Character.toUpperCase(EXIT_CHAR) + "') para salir.");

        while (!app.salir) {
            app.procesar();
        }

        System.out.println("Programa terminado.");
    }
}
