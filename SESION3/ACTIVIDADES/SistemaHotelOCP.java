import java.util.*;

public class SistemaHotelOCP {

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

    // ---------- Modelos ----------
    enum TipoHab { INDIVIDUAL, DOBLE, SUITE }
    enum EstadoHab { DISPONIBLE, OCUPADA, LIMPIEZA }
    enum EstadoRes { RESERVADA, CANCELADA, CHECKIN, CHECKOUT }

    // Interfaz PoliticaCancelacion (OCP: se pueden añadir nuevas politicas sin modificar Reserva)
    public static interface PoliticaCancelacion {
        // determina si la reserva puede ser cancelada segun la politica y la fecha de cancelacion
        boolean puedeCancelar(Reserva r, Fecha fechaCancelacion);
        // porcentaje de penalizacion (0.0 = sin penalizacion, 1.0 = penalizacion total)
        double porcentajePenalizacion(Reserva r, Fecha fechaCancelacion);
        String nombre();
    }

    // Implementaciones concretas
    // Politica flexible: permite cancelar si hay al menos 1 dia (24h) antes del check-in, sin penalizacion
    public static class PoliticaFlexible implements PoliticaCancelacion {
        public boolean puedeCancelar(Reserva r, Fecha fechaCancelacion){
            int dias = Fecha.diasEntre(fechaCancelacion, r.inicio);
            return dias >= 1;
        }
        public double porcentajePenalizacion(Reserva r, Fecha fechaCancelacion){
            return 0.0;
        }
        public String nombre(){ return "Flexible (>=24h sin penalizacion)"; }
    }

    // Politica moderada: si cancela >=3 dias antes => sin penalizacion;
    // si cancela entre 0 y 3 dias antes => permite cancelar con penalizacion 50%;
    // si cancela en o despues del check-in => no permitido
    public static class PoliticaModerada implements PoliticaCancelacion {
        public boolean puedeCancelar(Reserva r, Fecha fechaCancelacion){
            int dias = Fecha.diasEntre(fechaCancelacion, r.inicio);
            return dias >= 0; // permitimos intento de cancelacion antes del inicio (se aplica penalizacion segun tiempo)
        }
        public double porcentajePenalizacion(Reserva r, Fecha fechaCancelacion){
            int dias = Fecha.diasEntre(fechaCancelacion, r.inicio);
            if (dias >= 3) return 0.0;
            if (dias >= 0) return 0.5;
            return 1.0; // despues del inicio -> penalizacion total (no reembolso)
        }
        public String nombre(){ return "Moderada (>=72h sin penalizacion, <72h penalizacion 50%)"; }
    }

    // Politica estricta: no permite cancelaciones despues de realizar la reserva (nunca permite)
    public static class PoliticaEstricta implements PoliticaCancelacion {
        public boolean puedeCancelar(Reserva r, Fecha fechaCancelacion){
            return false;
        }
        public double porcentajePenalizacion(Reserva r, Fecha fechaCancelacion){
            return 1.0;
        }
        public String nombre(){ return "Estricta (no permite cancelacion)"; }
    }

    // Habitacion
    static class Habitacion {
        String id; TipoHab tipo; double precio; EstadoHab estado;
        Habitacion(String id, TipoHab t, double p){ this.id=id; this.tipo=t; this.precio=p; this.estado=EstadoHab.DISPONIBLE; }
        double calcularPrecio(Fecha inicio, Fecha fin){ long noches = Fecha.diasEntre(inicio, fin); return noches * precio; }
        public String toString(){ return id + " ["+tipo+"] $" + precio + " " + estado; }
    }

    // Cliente
    static class Cliente {
        String id, nombre, contacto;
        Cliente(String id,String n,String c){ this.id=id; this.nombre=n; this.contacto=c; }
        public String toString(){ return id + " " + nombre + " ("+contacto+")"; }
    }

    // Reserva ahora tiene PoliticaCancelacion asociada
    static class Reserva {
        String id, idCliente, idHabitacion;
        TipoHab tipo;
        Fecha inicio, fin;
        double precio;
        EstadoRes estado;
        PoliticaCancelacion politicaCancelacion;

        Reserva(String id, String idCliente, TipoHab tipo, Fecha inicio, Fecha fin, PoliticaCancelacion pol){
            this.id = id; this.idCliente = idCliente; this.tipo = tipo; this.inicio = inicio; this.fin = fin;
            this.precio = 0; this.estado = EstadoRes.RESERVADA; this.idHabitacion = null;
            this.politicaCancelacion = pol;
        }

        public String toString(){
            String polName = (politicaCancelacion==null? "(sin politica)" : politicaCancelacion.nombre());
            return id + " Cliente:" + idCliente + " Tipo:" + tipo + " " + inicio + "->" + fin
                + " Hab:" + (idHabitacion==null? "(no)" : idHabitacion) + " Estado:" + estado + " Precio:$" + precio
                + " Politica:" + polName;
        }
    }

    // Almacenamiento
    ArrayList<Habitacion> habitaciones = new ArrayList<Habitacion>();
    ArrayList<Cliente> clientes = new ArrayList<Cliente>();
    ArrayList<Reserva> reservas = new ArrayList<Reserva>();

    // Contadores
    int contHab=1, contCli=1, contRes=1;
    String nuevoIdHab(){ return "H"+(contHab++); }
    String nuevoIdCli(){ return "C"+(contCli++); }
    String nuevoIdRes(){ return "R"+(contRes++); }

    // Lista de politicas disponibles (se puede extender sin cambiar Reserva)
    ArrayList<PoliticaCancelacion> politicasDisponibles = new ArrayList<PoliticaCancelacion>();

    // Constructor: registrar politicas por defecto
    public SistemaHotelOCP(){
        politicasDisponibles.add(new PoliticaFlexible());
        politicasDisponibles.add(new PoliticaModerada());
        politicasDisponibles.add(new PoliticaEstricta());
    }

    // Operaciones basicas
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

    // listar politicas para seleccionar
    void listarPoliticas(){
        for(int i=0;i<politicasDisponibles.size();i++){
            System.out.println((i+1) + ") " + politicasDisponibles.get(i).nombre());
        }
    }

    // disponibilidad simple por tipo (comprueba conteo y solapamientos)
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

    // crear reserva: ahora se selecciona la politica al crearla
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

        // seleccionar politica
        System.out.println("Seleccione politica de cancelacion:");
        listarPoliticas();
        System.out.print("Opcion (numero): ");
        int opt = Integer.parseInt(sc.nextLine().trim());
        PoliticaCancelacion pol = politicasDisponibles.get(Math.max(0, Math.min(opt-1, politicasDisponibles.size()-1)));

        String id = nuevoIdRes();
        Reserva r = new Reserva(id, idCli, tipo, ini, fin, pol);
        // precio basado en primera habitacion del tipo
        Habitacion ejemplo = null;
        for(Habitacion h: habitaciones) if(h.tipo==tipo){ ejemplo = h; break; }
        long noches = Fecha.diasEntre(ini, fin);
        r.precio = (ejemplo==null?0: ejemplo.precio * noches);
        reservas.add(r);
        System.out.println("Reserva creada: " + r);
    }

    void listarReservas(){ if(reservas.isEmpty()){ System.out.println("No hay reservas."); return; } for(Reserva r: reservas) System.out.println(r); }
    void listarHabitaciones(){ if(habitaciones.isEmpty()){ System.out.println("No hay habitaciones."); return; } for(Habitacion h: habitaciones) System.out.println(h); }

    // cancelar reserva: se pide fecha de cancelacion y se aplica la politica asociada
    void cancelarReserva(Scanner sc){
        System.out.print("Id reserva a cancelar: ");
        String id = sc.nextLine().trim();
        Reserva r = null;
        for(Reserva s: reservas) if(s.id.equals(id)) { r = s; break; }
        if(r==null){ System.out.println("Reserva no encontrada."); return; }
        if(r.estado==EstadoRes.CANCELADA){ System.out.println("Reserva ya esta cancelada."); return; }

        System.out.print("Fecha de cancelacion (YYYY-MM-DD): ");
        Fecha fechaCancel = Fecha.parse(sc.nextLine().trim());

        PoliticaCancelacion pol = r.politicaCancelacion;
        if(pol == null){
            System.out.println("Reserva no tiene politica asociada. Cancelacion no permitida.");
            return;
        }

        if(!pol.puedeCancelar(r, fechaCancel)){
            System.out.println("Segun la politica (" + pol.nombre() + ") no es posible cancelar esta reserva.");
            return;
        }

        double penal = pol.porcentajePenalizacion(r, fechaCancel); // 0..1
        double reembolso = r.precio * (1.0 - penal);
        r.estado = EstadoRes.CANCELADA;
        // liberar habitacion si estaba asignada
        if(r.idHabitacion != null){
            for(Habitacion h: habitaciones) if(h.id.equals(r.idHabitacion)){ h.estado = EstadoHab.DISPONIBLE; break; }
            r.idHabitacion = null;
        }
        System.out.println("Reserva cancelada. Politica: " + pol.nombre());
        System.out.printf("Precio original: $%.2f  Penalizacion: %.0f%%  Reembolso: $%.2f%n", r.precio, penal*100.0, reembolso);
    }

    // check-in y check-out simples (asignacion por primera habitacion libre)
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

    // Menu interactivo
    void menu(){
        Scanner sc = new Scanner(System.in);
        while(true){
            System.out.println("\n--- Sistema Hotel OCP (interactivo) ---");
            System.out.println("1) Agregar habitacion");
            System.out.println("2) Agregar cliente");
            System.out.println("3) Crear reserva (seleccionar politica de cancelacion)");
            System.out.println("4) Check-in");
            System.out.println("5) Check-out");
            System.out.println("6) Cancelar reserva (usa politica asociada)");
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

    // Main en la misma clase
    public static void main(String[] args){
        SistemaHotelOCP app = new SistemaHotelOCP();
        System.out.println("Bienvenido al sistema OCP (usa formato fechas YYYY-MM-DD).");
        app.menu();
    }
}

