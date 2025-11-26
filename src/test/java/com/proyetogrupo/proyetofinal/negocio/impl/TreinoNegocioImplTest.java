package com.proyetogrupo.proyetofinal.negocio.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.proyetogrupo.proyetofinal.negocio.dao.impl.AlunoDAO;
import com.proyetogrupo.proyetofinal.negocio.dao.impl.ProfessorDAO;
import com.proyetogrupo.proyetofinal.negocio.dao.impl.TreinoDAO;
import com.proyetogrupo.proyetofinal.negocio.exceptions.BusinessException;
import com.proyetogrupo.proyetofinal.negocio.model.Treino;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.SQLException;

class TreinoNegocioImplTest {

    private TreinoDAO mockDao;
    private AlunoDAO mockAlunoDAO;
    private ProfessorDAO mockProfessorDAO;
    private TreinoNegocioImpl negocio;

    @BeforeEach
    void setUp() {
        mockDao = Mockito.mock(TreinoDAO.class);
        mockAlunoDAO = Mockito.mock(AlunoDAO.class);
        mockProfessorDAO = Mockito.mock(ProfessorDAO.class);
        negocio = new TreinoNegocioImpl(mockDao, mockAlunoDAO, mockProfessorDAO);
    }

    @Test
    void criarTreino_alreadyHasActive_throws() throws SQLException {
        Treino t = new Treino();
        t.setIdAluno(1);
        t.setIdProfessor(2);
        when(mockAlunoDAO.existsById(1)).thenReturn(true);
        when(mockProfessorDAO.existsById(2)).thenReturn(true);
        when(mockDao.existsActiveByAluno(1)).thenReturn(true);
        assertThrows(BusinessException.class, () -> negocio.criarTreino(t));
    }

    @Test
    void criarTreino_success_returnsSaved() throws SQLException {
        Treino t = new Treino();
        t.setIdAluno(1);
        t.setIdProfessor(2);
        t.setDataInicio(java.time.LocalDate.now());
        when(mockAlunoDAO.existsById(1)).thenReturn(true);
        when(mockProfessorDAO.existsById(2)).thenReturn(true);
        when(mockDao.existsActiveByAluno(1)).thenReturn(false);
        when(mockDao.saveAndReturn(t)).thenReturn(t);
        Treino out = negocio.criarTreino(t);
        assertNotNull(out);
        verify(mockDao).saveAndReturn(t);
    }
}