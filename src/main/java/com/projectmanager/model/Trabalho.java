package com.projectmanager.model;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table (name = "TRABALHO_APLIC")
public class Trabalho implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column (name = "TRAB_APLIC_ID")
	private int id;
	
	@Column (name = "TRAB_NOME")
	private String nome;
	
	@ManyToOne (fetch = FetchType.EAGER)
	@JoinColumn (name = "TRAB_PROFESSOR", referencedColumnName = "USU_APLIC_ID")
	private Usuario idProfessor;
	
	@Column (name = "TRAB_DESCRICAO")
	private String descricao;
	
	@Column (name = "TRAB_CRIACAO")
	private Date dtCriacao;
	
	@Column (name = "TRAB_ENTREGA")
	private Date dtEntrega;

	@Transient
	private String dtCriacaoFormatada;
	
	@Transient
	private String dtEntregaFormatada;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Date getDtCriacao() {
		return dtCriacao;
	}

	public void setDtCriacao(Date dtCriacao) {
		this.dtCriacao = dtCriacao;
	}

	public Date getDtEntrega() {
		return dtEntrega;
	}

	public void setDtEntrega(Date dtEntrega) {
		this.dtEntrega = dtEntrega;
	}

	public String getDtCriacaoFormatada() {
		return dtCriacaoFormatada;
	}

	public void setDtCriacaoFormatada(String dtCriacaoFormatada) {
		this.dtCriacaoFormatada = dtCriacaoFormatada;
	}

	public String getDtEntregaFormatada() {
		return dtEntregaFormatada;
	}

	public void setDtEntregaFormatada(String dtEntregaFormatada) {
		this.dtEntregaFormatada = dtEntregaFormatada;
	}

	public Usuario getIdProfessor() {
		return idProfessor;
	}

	public void setIdProfessor(Usuario idProfessor) {
		this.idProfessor = idProfessor;
	}
}
