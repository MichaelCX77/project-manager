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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Entity
@Table (name = "LIBERA_APLIC")
public class Liberacao implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column (name = "LIBERA_APLIC_ID")
	private int id;
	
	@ManyToOne (fetch = FetchType.EAGER)
	@JoinColumn (name = "RELACAO_CLASSE_ID", referencedColumnName = "RELACAO_CLASSE_ID")
	private RelacaoClasse relacaoClasse;
	
	@ManyToOne (fetch = FetchType.EAGER)
	@JoinColumn (name = "LIB_CPF", referencedColumnName = "USU_CPF")
	private Usuario usuario;
	
	@ManyToOne (fetch = FetchType.EAGER)
	@JoinColumn (name = "LIB_TIPO", referencedColumnName = "LIB_TIPO")
	private LiberacaoSiglasDesc liberacaoSiglas;
	
	@OneToOne (fetch = FetchType.EAGER)
	@JoinColumn (name = "CODIGOS_ID", referencedColumnName = "CODIGOS_APLIC_ID")
	private Codigos codigo;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public RelacaoClasse getRelacaoClasse() {
		return relacaoClasse;
	}

	public void setRelacaoClasse(RelacaoClasse relacaoClasse) {
		this.relacaoClasse = relacaoClasse;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public LiberacaoSiglasDesc getLiberacaoSiglas() {
		return liberacaoSiglas;
	}

	public void setLiberacaoSiglas(LiberacaoSiglasDesc liberacaoSiglas) {
		this.liberacaoSiglas = liberacaoSiglas;
	}

	public Codigos getCodigo() {
		return codigo;
	}

	public void setCodigo(Codigos codigo) {
		this.codigo = codigo;
	}
	
}
