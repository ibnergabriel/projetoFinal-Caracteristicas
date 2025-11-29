/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.proyetogrupo.proyetofinal;

import com.proyetogrupo.proyetofinal.negocio.dao.ServiceFactory;
import com.proyetogrupo.proyetofinal.negocio.impl.ProfessorNegocioImpl;
import com.proyetogrupo.proyetofinal.negocio.model.Aluno;
import com.proyetogrupo.proyetofinal.negocio.model.Professor;
import com.proyetogrupo.proyetofinal.persistencia.DB;
import java.time.LocalDate;

/**
 *
 * @author ibner
 */
public class ProyetoFinal {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        System.out.println("ok? okaaayyyy!!!");

        // Isso garante que o banco existe antes de abrir qualquer tela
        try {
            DB.inicializarBanco();
        } catch (Exception e) {
            System.out.println("Erro crítico ao criar banco: " + e.getMessage());
            DB.closeConnection();
        } 
        
        // O try-with-resources garante que o service.close() seja chamado no final
        try (ProfessorNegocioImpl professorService = ServiceFactory.criarProfessorService()){
            
            Professor primeiroProf = new Professor();
            primeiroProf.setNome("Giomar Proyeto Sequeiros");
            primeiroProf.setCREF("12345-G/PS");
            primeiroProf.setEmail("giomar@eng.uerj.br");
            primeiroProf.setSenha("Proyeto#2025");
            primeiroProf.setTelefone("4002-8922");
            primeiroProf.setSexo("M");
            primeiroProf.setUsuario("giomarokay");
            // ... setar dados ...
            
            // Isso vai validar, abrir transação, salvar e commitar (ou rollback)
            professorService.cadastrarProfessor(primeiroProf); 
            
            System.out.println("Sucesso!");

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}
