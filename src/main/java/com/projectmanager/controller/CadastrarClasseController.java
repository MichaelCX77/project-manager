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
import com.projectmanager.model.Classe;
import com.projectmanager.model.Curso;
import com.projectmanager.model.RelacaoAcesso;
import com.projectmanager.model.RelacaoClasse;
import com.projectmanager.model.Unidade;
import com.projectmanager.model.Usuario;
import com.projectmanager.repository.ClasseRepository;
import com.projectmanager.repository.CursoRepository;
import com.projectmanager.repository.RelacaoAcessoRepository;
import com.projectmanager.repository.RelacaoClasseRepository;
import com.projectmanager.repository.UnidadeRepository;
import com.projectmanager.repository.UsuarioRepository;
import com.projectmanager.util.DatasUtil;

@Controller
public class CadastrarClasseController {

	@Autowired
	RelacaoClasseRepository crudRelacaoClasse;
	
	@Autowired
	UnidadeRepository crudUnidade;
	
	@Autowired
	CursoRepository crudCurso;
	
	@Autowired
	UsuarioRepository crudUsuario;
	
	@Autowired
	RelacaoAcessoRepository crudRelacaoAcesso;
	
	@Autowired
	ClasseRepository crudClasse;
	
	@RequestMapping(method = RequestMethod.POST, path = "/criar-classe")
	public ModelAndView cadastrarCursoPOST(Classe form, @RequestParam(value = "idUnidade", required = true) Integer idUnidade) {
		
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
				
				mv = new ModelAndView("criar-classe");
				mv = recarregaMenu(mv);
				mv.addObject("unidade", listaUnidade);
				mv.addObject("classe", form);
				mv.setViewName("criar-classe");
				mv.addObject("mensagem","Selecione uma Unidade");
				return mv;
				
			}
		
//			Verificando se o código da classe já existe		
			List<RelacaoClasse> listaRelacaoClasse = new ArrayList<RelacaoClasse>();
			listaRelacaoClasse = crudRelacaoClasse.findRelacaoClasseByUnidade(crudUnidade.findById(idUnidade));	
			
			boolean nomeExiste = false;
			boolean codigoExiste = false;
			
			for (int i = 0; i < listaRelacaoClasse.size() && codigoExiste == false && nomeExiste == false ; i++) {
				
				Classe classe = listaRelacaoClasse.get(i).getClasse();
				
				if(classe != null) {
					if(classe.getCodigo().equals(form.getCodigo()) && 
							classe.getAno() == form.getAno() && 
							classe.getSemestre() == form.getSemestre() && 
							classe.getPeriodo() == form.getPeriodo()) {codigoExiste = true;}	
					else if(classe.getNome().equals(form.getCodigo()) && 
							classe.getAno() == form.getAno() && 
							classe.getSemestre() == form.getSemestre() && 
							classe.getPeriodo() == form.getPeriodo()) {nomeExiste = true;}	
				}

			}

			if (nomeExiste || codigoExiste) {
				
//					Adicionando Unidade selecionada								
					List<Unidade> listaUnidade = new ArrayList<Unidade>();
					listaUnidade.add(crudUnidade.findById(idUnidade));
					
					mv = new ModelAndView("criar-classe");
					mv = recarregaMenu(mv);
					mv.addObject("unidade",listaUnidade);
					mv.addObject("classe", form);
					
					if (nomeExiste) {
						mv.addObject("mensagem","Já existe nessa unidade uma classe com o nome especificado, por favor verifique.");
					} else {
						mv.addObject("mensagem","Já existe nessa unidade uma classe com o código especificado, por favor verifique.");
					}
					
					mv.setViewName("criar-classe");
					return mv;
				
			}
			
			Calendar cal = DatasUtil.getCalendarDate();
		    java.sql.Date sqlDate = new java.sql.Date(cal.getTimeInMillis());
			
		    form.setDtCriacao(sqlDate);
//			Salvando Classe
			crudClasse.save(form);
			
			RelacaoClasse relacaoClasse = new RelacaoClasse();
			
//		    Salvando registro da RelacaoClasse
			relacaoClasse.setEscola(listaRelacaoClasse.get(0).getEscola());
			relacaoClasse.setUnidade(crudUnidade.findById(idUnidade));
			relacaoClasse.setClasse(form);
			
			crudRelacaoClasse.save(relacaoClasse);
			
			mv = new ModelAndView("mensagem-logado");
			mv = recarregaMenu(mv);
			mv.setViewName("mensagem-logado");
			mv.addObject("mensagem","Classe cadastrada com sucesso.");
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
