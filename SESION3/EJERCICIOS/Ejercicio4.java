

abstract class ImpresoraBase {
    public abstract void imprimir(String documento);
}

abstract class EscanerBase {
    public abstract void escanear(String documento);
}

class Impresora extends ImpresoraBase {
    @Override
    public void imprimir(String documento) {
        System.out.println("Imprimiendo: " + documento);
    }
}

class ImpresoraMultifuncional extends ImpresoraBase {
    private EscanerBase escaner = new EscanerBase() {
        @Override
        public void escanear(String documento) {
            System.out.println("Escaneando: " + documento);
        }
    };

    @Override
    public void imprimir(String documento) {
        System.out.println("Imprimiendo: " + documento);
    }

    public void escanear(String documento) {
        escaner.escanear(documento);
    }
}

public class Ejercicio4 {
    public static void main(String[] args) {
        Impresora impresora = new Impresora();
        impresora.imprimir("Documento simple");

        ImpresoraMultifuncional multifuncional = new ImpresoraMultifuncional();
        multifuncional.imprimir("Documento multifuncional");
        multifuncional.escanear("Foto escaneada");
    }
}
