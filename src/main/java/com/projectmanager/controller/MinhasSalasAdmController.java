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
public class MinhasSalasAdmController {

	@Autowired
	UsuarioRepository crudUsuario;
	
	@Autowired
	RelacaoClasseRepository crudRelacaoClasse;
	
	@Autowired
	ClasseRepository crudClasse;
	
	@Autowired
	CursoRepository crudCurso;
	
	@Autowired
	RelacaoAcessoRepository crudRelacaoAcesso;
	
	@RequestMapping(method= RequestMethod.GET, path ="/detalhe-sala")
	public ModelAndView DetalheMinhasSalasAdmGET(@RequestParam(value = "idCurso", required = true) Integer idCurso,
			@RequestParam(value = "idClasse", required = true) Integer idClasse) {
		
		List<RelacaoClasse> listClasse = crudRelacaoClasse.findByClasseAndCurso(crudClasse.findById(idClasse), crudCurso.findById(idCurso));
		
		List<RelacaoAcesso> listProfessores = new ArrayList<RelacaoAcesso>();
		List<RelacaoClasse> listSalasSemProfessor = new ArrayList<RelacaoClasse>();
		
		for (int i = 0; i < listClasse.size(); i++) {
			
			List<RelacaoAcesso> listTemp = crudRelacaoAcesso.findByRelacaoClasse(listClasse.get(i));
			
			if(listTemp.size() > 0) {
				
				boolean prof = false;
				
				for (int k = 0; k < listTemp.size() && prof == false; k++) {
					
					Usuario usuarioTemp = listTemp.get(k).getUsuarioIncluido();
					
					User autoridades = new User(usuarioTemp.getUsername(), usuarioTemp.getPassword(), true, true, true, true, usuarioTemp.getAuthorities());
					
					if(autoridades.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_PR"))) {
						listTemp.get(k).getUsuarioIncluido().setEmail(usuarioTemp.getEmail().toLowerCase());
						listProfessores.add(listTemp.get(k));
						prof = true;
					}
				}
				if(!prof) {
					
					listProfessores.add(new RelacaoAcesso(new Usuario(999, "##Sem Professor##", "#SemProfessor#"),crudRelacaoClasse.findById(listClasse.get(i).getId())));
					listSalasSemProfessor.add(listClasse.get(i));
				}
			}
			else {
				listProfessores.add(new RelacaoAcesso(new Usuario(999, "##Sem Professor##", "#SemProfessor#"),crudRelacaoClasse.findById(listClasse.get(i).getId())));
				listSalasSemProfessor.add(listClasse.get(i));
			}
		}
		
		Classe classe = listClasse.get(0).getClasse();
		classe.setDtCriacaoFormatada(DatasUtil.convertAndFormatToString(classe.getDtCriacao(), "dd/MM/yyyy"));
		
		ModelAndView mv = new ModelAndView("detalhe-sala");
		mv.setViewName("detalhe-sala");
		mv = recarregaMenu(mv);
		mv.addObject("classe",classe);
		mv.addObject("curso",listClasse.get(0).getCurso());
		mv.addObject("listDisciplinasProfessores", listProfessores);
		mv.addObject("listSalasSemProfessor", listSalasSemProfessor);
		mv.addObject("idCurso", idCurso);
		mv.addObject("idClasse", idClasse);
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
