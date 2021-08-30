//package com.projectmanager.model;
//
//import java.util.List;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Id;
//import javax.persistence.ManyToMany;
//import javax.persistence.Table;
//
//import org.springframework.security.core.GrantedAuthority;
//
//@Entity
//@Table (name = "ROLE")
//public class Role implements GrantedAuthority{
//
//	private static final long serialVersionUID = 1L;
//
//	@Id
//	@Column (name = "NOME_ROLE")
//	private String nomeRole;
//
//	@ManyToMany(mappedBy = "roles")
//    private List<Usuario> usuarios;
//	
//	public String getNomeRole() {
//		return nomeRole;
//	}
//
//	public void setNomeRole(String nomeRole) {
//		this.nomeRole = nomeRole;
//	}
//	
//	public List<Usuario> getUsuarios() {
//		return usuarios;
//	}
//
//	public void setUsuarios(List<Usuario> usuarios) {
//		this.usuarios = usuarios;
//	}
//
//	@Override
//	public String getAuthority() {
//		// TODO Auto-generated method stub
//		return this.nomeRole;
//	}
//
//}
