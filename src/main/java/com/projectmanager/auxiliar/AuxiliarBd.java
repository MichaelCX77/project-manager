package com.projectmanager.auxiliar;

import java.util.ArrayList;
import java.util.List;

import com.projectmanager.model.Classe;
import com.projectmanager.model.Curso;
import com.projectmanager.model.Disciplina;
import com.projectmanager.model.RelacaoClasse;
import com.projectmanager.model.RelacaoTrabalho;
import com.projectmanager.model.Unidade;
import com.projectmanager.util.DatasUtil;

public class AuxiliarBd {

	public static List<Unidade> listaUnidade(List<RelacaoClasse> listaRelacaoClasse) {
		
		List<Unidade> listaUnidades = new ArrayList<Unidade>();
		
		for (int i = 0; i < listaRelacaoClasse.size(); i++) {
			
			RelacaoClasse relacaoClasse = listaRelacaoClasse.get(i);
			
			if(relacaoClasse.getUnidade() != null && !listaUnidades.contains(relacaoClasse.getUnidade())) {
				
				listaUnidades.add(relacaoClasse.getUnidade());
			}
		}
		return listaUnidades;
	}
	
	public static List<Curso> listaCurso(List<RelacaoClasse> listaRelacaoClasse) {
		
		List<Curso> listaCursos = new ArrayList<Curso>();
		
		for (int i = 0; i < listaRelacaoClasse.size(); i++) {
			
			RelacaoClasse relacaoClasse = listaRelacaoClasse.get(i);
			
			if(relacaoClasse.getCurso() != null && !listaCursos.contains(relacaoClasse.getCurso())) {
				
				listaCursos.add(relacaoClasse.getCurso());
			}
		}
		return listaCursos;
	}
	
	public static List<Disciplina> listaDisciplina(List<RelacaoClasse> listaRelacaoClasse) {
		
		List<Disciplina> listaDisciplina = new ArrayList<Disciplina>();
		
		for (int i = 0; i < listaRelacaoClasse.size(); i++) {
			
			RelacaoClasse relacaoClasse = listaRelacaoClasse.get(i);
			
			if(relacaoClasse.getDisciplina() != null && !listaDisciplina.contains(relacaoClasse.getDisciplina())) {
				
				listaDisciplina.add(relacaoClasse.getDisciplina());
			}
		}
		return listaDisciplina;
	}
	
	public static List<Classe> listaClasse(List<RelacaoClasse> listaRelacaoClasse) {
		
		List<Classe> listaClasse = new ArrayList<Classe>();
		
		for (int i = 0; i < listaRelacaoClasse.size(); i++) {
			
			RelacaoClasse relacaoClasse = listaRelacaoClasse.get(i);
			
			if(relacaoClasse.getClasse() != null && !listaClasse.contains(relacaoClasse.getClasse())) {
				
				listaClasse.add(relacaoClasse.getClasse());
			}
		}
		return listaClasse;
	}

	public static List<RelacaoClasse> listaSalas(List<RelacaoClasse> listaRelacaoClasse) {
		
		List<RelacaoClasse> listaSalas = new ArrayList<RelacaoClasse>();
		
		for (int i = 0; i < listaRelacaoClasse.size(); i++) {
			
			RelacaoClasse relacaoClasse = listaRelacaoClasse.get(i);
			
			if(relacaoClasse.getClasse() != null && 
					relacaoClasse.getCurso() != null &&
					relacaoClasse.getDisciplina() != null && 
					!listaSalas.contains(relacaoClasse)) {
				
				listaSalas.add(relacaoClasse);
			}
		}
		
		return listaSalas;
	}
	
	public static List<RelacaoClasse> listaRelacaoClassePorUnidade(List<RelacaoClasse> listaRelacaoClasse) {
		
		List<RelacaoClasse> listaUnidades = new ArrayList<RelacaoClasse>();
		
		for (int i = 0; i < listaRelacaoClasse.size(); i++) {
			
			RelacaoClasse relacaoClasse = listaRelacaoClasse.get(i);
			
			if(relacaoClasse.getUnidade() != null && relacaoClasse.getCurso() == null && relacaoClasse.getDisciplina() == null && 
					relacaoClasse.getClasse() == null && !listaUnidades.contains(relacaoClasse)) {
				
				listaUnidades.add(relacaoClasse);
			}
		}
		return listaUnidades;
	}
	
	public static List<RelacaoTrabalho> listaRelacaoTrabalhoPorTrabalho(List<RelacaoTrabalho> listaRelacaoTrabalho){
		
		List<RelacaoTrabalho> listaTrabalhos = new ArrayList<RelacaoTrabalho>();
		
		for (int i = 0; i < listaRelacaoTrabalho.size(); i++) {
			
			RelacaoTrabalho relacaoTrabalho = listaRelacaoTrabalho.get(i);
			
			if(relacaoTrabalho.getTrabalho() != null && relacaoTrabalho.getGrupo() == null && relacaoTrabalho.getTarefa() == null) {
				relacaoTrabalho.getTrabalho().setDtCriacaoFormatada(DatasUtil.convertAndFormatToString(relacaoTrabalho.getTrabalho().getDtCriacao(), "dd/MM/yyyy"));
				relacaoTrabalho.getTrabalho().setDtEntregaFormatada(DatasUtil.convertAndFormatToString(relacaoTrabalho.getTrabalho().getDtEntrega(), "dd/MM/yyyy"));
				listaTrabalhos.add(relacaoTrabalho);
			}
		}
		return listaTrabalhos;
	}
	
}
