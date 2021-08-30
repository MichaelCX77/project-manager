package com.projectmanager.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.projectmanager.model.RelacaoAcesso;
import com.projectmanager.model.RelacaoClasse;
import com.projectmanager.model.Usuario;

public interface RelacaoAcessoRepository extends CrudRepository<RelacaoAcesso, String>{

	ArrayList<RelacaoAcesso> findAllByUsuarioIncluidoAndAcesso(Usuario usuario, char c);

	List<RelacaoAcesso> findByRelacaoClasse(RelacaoClasse relacaoClasse);

	List<RelacaoAcesso> findByUsuarioIncluido(Usuario usuarioIncluido);
}
