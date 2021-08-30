package com.projectmanager.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table (name = "VIEW_PONTUACAO")
public class VwPontuacao implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column (name = "USU_ID")
	private int id;

	@Column (name = "SOMA_PONTOS")
	private int pontuacao;
	
	@Column (name = "QTD_AVALIACOES")
	private int qtdAvaliacoes;
	
	@Column (name = "MEDIA_PONTUACAO")
	private double mediaPontuacao;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPontuacao() {
		return pontuacao;
	}

	public void setPontuacao(int pontuacao) {
		this.pontuacao = pontuacao;
	}

	public int getQtdAvaliacoes() {
		return qtdAvaliacoes;
	}

	public void setQtdAvaliacoes(int qtdAvaliacoes) {
		this.qtdAvaliacoes = qtdAvaliacoes;
	}

	public double getMediaPontuacao() {
		return mediaPontuacao;
	}

	public void setMediaPontuacao(double mediaPontuacao) {
		this.mediaPontuacao = mediaPontuacao;
	}
	
}
