/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.proyetogrupo.proyetofinal.negocio.dao;

import com.proyetogrupo.proyetofinal.negocio.TreinoNegocio;
import java.util.List;

/**
 *
 * @author ibner
 */
public interface TreinoDAO {
    
    void insert(TreinoNegocio obj);
    void update(TreinoNegocio obj);
    void deleteById(Integer id);
    TreinoNegocio findById(Integer id);
    List<TreinoNegocio> findAll();
    
    // Retorna todos os treinos de um aluno específico (usando idAluno)
    List<TreinoNegocio> findByAlunoId(Integer idAluno);

    // Retorna todos os treinos montados por um professor específico (usando idProfessor)
    List<TreinoNegocio> findByProfessorId(Integer idProfessor);
}
