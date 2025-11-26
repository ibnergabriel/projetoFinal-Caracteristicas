package com.proyetogrupo.proyetofinal.negocio.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.proyetogrupo.proyetofinal.negocio.model.Pagamento;
import com.proyetogrupo.proyetofinal.persistencia.DB;

public class PagamentoDAO {

    public Optional<Pagamento> findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM Pagamento WHERE idPagamento = ?";
        try (Connection conn = DB.getConnection(); PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    Pagamento p = map(rs);
                    return Optional.of(p);
                }
            }
        }
        return Optional.empty();
    }

    public void save(Pagamento p) throws SQLException {
        String sql = "INSERT INTO Pagamento(idAluno, data_pagamento, valor) VALUES (?,?,?)";
        try (Connection conn = DB.getConnection();
             PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setInt(1, p.getIdAluno());
            st.setDate(2, p.getDataPagamento() == null ? null : Date.valueOf(p.getDataPagamento()));
            if (p.getValor() == null) st.setNull(3, Types.INTEGER);
            else st.setInt(3, p.getValor());
            st.executeUpdate();
            try (ResultSet keys = st.getGeneratedKeys()) {
                if (keys.next()) p.setIdPagamento(keys.getInt(1));
            }
        }
    }

    public List<Pagamento> listByAluno(int idAluno) throws SQLException {
        List<Pagamento> list = new ArrayList<>();
        String sql = "SELECT * FROM Pagamento WHERE idAluno = ?";
        try (Connection conn = DB.getConnection(); PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, idAluno);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    public boolean existsByAluno(int idAluno) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Pagamento WHERE idAluno = ?";
        try (Connection conn = DB.getConnection(); PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, idAluno);
            try (ResultSet rs = st.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }

    public void deleteById(Integer id) throws SQLException {
        String sql = "DELETE FROM Pagamento WHERE idPagamento = ?";
        try (Connection conn = DB.getConnection(); PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, id);
            st.executeUpdate();
        }
    }

    private Pagamento map(ResultSet rs) throws SQLException {
        Pagamento p = new Pagamento();
        p.setIdPagamento(rs.getInt("idPagamento"));
        p.setIdAluno(rs.getInt("idAluno"));
        Date d = rs.getDate("data_pagamento");
        if (d != null) p.setDataPagamento(d.toLocalDate());
        Integer valor = rs.getObject("valor", Integer.class);
        p.setValor(valor);
        return p;
    }
}