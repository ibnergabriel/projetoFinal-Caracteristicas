/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.proyetogrupo.proyetofinal.negocio.dao;

import com.proyetogrupo.proyetofinal.negocio.PagamentoNegocio;
import java.util.List;

/**
 *
 * @author ibner
 */
public interface PagamentoDAO {
    
    void insert(PagamentoNegocio obj);
    void update(PagamentoNegocio obj);
    void deleteById(Integer id);
    PagamentoNegocio findById(Integer id);
    List<PagamentoNegocio> findAll();
    
    // Retorna o histórico de pagamentos de um único aluno
    List<PagamentoNegocio> findByAlunoId(Integer idAluno);
    
}
