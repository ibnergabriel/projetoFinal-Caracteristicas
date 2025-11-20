/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyetogrupo.proyetofinal.persistencia;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

/**
 *
 * @author ibner
 */
public class DB {
    
    private static Connection connection = null;
    
    public static Connection getConnection(){
        if(connection == null){
            try{
                Properties props = loadProperties();
                String url = props.getProperty("dburl");
                // Pega usuário e senha
                String user = props.getProperty("user"); 
                String password = props.getProperty("password");
                
                // Estabelece a conexão com URL, Usuário e Senha
                connection = DriverManager.getConnection(url, user, password);
            }
            catch (SQLException e){
                // SQLException -- Exception
                throw new DBException("Erro ao conectar ao BD: " + e.getMessage());
            }
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            }
            catch(SQLException e){
                throw new DBException(e.getMessage());
            }
        }
    }

    private static Properties loadProperties(){
        try (FileInputStream fs = new FileInputStream("db.properties")) {
            Properties properties = new Properties();
            properties.load(fs);
            return properties;
        }
        catch (IOException e){
            throw new DBException("Não foi possível carregar o arquivo db.properties: " + e.getMessage());
        }
    }

    public static void closeStatement(Statement statement){
        if (statement != null){
            try {
                statement.close();
            }
            catch (SQLException e) {
                // Throw a RunTimeException
                throw new DBException(e.getMessage());
            }
        }
    }

    public static void closeResultSet(ResultSet resultSet){
        if (resultSet != null){
            try {
                resultSet.close();
            }
            catch (SQLException e) {
                // Throw a RunTimeException
                throw new DBException(e.getMessage());
            }
        }
    }
    
}
