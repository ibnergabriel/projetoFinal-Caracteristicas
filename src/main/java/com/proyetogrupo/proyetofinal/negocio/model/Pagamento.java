package com.proyetogrupo.proyetofinal.negocio.model;

import java.time.LocalDate;

public class Pagamento {
    private Integer idPagamento;
    private Integer idAluno;
    private LocalDate dataPagamento;
    private Integer valor;

    public Integer getIdPagamento() { return idPagamento; }
    public void setIdPagamento(Integer idPagamento) { this.idPagamento = idPagamento; }

    public Integer getIdAluno() { return idAluno; }
    public void setIdAluno(Integer idAluno) { this.idAluno = idAluno; }

    public LocalDate getDataPagamento() { return dataPagamento; }
    public void setDataPagamento(LocalDate dataPagamento) { this.dataPagamento = dataPagamento; }

    public Integer getValor() { return valor; }
    public void setValor(Integer valor) { this.valor = valor; }
}