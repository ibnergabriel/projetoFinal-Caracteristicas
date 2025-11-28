package com.proyetogrupo.proyetofinal.negocio.impl;

import com.proyetogrupo.proyetofinal.negocio.AlunoNegocio;
import com.proyetogrupo.proyetofinal.negocio.dao.AlunoDAO;
import com.proyetogrupo.proyetofinal.negocio.dao.PagamentoDAO;
import com.proyetogrupo.proyetofinal.negocio.dao.TreinoDAO;
import com.proyetogrupo.proyetofinal.negocio.exceptions.BusinessException;
import com.proyetogrupo.proyetofinal.negocio.model.Aluno;
import com.proyetogrupo.proyetofinal.negocio.util.ValidationUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public class AlunoNegocioImpl implements AlunoNegocio, AutoCloseable {

    private final AlunoDAO alunoDAO;
    private final PagamentoDAO pagamentoDAO;
    private final TreinoDAO treinoDAO;
    private final Connection connection;

    public AlunoNegocioImpl(AlunoDAO alunoDAO,
                            PagamentoDAO pagamentoDAO,
                            TreinoDAO treinoDAO,
                            Connection connection) {
        this.alunoDAO = alunoDAO;
        this.pagamentoDAO = pagamentoDAO;
        this.treinoDAO = treinoDAO;
        this.connection = connection;
    }

    // =======================
    // MÉTODOS DA INTERFACE
    // =======================

    @Override
    public Optional<Aluno> buscarPorId(String cpf) throws SQLException {
        if (ValidationUtil.isBlank(cpf)) {
            throw new BusinessException("CPF do aluno é obrigatório.");
        }
        return alunoDAO.findById(cpf);
    }

    @Override
    public void cadastrarAluno(Aluno aluno) throws SQLException {
        validarAluno(aluno, true);

        String cpf = aluno.getIdAluno(); // agora é String (CPF)
        if (alunoDAO.existsById(cpf)) {
            throw new BusinessException("Já existe um aluno cadastrado com esse CPF.");
        }

        alunoDAO.save(aluno);
    }

    @Override
    public void atualizarAluno(Aluno aluno) throws SQLException {
        validarAluno(aluno, false);

        String cpf = aluno.getIdAluno();
        if (!alunoDAO.existsById(cpf)) {
            throw new BusinessException("Aluno não encontrado para atualização.");
        }

        alunoDAO.update(aluno);
    }

    @Override
    public void removerAluno(String cpf) throws SQLException {
        if (ValidationUtil.isBlank(cpf)) {
            throw new BusinessException("CPF do aluno é obrigatório para remoção.");
        }

        if (!alunoDAO.existsById(cpf)) {
            throw new BusinessException("Aluno não encontrado para remoção.");
        }

        // Regra de negócio: não remover se tiver treino ativo ou pagamento
        if (treinoDAO.existsActiveByAluno(cpf)) {
            throw new BusinessException("Não é possível remover o aluno com treino ativo.");
        }

        if (pagamentoDAO.existsByAluno(cpf)) {
            throw new BusinessException("Não é possível remover o aluno com pagamentos registrados.");
        }

        alunoDAO.deleteById(cpf);
    }

    // =======================
    // SUPORTE / VALIDAÇÃO
    // =======================

    private void validarAluno(Aluno aluno, boolean novo) {
        if (aluno == null) {
            throw new BusinessException("Aluno não pode ser nulo.");
        }

        if (ValidationUtil.isBlank(aluno.getIdAluno())) {
            throw new BusinessException("CPF do aluno é obrigatório.");
        }

        if (ValidationUtil.isBlank(aluno.getNome())) {
            throw new BusinessException("Nome do aluno é obrigatório.");
        }

        if (!ValidationUtil.isBlank(aluno.getEmail())
                && !ValidationUtil.isValidEmail(aluno.getEmail())) {
            throw new BusinessException("E-mail do aluno é inválido.");
        }

        // aqui você pode colocar mais validações (idade, telefone, etc) se quiser
    }

    @Override
    public void close() throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
