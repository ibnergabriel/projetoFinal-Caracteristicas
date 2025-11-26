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
import static org.mockito.Mockito.when;

import com.proyetogrupo.proyetofinal.negocio.dao.impl.AlunoDAO;
import com.proyetogrupo.proyetofinal.negocio.dao.impl.PagamentoDAO;
import com.proyetogrupo.proyetofinal.negocio.exceptions.BusinessException;
import com.proyetogrupo.proyetofinal.negocio.model.Pagamento;

class PagamentoNegocioImplTest {

    private PagamentoDAO mockPagamentoDAO;
    private AlunoDAO mockAlunoDAO;
    private PagamentoNegocioImpl negocio;

    @BeforeEach
    void setUp() {
        mockPagamentoDAO = Mockito.mock(PagamentoDAO.class);
        mockAlunoDAO = Mockito.mock(AlunoDAO.class);
        negocio = new PagamentoNegocioImpl(mockPagamentoDAO, mockAlunoDAO);
    }

    @Test
    void registrarPagamento_invalidValue_throws() throws SQLException {
        Pagamento p = new Pagamento();
        p.setIdAluno(1);
        p.setValor(0);
        BusinessException thrown = assertThrows(BusinessException.class, () -> negocio.registrarPagamento(p));
        assertNotNull(thrown);
        verify(mockPagamentoDAO, never()).save(any());
    }

    @Test
    void registrarPagamento_success_callsSave() throws SQLException {
        Pagamento p = new Pagamento();
        p.setIdAluno(1);
        p.setValor(100);
        when(mockAlunoDAO.existsById(1)).thenReturn(true);
        negocio.registrarPagamento(p);
        verify(mockPagamentoDAO, times(1)).save(p);
    }
}