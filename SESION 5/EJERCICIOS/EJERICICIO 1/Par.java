package actividad2;
import java.util.Scanner;

public class Par<F, S> {

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


 public static void main(String[] args) {
     Scanner sc = new Scanner(System.in);

     
     System.out.print("Ingrese el primer valor (String): ");
     String primero = sc.nextLine();

     System.out.print("Ingrese el segundo valor (Integer): ");
     int segundo = sc.nextInt();

     
     Par<String, Integer> par = new Par<>(primero, segundo);

    
     System.out.println("El par ingresado es: " + par);

     sc.close();
 }
}
