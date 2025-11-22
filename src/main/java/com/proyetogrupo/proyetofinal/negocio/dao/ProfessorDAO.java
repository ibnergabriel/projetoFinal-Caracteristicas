/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.proyetogrupo.proyetofinal.negocio.dao;

import com.proyetogrupo.proyetofinal.negocio.ProfessorNegocio;
import java.util.List;

/**
 *
 * @author ibner
 */
public interface ProfessorDAO {
    
    void insert(ProfessorNegocio obj);
    void update(ProfessorNegocio obj);
    void deleteById(Integer id);
    ProfessorNegocio findById(Integer id);
    List<ProfessorNegocio> findAll(); 
    
}
