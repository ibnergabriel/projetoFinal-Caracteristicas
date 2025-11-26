package com.proyetogrupo.proyetofinal.negocio.impl;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mockito;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.proyetogrupo.proyetofinal.negocio.dao.impl.AlunoDAO;
import com.proyetogrupo.proyetofinal.negocio.dao.impl.PagamentoDAO;
import com.proyetogrupo.proyetofinal.negocio.dao.impl.TreinoDAO;
import com.proyetogrupo.proyetofinal.negocio.exceptions.BusinessException;
import com.proyetogrupo.proyetofinal.negocio.model.Aluno;

class AlunoNegocioImplTest {

    private AlunoDAO mockAlunoDAO;
    private TreinoDAO mockTreinoDAO;
    private PagamentoDAO mockPagamentoDAO;
    private AlunoNegocioImpl negocio;

    @BeforeEach
    void setUp() {
        mockAlunoDAO = Mockito.mock(AlunoDAO.class);
        mockTreinoDAO = Mockito.mock(TreinoDAO.class);
        mockPagamentoDAO = Mockito.mock(PagamentoDAO.class);
        negocio = new AlunoNegocioImpl(mockAlunoDAO, mockTreinoDAO, mockPagamentoDAO);
    }

    @Test
    void cadastrarAluno_ageUnder14_throws() throws SQLException {
        Aluno a = new Aluno();
        a.setNome("Joao");
        a.setIdade(13);
        BusinessException thrown = assertThrows(BusinessException.class, () -> negocio.cadastrarAluno(a));
        assertNotNull(thrown);
        verify(mockAlunoDAO, never()).save(any());
    }

    @Test
    void cadastrarAluno_valid_callsSave() throws SQLException {
        Aluno a = new Aluno();
        a.setNome("Maria");
        a.setIdade(20);
        negocio.cadastrarAluno(a);
        verify(mockAlunoDAO, times(1)).save(a);
    }
}