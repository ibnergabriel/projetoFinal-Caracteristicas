package com.proyetogrupo.proyetofinal.negocio.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

import com.proyetogrupo.proyetofinal.negocio.PagamentoNegocio;
import com.proyetogrupo.proyetofinal.negocio.dao.impl.AlunoDAO;
import com.proyetogrupo.proyetofinal.negocio.dao.impl.PagamentoDAO;
import com.proyetogrupo.proyetofinal.negocio.exceptions.BusinessException;
import com.proyetogrupo.proyetofinal.negocio.model.Pagamento;

public class PagamentoNegocioImpl implements PagamentoNegocio {

    private final PagamentoDAO dao;
    private final AlunoDAO alunoDAO;

    
    public PagamentoNegocioImpl() {
        this(new PagamentoDAO(), new AlunoDAO());
    }

    
    public PagamentoNegocioImpl(PagamentoDAO dao, AlunoDAO alunoDAO) {
        this.dao = Objects.requireNonNull(dao, "PagamentoDAO must not be null");
        this.alunoDAO = Objects.requireNonNull(alunoDAO, "AlunoDAO must not be null");
    }

    @Override
    public void registrarPagamento(Pagamento pagamento) throws SQLException {
        if (pagamento == null) throw new BusinessException("Pagamento inválido.");
        if (pagamento.getIdAluno() == null || !alunoDAO.existsById(pagamento.getIdAluno()))
            throw new BusinessException("Aluno não encontrado.");
        if (pagamento.getValor() == null || pagamento.getValor() <= 0) throw new BusinessException("Valor inválido.");
        dao.save(pagamento);
    }

    @Override
    public List<Pagamento> listarPorAluno(int idAluno) throws SQLException {
        return dao.listByAluno(idAluno);
    }
}