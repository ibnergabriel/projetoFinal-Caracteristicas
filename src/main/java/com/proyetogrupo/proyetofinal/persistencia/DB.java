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

    // --- NOVO MÉTODO PARA CRIAR TABELAS AUTOMATICAMENTE ---
    public static void inicializarBanco() {
        Statement st = null;
        try {
            Connection conn = getConnection();
            st = conn.createStatement();
            
            // Tabela Aluno
            st.execute("CREATE TABLE IF NOT EXISTS Aluno ("
                    + "idAluno INT PRIMARY KEY AUTO_INCREMENT, "
                    + "nome VARCHAR(100), "
                    + "idade INT, "
                    + "sexo VARCHAR(100), "
                    + "telefone VARCHAR(20), "
                    + "email VARCHAR(100), "
                    + "data_matricula DATE"
                    + ")");

            // Tabela Professor
            st.execute("CREATE TABLE IF NOT EXISTS Professor ("
                    + "idProfessor INT PRIMARY KEY AUTO_INCREMENT, "
                    + "nome VARCHAR(100), "
                    + "CREF VARCHAR(50), "
                    + "sexo VARCHAR(100), "
                    + "telefone VARCHAR(20), "
                    + "email VARCHAR(100), "
                    + "data_matricula DATE, "
                    + "usuario VARCHAR(50), "
                    + "senha VARCHAR(100)"
                    + ")");

            // Tabela Treino (Depende de Aluno e Professor)
            st.execute("CREATE TABLE IF NOT EXISTS Treino ("
                    + "idTreino INT PRIMARY KEY AUTO_INCREMENT, "
                    + "idAluno INT NOT NULL, "
                    + "idProfessor INT NOT NULL, "
                    + "status VARCHAR(100), "
                    + "descricao VARCHAR(100), "
                    + "data_inicio DATE, "
                    + "data_fim DATE, "
                    + "CONSTRAINT fk_treino_aluno FOREIGN KEY (idAluno) REFERENCES Aluno(idAluno), "
                    + "CONSTRAINT fk_treino_professor FOREIGN KEY (idProfessor) REFERENCES Professor(idProfessor)"
                    + ")");

            // Tabela Pagamento (Depende de Aluno)
            st.execute("CREATE TABLE IF NOT EXISTS Pagamento ("
                    + "idPagamento INT PRIMARY KEY AUTO_INCREMENT, "
                    + "idAluno INT NOT NULL, "
                    + "data_pagamento DATE, "
                    + "valor DECIMAL(10, 2), "
                    + "CONSTRAINT fk_pagamento_aluno FOREIGN KEY (idAluno) REFERENCES Aluno(idAluno)"
                    + ")");
            
            System.out.println("Banco de dados verificado/criado com sucesso!");

        } catch (SQLException e) {
            throw new DBException("Erro ao inicializar tabelas: " + e.getMessage());
        } finally {
            closeStatement(st);
        }
    }
    
}
