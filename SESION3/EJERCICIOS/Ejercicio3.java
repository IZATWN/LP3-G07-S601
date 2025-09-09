abstract class Vehiculo {
    public abstract void acelerar();
}

class Coche extends Vehiculo {
    @Override
    public void acelerar() {
        System.out.println("El coche acelera usando el motor");
    }
}

class Bicicleta extends Vehiculo {
    @Override
    public void acelerar() {
        System.out.println("La bicicleta acelera pedaleando");
    }
}

class Moto extends Vehiculo {
    @Override
    public void acelerar() {
        System.out.println("La moto acelera girando el acelerador");
    }
}

public class Ejercicio3 {
    public static void main(String[] args) {
        Vehiculo[] vehiculos = { new Coche(), new Bicicleta(), new Moto() };
        for (Vehiculo v : vehiculos) {
            v.acelerar();
        }
    }
}
