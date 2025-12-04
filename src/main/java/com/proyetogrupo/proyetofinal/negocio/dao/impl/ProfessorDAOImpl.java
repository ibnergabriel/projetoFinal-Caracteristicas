package com.proyetogrupo.proyetofinal.negocio.dao.impl;

import java.sql.SQLException;
import java.util.Optional;

import com.proyetogrupo.proyetofinal.negocio.dao.ProfessorDAO;
import com.proyetogrupo.proyetofinal.negocio.model.Professor;
import java.sql.*;

public class ProfessorDAOImpl implements ProfessorDAO {

    private final Connection connection;


    public ProfessorDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<Professor> findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM Professor WHERE idProfessor = ?";
        
        try (PreparedStatement st = this.connection.prepareStatement(sql)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public Professor findByUsuario(String usuario) throws SQLException {
        String sql = "SELECT * FROM Professor WHERE usuario = ?";
        
        try (PreparedStatement st = this.connection.prepareStatement(sql)) {
            st.setString(1, usuario);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        }
        return null;
    }

    @Override
    public boolean existsById(Integer id) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Professor WHERE idProfessor = ?";
        
        try (PreparedStatement st = this.connection.prepareStatement(sql)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
                return false;
            }
        }
    }

    @Override
    public void save(Professor p) throws SQLException {
        String sql = "INSERT INTO Professor(nome, CREF, sexo, telefone, email, data_matricula, usuario, senha) VALUES (?,?,?,?,?,?,?,?)";
        
        try (PreparedStatement st = this.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, p.getNome());
            st.setString(2, p.getCREF());
            st.setString(3, p.getSexo());
            st.setString(4, p.getTelefone());
            st.setString(5, p.getEmail());
            st.setDate(6, p.getDataMatricula() == null ? null : Date.valueOf(p.getDataMatricula()));
            st.setString(7, p.getUsuario());
            st.setString(8, p.getSenha());
            
            st.executeUpdate();
            
            try (ResultSet keys = st.getGeneratedKeys()) { 
                if (keys.next()) p.setIdProfessor(keys.getInt(1)); 
            }
        }
    }

    @Override
    public void update(Professor p) throws SQLException {
        String sql = "UPDATE Professor SET nome=?, CREF=?, sexo=?, telefone=?, email=?, data_matricula=?, usuario=?, senha=? WHERE idProfessor=?";
        
        try (PreparedStatement st = this.connection.prepareStatement(sql)) {
            st.setString(1, p.getNome());
            st.setString(2, p.getCREF());
            st.setString(3, p.getSexo());
            st.setString(4, p.getTelefone());
            st.setString(5, p.getEmail());
            st.setDate(6, p.getDataMatricula() == null ? null : Date.valueOf(p.getDataMatricula()));
            st.setString(7, p.getUsuario());
            st.setString(8, p.getSenha());
            st.setInt(9, p.getIdProfessor());
            
            st.executeUpdate();
        }
    }

    @Override
    public void deleteById(Integer id) throws SQLException {
        String sql = "DELETE FROM Professor WHERE idProfessor = ?";
        
        try (PreparedStatement st = this.connection.prepareStatement(sql)) {
            st.setInt(1, id);
            st.executeUpdate();
        }
    }

    // Método auxiliar privado (mantido da implementação original)
    private Professor map(ResultSet rs) throws SQLException {
        Professor p = new Professor();
        p.setIdProfessor(rs.getInt("idProfessor"));
        p.setNome(rs.getString("nome"));
        p.setCREF(rs.getString("CREF"));
        p.setSexo(rs.getString("sexo"));
        p.setTelefone(rs.getString("telefone"));
        p.setEmail(rs.getString("email"));
        
        Date d = rs.getDate("data_matricula");
        if (d != null) p.setDataMatricula(d.toLocalDate());
        
        p.setUsuario(rs.getString("usuario"));
        p.setSenha(rs.getString("senha"));
        
        return p;
    }
}