package com.projectmanager.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.projectmanager.auxiliar.AuxiliarBd;
import com.projectmanager.model.Disciplina;
import com.projectmanager.model.RelacaoClasse;
import com.projectmanager.model.RelacaoTrabalho;
import com.projectmanager.model.Trabalho;
import com.projectmanager.model.Usuario;
import com.projectmanager.repository.ClasseRepository;
import com.projectmanager.repository.CursoRepository;
import com.projectmanager.repository.DisciplinaRepository;
import com.projectmanager.repository.RelacaoClasseRepository;
import com.projectmanager.repository.RelacaoTrabalhoRepository;
import com.projectmanager.repository.TrabalhoRepository;
import com.projectmanager.repository.UsuarioRepository;
import com.projectmanager.util.DatasUtil;

@Controller
public class CriarTrabalhoController {

	@Autowired
	TrabalhoRepository crudTrabalho;
	
	@Autowired
	RelacaoTrabalhoRepository crudRelacaoTrabalho;
	
	@Autowired
	RelacaoClasseRepository crudRelacaoClasse;
	
	@Autowired
	ClasseRepository crudClasse;
	
	@Autowired
	CursoRepository crudCurso;
	
	@Autowired
	DisciplinaRepository crudDisciplina;
	
	@Autowired
	UsuarioRepository crudUsuario;
	
	@RequestMapping (method= RequestMethod.POST, path = "/novo-trabalho")
	public ModelAndView novoTrabalhoPOST(Trabalho trabalho, Integer idCurso, Integer idClasse, Integer idDisciplina) throws ParseException {
		
		ModelAndView mv = null;
		
		if(idDisciplina == 999) {
			
			mv = new ModelAndView("novo-trabalho");
			mv.setViewName("novo-trabalho");
			mv = recarregaTela("Selecione uma Disciplina.", idCurso, idClasse);
			mv.addObject("trabalho",trabalho);
			return mv;
		}
		
		Calendar cal = DatasUtil.getCalendarDate();
	    java.sql.Date sqlDateAtual = new java.sql.Date(cal.getTimeInMillis());

	    boolean dataValida = DatasUtil.comparaDatasSqlDate(sqlDateAtual, trabalho.getDtEntrega(), "yyyy-MM-dd");
	    
	    if(!dataValida) {
	    	
			mv = new ModelAndView("novo-trabalho");
			mv.setViewName("novo-trabalho");
			mv = recarregaTela("A data de entrega deve ser posterior a data de hoje.", idCurso, idClasse);
			mv.addObject("trabalho", trabalho);
			
			return mv;
	    	
	    }
	    
		UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Usuario usuario = crudUsuario.findByEmail(user.getUsername());
	
//		Salvando Trabalho
	    trabalho.setIdProfessor(usuario);
		trabalho.setDtCriacao(sqlDateAtual);
		crudTrabalho.save(trabalho);
		
		
		RelacaoClasse relacaoClasse = crudRelacaoClasse.findByClasseAndCursoAndDisciplina(crudClasse.findById(idClasse), crudCurso.findById(idCurso), crudDisciplina.findById(idDisciplina));

//		Salvando Relação entre Trabalho e Classe
		RelacaoTrabalho relacaoTrabalho = new RelacaoTrabalho();
		relacaoTrabalho.setRelacaoClasse(relacaoClasse);
		relacaoTrabalho.setTrabalho(trabalho);
		crudRelacaoTrabalho.save(relacaoTrabalho);
		
		mv = new ModelAndView("mensagem-trabalho");
		mv.setViewName("mensagem-trabalho");
		mv = recarregaMenu(mv);
		mv.addObject("idCurso",idCurso);
		mv.addObject("idClasse",idClasse);
		mv.addObject("mensagem","Trabalho criado com sucesso.");
		
		return mv;
	}
	
	public ModelAndView recarregaTela(String mensagem, Integer idCurso, Integer idClasse) {
		
		UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		List<RelacaoClasse> relacaoClasse = crudRelacaoClasse.findAllByRelacaoClasse(crudUsuario.findByEmail(user.getUsername()));
		List<RelacaoClasse> listSalasProfessor = AuxiliarBd.listaSalas(relacaoClasse);
		List<RelacaoClasse> listSalaAtual = crudRelacaoClasse.findByClasseAndCurso(crudClasse.findById(idClasse), crudCurso.findById(idCurso));
		
		
		for (int i = 0; i < listSalasProfessor.size(); i++) {
			
			if(!listSalaAtual.contains(listSalasProfessor.get(i))) {
				listSalasProfessor.remove(i);
				i--;
			}
		}
		
		List<Disciplina> listDisciplina = new ArrayList<Disciplina>();
		
		Disciplina disciplinaDefault = new Disciplina();
		disciplinaDefault.setId(999);
		disciplinaDefault.setNome("Selecione");
		listDisciplina.add(disciplinaDefault);
		
//		Recarregando Disciplina
		for (int i = 0; i < listSalasProfessor.size(); i++) {listDisciplina.add(listSalasProfessor.get(i).getDisciplina());}
		
		ModelAndView mv = new ModelAndView("novo-trabalho");
		mv.setViewName("novo-trabalho");
		mv = recarregaMenu(mv);
		mv.addObject("trabalho", new Trabalho());
		mv.addObject("idCurso", idCurso);
		mv.addObject("idClasse", idClasse);
		mv.addObject("mensagem", mensagem);
		mv.addObject("listDisciplina", listDisciplina);
		
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
