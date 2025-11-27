/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.proyetogrupo.proyetofinal;

import com.proyetogrupo.proyetofinal.negocio.dao.ServiceFactory;
import com.proyetogrupo.proyetofinal.negocio.impl.AlunoNegocioImpl;
import com.proyetogrupo.proyetofinal.negocio.model.Aluno;
import com.proyetogrupo.proyetofinal.persistencia.DB;

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
        try (AlunoNegocioImpl alunoService = ServiceFactory.criarAlunoService()) {
            
            Aluno novo = new Aluno();
            novo.setNome("Teste");
            // ... setar dados ...
            
            // Isso vai validar, abrir transação, salvar e commitar (ou rollback)
            alunoService.cadastrarAluno(novo); 
            
            System.out.println("Sucesso!");

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}
