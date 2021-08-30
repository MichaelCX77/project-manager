package com.projectmanager.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table (name = "COD_TIPO_DESC")
public class CodigoTipoDesc implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@Column (name = "COD_TIPO_SIGLA")
	private char id;
	
	@Column (name ="COD_TIPO_DESC")
	private String descricao;

	public CodigoTipoDesc() {

	}
	
	public CodigoTipoDesc(char id) {
		this.id = id;
	}
	
	public char getId() {
		return id;
	}

	public void setId(char id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
}
