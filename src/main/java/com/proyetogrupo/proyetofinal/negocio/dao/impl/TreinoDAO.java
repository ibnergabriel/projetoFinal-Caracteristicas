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

import com.proyetogrupo.proyetofinal.negocio.model.Treino;
import com.proyetogrupo.proyetofinal.persistencia.DB;

public class TreinoDAO {

    public Optional<Treino> findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM Treino WHERE idTreino = ?";
        try (Connection conn = DB.getConnection(); PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
            }
        }
        return Optional.empty();
    }

    public Treino saveAndReturn(Treino treino) throws SQLException {
        String sql = "INSERT INTO Treino(idAluno, idProfessor, descricao, status, data_inicio, data_fim) VALUES (?,?,?,?,?,?)";
        try (Connection conn = DB.getConnection();
             PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setInt(1, treino.getIdAluno());
            st.setInt(2, treino.getIdProfessor());
            st.setString(3, treino.getDescricao());
            st.setString(4, treino.getStatus());
            st.setDate(5, treino.getDataInicio() == null ? null : Date.valueOf(treino.getDataInicio()));
            st.setDate(6, treino.getDataFim() == null ? null : Date.valueOf(treino.getDataFim()));
            st.executeUpdate();
            try (ResultSet keys = st.getGeneratedKeys()) {
                if (keys.next()) treino.setIdTreino(keys.getInt(1));
            }
        }
        return treino;
    }

    public void update(Treino treino) throws SQLException {
        String sql = "UPDATE Treino SET idAluno=?, idProfessor=?, descricao=?, status=?, data_inicio=?, data_fim=? WHERE idTreino=?";
        try (Connection conn = DB.getConnection(); PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, treino.getIdAluno());
            st.setInt(2, treino.getIdProfessor());
            st.setString(3, treino.getDescricao());
            st.setString(4, treino.getStatus());
            st.setDate(5, treino.getDataInicio() == null ? null : Date.valueOf(treino.getDataInicio()));
            st.setDate(6, treino.getDataFim() == null ? null : Date.valueOf(treino.getDataFim()));
            st.setInt(7, treino.getIdTreino());
            st.executeUpdate();
        }
    }

    public void deleteById(Integer id) throws SQLException {
        String sql = "DELETE FROM Treino WHERE idTreino = ?";
        try (Connection conn = DB.getConnection(); PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, id);
            st.executeUpdate();
        }
    }

    public boolean existsActiveByAluno(Integer idAluno) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Treino WHERE idAluno = ? AND status = 'ATIVO'";
        try (Connection conn = DB.getConnection(); PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, idAluno);
            try (ResultSet rs = st.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }

    public boolean existsActiveByProfessor(Integer idProfessor) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Treino WHERE idProfessor = ? AND status = 'ATIVO'";
        try (Connection conn = DB.getConnection(); PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, idProfessor);
            try (ResultSet rs = st.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }

    public List<Treino> listByAluno(int idAluno) throws SQLException {
        List<Treino> list = new ArrayList<>();
        String sql = "SELECT * FROM Treino WHERE idAluno = ?";
        try (Connection conn = DB.getConnection(); PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, idAluno);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    public List<Treino> listByProfessor(int idProfessor) throws SQLException {
        List<Treino> list = new ArrayList<>();
        String sql = "SELECT * FROM Treino WHERE idProfessor = ?";
        try (Connection conn = DB.getConnection(); PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, idProfessor);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    private Treino map(ResultSet rs) throws SQLException {
        Treino t = new Treino();
        t.setIdTreino(rs.getInt("idTreino"));
        t.setIdAluno(rs.getInt("idAluno"));
        t.setIdProfessor(rs.getInt("idProfessor"));
        t.setDescricao(rs.getString("descricao"));
        t.setStatus(rs.getString("status"));
        Date dInicio = rs.getDate("data_inicio");
        if (dInicio != null) t.setDataInicio(dInicio.toLocalDate());
        Date dFim = rs.getDate("data_fim");
        if (dFim != null) t.setDataFim(dFim.toLocalDate());
        return t;
    }
}