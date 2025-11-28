package com.proyetogrupo.proyetofinal.negocio.dao;

import com.proyetogrupo.proyetofinal.negocio.model.Pagamento;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface PagamentoDAO {
    Optional<Pagamento> findById(Integer id) throws SQLException;
    void save(Pagamento p) throws SQLException;
    List<Pagamento> listByAluno(String cpfAluno) throws SQLException;   // ajustar
    boolean existsByAluno(String cpfAluno) throws SQLException;         // ajustar
    void deleteById(Integer id) throws SQLException;
    
}