package com.proyetogrupo.proyetofinal.negocio.impl;

import com.proyetogrupo.proyetofinal.negocio.PagamentoNegocio;
import com.proyetogrupo.proyetofinal.negocio.dao.AlunoDAO;
import com.proyetogrupo.proyetofinal.negocio.dao.PagamentoDAO;
import com.proyetogrupo.proyetofinal.negocio.exceptions.BusinessException;
import com.proyetogrupo.proyetofinal.negocio.model.Pagamento;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class PagamentoNegocioImpl implements PagamentoNegocio, AutoCloseable {

    private final PagamentoDAO dao;
    private final AlunoDAO alunoDAO;
    private final Connection connection;

    public PagamentoNegocioImpl(PagamentoDAO dao,
                                AlunoDAO alunoDAO,
                                Connection connection) {
        this.dao = dao;
        this.alunoDAO = alunoDAO;
        this.connection = connection;
    }

    @Override
    public void registrarPagamento(Pagamento pagamento) throws SQLException, BusinessException {
        if (pagamento == null) throw new BusinessException("Pagamento inválido.");
        if (pagamento.getIdAluno() == null || pagamento.getIdAluno().isBlank())
            throw new BusinessException("CPF do aluno é obrigatório no pagamento.");
        if (pagamento.getValor() == null || pagamento.getValor().doubleValue() <= 0)
            throw new BusinessException("Valor do pagamento deve ser maior que zero.");

        // Verifica se aluno existe
        if (!alunoDAO.existsById(pagamento.getIdAluno())) {
            throw new BusinessException("Aluno não encontrado para o CPF informado no pagamento.");
        }

        if (pagamento.getDataPagamento() == null) {
            pagamento.setDataPagamento(LocalDate.now());
        }

        try {
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
    public List<Pagamento> listarPorAluno(String cpfAluno) throws SQLException {
        Objects.requireNonNull(cpfAluno, "CPF do aluno não pode ser nulo.");
        return dao.listByAluno(cpfAluno);
    }


    public void excluirPagamento(Integer idPagamento) throws SQLException {
        try {
            connection.setAutoCommit(false);
            dao.deleteById(idPagamento);
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    @Override
    public void close() throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
