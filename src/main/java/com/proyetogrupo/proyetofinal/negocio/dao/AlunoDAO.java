package com.proyetogrupo.proyetofinal.negocio.dao;

import java.sql.*;
import java.util.List;
import java.util.Optional;

import com.proyetogrupo.proyetofinal.negocio.model.Aluno;

public interface AlunoDAO {

    Optional<Aluno> findById(String id) throws SQLException;   // CPF

    void save(Aluno a) throws SQLException;

    void update(Aluno a) throws SQLException;

    void deleteById(String id) throws SQLException;             // CPF

    boolean existsById(String id) throws SQLException;          // CPF

    List<Aluno> listAll() throws SQLException;
}
