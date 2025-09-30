package actividad2;
import java.util.Scanner;

public class PruebaPar {

    public static class Par<F, S> {
        private F primero;
        private S segundo;

       
        public Par(F primero, S segundo) {
            this.primero = primero;
            this.segundo = segundo;
        }

        public F getPrimero() {
            return primero;
        }

        public void setPrimero(F primero) {
            this.primero = primero;
        }

        public S getSegundo() {
            return segundo;
        }

        public void setSegundo(S segundo) {
            this.segundo = segundo;
        }

        @Override
        public String toString() {
            return "(Primero: " + primero + ", Segundo: " + segundo + ")";
        }

        
        public boolean esIgual(Par<F, S> otro) {
            if (otro == null) {
                return false;
            }
            return (this.primero == null ? otro.primero == null : this.primero.equals(otro.primero))
                && (this.segundo == null ? otro.segundo == null : this.segundo.equals(otro.segundo));
        }
    }

    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        
        System.out.print("Ingrese el primer valor del Par1 (String): ");
        String primero1 = sc.nextLine();
        System.out.print("Ingrese el segundo valor del Par1 (Integer): ");
        int segundo1 = sc.nextInt();
        sc.nextLine(); 

        Par<String, Integer> par1 = new Par<>(primero1, segundo1);

        
        System.out.print("Ingrese el primer valor del Par2 (String): ");
        String primero2 = sc.nextLine();
        System.out.print("Ingrese el segundo valor del Par2 (Integer): ");
        int segundo2 = sc.nextInt();

        Par<String, Integer> par2 = new Par<>(primero2, segundo2);

       
        System.out.println("\nPar1: " + par1);
        System.out.println("Par2: " + par2);

        
        System.out.println("\n¿Par1 es igual a Par2? " + par1.esIgual(par2));

        sc.close();
    }
}
