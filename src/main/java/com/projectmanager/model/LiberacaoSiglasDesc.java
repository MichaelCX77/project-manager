package com.projectmanager.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table (name = "LIB_SIGLAS_DESC")
public class LiberacaoSiglasDesc implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@Column (name = "LIB_TIPO")
	private char tipo;
	
	@Column (name = "LIB_DESC")
	private String descricao;

	public char getTipo() {
		return tipo;
	}

	public void setTipo(char tipo) {
		this.tipo = tipo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
}
