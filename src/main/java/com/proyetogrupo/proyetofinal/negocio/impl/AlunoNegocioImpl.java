package com.proyetogrupo.proyetofinal.negocio.impl;

import java.sql.SQLException;
import java.util.Optional;

import com.proyetogrupo.proyetofinal.negocio.AlunoNegocio;
import com.proyetogrupo.proyetofinal.negocio.dao.impl.AlunoDAO;
import com.proyetogrupo.proyetofinal.negocio.dao.impl.PagamentoDAO;
import com.proyetogrupo.proyetofinal.negocio.dao.impl.TreinoDAO;
import com.proyetogrupo.proyetofinal.negocio.exceptions.BusinessException;
import com.proyetogrupo.proyetofinal.negocio.model.Aluno;
import com.proyetogrupo.proyetofinal.negocio.util.ValidationUtil;

public class AlunoNegocioImpl implements AlunoNegocio {
    private final AlunoDAO dao;
    private final TreinoDAO treinoDAO;
    private final PagamentoDAO pagamentoDAO;

    public AlunoNegocioImpl() {
        this(new AlunoDAO(), new TreinoDAO(), new PagamentoDAO());
    }

    public AlunoNegocioImpl(AlunoDAO dao, TreinoDAO treinoDAO, PagamentoDAO pagamentoDAO) {
        this.dao = dao;
        this.treinoDAO = treinoDAO;
        this.pagamentoDAO = pagamentoDAO;
    }

    @Override
    public Optional<Aluno> buscarPorId(Integer id) throws SQLException {
        return dao.findById(id);
    }

    @Override
    public void cadastrarAluno(Aluno aluno) throws SQLException {
        if (aluno == null) throw new BusinessException("Aluno inválido.");
        if (ValidationUtil.isBlank(aluno.getNome())) throw new BusinessException("Nome obrigatório.");
        if (aluno.getIdade() == null || aluno.getIdade() < 14) throw new BusinessException("Idade mínima: 14 anos.");
        if (!ValidationUtil.isBlank(aluno.getEmail()) && !ValidationUtil.isValidEmail(aluno.getEmail())) throw new BusinessException("Email inválido.");
        dao.save(aluno);
    }

    @Override
    public void atualizarAluno(Aluno aluno) throws SQLException {
        if (aluno == null || aluno.getIdAluno() == null) throw new BusinessException("Aluno inválido.");
        dao.findById(aluno.getIdAluno()).orElseThrow(() -> new BusinessException("Aluno não encontrado."));
        dao.update(aluno);
    }

    @Override
    public void removerAluno(Integer id) throws SQLException {
        if (!dao.existsById(id)) throw new BusinessException("Aluno não encontrado.");
        if (treinoDAO.existsActiveByAluno(id)) throw new BusinessException("Aluno possui treino ativo.");
        if (pagamentoDAO.existsByAluno(id)) throw new BusinessException("Aluno possui pagamentos registrados.");
        dao.deleteById(id);
    }
}