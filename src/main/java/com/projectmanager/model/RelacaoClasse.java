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
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table (name = "RELACAO_CLASSE")
public class RelacaoClasse implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column (name = "RELACAO_CLASSE_ID")
	private int id;
	
	@ManyToOne (fetch = FetchType.EAGER)
	@JoinColumn (name = "ESC_ID", referencedColumnName = "ESC_APLIC_ID")
	private Escola escola;
	
	@ManyToOne (fetch = FetchType.EAGER)
	@JoinColumn (name = "UNI_ID", referencedColumnName = "UNI_APLIC_ID")
	private Unidade unidade;
	
	@ManyToOne (fetch = FetchType.EAGER)
	@JoinColumn (name = "CUR_ID", referencedColumnName = "CUR_APLIC_ID")
	private Curso curso;
	
	@ManyToOne (fetch = FetchType.EAGER)
	@JoinColumn (name = "DISC_ID", referencedColumnName = "DISC_APLIC_ID")
	private Disciplina disciplina;
	
	@ManyToOne (fetch = FetchType.EAGER)
	@JoinColumn (name = "CLAS_ID", referencedColumnName = "CLAS_APLIC_ID")
	private Classe classe;

	@Column (name = "DT_REGISTRO")
	private Date dtRegistro;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Escola getEscola() {
		return escola;
	}

	public void setEscola(Escola escola) {
		this.escola = escola;
	}

	public Unidade getUnidade() {
		return unidade;
	}

	public void setUnidade(Unidade unidade) {
		this.unidade = unidade;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public Disciplina getDisciplina() {
		return disciplina;
	}

	public void setDisciplina(Disciplina disciplina) {
		this.disciplina = disciplina;
	}

	public Classe getClasse() {
		return classe;
	}

	public void setClasse(Classe classe) {
		this.classe = classe;
	}

	public Date getDtRegistro() {
		return dtRegistro;
	}

	public void setDtRegistro(Date dtRegistro) {
		this.dtRegistro = dtRegistro;
	}
	
}
