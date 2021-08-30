package com.projectmanager.model;

import java.io.Serializable;
import java.sql.Date;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table (name = "USUARIO_APLIC")
public class Usuario implements Serializable, UserDetails{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column (name = "USU_APLIC_ID")
	private int id;
	
	@Column (name = "USU_NOME")
	private String nome;
	
	@Column (name = "USU_EMAIL")
	private String email;
	
	@Column (name = "USU_NASC")
	private Date dtNasc;
	
	@Column (name = "USU_REGISTRO")
	private String registro;
	
	@Column (name = "USU_CPF")
	private String cpf;
	
	@Column (name ="USU_SENHA")
	private String senha;
	
	@Column (name = "USU_ATIVO")
	private char ativo;
	
	@Column (name = "USU_CRIACAO")
	private Date dtCriacao;
	
	@Column (name = "USU_ULTIMO_ACESSO")
	private Date dtUltimoAcesso;
	
	@Column (name  ="USU_TENTATIVAS")
	private int tentativasLogin;

	@ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
	@JoinTable( 
	        name = "RELACAO_PERFIL", 
	        joinColumns = @JoinColumn(
	          name = "USU_ID", referencedColumnName = "USU_APLIC_ID"), 
	        inverseJoinColumns = @JoinColumn(
	          name = "PERFIL_ID", referencedColumnName = "PERFIL_ID")) 
    private List<PerfilDesc> perfis;
	
	public Usuario() {
	}

	public Usuario(int id, String nome, String registro) {
		super();
		this.id = id;
		this.nome = nome;
		this.registro = registro;
	}

	public List<PerfilDesc> getPerfis() {
		return perfis;
	}

	public void setPerfis(List<PerfilDesc> perfis) {
		this.perfis = perfis;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getDtNasc() {
		return dtNasc;
	}

	public void setDtNasc(Date dtNasc) {
		this.dtNasc = dtNasc;
	}

	public String getRegistro() {
		return registro;
	}

	public void setRegistro(String registro) {
		this.registro = registro;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public char getAtivo() {
		return ativo;
	}

	public void setAtivo(char ativo) {
		this.ativo = ativo;
	}

	public Date getDtCriacao() {
		return dtCriacao;
	}

	public void setDtCriacao(Date dtCriacao) {
		this.dtCriacao = dtCriacao;
	}

	public Date getDtUltimoAcesso() {
		return dtUltimoAcesso;
	}

	public void setDtUltimoAcesso(Date dtUltimoAcesso) {
		this.dtUltimoAcesso = dtUltimoAcesso;
	}

	public int getTentativasLogin() {
		return tentativasLogin;
	}

	public void setTentativasLogin(int tentativasLogin) {
		this.tentativasLogin = tentativasLogin;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		return (Collection<? extends GrantedAuthority>) this.perfis;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return this.senha;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.email;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
	
}
