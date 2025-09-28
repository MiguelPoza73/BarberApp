/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dominio;

/**
 *
 * @author migue
 */
public class Servicio {
    
    private int id;
    private String nombre;
    private int duracionMin;
    private double precio;
    
    public Servicio(int id, String nombre, int duracionMin, double precio){
        this.id=id;
        this.nombre=nombre;
        this.duracionMin=duracionMin;
        this.precio=precio;
    }
    
    public String getNombre(){
        return nombre;
    }
    
    public int getDuracionMin(){
        return duracionMin;
    }
    public double getPrecio(){
        return precio;
    }
    
    public int getId(){
        return id;
    }
    
    @Override
    public String toString() {
        return id + " - " + nombre + " (" + duracionMin + "min, " + precio + "€)";
    }
    
}
