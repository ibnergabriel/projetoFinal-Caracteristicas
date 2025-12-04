package com.proyetogrupo.proyetofinal.negocio.dao.impl;

import java.util.Optional;
import com.proyetogrupo.proyetofinal.negocio.dao.AlunoDAO;
import com.proyetogrupo.proyetofinal.negocio.model.Aluno;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlunoDAOImpl implements AlunoDAO {

    private final Connection connection;


    public AlunoDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<Aluno> findById(String id) throws SQLException {
        String sql = "SELECT * FROM Aluno WHERE idAluno = ?";

        try (PreparedStatement st = this.connection.prepareStatement(sql)) {
            st.setString(1, id); // id = CPF
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(map(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public void save(Aluno a) throws SQLException {
        String sql = "INSERT INTO Aluno (idAluno, nome, idade, sexo, telefone, email, data_matricula) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement st = this.connection.prepareStatement(sql)) {


            st.setString(1, a.getIdAluno());


            st.setString(2, a.getNome());


            if (a.getIdade() != null) {
                st.setInt(3, a.getIdade());
            } else {
                st.setNull(3, Types.INTEGER);
            }


            st.setString(4, a.getSexo());


            st.setString(5, a.getTelefone());


            st.setString(6, a.getEmail());


            if (a.getDataMatricula() != null) {
                st.setDate(7, Date.valueOf(a.getDataMatricula()));
            } else {
                st.setNull(7, Types.DATE);
            }

            st.executeUpdate();
        }
    }

    @Override
    public void update(Aluno a) throws SQLException {
        String sql = "UPDATE Aluno "
                   + "SET nome = ?, idade = ?, sexo = ?, telefone = ?, email = ?, data_matricula = ? "
                   + "WHERE idAluno = ?";

        try (PreparedStatement st = this.connection.prepareStatement(sql)) {

            st.setString(1, a.getNome());

            if (a.getIdade() != null) {
                st.setInt(2, a.getIdade());
            } else {
                st.setNull(2, Types.INTEGER);
            }

            st.setString(3, a.getSexo());
            st.setString(4, a.getTelefone());
            st.setString(5, a.getEmail());

            if (a.getDataMatricula() != null) {
                st.setDate(6, Date.valueOf(a.getDataMatricula()));
            } else {
                st.setNull(6, Types.DATE);
            }

            // WHERE idAluno = ?
            st.setString(7, a.getIdAluno()); // CPF

            st.executeUpdate();
        }
    }

    @Override
    public void deleteById(String id) throws SQLException {
        String sql = "DELETE FROM Aluno WHERE idAluno = ?";

        try (PreparedStatement st = this.connection.prepareStatement(sql)) {
            st.setString(1, id); // CPF
            st.executeUpdate();
        }
    }

    @Override
    public boolean existsById(String id) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Aluno WHERE idAluno = ?";

        try (PreparedStatement st = this.connection.prepareStatement(sql)) {
            st.setString(1, id); // CPF
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
                return false;
            }
        }
    }

    @Override
    public List<Aluno> listAll() throws SQLException {
        List<Aluno> list = new ArrayList<>();
        String sql = "SELECT * FROM Aluno";

        try (PreparedStatement st = this.connection.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                list.add(map(rs));
            }
        }
        return list;
    }

    private Aluno map(ResultSet rs) throws SQLException {
        Aluno a = new Aluno();

        // CPF como String
        a.setIdAluno(rs.getString("idAluno"));

        a.setNome(rs.getString("nome"));

        int idade = rs.getInt("idade");
        if (!rs.wasNull()) {
            a.setIdade(idade);
        }

        a.setSexo(rs.getString("sexo"));
        a.setTelefone(rs.getString("telefone"));
        a.setEmail(rs.getString("email"));

        Date d = rs.getDate("data_matricula");
        if (d != null) {
            a.setDataMatricula(d.toLocalDate());
        }

        return a;
    }
}
