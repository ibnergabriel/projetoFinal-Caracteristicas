package com.proyetogrupo.proyetofinal.negocio.impl;

import com.proyetogrupo.proyetofinal.negocio.dao.ProfessorDAO;
import com.proyetogrupo.proyetofinal.negocio.dao.TreinoDAO;
import com.proyetogrupo.proyetofinal.negocio.exceptions.BusinessException;
import com.proyetogrupo.proyetofinal.negocio.model.Professor;
import com.proyetogrupo.proyetofinal.negocio.util.SecurityUtil;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;

class ProfessorNegocioImplTest {

    private ProfessorDAO mockDao;
    private TreinoDAO mockTreinoDAO;
    private Connection mockConnection;
    
    private ProfessorNegocioImpl negocio;

    @BeforeEach
    void setUp() {
        mockDao = Mockito.mock(ProfessorDAO.class);
        mockTreinoDAO = Mockito.mock(TreinoDAO.class);
        mockConnection = Mockito.mock(Connection.class); // Mock da conexão

        // MUDANÇA 4: Instanciamos a classe de negócio injetando os mocks
        negocio = new ProfessorNegocioImpl(mockDao, mockTreinoDAO, mockConnection);
    }

    @Test
    void autenticar_success() throws SQLException {
        Professor p = new Professor();
        p.setUsuario("user");
        // Simula que a senha no banco já está hashada
        p.setSenha(SecurityUtil.sha256Hex("password"));
        
        when(mockDao.findByUsuario("user")).thenReturn(p);

        // O método autenticar vai hashar a senha de entrada e comparar
        Professor result = negocio.autenticar("user", "password");
        
        assertNotNull(result);
        assertEquals("user", result.getUsuario());
    }

    @Test
    void autenticar_userNotFound_throws() throws SQLException {
        when(mockDao.findByUsuario("nouser")).thenReturn(null);
        
        assertThrows(BusinessException.class, () -> negocio.autenticar("nouser", "password"));
    }

    @Test
    void autenticar_invalidPassword_throws() throws SQLException {
        Professor p = new Professor();
        p.setUsuario("user");
        p.setSenha(SecurityUtil.sha256Hex("other")); // Senha correta no banco é "other"
        
        when(mockDao.findByUsuario("user")).thenReturn(p);
        
        // Tentamos logar com "password" (incorreta)
        assertThrows(BusinessException.class, () -> negocio.autenticar("user", "password"));
    }
}