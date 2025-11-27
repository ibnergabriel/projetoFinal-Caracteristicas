package com.proyetogrupo.proyetofinal.negocio.impl;

import com.proyetogrupo.proyetofinal.negocio.dao.AlunoDAO;
import com.proyetogrupo.proyetofinal.negocio.dao.PagamentoDAO;
import com.proyetogrupo.proyetofinal.negocio.dao.TreinoDAO;
import com.proyetogrupo.proyetofinal.negocio.exceptions.BusinessException;
import com.proyetogrupo.proyetofinal.negocio.model.Aluno;
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

class AlunoNegocioImplTest {

    private AlunoDAO mockAlunoDAO;
    private TreinoDAO mockTreinoDAO;
    private PagamentoDAO mockPagamentoDAO;
    private Connection mockConnection;
    
    private AlunoNegocioImpl negocio;

    @BeforeEach
    void setUp() {
        // Criar os Mocks
        mockAlunoDAO = Mockito.mock(AlunoDAO.class);
        mockTreinoDAO = Mockito.mock(TreinoDAO.class);
        mockPagamentoDAO = Mockito.mock(PagamentoDAO.class);
        mockConnection = Mockito.mock(Connection.class); // Mock da conexão

        // Instanciar a classe de Negócio injetando os mocks
        negocio = new AlunoNegocioImpl(mockAlunoDAO, mockTreinoDAO, mockPagamentoDAO, mockConnection);
    }

    @Test
    void cadastrarAluno_ageUnder14_throws() throws SQLException {
        Aluno a = new Aluno();
        a.setNome("Joao");
        a.setIdade(13); // Idade inválida

        BusinessException thrown = assertThrows(BusinessException.class, () -> negocio.cadastrarAluno(a));
        
        assertNotNull(thrown);
        verify(mockAlunoDAO, never()).save(any());
    }

    @Test
    void cadastrarAluno_valid_callsSave() throws SQLException {
        Aluno a = new Aluno();
        a.setNome("Maria");
        a.setIdade(20); // Idade válida

        negocio.cadastrarAluno(a);

        // Verifica se a transação foi aberta, o aluno salvo e o commit realizado
        verify(mockConnection, times(1)).setAutoCommit(false);
        verify(mockAlunoDAO, times(1)).save(a);
        verify(mockConnection, times(1)).commit();
    }
}