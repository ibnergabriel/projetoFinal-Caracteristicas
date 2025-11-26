/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyetogrupo.proyetofinal.negocio;

import java.sql.SQLException;
import java.util.Optional;

import com.proyetogrupo.proyetofinal.negocio.model.Professor;

public interface ProfessorNegocio {
    Optional<Professor> buscarPorId(Integer id) throws SQLException;
    Professor autenticar(String usuario, String senha) throws SQLException;
    void cadastrarProfessor(Professor professor) throws SQLException;
    void atualizarProfessor(Professor professor) throws SQLException;
    void removerProfessor(Integer id) throws SQLException;
}