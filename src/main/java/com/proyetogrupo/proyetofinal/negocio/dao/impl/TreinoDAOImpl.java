package com.proyetogrupo.proyetofinal.negocio.dao.impl;

import com.proyetogrupo.proyetofinal.negocio.dao.TreinoDAO;
import com.proyetogrupo.proyetofinal.negocio.model.Treino;

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

public class TreinoDAOImpl implements TreinoDAO {

    private final Connection connection;

    public TreinoDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<Treino> findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM Treino WHERE idTreino = ?";

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
    public Treino saveAndReturn(Treino treino) throws SQLException {
        String sql = "INSERT INTO Treino(idAluno, idProfessor, descricao, status, data_inicio, data_fim) " +
                     "VALUES (?,?,?,?,?,?)";

        try (PreparedStatement st = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, treino.getIdAluno());      // CPF do aluno
            st.setInt(2, treino.getIdProfessor());
            st.setString(3, treino.getDescricao());
            st.setString(4, treino.getStatus());

            if (treino.getDataInicio() != null) {
                st.setDate(5, Date.valueOf(treino.getDataInicio()));
            } else {
                st.setNull(5, Types.DATE);
            }

            if (treino.getDataFim() != null) {
                st.setDate(6, Date.valueOf(treino.getDataFim()));
            } else {
                st.setNull(6, Types.DATE);
            }

            st.executeUpdate();

            try (ResultSet keys = st.getGeneratedKeys()) {
                if (keys.next()) {
                    treino.setIdTreino(keys.getInt(1));
                }
            }
        }

        return treino;
    }

    @Override
    public void update(Treino treino) throws SQLException {
        String sql = "UPDATE Treino SET idAluno = ?, idProfessor = ?, descricao = ?, status = ?, " +
                     "data_inicio = ?, data_fim = ? WHERE idTreino = ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, treino.getIdAluno());
            st.setInt(2, treino.getIdProfessor());
            st.setString(3, treino.getDescricao());
            st.setString(4, treino.getStatus());

            if (treino.getDataInicio() != null) {
                st.setDate(5, Date.valueOf(treino.getDataInicio()));
            } else {
                st.setNull(5, Types.DATE);
            }

            if (treino.getDataFim() != null) {
                st.setDate(6, Date.valueOf(treino.getDataFim()));
            } else {
                st.setNull(6, Types.DATE);
            }

            st.setInt(7, treino.getIdTreino());
            st.executeUpdate();
        }
    }

    @Override
    public void deleteById(Integer id) throws SQLException {
        String sql = "DELETE FROM Treino WHERE idTreino = ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, id);
            st.executeUpdate();
        }
    }

    @Override
    public boolean existsActiveByAluno(String cpfAluno) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Treino WHERE idAluno = ? AND status = 'ATIVO'";

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
    public boolean existsActiveByProfessor(Integer idProfessor) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Treino WHERE idProfessor = ? AND status = 'ATIVO'";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, idProfessor);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    @Override
    public List<Treino> listByAluno(String cpfAluno) throws SQLException {
        List<Treino> list = new ArrayList<>();
        String sql = "SELECT * FROM Treino WHERE idAluno = ?";

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
    public List<Treino> listByProfessor(int idProfessor) throws SQLException {
        List<Treino> list = new ArrayList<>();
        String sql = "SELECT * FROM Treino WHERE idProfessor = ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, idProfessor);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        }

        return list;
    }

    private Treino map(ResultSet rs) throws SQLException {
        Treino t = new Treino();
        t.setIdTreino(rs.getInt("idTreino"));
        t.setIdAluno(rs.getString("idAluno"));       // CPF como String
        t.setIdProfessor(rs.getInt("idProfessor"));
        t.setDescricao(rs.getString("descricao"));
        t.setStatus(rs.getString("status"));

        Date dInicio = rs.getDate("data_inicio");
        if (dInicio != null) {
            t.setDataInicio(dInicio.toLocalDate());
        }

        Date dFim = rs.getDate("data_fim");
        if (dFim != null) {
            t.setDataFim(dFim.toLocalDate());
        }

        return t;
    }
}
