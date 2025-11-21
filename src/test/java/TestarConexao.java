
import com.proyetogrupo.proyetofinal.persistencia.DB;
import java.sql.Connection;
import java.sql.SQLException;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ibner
 */
public class TestarConexao {
    public static void main(String[] args) throws SQLException {
        Connection conn = null;
        
        try {
            // Tenta obter a conexão usando o método que você configurou
            System.out.println("Tentando obter a conexão com o banco de dados...");
            conn = DB.getConnection();
            
            // Se chegou até aqui, a conexão foi bem-sucedida!
            System.out.println("✅ SUCESSO! Conexão estabelecida.");
            
            // Verifica se a conexão é válida (opcional, mas bom para confirmar)
            if (conn != null && conn.isValid(5)) {
                System.out.println("Conexão está ativa e válida.");
            }
            
        } 
        catch (RuntimeException e) { // Captura a DBException que você configurou
            System.err.println("❌ FALHA! Não foi possível conectar ao banco de dados.");
            System.err.println("Detalhes do Erro: " + e.getMessage());
            // Imprime a pilha de erros completa para debug
 
        } 
        finally {
            // Garante que a conexão seja fechada, não importa o resultado
            DB.closeConnection();
            System.out.println("Conexão fechada.");
        }
    }
}
