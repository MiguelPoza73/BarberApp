package app;

import dominio.*;
import repositorio.RepositorioMemoria;
import negocio.Agenda;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

public class BarberiaDinamica {

    private static final Scanner in = new Scanner(System.in);

    public static void main(String[] args) {

        RepositorioMemoria repo = new RepositorioMemoria();
        Agenda agenda = new Agenda(repo);

        // Seed básico
        Servicio s1 = new Servicio(repo.nextId(), "Corte Clasico", 30, 15);
        Servicio s2 = new Servicio(repo.nextId(), "Corte Degradado", 45, 17);
        repo.guardar(s1);
        repo.guardar(s2);

        Barbero b1 = new Barbero(repo.nextId(), "Miguel");
        Barbero b2 = new Barbero(repo.nextId(), "Felix");
        repo.guardar(b1);
        repo.guardar(b2);

        boolean salir = false;

        while (!salir) {
            System.out.println("\n---------- Barber MVP ----------");
            System.out.println("1) Listar Clientes");
            System.out.println("2) Crear Cliente");
            System.out.println("3) Listar Barberos");
            System.out.println("4) Crear Barbero");
            System.out.println("5) Listar Servicios");
            System.out.println("6) Crear Servicio");
            System.out.println("7) Reservar cita");
            System.out.println("8) Ver citas de un barbero (por dia)");
            System.out.println("0) Salir");
            System.out.print("Opcion: ");
            String op = in.nextLine();

            if ("1".equals(op)) {
                listarClientes(repo.clientes());
            } else if ("2".equals(op)) {
                crearCliente(repo);
            } else if ("3".equals(op)) {
                listarBarberos(repo.barberos());
            } else if ("4".equals(op)) {
                crearBarbero(repo);
            } else if ("5".equals(op)) {
                listarServicios(repo.servicios());
            } else if ("6".equals(op)) {
                crearServicio(repo);
            } else if ("7".equals(op)) {
                reservarCita(repo, agenda);
            } else if ("8".equals(op)) {
                verCitasPorDia(repo, agenda);
            } else if ("0".equals(op)) {
                salir = true;
            } else {
                System.out.println("Opcion invalida.");
            }
        }

        System.out.println("¡Hasta luego!");
    } // <-- CIERRA main AQUÍ

    // ===== Helpers =====

    private static void listarClientes(List<Cliente> clientes) {
        if (clientes.isEmpty()) {
            System.out.println("No hay clientes");
            return;
        }
        for (Cliente c : clientes) {
            System.out.println(c);
        }
    }

    private static void listarBarberos(List<Barbero> barberos) {
        if (barberos.isEmpty()) {
            System.out.println("No hay barberos");
            return;
        }
        for (Barbero b : barberos) {
            System.out.println(b);
        }
    }

    private static void listarServicios(List<Servicio> servicios) {
        if (servicios.isEmpty()) {
            System.out.println("No hay servicios");
            return;
        }
        for (Servicio s : servicios) {
            System.out.println(s);
        }
    }

    private static void crearCliente(RepositorioMemoria repo) {
        System.out.print("Nombre cliente: ");
        String nombre = in.nextLine();
        System.out.print("Telefono: ");
        String tel = in.nextLine();
        Cliente c = new Cliente(repo.nextId(), nombre, tel);
        repo.guardar(c);
        System.out.println("Cliente creado: " + c);
    }

    private static void crearBarbero(RepositorioMemoria repo) {
        System.out.print("Nombre barbero: ");
        String nombre = in.nextLine();
        Barbero b = new Barbero(repo.nextId(), nombre);
        repo.guardar(b);
        System.out.println("Barbero creado: " + b);
    }

    private static void crearServicio(RepositorioMemoria repo) {
        System.out.print("Nombre servicio: ");
        String nombre = in.nextLine();
        System.out.print("Duracion (min): ");
        int dur = leerEntero();
        System.out.print("Precio (€): ");
        double precio = leerDouble();
        Servicio s = new Servicio(repo.nextId(), nombre, dur, precio);
        repo.guardar(s);
        System.out.println("Servicio creado: " + s);
    }

    private static void reservarCita(RepositorioMemoria repo, negocio.Agenda agenda) {
        if (repo.clientes().isEmpty() || repo.barberos().isEmpty() || repo.servicios().isEmpty()) {
            System.out.println("Faltan datos: crea al menos un cliente, un barbero y un servicio.");
            return;
        }

        System.out.println("Clientes:");
        listarClientes(repo.clientes());
        System.out.print("Id cliente: ");
        int idCliente = leerEntero();
        Cliente cli = repo.buscarCliente(idCliente);
        if (cli == null) { System.out.println("Cliente no encontrado."); return; }

        System.out.println("Barberos:");
        listarBarberos(repo.barberos());
        System.out.print("Id barbero: ");
        int idBarbero = leerEntero();
        Barbero bar = repo.buscarBarbero(idBarbero);
        if (bar == null) { System.out.println("Barbero no encontrado."); return; }

        System.out.println("Servicios:");
        listarServicios(repo.servicios());
        System.out.print("Id servicio: ");
        int idServicio = leerEntero();
        Servicio ser = repo.buscarServicio(idServicio);
        if (ser == null) { System.out.println("Servicio no encontrado."); return; }

        System.out.print("Fecha (YYYY-MM-DD): ");
        LocalDate dia = LocalDate.parse(in.nextLine());
        System.out.print("Hora inicio (HH:MM): ");
        LocalTime hora = LocalTime.parse(in.nextLine());

        LocalDateTime inicio = LocalDateTime.of(dia, hora);
        Cita nueva = new Cita(repo.nextId(), bar, cli, ser, inicio);

        try {
            agenda.reservar(nueva);
            System.out.println("Cita reservada: " + nueva);
        } catch (IllegalArgumentException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private static void verCitasPorDia(RepositorioMemoria repo, negocio.Agenda agenda) {
        if (repo.barberos().isEmpty()) {
            System.out.println("No hay barberos.");
            return;
        }
        System.out.println("Barberos:");
        listarBarberos(repo.barberos());
        System.out.print("Id barbero: ");
        int idBarbero = leerEntero();
        Barbero b = repo.buscarBarbero(idBarbero);
        if (b == null) { System.out.println("Barbero no encontrado."); return; }

        System.out.print("Fecha (YYYY-MM-DD): ");
        LocalDate dia = LocalDate.parse(in.nextLine());

        List<Cita> citas = agenda.citasDe(b, dia);
        if (citas.isEmpty()) {
            System.out.println("No hay citas para ese barbero en ese dia.");
            return;
        }
        for (Cita c : citas) {
            System.out.println(c);
        }
    }

    private static int leerEntero() {
        while (true) {
            try {
                String s = in.nextLine();
                return Integer.parseInt(s.trim());
            } catch (Exception e) {
                System.out.print("Introduce un numero valido: ");
            }
        }
    }

    private static double leerDouble() {
        while (true) {
            try {
                String s = in.nextLine();
                return Double.parseDouble(s.trim());
            } catch (Exception e) {
                System.out.print("Introduce un numero valido (usa punto): ");
            }
        }
    }
}