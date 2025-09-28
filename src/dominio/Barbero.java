/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dominio;

/**
 *
 * @author migue
 */
public class Barbero {
    
    private int id;
    private String nombre;
    
    public Barbero(int id, String nombre){
        this.id=id;
        this.nombre=nombre;
         
        
    }
    
    public int getId(){return id;}
    public String getNombre(){return nombre;}
    
    @Override
    public String toString() {
        return id + " - " + nombre ;
    }
}
