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
import javax.validation.constraints.NotEmpty;

@Entity
@Table (name ="RELACAO_PERFIL")
public class RelacaoPerfil implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column (name = "RELACAO_PERFIL_ID")
	private int id;
	
	@ManyToOne (fetch = FetchType.EAGER)
	@JoinColumn (name = "UNI_ID", referencedColumnName = "UNI_APLIC_ID")
	private Unidade unidade;
	
	@ManyToOne (fetch = FetchType.EAGER)
	@JoinColumn (name = "USU_ID", referencedColumnName = "USU_APLIC_ID")
	private Usuario usuario;
	
	@ManyToOne (fetch = FetchType.EAGER)
	@JoinColumn (name = "PERFIL_ID", referencedColumnName = "PERFIL_ID")
	private PerfilDesc perfil;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Unidade getUnidade() {
		return unidade;
	}

	public void setUnidade(Unidade unidade) {
		this.unidade = unidade;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public PerfilDesc getPerfil() {
		return perfil;
	}

	public void setPerfil(PerfilDesc perfil) {
		this.perfil = perfil;
	}

}
