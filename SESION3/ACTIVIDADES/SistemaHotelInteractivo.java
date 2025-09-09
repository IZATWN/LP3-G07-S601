import java.util.*;

public class SistemaHotelInteractivo {

    // ---------- Fecha simple ----------
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

    // ---------- Modelos en espanol ----------
    enum TipoHab { INDIVIDUAL, DOBLE, SUITE }
    enum EstadoHab { DISPONIBLE, OCUPADA, LIMPIEZA }
    enum EstadoRes { RESERVADA, CANCELADA, CHECKIN, CHECKOUT }

    static class Habitacion {
        String id; TipoHab tipo; double precio; EstadoHab estado;
        Habitacion(String id, TipoHab t, double p){ this.id=id; tipo=t; precio=p; estado=EstadoHab.DISPONIBLE; }
        public String toString(){ return id + " ["+tipo+"] $" + precio + " " + estado; }
    }

    static class Cliente {
        String id, nombre, contacto;
        Cliente(String id,String n,String c){ this.id=id; nombre=n; contacto=c; }
        public String toString(){ return id + " " + nombre + " ("+contacto+")"; }
    }

    static class Reserva {
        String id, idCliente, idHabitacion; TipoHab tipo; Fecha inicio, fin; double precio; EstadoRes estado;
        Reserva(String id,String cli,TipoHab t,Fecha ini,Fecha fin){
            this.id=id; this.idCliente=cli; this.tipo=t; this.inicio=ini; this.fin=fin;
            this.precio=0; this.estado=EstadoRes.RESERVADA; this.idHabitacion=null;
        }
        public String toString(){
            return id + " Cliente:" + idCliente + " Tipo:" + tipo + " " + inicio + "->" + fin
                + " Hab:" + (idHabitacion==null? "(no)" : idHabitacion) + " Estado:" + estado + " Precio:$" + precio;
        }
    }

    // ---------- Almacenamiento (ArrayList, sin Map) ----------
    ArrayList<Habitacion> habitaciones = new ArrayList<Habitacion>();
    ArrayList<Cliente> clientes = new ArrayList<Cliente>();
    ArrayList<Reserva> reservas = new ArrayList<Reserva>();

    // contadores ID
    int contHab=1, contCli=1, contRes=1;
    String nuevoIdHab(){ return "H"+(contHab++); }
    String nuevoIdCli(){ return "C"+(contCli++); }
    String nuevoIdRes(){ return "R"+(contRes++); }

    // ---------- Operaciones ----------
    void agregarHabitacion(Scanner sc){
        System.out.print("Tipo (1-INDIVIDUAL 2-DOBLE 3-SUITE): ");
        int t = Integer.parseInt(sc.nextLine().trim());
        System.out.print("Precio por noche: ");
        double p = Double.parseDouble(sc.nextLine().trim());
        TipoHab tipo = (t==1?TipoHab.INDIVIDUAL: t==2?TipoHab.DOBLE:TipoHab.SUITE);
        Habitacion h = new Habitacion(nuevoIdHab(), tipo, p);
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

    int contarHabitacionesTipo(TipoHab tipo){
        int cnt=0;
        for(Habitacion h: habitaciones) if(h.tipo==tipo && h.estado!=EstadoHab.LIMPIEZA) cnt++;
        return cnt;
    }

    int reservasActivasTipoEnPeriodo(TipoHab tipo, Fecha inicio, Fecha fin){
        int cnt=0;
        for(Reserva r: reservas) if(r.estado==EstadoRes.RESERVADA && r.tipo==tipo && Fecha.seSolapan(r.inicio,r.fin,inicio,fin)) cnt++;
        return cnt;
    }

    Habitacion buscarHabitacionLibreParaPeriodo(TipoHab tipo, Fecha inicio, Fecha fin){
        for(Habitacion h: habitaciones){
            if(h.tipo!=tipo) continue;
            if(h.estado==EstadoHab.LIMPIEZA) continue;
            boolean ocupada=false;
            for(Reserva r: reservas){
                if(r.estado==EstadoRes.CANCELADA) continue;
                if(r.idHabitacion==null) continue;
                if(!r.idHabitacion.equals(h.id)) continue;
                if(Fecha.seSolapan(r.inicio, r.fin, inicio, fin)){ ocupada=true; break; }
            }
            if(!ocupada) return h;
        }
        return null;
    }

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

        int totalHab = contarHabitacionesTipo(tipo);
        int reservasTipo = reservasActivasTipoEnPeriodo(tipo, ini, fin);
        if(totalHab==0){ System.out.println("No hay habitaciones de ese tipo en el sistema."); return; }
        if(reservasTipo >= totalHab){ System.out.println("No hay disponibilidad para ese tipo en esas fechas."); return; }

        String id = nuevoIdRes();
        Reserva r = new Reserva(id, idCli, tipo, ini, fin);
        // precio calculado con la primera habitacion del tipo
        Habitacion ejemplo = null;
        for(Habitacion h: habitaciones) if(h.tipo==tipo){ ejemplo = h; break; }
        long noches = Fecha.diasEntre(ini, fin);
        r.precio = (ejemplo==null?0: ejemplo.precio * noches);
        reservas.add(r);
        System.out.println("Reserva creada: " + r);
    }

    void listarReservas(){
        if(reservas.isEmpty()){ System.out.println("No hay reservas."); return; }
        for(Reserva r: reservas) System.out.println(r);
    }

    void listarHabitaciones(){
        if(habitaciones.isEmpty()){ System.out.println("No hay habitaciones."); return; }
        for(Habitacion h: habitaciones) System.out.println(h);
    }

    void hacerCheckIn(Scanner sc){
        System.out.print("Id reserva para check-in: ");
        String id = sc.nextLine().trim();
        Reserva r = null;
        for(Reserva s: reservas) if(s.id.equals(id)) { r = s; break; }
        if(r==null){ System.out.println("Reserva no encontrada."); return; }
        if(r.estado != EstadoRes.RESERVADA){ System.out.println("Reserva no esta en estado RESERVADA."); return; }
        Habitacion h = buscarHabitacionLibreParaPeriodo(r.tipo, r.inicio, r.fin);
        if(h==null){ System.out.println("No hay habitacion libre para asignar."); return; }
        h.estado = EstadoHab.OCUPADA;
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
            for(Habitacion h: habitaciones) if(h.id.equals(r.idHabitacion)){ h.estado = EstadoHab.DISPONIBLE; break; }
            r.idHabitacion = null;
        }
        System.out.println("Reserva cancelada. Reembolso estimado: $" + reembolso);
    }

    // ---------- Menu interactivo ----------
    void menu(){
        Scanner sc = new Scanner(System.in);
        while(true){
            System.out.println("\n--- Sistema Hotel (interactivo) ---");
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

    // ---------- main (en la misma clase) ----------
    public static void main(String[] args){
        SistemaHotelInteractivo app = new SistemaHotelInteractivo();
        System.out.println("Bienvenido al sistema (usa formato fechas YYYY-MM-DD).");
        app.menu();
    }
}
