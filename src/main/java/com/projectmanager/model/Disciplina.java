package com.projectmanager.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Entity
@Table (name = "DISCIPLINA_APLIC")
public class Disciplina implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column (name ="DISC_APLIC_ID")
	private int id;
	
	@Column (name ="DISC_NOME")
	private String nome;
	
	@Column (name ="DISC_DESCRICAO")
	private String descricao;
	
	@Column (name ="DISC_CODIGO")
	private String codigo;
	
	public Disciplina() {
	}
	
	public Disciplina(int id, String nome, String descricao, String codigo) {
		super();
		this.id = id;
		this.nome = nome;
		this.descricao = descricao;
		this.codigo = codigo;
	}

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

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
}
