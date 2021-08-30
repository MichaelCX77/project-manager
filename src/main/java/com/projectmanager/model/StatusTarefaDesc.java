package com.projectmanager.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Entity
@Table (name="STATUS_TAREFA_DESC")
public class StatusTarefaDesc implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column (name = "STATUS_ID")
	private int id;
	
	@Column (name = "STATUS_DESC")
	@NotEmpty
	private String descricao;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
}
