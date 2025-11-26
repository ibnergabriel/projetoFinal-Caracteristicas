/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyetogrupo.proyetofinal.negocio;

import java.sql.SQLException;
import java.util.Optional;

import com.proyetogrupo.proyetofinal.negocio.model.Aluno;

public interface AlunoNegocio {
    Optional<Aluno> buscarPorId(Integer id) throws SQLException;
    void cadastrarAluno(Aluno aluno) throws SQLException;
    void atualizarAluno(Aluno aluno) throws SQLException;
    void removerAluno(Integer id) throws SQLException;
}