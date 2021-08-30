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
@Table (name = "RELACAO_NOTAS")
public class RelacaoNotas implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column (name = "RELACAO_NOTAS_ID")
	private int id;
	
	@NotEmpty
	@ManyToOne (fetch = FetchType.EAGER)
	@JoinColumn (name = "GRUPO_ID", referencedColumnName = "GRUPO_APLIC_ID")
	private Grupo grupo;
	
	@NotEmpty
	@ManyToOne (fetch = FetchType.EAGER)
	@JoinColumn (name = "USU_ID", referencedColumnName = "USU_APLIC_ID")
	private Usuario usuario;
	
	@Column (name = "NOTA")
	private double nota;
	
	@Column (name = "PONTUACAO")
	private int pontuacao;
	
	@ManyToOne (fetch = FetchType.EAGER)
	@JoinColumn (name = "AVAL_POS_ID", referencedColumnName = "AVAL_ID")
	private AvaliacaoDesc avPositiva;
	
	@ManyToOne (fetch = FetchType.EAGER)
	@JoinColumn (name = "AVAL_NEG_ID", referencedColumnName = "AVAL_ID")
	private AvaliacaoDesc avNegativa;
	
	@Column (name = "AVAL_COMENT")
	private int comentario;
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Grupo getGrupo() {
		return grupo;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public double getNota() {
		return nota;
	}

	public void setNota(double nota) {
		this.nota = nota;
	}

	public int getPontuacao() {
		return pontuacao;
	}

	public void setPontuacao(int pontuacao) {
		this.pontuacao = pontuacao;
	}

	public AvaliacaoDesc getAvPositiva() {
		return avPositiva;
	}

	public void setAvPositiva(AvaliacaoDesc avPositiva) {
		this.avPositiva = avPositiva;
	}

	public AvaliacaoDesc getAvNegativa() {
		return avNegativa;
	}

	public void setAvNegativa(AvaliacaoDesc avNegativa) {
		this.avNegativa = avNegativa;
	}

	public int getComentario() {
		return comentario;
	}

	public void setComentario(int comentario) {
		this.comentario = comentario;
	}
	
}
