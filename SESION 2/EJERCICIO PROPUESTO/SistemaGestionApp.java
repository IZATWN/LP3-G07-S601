package tp_01;
import java.util.*;

abstract class Persona {
 protected static int contadorPersonas = 0;
 protected String id;
 protected String nombre;

 public Persona(String id, String nombre) {
     this.id = id;
     this.nombre = nombre;
     contadorPersonas++;
 }

 public abstract void mostrarPerfil();

 public static int getContadorPersonas() {
     return contadorPersonas;
 }
}

interface Evaluador {
 void evaluar(Estudiante e, Curso c, double nota);
}

class Estudiante extends Persona {
 private String carrera;
 private List<Curso> cursosMatriculados;

 public Estudiante(String id, String nombre, String carrera) {
     super(id, nombre);
     this.carrera = carrera;
     this.cursosMatriculados = new ArrayList<Curso>();
 }

 public boolean inscribirse(Curso curso) {
     boolean ok = curso.matricular(this);
     if (ok) {
         cursosMatriculados.add(curso);
     }
     return ok;
 }

 public void retirar(Curso curso) {
     if (cursosMatriculados.remove(curso)) {
         curso.desmatricular(this);
     }
 }

 public int getCantidadCursos() {
     return cursosMatriculados.size();
 }

 public void mostrarPerfil() {
     System.out.println("Estudiante: " + nombre + " (ID: " + id + ") - Carrera: " + carrera + " - Cursos: " + getCantidadCursos());
 }
}

class Profesor extends Persona implements Evaluador {
 public Profesor(String id, String nombre) {
     super(id, nombre);
 }

 public void asignarProfesorACurso(Curso curso) {
     curso.setProfesor(this);
 }

 public void mostrarPerfil() {
     System.out.println("Profesor: " + nombre + " (ID: " + id + ")");
 }

 public void evaluar(Estudiante e, Curso c, double nota) {
     c.registrarNota(e, nota);
 }
}

class ProfesorAdjunto extends Profesor {
 private boolean esAdjunto;

 public ProfesorAdjunto(String id, String nombre) {
     super(id, nombre);
     this.esAdjunto = true;
 }

 public void mostrarPerfil() {
     System.out.println("Profesor Adjunto: " + nombre + " (ID: " + id + ") - Adjuntia: " + esAdjunto);
 }

 public void evaluar(Estudiante e, Curso c, double nota) {
     double notaAjustada = Math.min(20.0, nota + 0.5);
     c.registrarNota(e, notaAjustada);
 }
}

class Curso {
 public static final int MAX_ESTUDIANTES = 30;
 private static int totalCursos = 0;

 private String codigo;
 private String titulo;
 private Profesor profesor;

 private List<Estudiante> estudiantes;
 private List<Double> notas;

 public Curso(String codigo, String titulo) {
     this.codigo = codigo;
     this.titulo = titulo;
     this.estudiantes = new ArrayList<Estudiante>();
     this.notas = new ArrayList<Double>();
     totalCursos++;
 }

 public static int getTotalCursos() {
     return totalCursos;
 }

 public boolean matricular(Estudiante e) {
     if (estudiantes.size() >= MAX_ESTUDIANTES) return false;
     for (Estudiante ex : estudiantes) {
         if (ex.id.equals(e.id)) return false;
     }
     estudiantes.add(e);
     notas.add(null);
     return true;
 }

 public boolean desmatricular(Estudiante e) {
     for (int i = 0; i < estudiantes.size(); i++) {
         if (estudiantes.get(i).id.equals(e.id)) {
             estudiantes.remove(i);
             notas.remove(i);
             return true;
         }
     }
     return false;
 }

 public int getCantidadMatriculados() {
     return estudiantes.size();
 }

 public String getCodigo() {
     return codigo;
 }

 public String getTitulo() {
     return titulo;
 }

 public void setProfesor(Profesor profesor) {
     this.profesor = profesor;
 }

 public Profesor getProfesor() {
     return profesor;
 }

 public void registrarNota(Estudiante e, double nota) {
     for (int i = 0; i < estudiantes.size(); i++) {
         if (estudiantes.get(i).id.equals(e.id)) {
             notas.set(i, nota);
             return;
         }
     }
     System.out.println("Estudiante " + e.id + " no inscrito en el curso " + codigo);
 }

 public Double obtenerNota(Estudiante e) {
     for (int i = 0; i < estudiantes.size(); i++) {
         if (estudiantes.get(i).id.equals(e.id)) {
             return notas.get(i);
         }
     }
     return null;
 }

 public void mostrarInfo() {
     System.out.println("Curso: " + titulo + " (" + codigo + ") - Profesor: " + (profesor != null ? profesor.nombre : "Sin asignar") + " - Matriculados: " + getCantidadMatriculados());
 }

 public boolean tieneEstudiante(String idEstudiante) {
     for (Estudiante e : estudiantes) if (e.id.equals(idEstudiante)) return true;
     return false;
 }

 public List<Estudiante> listarEstudiantes() {
     return new ArrayList<Estudiante>(estudiantes);
 }

 public List<Double> listarNotas() {
     return new ArrayList<Double>(notas);
 }
}

class SistemaGestion {
 public static final String NOMBRE_SISTEMA = "Sistema de Gestion de Cursos Universitarios";

 private List<Curso> cursos;
 private List<Estudiante> estudiantes;
 private List<Profesor> profesores;

 public SistemaGestion() {
     cursos = new ArrayList<Curso>();
     estudiantes = new ArrayList<Estudiante>();
     profesores = new ArrayList<Profesor>();
 }

 public void agregarCurso(Curso c) {
     cursos.add(c);
 }

 public void registrarEstudiante(Estudiante e) {
     estudiantes.add(e);
 }

 public void registrarProfesor(Profesor p) {
     profesores.add(p);
 }

 public Curso buscarCurso(String codigo) {
     for (Curso c : cursos) if (c.getCodigo().equals(codigo)) return c;
     return null;
 }

 public Estudiante buscarEstudiante(String id) {
     for (Estudiante e : estudiantes) if (e.id.equals(id)) return e;
     return null;
 }

 public Profesor buscarProfesor(String id) {
     for (Profesor p : profesores) if (p.id.equals(id)) return p;
     return null;
 }

 public List<Curso> listarCursosDisponibles() {
     return new ArrayList<Curso>(cursos);
 }

 public List<Estudiante> listarEstudiantes() {
     return new ArrayList<Estudiante>(estudiantes);
 }

 public List<Profesor> listarProfesores() {
     return new ArrayList<Profesor>(profesores);
 }

 public List<String> cantidadMatriculadosPorCurso() {
     List<String> lista = new ArrayList<String>();
     for (Curso c : cursos) {
         lista.add(c.getCodigo() + " -> " + c.getCantidadMatriculados());
     }
     return lista;
 }

 public void mostrarResumen() {
     System.out.println("Total personas registradas: " + Persona.getContadorPersonas());
     System.out.println("Total cursos en el sistema: " + Curso.getTotalCursos());
     System.out.println("Cursos disponibles:");
     for (Curso c : cursos) {
         c.mostrarInfo();
     }
     System.out.println("-------------------------------");
 }
}

public class SistemaGestionApp {
 private static Scanner sc = new Scanner(System.in);
 private static SistemaGestion sistema = new SistemaGestion();

 private static String leerLinea(String prompt) {
     while (true) {
         System.out.print(prompt + ": ");
         String line = sc.nextLine().trim();
         if (line.isEmpty()) {
             System.out.println("No puede estar vacio");
             continue;
         }
         return line;
     }
 }

 private static void agregarProfesor() {
     System.out.println("== Agregar Profesor ==");
     String id = leerLinea("ID ");
     String nombre = leerLinea("Nombre ");
     Profesor p = new Profesor(id, nombre);
     sistema.registrarProfesor(p);
     System.out.println("Profesor agregado.");
 }

 private static void agregarCurso() {
     System.out.println("== Agregar Curso ==");
     String codigo = leerLinea("Codigo ");
     String titulo = leerLinea("Titulo del curso ");
     Curso c = new Curso(codigo, titulo);
     sistema.agregarCurso(c);
     System.out.println("Curso agregado. Titulo = " + c.getTitulo());
 }

 private static void agregarEstudiante() {
     System.out.println("== Agregar Estudiante ==");
     String id = leerLinea("ID ");
     String nombre = leerLinea("Nombre ");
     String carrera = leerLinea("Carrera");
     Estudiante e = new Estudiante(id, nombre, carrera);
     sistema.registrarEstudiante(e);
     System.out.println("Estudiante agregado.");
 }

 private static void inscribirEstudiante() {
     System.out.println("== Inscribir Estudiante ==");
     String idEst = leerLinea("ID Estudiante");
     String codigoCurso = leerLinea("Codigo Curso");
     Estudiante e = sistema.buscarEstudiante(idEst);
     Curso c = sistema.buscarCurso(codigoCurso);
     if (e == null) { System.out.println("Estudiante no encontrado."); return; }
     if (c == null) { System.out.println("Curso no encontrado."); return; }
     boolean ok = e.inscribirse(c);
     System.out.println(ok ? "Inscripcion exitosa." : "No se pudo inscribir ");
 }

 private static void asignarProfesor() {
     System.out.println("== Asignar Profesor a Curso ==");
     String idProf = leerLinea("ID Profesor");
     String codigoCurso = leerLinea("Codigo Curso");
     Profesor p = sistema.buscarProfesor(idProf);
     Curso c = sistema.buscarCurso(codigoCurso);
     if (p == null) { System.out.println("Profesor no encontrado."); return; }
     if (c == null) { System.out.println("Curso no encontrado."); return; }
     p.asignarProfesorACurso(c);
     System.out.println("Profesor asignado al curso.");
 }

 private static void evaluarEstudiante() {
     System.out.println("== Evaluar Estudiante ==");
     String idProf = leerLinea("ID Profesor ");
     String idEst = leerLinea("ID Estudiante");
     String codigoCurso = leerLinea("Codigo Curso");
     String notaS = leerLinea("Nota (numero decimal)");
     Profesor p = sistema.buscarProfesor(idProf);
     Estudiante e = sistema.buscarEstudiante(idEst);
     Curso c = sistema.buscarCurso(codigoCurso);
     if (p == null || e == null || c == null) {
         System.out.println("Profesor, estudiante o curso no encontrados.");
         return;
     }
     double nota;
     try {
         nota = Double.parseDouble(notaS);
     } catch (Exception ex) {
         System.out.println("Nota invalida.");
         return;
     }
     p.evaluar(e, c, nota);
     System.out.println("Evaluacion registrada.");
 }

 private static void listarCursos() {
     System.out.println("== Cursos Disponibles ==");
     for (Curso c : sistema.listarCursosDisponibles()) {
         c.mostrarInfo();
     }
 }

 private static void mostrarNotas() {
     System.out.println("== Mostrar Notas ==");
     String codigoCurso = leerLinea("Codigo Curso");
     Curso c = sistema.buscarCurso(codigoCurso);
     if (c == null) { System.out.println("Curso no encontrado."); return; }
     System.out.println("Notas del curso " + c.getTitulo() + ":");
     List<Estudiante> lista = c.listarEstudiantes();
     List<Double> notas = c.listarNotas();
     for (int i = 0; i < lista.size(); i++) {
         Estudiante e = lista.get(i);
         Double nota = notas.get(i);
         System.out.println(e.nombre + " (" + e.id + ") -> " + (nota != null ? nota : "Sin nota"));
     }
 }

 private static void mostrarResumen() {
     sistema.mostrarResumen();
 }

 private static void mostrarMenu() {
     System.out.println("--- MENU ---");
     System.out.println("1. Agregar profesor");
     System.out.println("2. Agregar curso");
     System.out.println("3. Agregar estudiante");
     System.out.println("4. Inscribir estudiante en curso");
     System.out.println("5. Asignar profesor a curso");
     System.out.println("6. Evaluar estudiante");
     System.out.println("7. Listar cursos");
     System.out.println("8. Mostrar notas de un curso");
     System.out.println("9. Mostrar resumen del sistema");
     System.out.println("0. Salir");
 }

 public static void main(String[] args) {
     boolean running = true;
     while (running) {
         mostrarMenu();
         String opc = leerLinea("Elija una opcion");
         switch (opc) {
             case "1": agregarProfesor(); break;
             case "2": agregarCurso(); break;
             case "3": agregarEstudiante(); break;
             case "4": inscribirEstudiante(); break;
             case "5": asignarProfesor(); break;
             case "6": evaluarEstudiante(); break;
             case "7": listarCursos(); break;
             case "8": mostrarNotas(); break;
             case "9": mostrarResumen(); break;
             case "0": running = false; break;
             default: System.out.println("Opcion invalida.");
         }
     }
     System.out.println("Saliendo.");
 }
}

