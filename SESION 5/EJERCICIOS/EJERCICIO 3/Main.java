package actividad2;
import java.util.Scanner;

class Par<F, S> {
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
}


public class Main {
 public static <F, S> void imprimirPar(Par<F, S> par) {
     System.out.println(par);
 }

 public static void main(String[] args) {
     Scanner sc = new Scanner(System.in);

     System.out.print("Ingrese el primer valor (String): ");
     String primero1 = sc.nextLine();
     System.out.print("Ingrese el segundo valor (Integer): ");
     int segundo1 = sc.nextInt();
     sc.nextLine(); 
     Par<String, Integer> par1 = new Par<>(primero1, segundo1);
     imprimirPar(par1);

     
     System.out.print("Ingrese el primer valor (Double): ");
     double primero2 = sc.nextDouble();
     System.out.print("Ingrese el segundo valor (Boolean: true/false): ");
     boolean segundo2 = sc.nextBoolean();
     Par<Double, Boolean> par2 = new Par<>(primero2, segundo2);
     imprimirPar(par2);

     sc.close();
 }
}
