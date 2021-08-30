package com.projectmanager.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.projectmanager.model.Classe;
import com.projectmanager.model.Curso;
import com.projectmanager.model.Disciplina;
import com.projectmanager.model.Escola;
import com.projectmanager.model.RelacaoClasse;
import com.projectmanager.model.Unidade;
import com.projectmanager.model.Usuario;

public interface RelacaoClasseRepository extends CrudRepository<RelacaoClasse, String>{

	ArrayList<RelacaoClasse> findByClasse(Classe classe);

	ArrayList<Unidade> findByUnidade(Unidade unidade);

	List<RelacaoClasse> findAllByEscola(Escola escola);

	List<RelacaoClasse> findAllByCurso(Curso curso);

	List<RelacaoClasse> findAllByUnidadeAndCurso(Unidade unidade,Curso curso);

	List<Escola> findEscolaByUnidade(Unidade unidade);

	boolean existsByEscolaAndUnidadeAndCursoAndDisciplinaAndClasse(Escola escola, Unidade unidade, Curso curso,
			Disciplina disciplina, Classe classe);

	RelacaoClasse findById(Integer idRelacaoClasse);

	@Query("SELECT rl FROM RelacaoClasse rl WHERE rl.unidade = :unidade AND rl.curso  IS NOT NULL AND rl.classe IS NOT NULL AND rl.disciplina IS NOT NULL ORDER BY rl.curso ASC, rl.classe")
	List<RelacaoClasse> findAllByUnidade(@Param("unidade") Unidade unidade);
	
	List<RelacaoClasse> findRelacaoClasseByUnidade(Unidade unidade);

	List<RelacaoClasse> findByClasseAndCurso(Classe idClasse, Curso idCurso);

	@Query("select rl from RelacaoClasse rl where rl.id in (select relacaoClasse from RelacaoAcesso where usuarioIncluido= :usuario) order by rl.curso asc, rl.classe")
	List<RelacaoClasse> findAllByRelacaoClasse(Usuario usuario);

	RelacaoClasse findByClasseAndCursoAndDisciplina(Classe idClasse, Curso idCurso, Disciplina idDisciplina);
}
