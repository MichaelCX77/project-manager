package com.projectmanager.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table (name = "GRUPO_APLIC")
public class Grupo implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column (name = "GRUPO_APLIC_ID")
	private int id;
	
	@Column (name = "GRUPO_ID")
	private int idGrupo;
	
	@Column (name = "GRUPO_NOME")
	private String nome;
	
	@Column (name = "GRUPO_USU_ID")
	private int usuario;
	
	@Column (name = "GRUPO_USU_LIDER")
	private String usuarioLider;
	
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

	public int getUsuario() {
		return usuario;
	}

	public void setUsuario(int usuario) {
		this.usuario = usuario;
	}

	public String getUsuarioLider() {
		return usuarioLider;
	}

	public void setUsuarioLider(String usuarioLider) {
		this.usuarioLider = usuarioLider;
	}

	public int getIdGrupo() {
		return idGrupo;
	}

	public void setIdGrupo(int idGrupo) {
		this.idGrupo = idGrupo;
	}
	
}
