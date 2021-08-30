package com.projectmanager.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.projectmanager.model.Grupo;
import com.projectmanager.model.RelacaoClasse;
import com.projectmanager.model.RelacaoTrabalho;
import com.projectmanager.model.Trabalho;

public interface RelacaoTrabalhoRepository extends CrudRepository<RelacaoTrabalho, String>{

	ArrayList<RelacaoTrabalho> findByGrupo(Grupo grupo);

	List<RelacaoTrabalho> findAllByRelacaoClasse(RelacaoClasse relacaoClasse);

	Trabalho findById(int id);
	
	RelacaoTrabalho findRelacaoTrabalhoById(int id);

	List<RelacaoTrabalho> findAllByTrabalho(Trabalho trabalho);
	
}
