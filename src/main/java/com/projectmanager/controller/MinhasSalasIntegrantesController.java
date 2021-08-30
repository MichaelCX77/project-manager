package com.projectmanager.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.projectmanager.model.Classe;
import com.projectmanager.model.Disciplina;
import com.projectmanager.model.RelacaoAcesso;
import com.projectmanager.model.RelacaoClasse;
import com.projectmanager.model.Usuario;
import com.projectmanager.repository.ClasseRepository;
import com.projectmanager.repository.CursoRepository;
import com.projectmanager.repository.RelacaoAcessoRepository;
import com.projectmanager.repository.RelacaoClasseRepository;
import com.projectmanager.repository.UsuarioRepository;
import com.projectmanager.util.DatasUtil;

@Controller
public class MinhasSalasIntegrantesController {

	@Autowired
	UsuarioRepository crudUsuario;
	
	@Autowired
	RelacaoClasseRepository crudRelacaoClasse;
	
	@Autowired
	RelacaoAcessoRepository crudRelacaoAcesso;
	
	@Autowired
	CursoRepository crudCurso;
	
	@Autowired
	ClasseRepository crudClasse;
	
	@RequestMapping (method= RequestMethod.GET, path = "/detalhe-sala-integrantes")
	public ModelAndView detalheMinhasSalasIntegrantesGET(@RequestParam(value = "idCurso", required = true) Integer idCurso,
			@RequestParam(value = "idClasse", required = true) Integer idClasse) {
		
		List<RelacaoClasse> listClasse = crudRelacaoClasse.findByClasseAndCurso(crudClasse.findById(idClasse), crudCurso.findById(idCurso));
		
		List<Disciplina> listDisciplinas = new ArrayList<Disciplina>();
//		Carregando lista de Disciplinas da sala
		for (int i = 0; i < listClasse.size(); i++) {
			
			listDisciplinas.add(listClasse.get(i).getDisciplina());
								
		}

//		Carregando alunos da Sala
		List<RelacaoAcesso> listAlunos = new ArrayList<RelacaoAcesso>();
		List<RelacaoAcesso> alunosTemp = crudRelacaoAcesso.findByRelacaoClasse(listClasse.get(0));
		
		for (int j = 0; j < alunosTemp.size(); j++) {
			
			Usuario aluno = alunosTemp.get(j).getUsuarioIncluido();
			
			User autoridades = new User(aluno.getUsername(), aluno.getPassword(), true, true, true, true, aluno.getAuthorities());
			
//			Verificando se tem todas as permissões de administrador
			if(autoridades.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_AL"))
					&& !autoridades.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_PR"))) {
				
				alunosTemp.get(j).setDtRegistroFormatada(DatasUtil.convertAndFormatToString(alunosTemp.get(j).getDtRegistro(), "dd/MM/yyyy"));
				alunosTemp.get(j).setDtVencimentoFormatada(DatasUtil.convertAndFormatToString(alunosTemp.get(j).getDtVencimento(), "dd/MM/yyyy"));
				alunosTemp.get(j).getUsuarioIncluido().setEmail(aluno.getEmail().toLowerCase());
				listAlunos.add(alunosTemp.get(j));
				
			}		
		}
		
		Classe classe = listClasse.get(0).getClasse();
		classe.setDtCriacaoFormatada(DatasUtil.convertAndFormatToString(classe.getDtCriacao(), "dd/MM/yyyy"));
		
		ModelAndView mv = new ModelAndView("detalhe-sala-integrantes");
		mv.setViewName("detalhe-sala-integrantes");
		mv = recarregaMenu(mv);
		mv.addObject("curso",listClasse.get(0).getCurso());
		mv.addObject("classe",classe);
		mv.addObject("usuario",new Usuario());
		mv.addObject("disciplina", listDisciplinas);
		mv.addObject("idCurso", idCurso);
		mv.addObject("idClasse", idClasse);
		
		if(listAlunos.size() <= 0) {mv.addObject("mensagemAlunos", "Essa sala ainda não possui alunos.");}
		else {
			mv.addObject("listAlunos", listAlunos);
		}
		
		return mv;
		
	}
	
	public ModelAndView recarregaMenu(ModelAndView mv) {
		
		UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Usuario usuario = crudUsuario.findByEmail(user.getUsername());

		String nomeUsuario = usuario.getNome();
		
		if(nomeUsuario.indexOf(" ") > -1){
			
			nomeUsuario = nomeUsuario.substring(0, nomeUsuario.indexOf(" ")) +  " " + nomeUsuario.substring(nomeUsuario.lastIndexOf(" ")+1, nomeUsuario.length());

		}
		mv.addObject("nomeUsuario",nomeUsuario);
		return mv;
	}
	
}
