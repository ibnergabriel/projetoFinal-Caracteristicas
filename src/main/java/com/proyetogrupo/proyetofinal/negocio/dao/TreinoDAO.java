package com.proyetogrupo.proyetofinal.negocio.dao;

import com.proyetogrupo.proyetofinal.negocio.model.Treino;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface TreinoDAO {
    Optional<Treino> findById(Integer id) throws SQLException;
    Treino saveAndReturn(Treino treino) throws SQLException;
    void update(Treino treino) throws SQLException;
    void deleteById(Integer id) throws SQLException;
    boolean existsActiveByAluno(Integer idAluno) throws SQLException;
    boolean existsActiveByProfessor(Integer idProfessor) throws SQLException;
    List<Treino> listByAluno(int idAluno) throws SQLException;
    List<Treino> listByProfessor(int idProfessor) throws SQLException;

}