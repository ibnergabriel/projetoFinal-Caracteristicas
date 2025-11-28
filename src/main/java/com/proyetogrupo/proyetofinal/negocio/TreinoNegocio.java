/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyetogrupo.proyetofinal.negocio;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.proyetogrupo.proyetofinal.negocio.model.Treino;

public interface TreinoNegocio {
    Treino criarTreino(Treino treino) throws SQLException;
    Treino atualizarTreino(Treino treino) throws SQLException;

    // id do treino continua inteiro
    void finalizarTreino(int id) throws SQLException;
    Optional<Treino> buscarPorId(int id) throws SQLException;

    // aqui é o CPF do aluno (String)
    List<Treino> listarPorAluno(String cpfAluno) throws SQLException;

    // professor continua com id numérico
    List<Treino> listarPorProfessor(int idProfessor) throws SQLException;
}