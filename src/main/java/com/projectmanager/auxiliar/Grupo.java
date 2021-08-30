package com.projectmanager.auxiliar;

import java.util.ArrayList;

import com.projectmanager.model.Usuario;

public class Grupo {

	private com.projectmanager.model.Grupo grupo;
	
	private Usuario usuarioLider;
	
	private ArrayList<Usuario> listUsuarios;
	
	public Grupo(com.projectmanager.model.Grupo grupo, ArrayList<Usuario> lisUsuarios, Usuario usuarioLider) {
		super();
		this.listUsuarios = lisUsuarios;
		this.grupo = grupo;
		this.usuarioLider = usuarioLider;
	}

	public com.projectmanager.model.Grupo getGrupo() {
		return grupo;
	}

	public void setGrupo(com.projectmanager.model.Grupo grupo) {
		this.grupo = grupo;
	}
	
	public ArrayList<Usuario> getLisUsuarios() {
		return listUsuarios;
	}

	public void setLisUsuarios(ArrayList<Usuario> lisUsuarios) {
		this.listUsuarios = lisUsuarios;
	}

	public Usuario getUsuarioLider() {
		return usuarioLider;
	}

	public void setUsuarioLider(Usuario usuarioLider) {
		this.usuarioLider = usuarioLider;
	}

}
