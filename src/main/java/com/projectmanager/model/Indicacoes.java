package com.projectmanager.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table (name = "INDICACOES")
public class Indicacoes implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column (name = "IND_ID")
	private int id;

	@Column (name = "ID_INDICADOR")
	private int idicador;
	
	@Column (name = "ID_INDICADO")
	private int indicado;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdicador() {
		return idicador;
	}

	public void setIdicador(int idicador) {
		this.idicador = idicador;
	}

	public int getIndicado() {
		return indicado;
	}

	public void setIndicado(int indicado) {
		this.indicado = indicado;
	}
	
}
