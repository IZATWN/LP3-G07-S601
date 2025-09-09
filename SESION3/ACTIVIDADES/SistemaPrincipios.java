

import java.util.*;

/*
 Archivo: SistemaPrincipios.java
 Contiene ejemplos de LSP, ISP y DIP en un codigo compacto.
 No usa acentos en comentarios/strings para ajustarse a tu pedido.
*/

public class SistemaPrincipios {

    // --------------- Fecha simple (mini) ---------------
    static class Fecha {
        int y,m,d;
        Fecha(int y,int m,int d){ this.y=y; this.m=m; this.d=d; }
        static Fecha parse(String s){
            String[] p = s.split("-");
            return new Fecha(Integer.parseInt(p[0]), Integer.parseInt(p[1]), Integer.parseInt(p[2]));
        }
        int aDias(){
            int Y=y, M=m;
            if(M<=2){ Y--; M+=12; }
            int A = Y/100;
            int B = 2 - A + A/4;
            int jd = (int)(Math.floor(365.25*(Y+4716)) + Math.floor(30.6001*(M+1)) + d + B - 1524);
            return jd;
        }
        static int diasEntre(Fecha a, Fecha b){ return b.aDias() - a.aDias(); }
        public String toString(){ return String.format("%04d-%02d-%02d", y,m,d); }
    }

    // --------------- LSP: Habitacion base y subclases ---------------
    // Clase base: define contrato claro para reservar, liberar y calcular precio.
    static class Habitacion {
        protected String id;
        protected String tipo;
        protected double precioBase;
        protected boolean ocupada;

        public Habitacion(String id, String tipo, double precioBase){
            this.id = id; this.tipo = tipo; this.precioBase = precioBase; this.ocupada = false;
        }

        // Contrato: reservar() marca ocupada y no debe lanzar excepciones en condiciones normales.
        public void reservar(){
            if(ocupada) throw new IllegalStateException("Habitacion ya ocupada");
            ocupada = true;
        }

        // liberar deja libre la habitacion
        public void liberar(){
            ocupada = false;
        }

        // Calculo de precio por defecto: noches * precioBase
        public double calcularPrecio(Fecha inicio, Fecha fin){
            long noches = Fecha.diasEntre(inicio, fin);
            if(noches < 0) throw new IllegalArgumentException("Fechas invalidas");
            return noches * precioBase;
        }

        public String getId(){ return id; }
        public String getTipo(){ return tipo; }
        public boolean isOcupada(){ return ocupada; }

        @Override
        public String toString(){
            return id + " {" + tipo + "} precioBase=" + precioBase + " ocupada=" + ocupada;
        }
    }

    // Subclase que extiende sin cambiar contrato => respeta LSP
    static class HabitacionStandard extends Habitacion {
        public HabitacionStandard(String id, double precioBase){
            super(id, "Standard", precioBase);
        }
        // no sobrescribimos reservar ni calcularPrecio => cumple contrato
    }

    // Subclase premium que añade servicio minibar pero no rompe reservar()
    static class HabitacionPremium extends Habitacion implements ServicioLimpieza, ServicioComida {
        public HabitacionPremium(String id, double precioBase){
            super(id, "Premium", precioBase);
        }

        // servicio extra: pedir comida (implementa ISP)
        public void pedirComida(String pedido){
            // accion simple: simular aceptacion
            System.out.println("Servicio comida: pedido '" + pedido + "' aceptado para habitacion " + id);
        }

        // ServicioLimpieza (ISP)
        public void solicitarLimpieza(){
            System.out.println("Limpieza solicitada para habitacion " + id);
        }

        // no alteramos reservar()/liberar() -> LSP respetado
    }

    // Suite que implementa servicios adicionales (ISP) y mantiene contrato de Habitacion
    static class Suite extends Habitacion implements ServicioLimpieza, ServicioComida, ServicioLavanderia {
        public Suite(String id, double precioBase){
            super(id, "Suite", precioBase);
        }

        public void solicitarLimpieza(){ System.out.println("Limpieza solicitada para suite " + id); }
        public void pedirComida(String pedido){ System.out.println("Servicio comida: '" + pedido + "' en suite " + id); }
        public void solicitarLavado(String prendas){ System.out.println("Lavanderia: recibir prendas '" + prendas + "' en suite " + id); }

        // ejemplo de metodo adicional propio, sin romper contrato base
        public void abrirBar(){
            System.out.println("Bar de la suite " + id + " abierto.");
        }
    }

    // --------------- ISP: interfaces pequeñas ---------------
    interface ServicioLimpieza { void solicitarLimpieza(); }
    interface ServicioComida { void pedirComida(String pedido); }
    interface ServicioLavanderia { void solicitarLavado(String prendas); }

    // --------------- DIP: CanalNotificacion y implementaciones ---------------
    interface CanalNotificacion { void enviarNotificacion(String destino, String mensaje); }

    static class EnviadorCorreo implements CanalNotificacion {
        public void enviarNotificacion(String destino, String mensaje){
            // simular envio
            System.out.println("[Email -> " + destino + "] " + mensaje);
        }
    }

    static class EnviadorSMS implements CanalNotificacion {
        public void enviarNotificacion(String destino, String mensaje){
            System.out.println("[SMS -> " + destino + "] " + mensaje);
        }
    }

    // NotificadorReserva depende de la abstraccion CanalNotificacion (DIP)
    static class NotificadorReserva {
        private CanalNotificacion canal;
        public NotificadorReserva(CanalNotificacion canal){ this.canal = canal; }
        public void notificarCreacion(String destino, String mensaje){ canal.enviarNotificacion(destino, mensaje); }
        public void notificarCancelacion(String destino, String mensaje){ canal.enviarNotificacion(destino, mensaje); }
    }

    // --------------- Reserva simple (usa Habitacion por id) ---------------
    static class Reserva {
        String id;
        String idCliente;
        String idHabitacion; // null si aun no asignada
        Fecha inicio, fin;
        double precio;
        boolean activa;

        Reserva(String id, String idCliente, Fecha inicio, Fecha fin){
            this.id = id; this.idCliente = idCliente; this.inicio = inicio; this.fin = fin; this.idHabitacion = null;
            this.precio = 0; this.activa = true;
        }

        public void asignarHabitacion(Habitacion h){
            if(!activa) throw new IllegalStateException("Reserva no activa");
            this.idHabitacion = h.getId();
        }
        public void cancelar(){ this.activa = false; }
        public String toString(){ return "Reserva " + id + " cliente:" + idCliente + " hab:" + (idHabitacion==null?"(sin)":idHabitacion) + " " + inicio + "->" + fin + " precio:" + precio + " activa:" + activa; }
    }

    // --------------- Controlador minimo que demuestra integracion ---------------
    // Usa polimorfismo para manejar cualquier Habitacion (LSP)
    // Usa interfaces pequenas para ofrecer servicios (ISP)
    // Usa Notificador via CanalNotificacion por inyeccion (DIP)
    static class Controlador {
        ArrayList<Habitacion> listaHabitaciones = new ArrayList<Habitacion>();
        ArrayList<Reserva> listaReservas = new ArrayList<Reserva>();
        NotificadorReserva notificador;

        Controlador(CanalNotificacion canal){
            this.notificador = new NotificadorReserva(canal);
        }

        void agregarHabitacion(Habitacion h){ listaHabitaciones.add(h); }
        Habitacion buscarPorId(String id){
            for(Habitacion h: listaHabitaciones) if(h.getId().equals(id)) return h;
            return null;
        }

        // ejemplo: asignar primera habitacion libre del tipo (respetando contrato Habitacion)
        Habitacion asignarHabitacionLibre(String tipo, Fecha inicio, Fecha fin){
            for(Habitacion h: listaHabitaciones){
                if(!h.getTipo().equals(tipo)) continue;
                if(h.isOcupada()) continue;
                // simplificacion: no comprobamos solapamientos de reservas en este ejemplo compacto
                return h;
            }
            return null;
        }

        Reserva crearReserva(String idRes, String idCliente, Fecha inicio, Fecha fin, String tipoHabitacion, String destinoNotificacion){
            Reserva r = new Reserva(idRes, idCliente, inicio, fin);
            Habitacion h = asignarHabitacionLibre(tipoHabitacion, inicio, fin);
            if(h != null){
                h.reservar();
                r.asignarHabitacion(h);
                r.precio = h.calcularPrecio(inicio, fin);
            } else {
                r.precio = 0;
            }
            listaReservas.add(r);
            notificador.notificarCreacion(destinoNotificacion, "Reserva creada: " + r);
            return r;
        }

        void cancelarReserva(String idReserva, String destinoNotificacion){
            for(Reserva r: listaReservas){
                if(r.id.equals(idReserva) && r.activa){
                    r.cancelar();
                    // liberar habitacion si estaba asignada
                    if(r.idHabitacion != null){
                        Habitacion h = buscarPorId(r.idHabitacion);
                        if(h != null) h.liberar();
                    }
                    notificador.notificarCancelacion(destinoNotificacion, "Reserva cancelada: " + r.id);
                    return;
                }
            }
            System.out.println("Reserva no encontrada o ya inactiva: " + idReserva);
        }

        // utilizar servicios via ISP: solicitar limpieza si la habitacion implementa ServicioLimpieza
        void solicitarLimpiezaHabitacion(String idHab){
            Habitacion h = buscarPorId(idHab);
            if(h == null) { System.out.println("No existe habitacion " + idHab); return; }
            if(h instanceof ServicioLimpieza){
                ((ServicioLimpieza)h).solicitarLimpieza();
            } else {
                System.out.println("Habitacion " + idHab + " no soporta servicio de limpieza");
            }
        }

        void pedidoComidaHabitacion(String idHab, String pedido){
            Habitacion h = buscarPorId(idHab);
            if(h == null) { System.out.println("No existe habitacion " + idHab); return; }
            if(h instanceof ServicioComida){
                ((ServicioComida)h).pedirComida(pedido);
            } else {
                System.out.println("Habitacion " + idHab + " no soporta servicio de comida");
            }
        }

        void solicitarLavado(String idHab, String prendas){
            Habitacion h = buscarPorId(idHab);
            if(h == null) { System.out.println("No existe habitacion " + idHab); return; }
            if(h instanceof ServicioLavanderia){
                ((ServicioLavanderia)h).solicitarLavado(prendas);
            } else {
                System.out.println("Habitacion " + idHab + " no soporta lavanderia");
            }
        }
    }

    // --------------- Main: demostracion ---------------
    public static void main(String[] args){
        // Usamos CanalNotificacion por inyeccion: swap facil a SMS si se desea
        CanalNotificacion canalEmail = new EnviadorCorreo();
        Controlador ctrl = new Controlador(canalEmail);

        // crear habitaciones (subclases)
        HabitacionStandard h1 = new HabitacionStandard("H1", 30.0);
        HabitacionPremium h2 = new HabitacionPremium("H2", 60.0);
        Suite h3 = new Suite("H3", 150.0);

        // agregar al controlador
        ctrl.agregarHabitacion(h1); ctrl.agregarHabitacion(h2); ctrl.agregarHabitacion(h3);

        // crear reserva (se asigna primera habitacion libre del tipo)
        Fecha inicio = Fecha.parse("2025-09-20");
        Fecha fin = Fecha.parse("2025-09-22");
        Reserva r = ctrl.crearReserva("R1", "C1", inicio, fin, "Premium", "cliente1@mail.com");

        // Demonstrar LSP: tratar subclases como Habitacion
        Habitacion any = h3; // Suite como Habitacion
        System.out.println("Uso polimorfico: " + any);
        System.out.println("Calculo precio polimorfico: " + any.calcularPrecio(inicio, fin));

        // Demonstrar ISP: solicitar servicios segun interfaces
        System.out.println("\nSolicitando servicios:");
        ctrl.solicitarLimpiezaHabitacion("H2"); // Premium soporta limpieza
        ctrl.pedidoComidaHabitacion("H2", "Pizza");
        ctrl.pedidoComidaHabitacion("H1", "Sandwich"); // Standard no soporta comida
        ctrl.solicitarLavado("H3", "2 camisas"); // Suite soporta lavanderia

        // Demonstrar DIP: notificacion por email (si quisieramos SMS, cambiamos implementacion)
        ctrl.cancelarReserva("R1", "cliente1@mail.com");

        // cambiar canal a SMS sin modificar NotificadorReserva/Controlador facilmente:
        System.out.println("\nCambiando canal a SMS (sin tocar NotificadorReserva)");
        ctrl.notificador = new NotificadorReserva(new EnviadorSMS());
        Reserva r2 = ctrl.crearReserva("R2", "C2", inicio, fin, "Suite", "999999999");
        ctrl.cancelarReserva("R2", "999999999");
    }

}
