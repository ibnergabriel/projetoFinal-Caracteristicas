package com.proyetogrupo.proyetofinal.negocio.impl;

import com.proyetogrupo.proyetofinal.negocio.dao.AlunoDAO;
import com.proyetogrupo.proyetofinal.negocio.dao.PagamentoDAO;
import com.proyetogrupo.proyetofinal.negocio.exceptions.BusinessException;
import com.proyetogrupo.proyetofinal.negocio.model.Pagamento;
import java.sql.Connection;
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

// MUDANÇA 1: O nome da classe deve refletir que estamos testando a Regra de Negócio
class PagamentoNegocioImplTest {

    private PagamentoDAO mockPagamentoDAO;
    private AlunoDAO mockAlunoDAO;
    private Connection mockConnection; // MUDANÇA 2: Precisamos mockar a conexão
    
    // MUDANÇA 3: A classe que estamos testando é a NegocioImpl, não a DAOImpl
    private PagamentoNegocioImpl negocio;

    @BeforeEach
    void setUp() {
        // Criamos os Mocks (objetos falsos)
        mockPagamentoDAO = Mockito.mock(PagamentoDAO.class);
        mockAlunoDAO = Mockito.mock(AlunoDAO.class);
        mockConnection = Mockito.mock(Connection.class); // Mock da conexão
        
        // Injetamos os mocks no Serviço
        negocio = new PagamentoNegocioImpl(mockPagamentoDAO, mockAlunoDAO, mockConnection);
    }

    @Test
    void registrarPagamento_invalidValue_throws() throws SQLException {
        Pagamento p = new Pagamento();
        p.setIdAluno(1);
        p.setValor(0); // Valor inválido
        
        // Como o aluno é verificado antes do valor, precisamos simular que o aluno existe
        // para garantir que o erro seja de VALOR e não de ALUNO.
        when(mockAlunoDAO.existsById(1)).thenReturn(true);

        BusinessException thrown = assertThrows(BusinessException.class, () -> negocio.registrarPagamento(p));
        
        assertNotNull(thrown);
        // Verifica se o save NUNCA foi chamado
        verify(mockPagamentoDAO, never()).save(any());
    }

    @Test
    void registrarPagamento_success_callsSave() throws SQLException {
        Pagamento p = new Pagamento();
        p.setIdAluno(1);
        p.setValor(100);
        
        // Simulamos que o aluno existe
        when(mockAlunoDAO.existsById(1)).thenReturn(true);
        
        // Executa o método
        negocio.registrarPagamento(p);
        
        // MUDANÇA 4: Verifica se abriu transação, salvou e fez commit
        verify(mockConnection, times(1)).setAutoCommit(false);
        verify(mockPagamentoDAO, times(1)).save(p);
        verify(mockConnection, times(1)).commit();
    }
}