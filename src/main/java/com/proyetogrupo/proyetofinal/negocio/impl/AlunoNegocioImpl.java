package com.proyetogrupo.proyetofinal.negocio.impl;

import com.proyetogrupo.proyetofinal.negocio.AlunoNegocio;
import com.proyetogrupo.proyetofinal.negocio.dao.AlunoDAO;
import com.proyetogrupo.proyetofinal.negocio.dao.PagamentoDAO;
import com.proyetogrupo.proyetofinal.negocio.dao.TreinoDAO;
import com.proyetogrupo.proyetofinal.negocio.exceptions.BusinessException;
import com.proyetogrupo.proyetofinal.negocio.model.Aluno;
import com.proyetogrupo.proyetofinal.negocio.util.ValidationUtil;
import java.sql.*;
import java.util.Optional;

/**
 *
 * @author ibner
 */
public class AlunoNegocioImpl implements AlunoNegocio, AutoCloseable {

    private final AlunoDAO dao;
    private final TreinoDAO treinoDAO;
    private final PagamentoDAO pagamentoDAO;
    private final Connection connection;

    public AlunoNegocioImpl(AlunoDAO dao, TreinoDAO treinoDAO, PagamentoDAO pagamentoDAO, Connection connection) {
        this.dao = dao;
        this.treinoDAO = treinoDAO;
        this.pagamentoDAO = pagamentoDAO;
        this.connection = connection;
    }

    @Override
    public Optional<Aluno> buscarPorId(Integer id) throws SQLException {
        return dao.findById(id);
    }

    @Override
    public void cadastrarAluno(Aluno aluno) throws SQLException {
        try {
            // Validações
            if (aluno == null) throw new BusinessException("Aluno inválido.");
            if (ValidationUtil.isBlank(aluno.getNome())) throw new BusinessException("Nome obrigatório.");
            if (aluno.getIdade() == null || aluno.getIdade() < 14) throw new BusinessException("Idade mínima: 14 anos.");
            if (!ValidationUtil.isBlank(aluno.getEmail()) && !ValidationUtil.isValidEmail(aluno.getEmail())) 
                throw new BusinessException("Email inválido.");

            // Transação
            connection.setAutoCommit(false);
            dao.save(aluno);
            connection.commit();

        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    @Override
    public void atualizarAluno(Aluno aluno) throws SQLException {
        try {
            if (aluno == null || aluno.getIdAluno() == null) throw new BusinessException("Aluno inválido.");
            
            dao.findById(aluno.getIdAluno())
               .orElseThrow(() -> new BusinessException("Aluno não encontrado."));

            connection.setAutoCommit(false);
            dao.update(aluno);
            connection.commit();

        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    @Override
    public void removerAluno(Integer id) throws SQLException {
        try {
            if (!dao.existsById(id)) throw new BusinessException("Aluno não encontrado.");
            if (treinoDAO.existsActiveByAluno(id)) throw new BusinessException("Aluno possui treino ativo.");
            if (pagamentoDAO.existsByAluno(id)) throw new BusinessException("Aluno possui pagamentos registrados.");

            connection.setAutoCommit(false);
            dao.deleteById(id);
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
        if (connection != null && !connection.isClosed()) connection.close();
    }
}
