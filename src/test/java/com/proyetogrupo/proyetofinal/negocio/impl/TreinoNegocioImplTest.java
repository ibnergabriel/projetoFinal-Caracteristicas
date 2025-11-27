package com.proyetogrupo.proyetofinal.negocio.impl;

import com.proyetogrupo.proyetofinal.negocio.dao.AlunoDAO;
import com.proyetogrupo.proyetofinal.negocio.dao.ProfessorDAO;
import com.proyetogrupo.proyetofinal.negocio.dao.TreinoDAO;
import com.proyetogrupo.proyetofinal.negocio.exceptions.BusinessException;
import com.proyetogrupo.proyetofinal.negocio.model.Treino;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class TreinoNegocioImplTest {

    private TreinoDAO mockDao;
    private AlunoDAO mockAlunoDAO;
    private ProfessorDAO mockProfessorDAO;
    private Connection mockConnection;
    
    private TreinoNegocioImpl negocio;

    @BeforeEach
    void setUp() {
        mockDao = Mockito.mock(TreinoDAO.class);
        mockAlunoDAO = Mockito.mock(AlunoDAO.class);
        mockProfessorDAO = Mockito.mock(ProfessorDAO.class);
        mockConnection = Mockito.mock(Connection.class); // Inicializa o mock
        
        negocio = new TreinoNegocioImpl(mockDao, mockAlunoDAO, mockProfessorDAO, mockConnection);
    }

    @Test
    void criarTreino_alreadyHasActive_throws() throws SQLException {
        Treino t = new Treino();
        t.setIdAluno(1);
        t.setIdProfessor(2);
        
        // Simula que aluno e professor existem
        when(mockAlunoDAO.existsById(1)).thenReturn(true);
        when(mockProfessorDAO.existsById(2)).thenReturn(true);
        // Simula que JÁ existe treino ativo
        when(mockDao.existsActiveByAluno(1)).thenReturn(true);
        
        // Deve lançar exceção
        assertThrows(BusinessException.class, () -> negocio.criarTreino(t));
    }

    @Test
    void criarTreino_success_returnsSaved() throws SQLException {
        Treino t = new Treino();
        t.setIdAluno(1);
        t.setIdProfessor(2);
        t.setDataInicio(java.time.LocalDate.now()); // Data obrigatória
        
        when(mockAlunoDAO.existsById(1)).thenReturn(true);
        when(mockProfessorDAO.existsById(2)).thenReturn(true);
        when(mockDao.existsActiveByAluno(1)).thenReturn(false); // Não tem treino ativo
        when(mockDao.saveAndReturn(t)).thenReturn(t);
        
        Treino out = negocio.criarTreino(t);
        
        assertNotNull(out);
        
        verify(mockConnection, times(1)).setAutoCommit(false);
        verify(mockDao).saveAndReturn(t);
        verify(mockConnection, times(1)).commit();
    }
}