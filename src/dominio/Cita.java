/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dominio;

/**
 *
 * @author migue
 */
import dominio.Cliente;
import dominio.Servicio;
import dominio.Barbero;
import java.time.LocalDateTime;

public class Cita {
    
    private int id;
    private Barbero barbero;
    private Cliente cliente;
    private Servicio servicio;
    private LocalDateTime inicio;
    private LocalDateTime fin;

    public Cita(int id, Barbero barbero, Cliente cliente, Servicio servicio, LocalDateTime inicio) {
        this.id = id;
        this.barbero = barbero;
        this.cliente = cliente;
        this.servicio = servicio;
        this.inicio = inicio;
        this.fin=inicio.plusMinutes(servicio.getDuracionMin());
    }
    
    public int getId(){return id;}
    public Barbero getBarbero(){return barbero;}
    public Cliente getCliente(){return cliente;}
    public Servicio getServicio(){return servicio;}
    public LocalDateTime getInicio(){return inicio;}
    public LocalDateTime getFin(){return fin;}
    
    @Override
    public String toString(){
        return String.format("#%d | %s con %s | %s %s-%s",
                id,
                cliente.getNombre(),
                barbero.getNombre(),
                servicio.getNombre(),
                inicio.toLocalDate(), inicio.toLocalTime()+ "->"+fin.toLocalTime());
                
    }
    
    
    
}
