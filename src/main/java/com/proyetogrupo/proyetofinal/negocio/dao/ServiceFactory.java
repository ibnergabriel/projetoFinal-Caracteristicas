/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyetogrupo.proyetofinal.negocio.dao; // Ajuste o pacote se necessário

import com.proyetogrupo.proyetofinal.negocio.dao.impl.AlunoDAOImpl;
import com.proyetogrupo.proyetofinal.negocio.dao.impl.PagamentoDAOImpl;
import com.proyetogrupo.proyetofinal.negocio.dao.impl.ProfessorDAOImpl;
import com.proyetogrupo.proyetofinal.negocio.dao.impl.TreinoDAOImpl;
import com.proyetogrupo.proyetofinal.negocio.impl.AlunoNegocioImpl;
import com.proyetogrupo.proyetofinal.negocio.impl.PagamentoNegocioImpl;
import com.proyetogrupo.proyetofinal.negocio.impl.ProfessorNegocioImpl;
import com.proyetogrupo.proyetofinal.negocio.impl.TreinoNegocioImpl;
import com.proyetogrupo.proyetofinal.persistencia.DB;
import java.sql.Connection;

/**
 *
 * @author ibner
 */
public class ServiceFactory {
    
    // Se DB.getConnection() lança SQLException, adicione "throws SQLException" na assinatura
    // Se DB.getConnection() trata o erro internamente, deixe como está abaixo.

    public static AlunoNegocioImpl criarAlunoService() {
        // Obtém a conexão (se der erro no DB, ele vai estourar aqui e parar o programa, o que é correto)
        Connection conn = DB.getConnection(); 
        
        // Injeta a conexão nos DAOs
        AlunoDAOImpl alunoDAO = new AlunoDAOImpl(conn);
        TreinoDAOImpl treinoDAO = new TreinoDAOImpl(conn);
        PagamentoDAOImpl pagamentoDAO = new PagamentoDAOImpl(conn);
        
        // Retorna o Serviço montado
        return new AlunoNegocioImpl(alunoDAO, pagamentoDAO, treinoDAO, conn);
    }

    public static ProfessorNegocioImpl criarProfessorService() {
        Connection conn = DB.getConnection();
        
        ProfessorDAOImpl professorDAO = new ProfessorDAOImpl(conn);
        TreinoDAOImpl treinoDAO = new TreinoDAOImpl(conn);
        
        return new ProfessorNegocioImpl(professorDAO, treinoDAO, conn);
    }
    
    public static TreinoNegocioImpl criarTreinoService() {
        Connection conn = DB.getConnection();
        
        TreinoDAOImpl treinoDAO = new TreinoDAOImpl(conn);
        AlunoDAOImpl alunoDAO = new AlunoDAOImpl(conn);
        ProfessorDAOImpl professorDAO = new ProfessorDAOImpl(conn);
        
        return new TreinoNegocioImpl(treinoDAO, alunoDAO, professorDAO, conn);
    }

    public static PagamentoNegocioImpl criarPagamentoService() {
        Connection conn = DB.getConnection();
        
        PagamentoDAOImpl pagamentoDAO = new PagamentoDAOImpl(conn);
        AlunoDAOImpl alunoDAO = new AlunoDAOImpl(conn);
        
        return new PagamentoNegocioImpl(pagamentoDAO, alunoDAO, conn);
    }
}