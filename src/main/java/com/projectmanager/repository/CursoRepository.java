package com.projectmanager.repository;

import org.springframework.data.repository.CrudRepository;

import com.projectmanager.model.Curso;

public interface CursoRepository extends CrudRepository<Curso, String>{

	Curso findById(Integer idCurso);

}
