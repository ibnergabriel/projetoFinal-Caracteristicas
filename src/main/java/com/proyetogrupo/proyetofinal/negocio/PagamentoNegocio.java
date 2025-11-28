/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyetogrupo.proyetofinal.negocio;

import java.sql.SQLException;
import java.util.List;

import com.proyetogrupo.proyetofinal.negocio.model.Pagamento;

public interface PagamentoNegocio {
    void registrarPagamento(Pagamento pagamento) throws SQLException;

    // aqui também, CPF do aluno
    List<Pagamento> listarPorAluno(String cpfAluno) throws SQLException;
}