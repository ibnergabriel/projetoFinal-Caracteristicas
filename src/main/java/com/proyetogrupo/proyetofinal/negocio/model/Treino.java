package com.proyetogrupo.proyetofinal.negocio.model;

import java.time.LocalDate;

public class Treino {
    private Integer idTreino;
    private String idAluno;
    private Integer idProfessor;
    private String descricao;
    private String status;
    private LocalDate dataInicio;
    private LocalDate dataFim;

    public Integer getIdTreino() { return idTreino; }
    public void setIdTreino(Integer idTreino) { this.idTreino = idTreino; }

    public String getIdAluno() { return idAluno; }
    public void setIdAluno(String idAluno) { this.idAluno = idAluno; }

    public Integer getIdProfessor() { return idProfessor; }
    public void setIdProfessor(Integer idProfessor) { this.idProfessor = idProfessor; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }

    public LocalDate getDataFim() { return dataFim; }
    public void setDataFim(LocalDate dataFim) { this.dataFim = dataFim; }
}