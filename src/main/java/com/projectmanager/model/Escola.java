package com.projectmanager.model;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table (name = "ESCOLA_APLIC")
public class Escola implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column (name = "ESC_APLIC_ID")
	private int id;
	
	@Column (name = "ESC_NOME")
	private String nome;
	
	@Column (name = "ESC_CNPJ")
	private String cnpj;
	
	@Column (name = "DIR_FUND_NOME")
	private String nomeFundador;
	
	@Column (name = "DIR_FUND_EMAIL")
	private String emailFundador;
	
	@Column (name = "DIR_FUND_TELEFONE")
	private String telefoneFundador;

	@Column (name = "DIR_FUND_CPF")
	private String cpfFundador;
	
	@Column (name = "DIR_FUND_DT_NASC")
	private Date dtNascFundador;
	
	@Column (name = "SENHA_DEFAULT")
	private String senha;

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

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getNomeFundador() {
		return nomeFundador;
	}

	public void setNomeFundador(String nomeFundador) {
		this.nomeFundador = nomeFundador;
	}

	public String getEmailFundador() {
		return emailFundador;
	}

	public void setEmailFundador(String emailFundador) {
		this.emailFundador = emailFundador;
	}

	public String getTelefoneFundador() {
		return telefoneFundador;
	}

	public void setTelefoneFundador(String telefoneFundador) {
		this.telefoneFundador = telefoneFundador;
	}

	public String getCpfFundador() {
		return cpfFundador;
	}

	public void setCpfFundador(String cpfFundador) {
		this.cpfFundador = cpfFundador;
	}

	public Date getDtNascFundador() {
		return dtNascFundador;
	}

	public void setDtNascFundador(Date dtNascFundador) {
		this.dtNascFundador = dtNascFundador;
	}
	
	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
	
}
