package com.projectmanager.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

@Entity
@Table (name = "PERFIL_DESC")
public class PerfilDesc implements Serializable,GrantedAuthority{

	private static final long serialVersionUID = 1L;

	@Id
	@Column (name = "PERFIL_ID")
	private int id;
	
	@Column (name = "PERFIL_NOME")
	private String nome;
	
	@Column (name = "PERFIL_DESC")
	private String descricao;

	@ManyToMany(mappedBy = "perfis")
    private List<Usuario> usuarios;

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
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

	@Override
	public String getAuthority() {
		// TODO Auto-generated method stub
		return this.nome;
	}
	
}
