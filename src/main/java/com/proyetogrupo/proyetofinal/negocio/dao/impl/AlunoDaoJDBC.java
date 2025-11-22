/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyetogrupo.proyetofinal.negocio.dao.impl;

import com.proyetogrupo.proyetofinal.negocio.AlunoNegocio;
import java.sql.*;

/**
 *
 * @author ibner
 */
public class AlunoDaoJDBC {
    
    private Connection connection;
    
    public AlunoDaoJDBC(Connection connection){
        this.connection = connection;
    }
    
    public void insert(AlunoNegocio obj){
        PreparedStatement preparedStatment = null;
    }
}
