package com.proyetogrupo.proyetofinal.negocio.impl;

import com.proyetogrupo.proyetofinal.negocio.TreinoNegocio;
import com.proyetogrupo.proyetofinal.negocio.dao.AlunoDAO;
import com.proyetogrupo.proyetofinal.negocio.dao.ProfessorDAO;
import com.proyetogrupo.proyetofinal.negocio.dao.TreinoDAO;
import com.proyetogrupo.proyetofinal.negocio.exceptions.BusinessException;
import com.proyetogrupo.proyetofinal.negocio.model.Treino;
import com.proyetogrupo.proyetofinal.negocio.util.ValidationUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class TreinoNegocioImpl implements TreinoNegocio, AutoCloseable {

    private final TreinoDAO treinoDAO;
    private final AlunoDAO alunoDAO;
    private final ProfessorDAO professorDAO;
    private final Connection connection;

    public TreinoNegocioImpl(TreinoDAO treinoDAO,
                             AlunoDAO alunoDAO,
                             ProfessorDAO professorDAO,
                             Connection connection) {
        this.treinoDAO = treinoDAO;
        this.alunoDAO = alunoDAO;
        this.professorDAO = professorDAO;
        this.connection = connection;
    }

    // ============================================================
    // MÉTODOS EXIGIDOS PELA INTERFACE TreinoNegocio
    // ============================================================

    @Override
    public Treino criarTreino(Treino treino) throws SQLException {
        validarTreino(treino);

        String cpfAluno = treino.getIdAluno(); // CPF como String
        if (!alunoDAO.existsById(cpfAluno)) {
            throw new BusinessException("Aluno não encontrado para o CPF informado.");
        }

        Integer idProfessor = treino.getIdProfessor();
        if (idProfessor == null || !professorDAO.existsById(idProfessor)) {
            throw new BusinessException("Professor não encontrado para o id informado.");
        }

        // regra: não criar treino se já houver treino ativo para o aluno
        if (treinoDAO.existsActiveByAluno(cpfAluno)) {
            throw new BusinessException("Já existe um treino ativo para este aluno.");
        }

        if (treino.getDataInicio() == null) {
            treino.setDataInicio(LocalDate.now());
        }
        treino.setStatus("ATIVO");

        try {
            connection.setAutoCommit(false);
            Treino salvo = treinoDAO.saveAndReturn(treino);
            connection.commit();
            return salvo;
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    @Override
    public Treino atualizarTreino(Treino treino) throws SQLException {
        if (treino.getIdTreino() == null) {
            throw new BusinessException("Id do treino é obrigatório para atualização.");
        }

        validarTreino(treino);

        if (treinoDAO.findById(treino.getIdTreino()).isEmpty()) {
            throw new BusinessException("Treino não encontrado para atualização.");
        }

        try {
            connection.setAutoCommit(false);
            treinoDAO.update(treino);
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
        Optional<Treino> opt = treinoDAO.findById(id);
        if (opt.isEmpty()) {
            throw new BusinessException("Treino não encontrado para finalização.");
        }

        Treino treino = opt.get();
        treino.setStatus("FINALIZADO");
        treino.setDataFim(LocalDate.now());

        try {
            connection.setAutoCommit(false);
            treinoDAO.update(treino);
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    @Override
    public Optional<Treino> buscarPorId(int id) throws SQLException {
        return treinoDAO.findById(id);
    }

    @Override
    public List<Treino> listarPorAluno(String cpfAluno) throws SQLException {
        if (ValidationUtil.isBlank(cpfAluno)) {
            throw new BusinessException("CPF do aluno é obrigatório.");
        }
        return treinoDAO.listByAluno(cpfAluno);
    }

    @Override
    public List<Treino> listarPorProfessor(int idProfessor) throws SQLException {
        return treinoDAO.listByProfessor(idProfessor);
    }

    // ============================================================
    // VALIDAÇÃO
    // ============================================================

    private void validarTreino(Treino treino) throws BusinessException {
        if (treino == null) {
            throw new BusinessException("Treino inválido.");
        }

        if (ValidationUtil.isBlank(treino.getIdAluno())) {
            throw new BusinessException("CPF do aluno é obrigatório no treino.");
        }

        if (treino.getIdProfessor() == null) {
            throw new BusinessException("Professor é obrigatório no treino.");
        }

        if (ValidationUtil.isBlank(treino.getDescricao())) {
            throw new BusinessException("Descrição do treino é obrigatória.");
        }
    }

    // ============================================================
    // AutoCloseable
    // ============================================================

    @Override
    public void close() throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
