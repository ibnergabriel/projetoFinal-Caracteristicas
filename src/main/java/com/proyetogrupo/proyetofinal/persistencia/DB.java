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
                String user = props.getProperty("user");
                String password = props.getProperty("password");

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
        Connection conn = null;
        Statement st = null;

        // NOTE: aqui usamos utf8mb4_0900_ai_ci, que é a collation do seu servidor (MySQL 8 em Ubuntu)
        String collate = "utf8mb4_0900_ai_ci";

        String sqlAluno =
            "CREATE TABLE IF NOT EXISTS Aluno ("
          + " idAluno VARCHAR(14) CHARACTER SET utf8mb4 COLLATE " + collate + " NOT NULL, "
          + " nome VARCHAR(100) CHARACTER SET utf8mb4 COLLATE " + collate + ", "
          + " idade INT, "
          + " sexo VARCHAR(100) CHARACTER SET utf8mb4 COLLATE " + collate + ", "
          + " telefone VARCHAR(20) CHARACTER SET utf8mb4 COLLATE " + collate + ", "
          + " email VARCHAR(100) CHARACTER SET utf8mb4 COLLATE " + collate + ", "
          + " data_matricula DATE, "
          + " PRIMARY KEY (idAluno)"
          + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=" + collate + ";";

        String sqlProfessor =
            "CREATE TABLE IF NOT EXISTS Professor ("
          + " idProfessor INT PRIMARY KEY AUTO_INCREMENT, "
          + " nome VARCHAR(100) CHARACTER SET utf8mb4 COLLATE " + collate + ", "
          + " CREF VARCHAR(50) CHARACTER SET utf8mb4 COLLATE " + collate + ", "
          + " sexo VARCHAR(100) CHARACTER SET utf8mb4 COLLATE " + collate + ", "
          + " telefone VARCHAR(20) CHARACTER SET utf8mb4 COLLATE " + collate + ", "
          + " email VARCHAR(100) CHARACTER SET utf8mb4 COLLATE " + collate + ", "
          + " data_matricula DATE, "
          + " usuario VARCHAR(50) CHARACTER SET utf8mb4 COLLATE " + collate + ", "
          + " senha VARCHAR(100) CHARACTER SET utf8mb4 COLLATE " + collate
          + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=" + collate + ";";

        String sqlTreino =
            "CREATE TABLE IF NOT EXISTS Treino ("
          + " idTreino INT PRIMARY KEY AUTO_INCREMENT, "
          + " idAluno VARCHAR(14) CHARACTER SET utf8mb4 COLLATE " + collate + " NOT NULL, "
          + " idProfessor INT NOT NULL, "
          + " status VARCHAR(100) CHARACTER SET utf8mb4 COLLATE " + collate + ", "
          + " descricao VARCHAR(100) CHARACTER SET utf8mb4 COLLATE " + collate + ", "
          + " data_inicio DATE, "
          + " data_fim DATE, "
          + " CONSTRAINT fk_treino_aluno FOREIGN KEY (idAluno) REFERENCES Aluno(idAluno), "
          + " CONSTRAINT fk_treino_professor FOREIGN KEY (idProfessor) REFERENCES Professor(idProfessor)"
          + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=" + collate + ";";

        String sqlPagamento =
            "CREATE TABLE IF NOT EXISTS Pagamento ("
          + " idPagamento INT PRIMARY KEY AUTO_INCREMENT, "
          + " idAluno VARCHAR(14) CHARACTER SET utf8mb4 COLLATE " + collate + " NOT NULL, "
          + " data_pagamento DATE, "
          + " valor DECIMAL(10, 2), "
          + " CONSTRAINT fk_pagamento_aluno FOREIGN KEY (idAluno) REFERENCES Aluno(idAluno)"
          + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=" + collate + ";";

        try {
            conn = getConnection();
            st = conn.createStatement();

            // cria a tabela referenciada primeiro
            st.execute(sqlAluno);

            // criar as demais tabelas
            st.execute(sqlProfessor);
            st.execute(sqlTreino);
            st.execute(sqlPagamento);

            System.out.println("Banco de dados verificado/criado com sucesso!");
        } catch (SQLException e) {
            // diagnóstico detalhado
            System.err.println("SQLException message: " + e.getMessage());
            System.err.println("SQLState: " + e.getSQLState());
            System.err.println("ErrorCode: " + e.getErrorCode());
            e.printStackTrace();

            // tenta coletar infos do servidor (charset/collation/version)
            Statement infoSt = null;
            ResultSet rs = null;
            try {
                Connection connInfo = (conn != null) ? conn : getConnection();
                infoSt = connInfo.createStatement();

                rs = infoSt.executeQuery("SHOW VARIABLES LIKE 'character_set_database'");
                while (rs.next()) System.err.println(rs.getString(1) + " = " + rs.getString(2));
                closeResultSet(rs);

                rs = infoSt.executeQuery("SHOW VARIABLES LIKE 'collation_database'");
                while (rs.next()) System.err.println(rs.getString(1) + " = " + rs.getString(2));
                closeResultSet(rs);

                rs = infoSt.executeQuery("SELECT VERSION()");
                while (rs.next()) System.err.println("Server version = " + rs.getString(1));
                closeResultSet(rs);
            } catch (SQLException ex2) {
                System.err.println("Erro ao obter variáveis do servidor: " + ex2.getMessage());
            } finally {
                closeStatement(infoSt);
                closeResultSet(rs);
            }

            throw new DBException("Erro ao inicializar tabelas: " + e.getMessage());
        } finally {
            closeStatement(st);
        }
    }
}
