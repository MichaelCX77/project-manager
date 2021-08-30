package com.projectmanager.model;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table (name = "CLASSE_APLIC")
public class Classe implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column (name = "CLAS_APLIC_ID")
	private int id;
	
	@Column (name = "CLAS_NOME")
	private String nome;
	
	@Column (name = "CLAS_COD")
	private String codigo;
	
	@Column (name = "CLAS_DATA_CRIACAO")
	private Date dtCriacao;
	
	@Column (name = "CLAS_PERIODO")
	private int periodo;
	
	@Column (name = "CLAS_SEMESTRE")
	private int semestre;
	
	@Column (name = "CLAS_ANO")
	private int ano;
	
	@Transient
	private Curso curso;	
	
	@Transient
	private String dtCriacaoFormatada;

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

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public Date getDtCriacao() {
		return dtCriacao;
	}

	public void setDtCriacao(Date dtCriacao) {
		this.dtCriacao = dtCriacao;
	}

	public int getPeriodo() {
		return periodo;
	}

	public void setPeriodo(int periodo) {
		this.periodo = periodo;
	}

	public int getSemestre() {
		return semestre;
	}

	public void setSemestre(int semestre) {
		this.semestre = semestre;
	}

	public int getAno() {
		return ano;
	}

	public void setAno(int ano) {
		this.ano = ano;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public String getDtCriacaoFormatada() {
		return dtCriacaoFormatada;
	}

	public void setDtCriacaoFormatada(String dtCriacaoFormatada) {
		this.dtCriacaoFormatada = dtCriacaoFormatada;
	}
}
