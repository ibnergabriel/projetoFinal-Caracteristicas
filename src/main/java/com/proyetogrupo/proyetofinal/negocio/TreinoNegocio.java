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
    void finalizarTreino(int id) throws SQLException;
    Optional<Treino> buscarPorId(int id) throws SQLException;
    List<Treino> listarPorAluno(int idAluno) throws SQLException;
    List<Treino> listarPorProfessor(int idProfessor) throws SQLException;
}