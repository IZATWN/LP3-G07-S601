abstract class Forma {
    public abstract void dibujar();
}

class Circulo extends Forma {
    @Override
    public void dibujar() {
        System.out.println("Dibujando un circulo");
    }
}

class Rectangulo extends Forma {
    @Override
    public void dibujar() {
        System.out.println("Dibujando un rectangulo");
    }
}

class Triangulo extends Forma {
    @Override
    public void dibujar() {
        System.out.println("Dibujando un triangulo");
    }
}

abstract class Vehiculo {
    public abstract void acelerar();
}

class Coche extends Vehiculo {
    @Override
    public void acelerar() {
        System.out.println("El coche esta acelerando");
    }
}

class Bicicleta extends Vehiculo {
    @Override
    public void acelerar() {
        System.out.println("La bicicleta esta acelerando");
    }
}

class Moto extends Vehiculo {
    @Override
    public void acelerar() {
        System.out.println("La moto esta acelerando");
    }
}

public class Ejercicio2 {
    public static void main(String[] args) {
        Forma[] formas = { new Circulo(), new Rectangulo(), new Triangulo() };
        for (Forma f : formas) {
            f.dibujar();
        }

        System.out.println();

        Vehiculo[] vehiculos = { new Coche(), new Bicicleta(), new Moto() };
        for (Vehiculo v : vehiculos) {
            v.acelerar();
        }
    }
}
