package com.projectmanager.controller;

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

import com.projectmanager.model.RelacaoAcesso;
import com.projectmanager.model.RelacaoClasse;
import com.projectmanager.model.Unidade;
import com.projectmanager.model.Usuario;
import com.projectmanager.repository.RelacaoAcessoRepository;
import com.projectmanager.repository.RelacaoClasseRepository;
import com.projectmanager.repository.UnidadeRepository;
import com.projectmanager.repository.UsuarioRepository;
import com.projectmanager.util.DatasUtil;

@Controller
public class CadastrarUnidadeController {

	@Autowired
	UnidadeRepository crudUnidade;
	
	@Autowired
	UsuarioRepository crudUsuario;
	
	@Autowired
	RelacaoAcessoRepository crudRelacaoAcesso;
	
	@Autowired
	RelacaoClasseRepository crudRelacaoClasse;
	
	
	@RequestMapping (method = RequestMethod.POST, path = "/criar-unidade")
	public ModelAndView criarUnidadePOST(Unidade form) {
		
		ModelAndView mv = null;
		
		if(form.getEstado().length() > 2) {
			
			mv = new ModelAndView("criar-unidade");
			mv = recarregaMenu(mv);
			mv.addObject("mensagem","O estado deve ser descrito por sigla, ex.: (SP)");
			mv.addObject("unidade",form);
			mv.setViewName("criar-unidade");
			return mv;
			
		}
		
		String cep = form.getCep().replaceAll("\\D","");
		
		if(crudUnidade.existsByCep(cep)) {
			mv = new ModelAndView("criar-unidade");
			mv = recarregaMenu(mv);
			mv.addObject("mensagem","Já existe uma unidade cadastrada com esse CEP, por favor verifique.");
			mv.addObject("unidade",form);
			mv.setViewName("criar-unidade");
			return mv;
		}
		
		if(crudUnidade.existsByNome(form.getNome())) {
			mv = new ModelAndView("criar-unidade");
			mv = recarregaMenu(mv);
			mv.addObject("mensagem","Já existe uma unidade cadastrada com esse Nome, por favor verifique.");
			mv.addObject("unidade",form);
			mv.setViewName("criar-unidade");
			return mv;
		}
		
		UserDetails userSession = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Usuario usuario = crudUsuario.findByEmail(userSession.getUsername());
		
		List<RelacaoAcesso> acessos = new ArrayList<RelacaoAcesso>();
		acessos = crudRelacaoAcesso.findAllByUsuarioIncluidoAndAcesso(usuario, 'S');
		
		if(acessos.size() <= 0) {
			
			mv = new ModelAndView("mensagem-logado");
			mv = recarregaMenu(mv);
			mv.addObject("mensagem-logado","Você não pode criar unidades nessa instituição.");
			mv.setViewName("mensagem-logado");
			return mv;
			
		}
		
//		Salvando Unidade
		form.setCep(cep);
		crudUnidade.save(form);
		
		Calendar cal = DatasUtil.getCalendarDate();
	    java.sql.Date sqlDate = new java.sql.Date(cal.getTimeInMillis());
		
//	    Salvando Relação Escola
		Unidade unidade = crudUnidade.findByCep(form.getCep());
		RelacaoClasse relacaoClasse = new RelacaoClasse();
		relacaoClasse.setEscola(acessos.get(0).getRelacaoClasse().getEscola());
		relacaoClasse.setUnidade(unidade);
		relacaoClasse.setDtRegistro(sqlDate);
		crudRelacaoClasse.save(relacaoClasse);
		
		mv = new ModelAndView("mensagem-logado");
		mv = recarregaMenu(mv);
		mv.addObject("mensagem", "Cadastro de Unidade efetuado com sucesso");
		
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
