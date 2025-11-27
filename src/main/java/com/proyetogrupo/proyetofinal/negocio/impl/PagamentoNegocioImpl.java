package com.proyetogrupo.proyetofinal.negocio.impl;

import com.proyetogrupo.proyetofinal.negocio.PagamentoNegocio;
import com.proyetogrupo.proyetofinal.negocio.dao.AlunoDAO;      // Interface
import com.proyetogrupo.proyetofinal.negocio.dao.PagamentoDAO;  // Interface
import com.proyetogrupo.proyetofinal.negocio.exceptions.BusinessException;
import com.proyetogrupo.proyetofinal.negocio.model.Pagamento;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class PagamentoNegocioImpl implements PagamentoNegocio, AutoCloseable {

    private final PagamentoDAO dao;
    private final AlunoDAO alunoDAO;
    private final Connection connection;

    public PagamentoNegocioImpl(PagamentoDAO dao, AlunoDAO alunoDAO, Connection connection) {
        this.dao = Objects.requireNonNull(dao);
        this.alunoDAO = Objects.requireNonNull(alunoDAO);
        this.connection = Objects.requireNonNull(connection);
    }

    @Override
    public void registrarPagamento(Pagamento pagamento) throws SQLException {
        try {
            if (pagamento == null) throw new BusinessException("Pagamento inválido.");
            
            if (pagamento.getIdAluno() == null || !alunoDAO.existsById(pagamento.getIdAluno()))
                throw new BusinessException("Aluno não encontrado.");
            
            if (pagamento.getValor() == null || pagamento.getValor() <= 0) 
                throw new BusinessException("Valor inválido.");

            connection.setAutoCommit(false);
            dao.save(pagamento);
            connection.commit();

        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    @Override
    public List<Pagamento> listarPorAluno(int idAluno) throws SQLException {
        return dao.listByAluno(idAluno);
    }
    
    @Override
    public void close() throws Exception {
        if (connection != null && !connection.isClosed()) connection.close();
    }
}