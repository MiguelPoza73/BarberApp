/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repositorio;

/**
 *
 * @author migue
 */

import dominio.*;
import java.util.*;

public class RepositorioMemoria {
    
    private List<Cliente> clientes= new ArrayList<>();
    private List<Barbero> barberos=new ArrayList<>();
    private List<Servicio>servicios= new ArrayList<>();
    private List<Cita>citas=new ArrayList<>();
    
    //contador
    private int genId=1;
    
    public int nextId(){
        return genId++;
    }
    
    public List<Cliente> clientes(){return clientes;}
    public List<Barbero> barberos(){return barberos;}
    public List<Servicio> servicios(){return servicios;}
    public List<Cita> citas(){return citas;}
    
    public void guardar(Barbero b){barberos.add(b);}
    public void guardar(Servicio s){servicios.add(s);}
    public void guardar(Cliente c){clientes.add(c);}
    public void guardar(Cita c){citas.add(c);}
    
    
    
    public Cliente buscarCliente (int id){
        for(Cliente c: clientes){
            if (c.getId()==id){
                return c;
            }
        }
        return null;
    }
    
    
    public Barbero buscarBarbero(int id){
        for (Barbero b: barberos){
            if(b.getId()==id){
                return b;
            }
        }
        return null;
        
    }
    
    public Servicio buscarServicio(int id){
        for(Servicio s: servicios){
            if(s.getId()==id){return s;}
        }
        return null;
    }
    
    public Cita buscarCita(int id){
        for(Cita c: citas){
            if(c.getId()==id){return c;}
        }
        return null;
    }
    
    
    
  
}
