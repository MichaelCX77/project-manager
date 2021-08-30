package com.projectmanager.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table (name = "RELACAO_TRAB")
public class RelacaoTrabalho implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column (name = "RELACAO_TRAB_ID")
	private int id;
	
	@ManyToOne (fetch = FetchType.EAGER)
	@JoinColumn (name = "RELACAO_CLASSE_ID", referencedColumnName = "RELACAO_CLASSE_ID")
	private RelacaoClasse relacaoClasse;
	
	@ManyToOne (fetch = FetchType.EAGER)
	@JoinColumn (name = "TRAB_ID", referencedColumnName = "TRAB_APLIC_ID")
	private Trabalho trabalho;
	
	@ManyToOne (fetch = FetchType.EAGER)
	@JoinColumn (name = "GRUPO_ID", referencedColumnName = "GRUPO_APLIC_ID")
	private Grupo grupo;
	
	@ManyToOne (fetch = FetchType.EAGER)
	@JoinColumn (name = "TAR_ID", referencedColumnName = "TAR_APLIC_ID")
	private Tarefa tarefa;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public RelacaoClasse getRelacaoClasse() {
		return relacaoClasse;
	}

	public void setRelacaoClasse(RelacaoClasse relacaoClasse) {
		this.relacaoClasse = relacaoClasse;
	}

	public Trabalho getTrabalho() {
		return trabalho;
	}

	public void setTrabalho(Trabalho trabalho) {
		this.trabalho = trabalho;
	}

	public Grupo getGrupo() {
		return grupo;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}

	public Tarefa getTarefa() {
		return tarefa;
	}

	public void setTarefa(Tarefa tarefa) {
		this.tarefa = tarefa;
	}
	
}
