package com.proyetogrupo.proyetofinal.negocio.impl;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.proyetogrupo.proyetofinal.negocio.TreinoNegocio;
import com.proyetogrupo.proyetofinal.negocio.dao.impl.AlunoDAO;
import com.proyetogrupo.proyetofinal.negocio.dao.impl.ProfessorDAO;
import com.proyetogrupo.proyetofinal.negocio.dao.impl.TreinoDAO;
import com.proyetogrupo.proyetofinal.negocio.exceptions.BusinessException;
import com.proyetogrupo.proyetofinal.negocio.model.Treino;

public class TreinoNegocioImpl implements TreinoNegocio {

    // fields without inline initialization to allow constructor injection in tests
    private final TreinoDAO dao;
    private final AlunoDAO alunoDAO;
    private final ProfessorDAO professorDAO;

    // default constructor (production)
    public TreinoNegocioImpl() {
        this(new TreinoDAO(), new AlunoDAO(), new ProfessorDAO());
    }

    // constructor used by tests to inject mocks
    public TreinoNegocioImpl(TreinoDAO dao, AlunoDAO alunoDAO, ProfessorDAO professorDAO) {
        this.dao = dao;
        this.alunoDAO = alunoDAO;
        this.professorDAO = professorDAO;
    }

    @Override
    public Treino criarTreino(Treino treino) throws SQLException {
        if (treino == null) throw new BusinessException("Treino inválido.");
        if (treino.getIdAluno() == null || !alunoDAO.existsById(treino.getIdAluno())) throw new BusinessException("Aluno não encontrado.");
        if (treino.getIdProfessor() == null || !professorDAO.existsById(treino.getIdProfessor())) throw new BusinessException("Professor não encontrado.");
        LocalDate inicio = treino.getDataInicio();
        LocalDate fim = treino.getDataFim();
        if (inicio == null) throw new BusinessException("Data de início é obrigatória.");
        if (fim != null && fim.isBefore(inicio)) throw new BusinessException("Data de fim anterior ao início.");
        if (dao.existsActiveByAluno(treino.getIdAluno())) throw new BusinessException("Aluno já possui treino ativo.");
        if (treino.getStatus() == null) treino.setStatus("ATIVO");
        return dao.saveAndReturn(treino);
    }

    @Override
    public Treino atualizarTreino(Treino treino) throws SQLException {
        if (treino == null || treino.getIdTreino() == null) throw new BusinessException("Treino inválido.");
        dao.findById(treino.getIdTreino()).orElseThrow(() -> new BusinessException("Treino não encontrado."));
        if (treino.getDataInicio() != null && treino.getDataFim() != null && treino.getDataFim().isBefore(treino.getDataInicio()))
            throw new BusinessException("Data de fim anterior ao início.");
        dao.update(treino);
        return treino;
    }

    @Override
    public void finalizarTreino(int id) throws SQLException {
        Treino t = dao.findById(id).orElseThrow(() -> new BusinessException("Treino não encontrado."));
        t.setStatus("CONCLUIDO");
        dao.update(t);
    }

    @Override public Optional<Treino> buscarPorId(int id) throws SQLException { return dao.findById(id); }
    @Override public List<Treino> listarPorAluno(int idAluno) throws SQLException { return dao.listByAluno(idAluno); }
    @Override public List<Treino> listarPorProfessor(int idProfessor) throws SQLException { return dao.listByProfessor(idProfessor); }
}