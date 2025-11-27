package com.proyetogrupo.proyetofinal.negocio.dao;

import java.sql.SQLException;
import java.util.Optional;

import com.proyetogrupo.proyetofinal.negocio.model.Professor;

public interface ProfessorDAO {
    
    Optional<Professor> findById(Integer id) throws SQLException;
    Professor findByUsuario(String usuario) throws SQLException;
    boolean existsById(Integer id) throws SQLException;
    void save(Professor p) throws SQLException;
    void update(Professor p) throws SQLException;
    void deleteById(Integer id) throws SQLException;
    
}