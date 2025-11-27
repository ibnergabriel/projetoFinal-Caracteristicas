package com.proyetogrupo.proyetofinal.negocio.dao;

import com.proyetogrupo.proyetofinal.negocio.model.Pagamento;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface PagamentoDAO {
    Optional<Pagamento> findById(Integer id) throws SQLException;
    void save(Pagamento p) throws SQLException;
    List<Pagamento> listByAluno(int idAluno) throws SQLException;
    boolean existsByAluno(int idAluno) throws SQLException;
    void deleteById(Integer id) throws SQLException;
    
}