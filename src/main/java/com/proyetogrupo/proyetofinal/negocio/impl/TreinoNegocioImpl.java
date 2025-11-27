package com.proyetogrupo.proyetofinal.negocio.impl;

import com.proyetogrupo.proyetofinal.negocio.TreinoNegocio;
import com.proyetogrupo.proyetofinal.negocio.dao.AlunoDAO;      // Interface
import com.proyetogrupo.proyetofinal.negocio.dao.ProfessorDAO;  // Interface
import com.proyetogrupo.proyetofinal.negocio.dao.TreinoDAO;     // Interface
import com.proyetogrupo.proyetofinal.negocio.exceptions.BusinessException;
import com.proyetogrupo.proyetofinal.negocio.model.Treino;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class TreinoNegocioImpl implements TreinoNegocio, AutoCloseable {

    private final TreinoDAO dao;
    private final AlunoDAO alunoDAO;
    private final ProfessorDAO professorDAO;
    private final Connection connection;

    public TreinoNegocioImpl(TreinoDAO dao, AlunoDAO alunoDAO, ProfessorDAO professorDAO, Connection connection) {
        this.dao = dao;
        this.alunoDAO = alunoDAO;
        this.professorDAO = professorDAO;
        this.connection = connection;
    }

    @Override
    public Treino criarTreino(Treino treino) throws SQLException {
        try {
            if (treino == null) throw new BusinessException("Treino inválido.");
            
            if (treino.getIdAluno() == null || !alunoDAO.existsById(treino.getIdAluno())) 
                throw new BusinessException("Aluno não encontrado.");
            
            if (treino.getIdProfessor() == null || !professorDAO.existsById(treino.getIdProfessor())) 
                throw new BusinessException("Professor não encontrado.");

            LocalDate inicio = treino.getDataInicio();
            LocalDate fim = treino.getDataFim();

            if (inicio == null) throw new BusinessException("Data de início é obrigatória.");
            if (fim != null && fim.isBefore(inicio)) throw new BusinessException("Data de fim anterior ao início.");

            if (dao.existsActiveByAluno(treino.getIdAluno())) 
                throw new BusinessException("Aluno já possui treino ativo.");

            if (treino.getStatus() == null) treino.setStatus("ATIVO");

            connection.setAutoCommit(false);
            Treino novo = dao.saveAndReturn(treino);
            connection.commit();
            return novo;

        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    @Override
    public Treino atualizarTreino(Treino treino) throws SQLException {
        try {
            if (treino == null || treino.getIdTreino() == null) throw new BusinessException("Treino inválido.");
            
            dao.findById(treino.getIdTreino())
               .orElseThrow(() -> new BusinessException("Treino não encontrado."));

            if (treino.getDataInicio() != null && treino.getDataFim() != null && treino.getDataFim().isBefore(treino.getDataInicio()))
                throw new BusinessException("Data de fim anterior ao início.");

            connection.setAutoCommit(false);
            dao.update(treino);
            connection.commit();
            return treino;

        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    @Override
    public void finalizarTreino(int id) throws SQLException {
        try {
            Treino t = dao.findById(id)
                          .orElseThrow(() -> new BusinessException("Treino não encontrado."));
            
            t.setStatus("CONCLUIDO");
            
            connection.setAutoCommit(false);
            dao.update(t);
            connection.commit();

        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    @Override public Optional<Treino> buscarPorId(int id) throws SQLException { return dao.findById(id); }
    @Override public List<Treino> listarPorAluno(int idAluno) throws SQLException { return dao.listByAluno(idAluno); }
    @Override public List<Treino> listarPorProfessor(int idProfessor) throws SQLException { return dao.listByProfessor(idProfessor); }
    
    @Override
    public void close() throws Exception {
        if (connection != null && !connection.isClosed()) connection.close();
    }
}