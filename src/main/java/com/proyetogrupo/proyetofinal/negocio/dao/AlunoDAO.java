package com.proyetogrupo.proyetofinal.negocio.dao;

import java.sql.*;
import java.util.List;
import java.util.Optional;

import com.proyetogrupo.proyetofinal.negocio.model.Aluno;

public interface AlunoDAO {
    Optional<Aluno> findById(Integer id) throws SQLException;
    void save(Aluno a) throws SQLException;
    void update(Aluno a) throws SQLException;
    void deleteById(Integer id) throws SQLException;
    boolean existsById(Integer id) throws SQLException;
    List<Aluno> listAll() throws SQLException;
}