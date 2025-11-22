/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.proyetogrupo.proyetofinal.negocio.dao;

import com.proyetogrupo.proyetofinal.negocio.AlunoNegocio;
import java.util.List;

/**
 *
 * @author ibner
 */
public interface AlunoDAO {
    
    void insert(AlunoNegocio obj); //Insere no banco de dados
    void update(AlunoNegocio obj);
    void deleteById(Integer id);
    AlunoNegocio findById(Integer id);
    List<AlunoNegocio> findAll(); 
}
