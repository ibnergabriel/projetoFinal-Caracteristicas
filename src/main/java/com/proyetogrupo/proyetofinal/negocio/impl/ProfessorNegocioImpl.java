package com.proyetogrupo.proyetofinal.negocio.impl;

import java.sql.SQLException;
import java.util.Optional;

import com.proyetogrupo.proyetofinal.negocio.ProfessorNegocio;
import com.proyetogrupo.proyetofinal.negocio.dao.impl.ProfessorDAO;
import com.proyetogrupo.proyetofinal.negocio.dao.impl.TreinoDAO;
import com.proyetogrupo.proyetofinal.negocio.exceptions.BusinessException;
import com.proyetogrupo.proyetofinal.negocio.model.Professor;
import com.proyetogrupo.proyetofinal.negocio.util.SecurityUtil;
import com.proyetogrupo.proyetofinal.negocio.util.ValidationUtil;

public class ProfessorNegocioImpl implements ProfessorNegocio {
    private final ProfessorDAO dao;
    private final TreinoDAO treinoDAO;

    
    public ProfessorNegocioImpl() {
        this(new ProfessorDAO(), new TreinoDAO());
    }

    
    public ProfessorNegocioImpl(ProfessorDAO dao, TreinoDAO treinoDAO) {
        this.dao = dao;
        this.treinoDAO = treinoDAO;
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
        if (professor == null) throw new BusinessException("Professor inválido.");
        if (ValidationUtil.isBlank(professor.getNome())) throw new BusinessException("Nome obrigatório.");
        if (ValidationUtil.isBlank(professor.getUsuario())) throw new BusinessException("Usuário obrigatório.");
        if (dao.findByUsuario(professor.getUsuario()) != null) throw new BusinessException("Usuário já existe.");
        if (!ValidationUtil.isBlank(professor.getSenha())) {
            professor.setSenha(SecurityUtil.sha256Hex(professor.getSenha()));
        }
        dao.save(professor);
    }

    @Override
    public void atualizarProfessor(Professor professor) throws SQLException {
        if (professor == null || professor.getIdProfessor() == null) throw new BusinessException("Professor inválido.");
        dao.findById(professor.getIdProfessor()).orElseThrow(() -> new BusinessException("Professor não encontrado."));
        if (!ValidationUtil.isBlank(professor.getSenha())) {
            professor.setSenha(SecurityUtil.sha256Hex(professor.getSenha()));
        }
        dao.update(professor);
    }

    @Override
    public void removerProfessor(Integer id) throws SQLException {
        if (!dao.existsById(id)) throw new BusinessException("Professor não encontrado.");
        if (treinoDAO.existsActiveByProfessor(id)) throw new BusinessException("Professor possui treinos ativos.");
        dao.deleteById(id);
    }
}