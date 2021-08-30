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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table (name = "TAREFA_APLIC")
public class Tarefa implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column (name = "TAR_APLIC_ID")
	private int id;
	
	@Column (name = "TAR_NOME")
	private String nome;
	
	@Column (name = "TAR_DESC")
	private String descricao;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TAR_RESP_ID", referencedColumnName = "USU_APLIC_ID")
	private Usuario usuarioResp;
	
	@Column (name = "TAR_INI")
	private Date dtInicial;
	
	@Column (name = "TAR_FIM")
	private Date dtFinal;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "TAR_STATUS", referencedColumnName = "STATUS_ID")
	private StatusTarefaDesc statusId;
	
	@Transient
	private Disciplina disciplina;
	
	@Transient
	private Classe classe;
	
	@Transient
	private Curso curso;
	
	@Transient
	private String dtInicialFormatada;
	
	@Transient
	private String dtFinalFormatada;
	
	public Usuario getUsuarioResp() {
		return usuarioResp;
	}

	public void setUsuarioResp(Usuario usuarioResp) {
		this.usuarioResp = usuarioResp;
	}

	public StatusTarefaDesc getStatusId() {
		return statusId;
	}

	public void setStatusId(StatusTarefaDesc statusId) {
		this.statusId = statusId;
	}

	public Disciplina getDisciplina() {
		return disciplina;
	}

	public void setDisciplina(Disciplina disciplina) {
		this.disciplina = disciplina;
	}

	
	public Classe getClasse() {
		return classe;
	}

	public void setClasse(Classe classe) {
		this.classe = classe;
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

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Usuario getResponsavel() {
		return usuarioResp;
	}

	public void setResponsavel(Usuario usuario) {
		this.usuarioResp = usuario;
	}

	public Date getDtInicial() {
		return dtInicial;
	}

	public void setDtInicial(Date dtInicial) {
		this.dtInicial = dtInicial;
	}

	public Date getDtFinal() {
		return dtFinal;
	}

	public void setDtFinal(Date dtFinal) {
		this.dtFinal = dtFinal;
	}

	public String getDtInicialFormatada() {
		return dtInicialFormatada;
	}

	public void setDtInicialFormatada(String dtInicialFormatada) {
		this.dtInicialFormatada = dtInicialFormatada;
	}

	public String getDtFinalFormatada() {
		return dtFinalFormatada;
	}

	public void setDtFinalFormatada(String dtFinalFormatada) {
		this.dtFinalFormatada = dtFinalFormatada;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

}
