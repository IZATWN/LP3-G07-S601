
import java.util.*;

public class SistemaHotelSRP {

    
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
        static boolean seSolapan(Fecha aIni, Fecha aFin, Fecha bIni, Fecha bFin){
            return !(aFin.aDias() <= bIni.aDias() || aIni.aDias() >= bFin.aDias());
        }
        public String toString(){ return String.format("%04d-%02d-%02d", y,m,d); }
    }

    // -------- Modelos en espanol (sin tildes) --------
    enum TipoHab { INDIVIDUAL, DOBLE, SUITE }
    enum EstadoHab { DISPONIBLE, OCUPADA, LIMPIEZA }
    enum EstadoRes { RESERVADA, CANCELADA, CHECKIN, CHECKOUT }

    // Clase Habitacion: ya no tiene metodos de disponibilidad (delegado al Gestor)
    static class Habitacion {
        String id;
        TipoHab tipo;
        double precioBase;
        EstadoHab estado;
        // referencia al gestor (puede ser compartida entre habitaciones)
        GestorDisponibilidadHabitacion gestorDisponibilidad;

        Habitacion(String id, TipoHab tipo, double precioBase, GestorDisponibilidadHabitacion gestor){
            this.id = id; this.tipo = tipo; this.precioBase = precioBase; this.estado = EstadoHab.DISPONIBLE;
            this.gestorDisponibilidad = gestor;
        }

        // metodos que son responsabilidad de Habitacion
        void marcarReservada(){ this.estado = EstadoHab.OCUPADA; }
        void marcarDisponible(){ this.estado = EstadoHab.DISPONIBLE; }

        // calculo de precio sencillo (puede ampliarse con temporadas/promociones)
        double calcularPrecio(Fecha inicio, Fecha fin){
            long noches = Fecha.diasEntre(inicio, fin);
            return noches * this.precioBase;
        }

        // metodo que delega la verificacion de disponibilidad al gestor
        boolean estaDisponible(Fecha inicio, Fecha fin){
            return gestorDisponibilidad.estaDisponible(this, inicio, fin);
        }

        public String toString(){
            return id + " ["+tipo+"] $" + precioBase + " " + estado;
        }
    }

    // Nueva clase: GestorDisponibilidadHabitacion (responsable unico de disponibilidad)
    static class GestorDisponibilidadHabitacion {
        ArrayList<Reserva> reservas; // acceso a reservas para comprobar solapamientos
        // (puede ampliarse con lista de promociones, calendario, etc.)

        GestorDisponibilidadHabitacion(ArrayList<Reserva> reservas){
            this.reservas = reservas;
        }

        // metodo principal: comprueba si una habitacion esta libre en el periodo
        boolean estaDisponible(Habitacion h, Fecha inicio, Fecha fin){
            // si habitacion fuera de servicio o en limpieza, no esta disponible
            if(h.estado == EstadoHab.LIMPIEZA) return false;
            for(Reserva r : reservas){
                if(r.estado == EstadoRes.CANCELADA) continue;
                if(r.idHabitacion == null) continue; // reserva sin habitacion asignada no bloquea una concreta
                if(!r.idHabitacion.equals(h.id)) continue;
                if(Fecha.seSolapan(r.inicio, r.fin, inicio, fin)) return false;
            }
            return true;
        }

        // buscar y retornar la primera habitacion libre del tipo en el periodo (null si no hay)
        Habitacion buscarHabitacionLibre(ArrayList<Habitacion> listaHabitaciones, TipoHab tipo, Fecha inicio, Fecha fin){
            for(Habitacion h : listaHabitaciones){
                if(h.tipo != tipo) continue;
                if(estaDisponible(h, inicio, fin)) return h;
            }
            return null;
        }

        // listar habitaciones disponibles del tipo en el periodo
        ArrayList<Habitacion> listarDisponibles(ArrayList<Habitacion> listaHabitaciones, TipoHab tipo, Fecha inicio, Fecha fin){
            ArrayList<Habitacion> res = new ArrayList<Habitacion>();
            for(Habitacion h : listaHabitaciones){
                if(h.tipo != tipo) continue;
                if(estaDisponible(h, inicio, fin)) res.add(h);
            }
            return res;
        }
    }

    // Cliente y Reserva (simples)
    static class Cliente {
        String id, nombre, contacto;
        Cliente(String id,String n,String c){ this.id=id; this.nombre=n; this.contacto=c; }
        public String toString(){ return id + " " + nombre + " ("+contacto+")"; }
    }

    static class Reserva {
        String id, idCliente, idHabitacion;
        TipoHab tipo;
        Fecha inicio, fin;
        double precio;
        EstadoRes estado;
        Reserva(String id, String idCliente, TipoHab tipo, Fecha inicio, Fecha fin){
            this.id = id; this.idCliente = idCliente; this.tipo = tipo; this.inicio = inicio; this.fin = fin;
            this.precio = 0; this.estado = EstadoRes.RESERVADA; this.idHabitacion = null;
        }
        public String toString(){
            return id + " Cliente:" + idCliente + " Tipo:" + tipo + " " + inicio + "->" + fin
                + " Hab:" + (idHabitacion==null? "(no)" : idHabitacion) + " Estado:" + estado + " Precio:$" + precio;
        }
    }

    // -------- Almacenamiento con ArrayList (sin Map) --------
    ArrayList<Habitacion> habitaciones = new ArrayList<Habitacion>();
    ArrayList<Cliente> clientes = new ArrayList<Cliente>();
    ArrayList<Reserva> reservas = new ArrayList<Reserva>();
    GestorDisponibilidadHabitacion gestor; // instancia del gestor (compartida)

    // contadores id
    int contHab=1, contCli=1, contRes=1;
    String nuevoIdHab(){ return "H"+(contHab++); }
    String nuevoIdCli(){ return "C"+(contCli++); }
    String nuevoIdRes(){ return "R"+(contRes++); }

    // constructor: crea el gestor y lo aplica a las habitaciones cuando se agreguen
    public SistemaHotelSRP(){
        this.gestor = new GestorDisponibilidadHabitacion(this.reservas);
    }

    // operaciones: agregar habitacion asignando gestor
    void agregarHabitacion(Scanner sc){
        System.out.print("Tipo (1-INDIVIDUAL 2-DOBLE 3-SUITE): ");
        int t = Integer.parseInt(sc.nextLine().trim());
        System.out.print("Precio por noche: ");
        double p = Double.parseDouble(sc.nextLine().trim());
        TipoHab tipo = (t==1?TipoHab.INDIVIDUAL: t==2?TipoHab.DOBLE:TipoHab.SUITE);
        Habitacion h = new Habitacion(nuevoIdHab(), tipo, p, gestor);
        habitaciones.add(h);
        System.out.println("Habitacion agregada: " + h);
    }

    void agregarCliente(Scanner sc){
        System.out.print("Nombre: ");
        String n = sc.nextLine().trim();
        System.out.print("Contacto: ");
        String c = sc.nextLine().trim();
        Cliente cli = new Cliente(nuevoIdCli(), n, c);
        clientes.add(cli);
        System.out.println("Cliente agregado: " + cli);
    }

    // crear reserva usa el gestor para comprobar disponibilidad por tipo
    void crearReserva(Scanner sc){
        if(clientes.isEmpty()){ System.out.println("No hay clientes. Primero agregue uno."); return;}
        System.out.println("Clientes:");
        for(Cliente c: clientes) System.out.println(c);
        System.out.print("Ingrese id cliente (ej C1): ");
        String idCli = sc.nextLine().trim();
        Cliente cli = null;
        for(Cliente c: clientes) if(c.id.equals(idCli)) { cli = c; break; }
        if(cli==null){ System.out.println("Cliente no encontrado."); return; }

        System.out.print("Tipo (1-IND 2-DOB 3-SUI): ");
        int tt = Integer.parseInt(sc.nextLine().trim());
        TipoHab tipo = tt==1?TipoHab.INDIVIDUAL: tt==2?TipoHab.DOBLE:TipoHab.SUITE;
        System.out.print("Fecha inicio (YYYY-MM-DD): ");
        Fecha ini = Fecha.parse(sc.nextLine().trim());
        System.out.print("Fecha fin (YYYY-MM-DD): ");
        Fecha fin = Fecha.parse(sc.nextLine().trim());

        // uso del gestor: listar disponibles para el tipo en el periodo
        ArrayList<Habitacion> disponibles = gestor.listarDisponibles(habitaciones, tipo, ini, fin);
        if(disponibles.isEmpty()){ System.out.println("No hay disponibilidad para ese tipo en esas fechas."); return; }

        String id = nuevoIdRes();
        Reserva r = new Reserva(id, idCli, tipo, ini, fin);
        // precio calculado por la habitacion ejemplo (podria usarse politica de promociones)
        Habitacion ejemplo = disponibles.get(0);
        r.precio = ejemplo.calcularPrecio(ini, fin);
        reservas.add(r);
        System.out.println("Reserva creada: " + r);
    }

    // check-in: usar gestor para asignar habitacion libre
    void hacerCheckIn(Scanner sc){
        System.out.print("Id reserva para check-in: ");
        String id = sc.nextLine().trim();
        Reserva r = null;
        for(Reserva s: reservas) if(s.id.equals(id)) { r = s; break; }
        if(r==null){ System.out.println("Reserva no encontrada."); return; }
        if(r.estado != EstadoRes.RESERVADA){ System.out.println("Reserva no esta en estado RESERVADA."); return; }
        Habitacion h = gestor.buscarHabitacionLibre(habitaciones, r.tipo, r.inicio, r.fin);
        if(h==null){ System.out.println("No hay habitacion libre para asignar."); return; }
        h.marcarReservada();
        r.idHabitacion = h.id;
        r.estado = EstadoRes.CHECKIN;
        System.out.println("Check-in OK. Hab asignada: " + h.id);
    }

    void hacerCheckOut(Scanner sc){
        System.out.print("Id reserva para check-out: ");
        String id = sc.nextLine().trim();
        Reserva r = null;
        for(Reserva s: reservas) if(s.id.equals(id)) { r = s; break; }
        if(r==null){ System.out.println("Reserva no encontrada."); return; }
        if(r.estado != EstadoRes.CHECKIN){ System.out.println("Reserva no esta en CHECKIN."); return; }
        Habitacion h = null;
        for(Habitacion hh: habitaciones) if(hh.id.equals(r.idHabitacion)) { h = hh; break; }
        if(h!=null) h.estado = EstadoHab.LIMPIEZA;
        r.estado = EstadoRes.CHECKOUT;
        System.out.println("Check-out realizado. Hab marcada para limpieza: " + (h==null? "(no encontrada)": h.id));
    }

    void cancelarReserva(Scanner sc){
        System.out.print("Id reserva a cancelar: ");
        String id = sc.nextLine().trim();
        Reserva r = null;
        for(Reserva s: reservas) if(s.id.equals(id)) { r = s; break; }
        if(r==null){ System.out.println("Reserva no encontrada."); return; }
        if(r.estado==EstadoRes.CANCELADA){ System.out.println("Ya esta cancelada."); return; }
        double reembolso = (r.idHabitacion==null)? r.precio : r.precio * 0.5;
        r.estado = EstadoRes.CANCELADA;
        if(r.idHabitacion!=null){
            for(Habitacion h: habitaciones) if(h.id.equals(r.idHabitacion)){ h.marcarDisponible(); break; }
            r.idHabitacion = null;
        }
        System.out.println("Reserva cancelada. Reembolso estimado: $" + reembolso);
    }

    void listarHabitaciones(){ for(Habitacion h: habitaciones) System.out.println(h); }
    void listarReservas(){ for(Reserva r: reservas) System.out.println(r); }

    // menu interactivo
    void menu(){
        Scanner sc = new Scanner(System.in);
        while(true){
            System.out.println("\n--- Sistema Hotel SRP (interactivo) ---");
            System.out.println("1) Agregar habitacion");
            System.out.println("2) Agregar cliente");
            System.out.println("3) Crear reserva");
            System.out.println("4) Check-in");
            System.out.println("5) Check-out");
            System.out.println("6) Cancelar reserva");
            System.out.println("7) Listar habitaciones");
            System.out.println("8) Listar reservas");
            System.out.println("9) Salir");
            System.out.print("Elige opcion: ");
            String op = sc.nextLine().trim();
            try{
                if(op.equals("1")) agregarHabitacion(sc);
                else if(op.equals("2")) agregarCliente(sc);
                else if(op.equals("3")) crearReserva(sc);
                else if(op.equals("4")) hacerCheckIn(sc);
                else if(op.equals("5")) hacerCheckOut(sc);
                else if(op.equals("6")) cancelarReserva(sc);
                else if(op.equals("7")) listarHabitaciones();
                else if(op.equals("8")) listarReservas();
                else if(op.equals("9")) { System.out.println("Adios."); break; }
                else System.out.println("Opcion no valida.");
            }catch(Exception ex){
                System.out.println("Error: " + ex.getMessage());
            }
        }
        sc.close();
    }

    // main en la misma clase
    public static void main(String[] args){
        SistemaHotelSRP app = new SistemaHotelSRP();
        System.out.println("Bienvenido al sistema SRP (usa formato fechas YYYY-MM-DD).");
        app.menu();
    }
}
