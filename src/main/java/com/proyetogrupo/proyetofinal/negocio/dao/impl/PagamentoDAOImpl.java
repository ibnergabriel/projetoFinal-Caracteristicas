package com.proyetogrupo.proyetofinal.negocio.dao.impl;

import com.proyetogrupo.proyetofinal.negocio.dao.PagamentoDAO;
import com.proyetogrupo.proyetofinal.negocio.model.Pagamento;

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

public class PagamentoDAOImpl implements PagamentoDAO {

    private final Connection connection;

    public PagamentoDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<Pagamento> findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM Pagamento WHERE idPagamento = ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(map(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public void save(Pagamento p) throws SQLException {
        String sql = "INSERT INTO Pagamento(idAluno, data_pagamento, valor) VALUES (?,?,?)";

        try (PreparedStatement st = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, p.getIdAluno());   // CPF

            if (p.getDataPagamento() != null) {
                st.setDate(2, Date.valueOf(p.getDataPagamento()));
            } else {
                st.setNull(2, Types.DATE);
            }

            if (p.getValor() != null) {
                st.setInt(3, p.getValor());
            } else {
                st.setNull(3, Types.INTEGER);
            }

            st.executeUpdate();

            try (ResultSet keys = st.getGeneratedKeys()) {
                if (keys.next()) {
                    p.setIdPagamento(keys.getInt(1));
                }
            }
        }
    }

    @Override
    public List<Pagamento> listByAluno(String cpfAluno) throws SQLException {
        List<Pagamento> list = new ArrayList<>();
        String sql = "SELECT * FROM Pagamento WHERE idAluno = ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, cpfAluno);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        }

        return list;
    }

    @Override
    public boolean existsByAluno(String cpfAluno) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Pagamento WHERE idAluno = ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, cpfAluno);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }

        return false;
    }

    @Override
    public void deleteById(Integer id) throws SQLException {
        String sql = "DELETE FROM Pagamento WHERE idPagamento = ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, id);
            st.executeUpdate();
        }
    }

    private Pagamento map(ResultSet rs) throws SQLException {
        Pagamento p = new Pagamento();
        p.setIdPagamento(rs.getInt("idPagamento"));
        p.setIdAluno(rs.getString("idAluno"));  // CPF como String

        Date d = rs.getDate("data_pagamento");
        if (d != null) {
            p.setDataPagamento(d.toLocalDate());
        }

        Integer valor = rs.getObject("valor", Integer.class);
        p.setValor(valor);

        return p;
    }
}
