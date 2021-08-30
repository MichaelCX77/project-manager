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
import javax.persistence.Transient;

@Entity
@Table (name ="RELACAO_ACESSO")
public class RelacaoAcesso implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column (name = "RELACAO_ACESSO_ID")
	private int id;
	
	@ManyToOne (fetch = FetchType.EAGER)
	@JoinColumn (name = "RELACAO_CLASSE_ID", referencedColumnName = "RELACAO_CLASSE_ID")
	private RelacaoClasse relacaoClasse;
	
	@ManyToOne (fetch = FetchType.EAGER)
	@JoinColumn (name = "USU_SOLICT_ID", referencedColumnName = "USU_APLIC_ID")
	private Usuario usuarioSolicitante;
	
	@ManyToOne (fetch = FetchType.EAGER)
	@JoinColumn (name = "USU_ID", referencedColumnName = "USU_APLIC_ID")
	private Usuario usuarioIncluido;
	
	@Column (name = "ACES")
	private char acesso;
	
	@Column (name = "DT_REGISTRO")
	private Date dtRegistro;
	
	@Column (name = "DT_VENCIMENTO")
	private Date dtVencimento;
	
	@Transient
	private String dtRegistroFormatada;
	
	@Transient
	private String dtVencimentoFormatada;

	public RelacaoAcesso() {
	}

	public RelacaoAcesso(Usuario usuarioIncluido, RelacaoClasse relacaoClasse) {
		super();
		this.usuarioIncluido = usuarioIncluido;
		this.relacaoClasse = relacaoClasse;
	}

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

	public Usuario getUsuarioSolicitante() {
		return usuarioSolicitante;
	}

	public void setUsuarioSolicitante(Usuario usuarioSolicitante) {
		this.usuarioSolicitante = usuarioSolicitante;
	}

	public Usuario getUsuarioIncluido() {
		return usuarioIncluido;
	}

	public void setUsuarioIncluido(Usuario usuarioIncluido) {
		this.usuarioIncluido = usuarioIncluido;
	}

	public char getAcesso() {
		return acesso;
	}

	public void setAcesso(char acesso) {
		this.acesso = acesso;
	}

	public Date getDtRegistro() {
		return dtRegistro;
	}

	public void setDtRegistro(Date dtRegistro) {
		this.dtRegistro = dtRegistro;
	}

	public Date getDtVencimento() {
		return dtVencimento;
	}

	public void setDtVencimento(Date dtVencimento) {
		this.dtVencimento = dtVencimento;
	}

	public String getDtRegistroFormatada() {
		return dtRegistroFormatada;
	}

	public void setDtRegistroFormatada(String string) {
		this.dtRegistroFormatada = string;
	}

	public String getDtVencimentoFormatada() {
		return dtVencimentoFormatada;
	}

	public void setDtVencimentoFormatada(String dtVencimentoFormatada) {
		this.dtVencimentoFormatada = dtVencimentoFormatada;
	}
}
