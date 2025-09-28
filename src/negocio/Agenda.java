/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio;

/**
 *
 * @author migue
 */

import dominio.*;
import repositorio.RepositorioMemoria;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Agenda {
    
    private RepositorioMemoria repo;
    
    public Agenda(RepositorioMemoria repo){
        this.repo=repo;
    }
    
    //Devolver todas las citas de un barbero en un día (sin orden de llegada, solo por como se han ido reservando)
    public List<Cita> citasDe(Barbero b, LocalDate dia){
        List<Cita> resultado=new ArrayList<>();
        
        for (Cita c: repo.citas()){
            if(c.getBarbero().getId()==b.getId() && c.getInicio().toLocalDate().equals(dia)){
                resultado.add(c);
            }
            
        }
        return resultado;
    }
    
    //Comprobar si una cita se solapa con las ya existentes de un barbero
    
    public boolean haySolape (Cita nueva){
        
        for(Cita c : repo.citas()){
            if (c.getBarbero().getId() == nueva.getBarbero().getId()){
                boolean solapan= c.getInicio().isBefore(nueva.getFin()) && nueva.getInicio().isBefore(c.getFin());
                if (solapan)return true;
            }
        }
        return false;
    }
    
    public void reservar(Cita c){
        if(haySolape(c)){
            throw new IllegalArgumentException("El barbero ya tiene una cita en este horario");
        }
        repo.guardar(c);
        
    }
    
    
}
