package com.projectmanager.controller;

import java.util.ArrayList;
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
import com.projectmanager.model.Disciplina;
import com.projectmanager.model.RelacaoAcesso;
import com.projectmanager.model.RelacaoClasse;
import com.projectmanager.model.Unidade;
import com.projectmanager.model.Usuario;
import com.projectmanager.repository.CursoRepository;
import com.projectmanager.repository.DisciplinaRepository;
import com.projectmanager.repository.RelacaoAcessoRepository;
import com.projectmanager.repository.RelacaoClasseRepository;
import com.projectmanager.repository.UnidadeRepository;
import com.projectmanager.repository.UsuarioRepository;

@Controller
public class CadastrarDisciplinaController {

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
	DisciplinaRepository crudDisciplina;
	
	@RequestMapping(method = RequestMethod.POST, path = "/criar-disciplina")
	public ModelAndView cadastrarCursoPOST(Disciplina form, @RequestParam(value = "idUnidade", required = true) Integer idUnidade) {
		
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
				
				mv = new ModelAndView("criar-disciplina");
				mv = recarregaMenu(mv);
				mv.addObject("unidade", listaUnidade);
				mv.addObject("disciplina", form);
				mv.addObject("curso", curso);
				mv.setViewName("criar-disciplina");
				mv.addObject("mensagem","Selecione uma Unidade");
				return mv;
				
			}
		else {
			
//			Verificando se o código da disciplina já existe		
			List<RelacaoClasse> listaRelacaoClasse = new ArrayList<RelacaoClasse>();
			listaRelacaoClasse = crudRelacaoClasse.findRelacaoClasseByUnidade(crudUnidade.findById(idUnidade));	
			
			boolean codigoExiste = false;
			boolean nomeExiste = false;
			
			
			for (int i = 0; i < listaRelacaoClasse.size() && nomeExiste == false && codigoExiste == false; i++) {
				
				Disciplina disciplina = listaRelacaoClasse.get(i).getDisciplina();
				
				if(disciplina != null) {
					if(disciplina.getCodigo().equals(form.getCodigo())) {codigoExiste = true;}
					else if(disciplina.getNome().equals(form.getNome())) {	nomeExiste = true;}						
				}

			}

			if (codigoExiste) {
				
//					Adicionando Unidade selecionada								
					List<Unidade> listaUnidade = new ArrayList<Unidade>();
					listaUnidade.add(crudUnidade.findById(idUnidade));
					
					mv = new ModelAndView("criar-disciplina");
					mv = recarregaMenu(mv);
					mv.addObject("unidade",listaUnidade);
					mv.addObject("disciplina", form);
					mv.addObject("mensagem","O código definido para a disciplina já foi utilizado nessa unidade,  por favor defina outro código.");
					mv.setViewName("criar-disciplina");
					return mv;
				
			} else if(nomeExiste){

//				Adicionando Unidade selecionada								
				List<Unidade> listaUnidade = new ArrayList<Unidade>();
				listaUnidade.add(crudUnidade.findById(idUnidade));
				
				mv = new ModelAndView("criar-disciplina");
				mv = recarregaMenu(mv);
				mv.addObject("unidade",listaUnidade);
				mv.addObject("disciplina", form);
				mv.addObject("mensagem","O nome definido para a disciplina já foi utilizado nessa unidade, por favor defina outro nome.");
				mv.setViewName("criar-disciplina");
				return mv;
				
			}

//			Salvando Disciplina
			crudDisciplina.save(form);
			
			RelacaoClasse relacaoClasse = new RelacaoClasse();
			
//		    Salvando registro da RelacaoClasse
			relacaoClasse.setEscola(listaRelacaoClasse.get(0).getEscola());
			relacaoClasse.setUnidade(crudUnidade.findById(idUnidade));
			relacaoClasse.setDisciplina(form);
			
			crudRelacaoClasse.save(relacaoClasse);
			
			mv = new ModelAndView("mensagem-logado");
			mv = recarregaMenu(mv);
			mv.setViewName("mensagem-logado");
			mv.addObject("mensagem","Disciplina cadastrada com sucesso.");
			return mv;
		}	
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
