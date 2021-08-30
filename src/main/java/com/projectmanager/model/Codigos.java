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

@Entity
@Table (name = "CODIGOS_APLIC")
public class Codigos implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column (unique = true,name = "CODIGOS_APLIC_ID")
	private int id;
	
	@Column (name = "USU_EMAIL")
	private String usuarioEmail;
	
	@ManyToOne (fetch = FetchType.EAGER)
	@JoinColumn (name = "COD_TIPO_DESC", referencedColumnName = "COD_TIPO_SIGLA")
	private CodigoTipoDesc codigoTipo;
	
	@Column (name = "CODIGO")
	private String codigo;
	
	@Column (name = "DT_GERACAO")
	private Date dtGeracao;
	
	@Column (name = "ATIVO")
	private char ativo;


	public CodigoTipoDesc getCodigoTipo() {
		return codigoTipo;
	}

	public void setCodigoTipo(CodigoTipoDesc codigoTipo) {
		this.codigoTipo = codigoTipo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsuarioEmail() {
		return usuarioEmail;
	}

	public void setUsuarioEmail(String usuarioEmail) {
		this.usuarioEmail = usuarioEmail;
	}

	public CodigoTipoDesc getTipo() {
		return codigoTipo;
	}

	public void setTipo(CodigoTipoDesc codigoTipo) {
		this.codigoTipo = codigoTipo;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public Date getDtGeracao() {
		return dtGeracao;
	}

	public void setDtGeracao(Date dtGeracao) {
		this.dtGeracao = dtGeracao;
	}

	public char getAtivo() {
		return ativo;
	}

	public void setAtivo(char ativo) {
		this.ativo = ativo;
	}
	
}
