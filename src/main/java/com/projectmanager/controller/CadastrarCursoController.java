package com.projectmanager.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.projectmanager.auxiliar.AuxiliarBd;
import com.projectmanager.model.Curso;
import com.projectmanager.model.RelacaoAcesso;
import com.projectmanager.model.RelacaoClasse;
import com.projectmanager.model.Unidade;
import com.projectmanager.model.Usuario;
import com.projectmanager.repository.CursoRepository;
import com.projectmanager.repository.RelacaoAcessoRepository;
import com.projectmanager.repository.RelacaoClasseRepository;
import com.projectmanager.repository.UnidadeRepository;
import com.projectmanager.repository.UsuarioRepository;
import com.projectmanager.util.DatasUtil;

@Controller
public class CadastrarCursoController {

	@Autowired
	CursoRepository crudCurso;
	
	@Autowired
	UsuarioRepository crudUsuario;
	
	@Autowired
	RelacaoAcessoRepository crudRelacaoAcesso;
	
	@Autowired
	RelacaoClasseRepository crudRelacaoClasse;
	
	@Autowired
	UnidadeRepository crudUnidade;
	
	@RequestMapping(method = RequestMethod.POST, path = "/criar-curso")
	public ModelAndView cadastrarCursoPOST(Curso form, @RequestParam(value = "idUnidade", required = true) Integer idUnidade) {
		
		ModelAndView mv = null;
		
		if(idUnidade == 999) {
			
			UserDetails userSession = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Usuario usuario = crudUsuario.findByEmail(userSession.getUsername());
			
			List<RelacaoAcesso> relacaoAcesso = new ArrayList<RelacaoAcesso>();
			relacaoAcesso = crudRelacaoAcesso.findAllByUsuarioIncluidoAndAcesso(usuario, 'S');
			
			List<RelacaoClasse> listaRelacaoClasse = new ArrayList<RelacaoClasse>();
			
			if(userSession.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ES"))) {
				listaRelacaoClasse = crudRelacaoClasse.findAllByEscola(relacaoAcesso.get(0).getRelacaoClasse().getEscola());	
			}
			else {
				listaRelacaoClasse = crudRelacaoClasse.findRelacaoClasseByUnidade(relacaoAcesso.get(0).getRelacaoClasse().getUnidade());	
			}						
				List<Unidade> listaUnidade = new ArrayList<Unidade>();
				
	//			Adicionando seleção default
				Unidade unidade = new Unidade();
				unidade.setId(999);
				unidade.setNome("Selecione");
				
				listaUnidade.add(unidade);
				listaUnidade.addAll(AuxiliarBd.listaUnidade(listaRelacaoClasse));
				
//				Adicionando seleção default
				Curso curso = new Curso();
				curso.setId(999);
				curso.setNomeCurso("Selecione");
				
				mv = new ModelAndView("criar-curso");
				mv = recarregaMenu(mv);
				mv.addObject("unidade", listaUnidade);
				mv.addObject("curso", form);
				mv.setViewName("criar-curso");
				mv.addObject("mensagem","Selecione uma Unidade");
				return mv;
				
			}
		
//		Verificando se o código e o nome da unidade já existem
		List<RelacaoClasse> listaRelacaoClasse = new ArrayList<RelacaoClasse>();
		listaRelacaoClasse = crudRelacaoClasse.findRelacaoClasseByUnidade(crudUnidade.findById(idUnidade));		
		
		boolean codigoExiste = false;
		boolean nomeExiste = false;
		
		for (int i = 0; i < listaRelacaoClasse.size() && nomeExiste == false && codigoExiste == false ; i++) {
			
			Curso curso = listaRelacaoClasse.get(i).getCurso();
			
			if(curso != null) {
				if(curso.getCodigoCurso().equals(form.getCodigoCurso())) {codigoExiste = true;}
				else if(curso.getNomeCurso().equals(form.getNomeCurso())) {	nomeExiste = true;}						
			}			
		}
		
		if (codigoExiste) {
			
			List<Unidade> listaUnidades = AuxiliarBd.listaUnidade(listaRelacaoClasse);
			
			mv = new ModelAndView("redirect:/criar-curso");
			mv = recarregaMenu(mv);
			mv.setViewName("criar-curso");
			mv.addObject("mensagem","O código definido para o curso já foi registrado na unidade selecionada, por favor defina outro código.");
			mv.addObject("curso",form);
			mv.addObject("unidade",listaUnidades);
			return mv;
			
		} else if(nomeExiste){

			List<Unidade> listaUnidades = AuxiliarBd.listaUnidade(listaRelacaoClasse);
			
			mv = new ModelAndView("criar-curso");
			mv = recarregaMenu(mv);
			mv.setViewName("criar-curso");
			mv.addObject("mensagem","O nome definido para curso já foi registrado na unidade selecionada, por favor defina outro código.");
			mv.addObject("curso",form);
			mv.addObject("unidade",listaUnidades);
			return mv;
			
		}

		RelacaoClasse relacaoClasse = new RelacaoClasse();
		
		Calendar cal = DatasUtil.getCalendarDate();
	    java.sql.Date sqlDate = new java.sql.Date(cal.getTimeInMillis());

//		Salvando registro de Curso
		crudCurso.save(form);
		
//	    Salvando registro da RelacaoClasse
	    relacaoClasse.setEscola(listaRelacaoClasse.get(0).getEscola());
		relacaoClasse.setUnidade(crudUnidade.findById(idUnidade));
		relacaoClasse.setCurso(form);
		relacaoClasse.setDtRegistro(sqlDate);
		crudRelacaoClasse.save(relacaoClasse);
		
		mv = new ModelAndView("mensagem-logado");
		mv = recarregaMenu(mv);
		mv.setViewName("mensagem-logado");
		mv.addObject("mensagem","Curso cadastrado com sucesso.");
		
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
