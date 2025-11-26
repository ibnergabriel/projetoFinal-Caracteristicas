package com.proyetogrupo.proyetofinal.negocio.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.proyetogrupo.proyetofinal.negocio.model.Aluno;
import com.proyetogrupo.proyetofinal.persistencia.DB;

public class AlunoDAO {

    public Optional<Aluno> findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM Aluno WHERE idAluno = ?";
        try (Connection conn = DB.getConnection(); PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
            }
        }
        return Optional.empty();
    }

    public void save(Aluno a) throws SQLException {
        String sql = "INSERT INTO Aluno(nome, idade, sexo, telefone, email, data_matricula) VALUES (?,?,?,?,?,?)";
        try (Connection conn = DB.getConnection();
             PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, a.getNome());
            st.setObject(2, a.getIdade());
            st.setString(3, a.getSexo());
            st.setString(4, a.getTelefone());
            st.setString(5, a.getEmail());
            st.setDate(6, a.getDataMatricula() == null ? null : Date.valueOf(a.getDataMatricula()));
            st.executeUpdate();
            try (ResultSet keys = st.getGeneratedKeys()) {
                if (keys.next()) a.setIdAluno(keys.getInt(1));
            }
        }
    }

    public void update(Aluno a) throws SQLException {
        String sql = "UPDATE Aluno SET nome=?, idade=?, sexo=?, telefone=?, email=?, data_matricula=? WHERE idAluno=?";
        try (Connection conn = DB.getConnection(); PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, a.getNome());
            st.setObject(2, a.getIdade());
            st.setString(3, a.getSexo());
            st.setString(4, a.getTelefone());
            st.setString(5, a.getEmail());
            st.setDate(6, a.getDataMatricula() == null ? null : Date.valueOf(a.getDataMatricula()));
            st.setInt(7, a.getIdAluno());
            st.executeUpdate();
        }
    }

    public void deleteById(Integer id) throws SQLException {
        String sql = "DELETE FROM Aluno WHERE idAluno = ?";
        try (Connection conn = DB.getConnection(); PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, id);
            st.executeUpdate();
        }
    }

    public boolean existsById(Integer id) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Aluno WHERE idAluno = ?";
        try (Connection conn = DB.getConnection(); PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }

    public List<Aluno> listAll() throws SQLException {
        List<Aluno> list = new ArrayList<>();
        String sql = "SELECT * FROM Aluno";
        try (Connection conn = DB.getConnection(); PreparedStatement st = conn.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    private Aluno map(ResultSet rs) throws SQLException {
        Aluno a = new Aluno();
        a.setIdAluno(rs.getInt("idAluno"));
        a.setNome(rs.getString("nome"));
        int idade = rs.getInt("idade");
        if (!rs.wasNull()) a.setIdade(idade);
        a.setSexo(rs.getString("sexo"));
        a.setTelefone(rs.getString("telefone"));
        a.setEmail(rs.getString("email"));
        Date d = rs.getDate("data_matricula");
        if (d != null) a.setDataMatricula(d.toLocalDate());
        return a;
    }
}