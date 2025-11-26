package com.proyetogrupo.proyetofinal.negocio.impl;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;

import com.proyetogrupo.proyetofinal.negocio.dao.impl.ProfessorDAO;
import com.proyetogrupo.proyetofinal.negocio.dao.impl.TreinoDAO;
import com.proyetogrupo.proyetofinal.negocio.exceptions.BusinessException;
import com.proyetogrupo.proyetofinal.negocio.model.Professor;
import com.proyetogrupo.proyetofinal.negocio.util.SecurityUtil;

class ProfessorNegocioImplTest {

    private ProfessorDAO mockDao;
    private TreinoDAO mockTreinoDAO;
    private ProfessorNegocioImpl negocio;

    @BeforeEach
    void setUp() {
        mockDao = Mockito.mock(ProfessorDAO.class);
        mockTreinoDAO = Mockito.mock(TreinoDAO.class);
        negocio = new ProfessorNegocioImpl(mockDao, mockTreinoDAO);
    }

    @Test
    void autenticar_success() throws SQLException {
        Professor p = new Professor();
        p.setUsuario("user");
        p.setSenha(SecurityUtil.sha256Hex("password"));
        when(mockDao.findByUsuario("user")).thenReturn(p);

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
        p.setSenha(SecurityUtil.sha256Hex("other"));
        when(mockDao.findByUsuario("user")).thenReturn(p);
        assertThrows(BusinessException.class, () -> negocio.autenticar("user", "password"));
    }
}