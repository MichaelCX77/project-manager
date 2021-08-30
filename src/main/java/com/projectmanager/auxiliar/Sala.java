package com.projectmanager.auxiliar;

import java.util.ArrayList;

import com.projectmanager.model.Classe;
import com.projectmanager.model.Curso;
import com.projectmanager.model.Disciplina;

public class Sala {
	
	private Curso curso;
	
	private Classe classe;
	
	private ArrayList<Disciplina> disciplinas;
	
	public Sala(Curso curso, Classe classe, ArrayList<Disciplina> disciplinas) {
		super();
		this.curso = curso;
		this.classe = classe;
		this.disciplinas = disciplinas;
	}

	public ArrayList<Disciplina> getDisciplinas() {
		return disciplinas;
	}

	public void setDisciplinas(ArrayList<Disciplina> disciplinas) {
		this.disciplinas = disciplinas;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public Classe getClasse() {
		return classe;
	}

	public void setClasse(Classe classe) {
		this.classe = classe;
	}
}
