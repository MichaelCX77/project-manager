package com.projectmanager.repository;

import org.springframework.data.repository.CrudRepository;

import com.projectmanager.model.Usuario;

public interface UsuarioRepository extends CrudRepository<Usuario, String>{

	Usuario findByCpf(String cpf);
	
	Usuario findByEmail(String email);

	Usuario findById(int id);
	
}
