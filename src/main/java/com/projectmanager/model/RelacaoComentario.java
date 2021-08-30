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
@Table (name = "RELACAO_COMENT")
public class RelacaoComentario implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column (name = "RELACAO_COMENT_ID")
	private int id;
	
	@NotEmpty
	@ManyToOne (fetch = FetchType.EAGER)
	@JoinColumn (name = "TAR_ID", referencedColumnName = "TAR_APLIC_ID")
	private Tarefa tarefa;
	
	@NotEmpty
	@ManyToOne (fetch = FetchType.EAGER)
	@JoinColumn (name = "USU_ID", referencedColumnName = "USU_APLIC_ID")
	private Usuario usuario;
	
	@NotNull
	@Column (name = "COMENT_DATE")
	private Date dtComentario;

	@NotEmpty
	@Column (name = "COMENT_DESC")
	private String descricao;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Tarefa getTarefa() {
		return tarefa;
	}

	public void setTarefa(Tarefa tarefa) {
		this.tarefa = tarefa;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Date getDtComentario() {
		return dtComentario;
	}

	public void setDtComentario(Date dtComentario) {
		this.dtComentario = dtComentario;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
}
