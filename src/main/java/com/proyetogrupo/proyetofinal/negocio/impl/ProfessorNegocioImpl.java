/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyetogrupo.proyetofinal.negocio.impl;

import com.proyetogrupo.proyetofinal.negocio.ProfessorNegocio;
import com.proyetogrupo.proyetofinal.negocio.dao.ProfessorDAO;
import com.proyetogrupo.proyetofinal.negocio.dao.TreinoDAO;
import com.proyetogrupo.proyetofinal.negocio.exceptions.BusinessException;
import com.proyetogrupo.proyetofinal.negocio.model.Professor;
import com.proyetogrupo.proyetofinal.negocio.util.SecurityUtil;
import com.proyetogrupo.proyetofinal.negocio.util.ValidationUtil;
import java.sql.*;
import java.util.Optional;

/**
 *
 * @author ibner
 */
public class ProfessorNegocioImpl implements ProfessorNegocio, AutoCloseable {

    private final ProfessorDAO dao;
    private final TreinoDAO treinoDAO;
    private final Connection connection;

    public ProfessorNegocioImpl(ProfessorDAO dao, TreinoDAO treinoDAO, Connection connection) {
        this.dao = dao;
        this.treinoDAO = treinoDAO;
        this.connection = connection;
    }

    @Override
    public Optional<Professor> buscarPorId(Integer id) throws SQLException {
        return dao.findById(id);
    }

    @Override
    public Professor autenticar(String usuario, String senha) throws SQLException {
        if (ValidationUtil.isBlank(usuario) || ValidationUtil.isBlank(senha)) {
            throw new BusinessException("Usuário e senha obrigatórios.");
        }
        Professor p = dao.findByUsuario(usuario);
        if (p == null) throw new BusinessException("Usuário não encontrado.");
        if (!SecurityUtil.verify(senha, p.getSenha())) throw new BusinessException("Credenciais inválidas.");
        return p;
    }

    @Override
    public void cadastrarProfessor(Professor professor) throws SQLException {
        try {
            if (professor == null) throw new BusinessException("Professor inválido.");
            if (ValidationUtil.isBlank(professor.getNome())) throw new BusinessException("Nome obrigatório.");
            if (ValidationUtil.isBlank(professor.getUsuario())) throw new BusinessException("Usuário obrigatório.");
            
            if (dao.findByUsuario(professor.getUsuario()) != null) throw new BusinessException("Usuário já existe.");

            if (ValidationUtil.isBlank(professor.getSenha())) {
                throw new BusinessException("Senha obrigatória.");
            }


            connection.setAutoCommit(false);
            dao.save(professor);
            connection.commit();

        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    @Override
    public void atualizarProfessor(Professor professor) throws SQLException {
        try {
            if (professor == null || professor.getIdProfessor() == null) throw new BusinessException("Professor inválido.");
            
            dao.findById(professor.getIdProfessor())
               .orElseThrow(() -> new BusinessException("Professor não encontrado."));

            if (!ValidationUtil.isBlank(professor.getSenha())) {
                professor.setSenha(SecurityUtil.sha256Hex(professor.getSenha()));
            }

            connection.setAutoCommit(false);
            dao.update(professor);
            connection.commit();

        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    @Override
    public void removerProfessor(Integer id) throws SQLException {
        try {
            if (!dao.existsById(id)) throw new BusinessException("Professor não encontrado.");
            if (treinoDAO.existsActiveByProfessor(id)) throw new BusinessException("Professor possui treinos ativos.");

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
