package com.projectmanager.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.projectmanager.model.Tarefa;
import com.projectmanager.model.Usuario;

public interface TarefaRepository extends CrudRepository<Tarefa, String>{

	Tarefa findAllById(int i);

	ArrayList<Tarefa> findAllByUsuarioResp(Usuario usuario);

}
