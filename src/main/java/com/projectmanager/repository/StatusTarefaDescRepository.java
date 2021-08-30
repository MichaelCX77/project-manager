package com.projectmanager.repository;

import org.springframework.data.repository.CrudRepository;

import com.projectmanager.model.StatusTarefaDesc;

public interface StatusTarefaDescRepository extends CrudRepository<StatusTarefaDesc, String> {

	StatusTarefaDesc findById(int id);

}
