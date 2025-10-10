/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app;

/**
 *
 * @author migue
 */

import dominio.*;
import negocio.Agenda;
import repositorio.RepositorioMemoria;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;



public class BarberiaSwingApp extends JFrame{
    
    //Núcleo de la App
    
    //Repositorio en memoria (ir registrando el dominio)
    private RepositorioMemoria repo;
    //Negocio: las reglas del servicio
    private Agenda agenda;
    
    //Area de texto para mostrar resultados
    private JTextArea output;
    
    public BarberiaSwingApp() {
        super("Barbería (Swing MVP)");
        this.repo = new RepositorioMemoria();
        this.agenda = new Agenda(repo);
        seedBasico();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        // ===== Header (título + subtítulo) =====
        JLabel title = new JLabel("Barbería Dinámica");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));
        JLabel subtitle = new JLabel("Citas • Clientes • Barberos • Servicios");
        subtitle.setFont(subtitle.getFont().deriveFont(Font.PLAIN, 13f));
        subtitle.setForeground(new Color(90, 90, 90));

        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(BorderFactory.createEmptyBorder(16, 16, 12, 16));
        header.add(title);
        header.add(Box.createVerticalStrut(4));
        header.add(subtitle);

        // ===== Sidebar (vertical) =====
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(16, 12, 16, 12));

        JButton btnListarClientes = createSideButton("👥  Listar clientes");
        JButton btnCrearCliente = createSideButton("➕  Crear cliente");
        sidebar.add(btnListarClientes);
        sidebar.add(btnCrearCliente);
        sidebar.add(Box.createVerticalStrut(10));

        JButton btnListarBarb = createSideButton("💈  Listar barberos");
        JButton btnCrearBarb = createSideButton("➕  Crear barbero");
        sidebar.add(btnListarBarb);
        sidebar.add(btnCrearBarb);
        sidebar.add(Box.createVerticalStrut(10));

        JButton btnListarServ = createSideButton("🧾  Listar servicios");
        JButton btnCrearServ = createSideButton("➕  Crear servicio");
        sidebar.add(btnListarServ);
        sidebar.add(btnCrearServ);
        sidebar.add(Box.createVerticalStrut(10));

        JButton btnReservar = createSideButton("📅  Reservar cita");
        JButton btnVerCitas = createSideButton("🔎  Ver citas por día");
        sidebar.add(btnReservar);
        sidebar.add(btnVerCitas);
        sidebar.add(Box.createVerticalGlue()); // empuja hacia arriba

        // ===== Contenido (área de resultados) =====
        output = new JTextArea();
        output.setEditable(false);
        output.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        JScrollPane scroll = new JScrollPane(output);
        scroll.setBorder(BorderFactory.createTitledBorder("Resultados"));

        // ===== Estructura global =====
        JPanel root = new JPanel(new BorderLayout());
        root.add(header, BorderLayout.NORTH);
        root.add(scroll, BorderLayout.CENTER);
        root.add(sidebar, BorderLayout.WEST);
        root.setBorder(BorderFactory.createEmptyBorder(0, 8, 8, 8));
        setContentPane(root);

        // ===== Listeners (igual que antes) =====
        btnListarClientes.addActionListener(e -> listarClientes());
        btnCrearCliente.addActionListener(e -> crearCliente());
        btnListarBarb.addActionListener(e -> listarBarberos());
        btnCrearBarb.addActionListener(e -> crearBarbero());
        btnListarServ.addActionListener(e -> listarServicios());
        btnCrearServ.addActionListener(e -> crearServicio());
        btnReservar.addActionListener(e -> reservarCita());
        btnVerCitas.addActionListener(e -> verCitasPorDia());
    }

    /**
     * Helper para dar estilo consistente a los botones de la barra lateral.
     */
    private JButton createSideButton(String text) {
        JButton b = new JButton(text);
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        b.setFocusPainted(false);
        b.setFont(b.getFont().deriveFont(Font.PLAIN, 14f));
        b.setMargin(new Insets(10, 12, 10, 12));
        b.setMaximumSize(new Dimension(220, 40)); // ancho fijo, alto cómodo
        return b;
    }

    
    private void verCitasPorDia() {
        if (repo.barberos().isEmpty()) {
            msg("No hay barberos.");
            return;
        }
        listarBarberos();
        Integer idBar = leerEnteroDialog("ID del barbero:");
        if (idBar == null) {
            return;
        }
        Barbero b = repo.buscarBarbero(idBar);
        if (b == null) {
            msg("Barbero no encontrado.");
            return;
        }

        String sFecha = JOptionPane.showInputDialog(this, "Fecha (YYYY-MM-DD):");
        if (sFecha == null || sFecha.trim().isEmpty()) {
            return;
        }

        try {
            LocalDate dia = LocalDate.parse(sFecha.trim());
            List<Cita> citas = agenda.citasDe(b, dia);
            StringBuilder sb = new StringBuilder("=== CITAS (" + b.getNombre() + " / " + dia + ") ===\n");
            if (citas.isEmpty()) {
                sb.append("No hay citas.\n");
            }
            for (Cita c : citas) {
                sb.append(c).append('\n');
            }
            output.setText(sb.toString());
        } catch (Exception ex) {
            msg("Error: " + ex.getMessage());
        }
    }
    
    private void reservarCita() {
        // Comprobación previa: necesitamos tener al menos un cliente, barbero y servicio creados.
        if (repo.clientes().isEmpty() || repo.barberos().isEmpty() || repo.servicios().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Crea al menos un cliente, un barbero y un servicio.");
            return;
        }

        // Mostramos listas para que el usuario vea los IDs disponibles
        listarClientes();
        Integer idCliente = leerEnteroDialog("ID del cliente:");
        if (idCliente == null) {
            return;
        }
        Cliente cli = repo.buscarCliente(idCliente);
        if (cli == null) {
            msg("Cliente no encontrado.");
            return;
        }

        listarBarberos();
        Integer idBar = leerEnteroDialog("ID del barbero:");
        if (idBar == null) {
            return;
        }
        Barbero bar = repo.buscarBarbero(idBar);
        if (bar == null) {
            msg("Barbero no encontrado.");
            return;
        }

        listarServicios();
        Integer idSer = leerEnteroDialog("ID del servicio:");
        if (idSer == null) {
            return;
        }
        Servicio ser = repo.buscarServicio(idSer);
        if (ser == null) {
            msg("Servicio no encontrado.");
            return;
        }

        // Fechas/horas: pedimos cadenas y parseamos a tipos de java.time
        String sFecha = JOptionPane.showInputDialog(this, "Fecha (YYYY-MM-DD):");
        if (sFecha == null || sFecha.trim().isEmpty()) {
            return;
        }
        String sHora = JOptionPane.showInputDialog(this, "Hora inicio (HH:MM):");
        if (sHora == null || sHora.trim().isEmpty()) {
            return;
        }

        try {
            LocalDate dia = LocalDate.parse(sFecha.trim());     // Ej: "2025-09-30"
            LocalTime hora = LocalTime.parse(sHora.trim());     // Ej: "10:30"
            LocalDateTime ini = LocalDateTime.of(dia, hora);    // Combina fecha + hora

            Cita nueva = new Cita(repo.nextId(), bar, cli, ser, ini);
            agenda.reservar(nueva);                             // Valida solape y guarda
            output.setText("Cita reservada:\n" + nueva);
        } catch (Exception ex) {
            // Puede fallar por formato de fecha/hora o por solape (IllegalArgumentException)
            msg("Error: " + ex.getMessage());
        }
    }
    
    
    private void crearServicio(){
        String nombre=JOptionPane.showInputDialog(this, "Nombre del servicio: ");
        if (nombre==null || nombre.trim().isEmpty())return;
        
        Integer dur=leerEnteroDialog("Duracion minima en min: ");
        if(dur==null)return;
        
        Double precio=leerDoubleDialog("Precio del Servicio: ");
        if(precio==null)return;
        
        Servicio s=new Servicio(repo.nextId(), nombre, dur, precio);
        repo.guardar(s);
        output.setText("Servicio Creado: \n"+s);
    }
    
    private void listarServicios(){
        StringBuilder sb=new StringBuilder("-----------------SERVICIOS-----------------\n");
        List<Servicio> ls=repo.servicios();
        if(ls.isEmpty())sb.append("No hay servicios disponibles. \n");
        for (Servicio s: ls){
            sb.append(s).append("\n");
        }
        
        output.setText(sb.toString());
    }
    
    private void crearBarbero(){
        String nombre=JOptionPane.showInputDialog(this, "Nombre del Barbero: ");
        if (nombre== null || nombre.trim().isEmpty())return ;
        
        Barbero b= new Barbero(repo.nextId(), nombre.trim());
        repo.guardar(b);
        output.setText("Barbero creado: \n"+b);
    }
    
    private void listarBarberos(){
        StringBuilder sb= new StringBuilder("-----------------BARBEROS-----------------\n");
        List<Barbero> ls=repo.barberos();
        if(ls.isEmpty())sb.append("No hay barberos.\n");
        for (Barbero b: ls){
            sb.append(b).append("\n");
        }
        output.setText(sb.toString());
    }
    
    private void crearCliente(){
        String nombre=JOptionPane.showInputDialog(this, "Nombre del Cliente:");
        if(nombre==null || nombre.trim().isEmpty())return;
        String tel=JOptionPane.showInputDialog(this, "Telefono: ");
        if (tel==null || tel.trim().isEmpty())return;
        
        Cliente c=new Cliente(repo.nextId(), nombre.trim(), tel.trim());
        repo.guardar(c);
        output.setText("Cliente creado: \n " + c);
    }
    
    private void listarClientes(){
        StringBuilder sb=new StringBuilder("-----------------CLIENTES-----------------\n");
        List<Cliente> ls=repo.clientes();
        if(ls.isEmpty()){
            sb.append("No hay clientes. \n");
        }
        for (Cliente c: ls){
            sb.append(c).append("\n");
            
        }
        output.setText(sb.toString());
    }
    
    
    private void seedBasico() {
        Servicio s1 = new Servicio(repo.nextId(), "Corte Clásico", 30, 14.0);
        Servicio s2 = new Servicio(repo.nextId(), "Arreglo de barba", 20, 10.0);
        repo.guardar(s1);
        repo.guardar(s2);

        Barbero b1 = new Barbero(repo.nextId(), "Miguel");
        Barbero b2 = new Barbero(repo.nextId(), "Félix");
        repo.guardar(b1);
        repo.guardar(b2);
    }
    private Integer leerEnteroDialog(String prompt) {
        while (true) {
            String s = JOptionPane.showInputDialog(this, prompt);
            if (s == null) return null; // Cancelado
            s = s.trim();
            if (s.isEmpty()) return null;
            try {
                return Integer.parseInt(s);
            } catch (Exception e) {
                msg("Introduce un número válido.");
            }
        }
    }

    /** Pide un número decimal por diálogo. Devuelve null si se cancela o está vacío. */
    private Double leerDoubleDialog(String prompt) {
        while (true) {
            String s = JOptionPane.showInputDialog(this, prompt);
            if (s == null) return null; // Cancelado
            s = s.trim();
            if (s.isEmpty()) return null;
            try {
                return Double.parseDouble(s);
            } catch (Exception e) {
                msg("Introduce un número válido (usa punto).");
            }
        }
    }
    
    private void msg(String texto) {
        JOptionPane.showMessageDialog(this, texto);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
                UIManager.put("control", new Color(45, 45, 45));       // fondo de paneles
                UIManager.put("info", new Color(60, 63, 65));          // tooltips
                UIManager.put("nimbusBase", new Color(18, 30, 49));    // base de componentes
                UIManager.put("nimbusBlueGrey", new Color(48, 60, 80));// bordes
                UIManager.put("nimbusFocus", new Color(115, 164, 209));// enfoque
                UIManager.put("text", new Color(230, 230, 230));       // texto general
                UIManager.put("controlText", Color.WHITE);             // texto de botones
                UIManager.put("nimbusLightBackground", new Color(60, 63, 65)); // fondos inputs
                UIManager.put("infoText", Color.WHITE);
                UIManager.put("menuText", Color.WHITE);
                UIManager.put("OptionPane.messageForeground", Color.WHITE);

            } catch (Exception ignored) {
            }
            new BarberiaSwingApp().setVisible(true);
        });
    }

    
}
