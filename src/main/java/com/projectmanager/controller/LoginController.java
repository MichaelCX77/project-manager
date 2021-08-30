package com.projectmanager.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.projectmanager.model.Usuario;

@Controller
public class LoginController {
	
//	@PostMapping(value = "/", produces = MediaType.TEXT_PLAIN_VALUE)
//	public ModelAndView index(@Valid Usuario form) {
//		
//		return form(form);
//		
//	}
//	
	@RequestMapping (method = RequestMethod.GET, path = "/login")	
	public ModelAndView login() {
		
		ModelAndView mv = new ModelAndView("login");
		Usuario usuario = new Usuario();
		mv.addObject("usuario",usuario);
		return mv;
		
	}

	@RequestMapping (method = RequestMethod.GET, path = "/")
	public String entrar() {
		
		return "redirect:/home";
		
	}

	
//	@PostMapping(value="/entrar", produces = MediaType.TEXT_PLAIN_VALUE)
//	public ModelAndView form(@Valid Usuario form) {
//		
//		ModelAndView mv = null;
//		
//		Usuario usuario = crudUsuario.findByEmail(form.getEmail().toUpperCase());
//			
//		if (usuario == null) {
//			mv = new ModelAndView("login");
//				mv.addObject("mensagem","Usuário Inexistente.");
//				mv.addObject("usuario",form);
//				return mv;
//		}
//		else if(!usuario.getSenha().equals(form.getSenha())){
//			mv = new ModelAndView("login");
//			mv.addObject("usuario",form);
//			mv.addObject("mensagem","Senha incorreta.");
//			return mv;
//		}
//		else {
//			mv = new ModelAndView("home");
//			
//			List<Grupo> listGrupos = new ArrayList<Grupo>();
//			listGrupos = crudGrupo.findByUsuario(usuario.getId());
//			
//			for (int i = 0; i < listGrupos.size(); i++) {
//				
//				Grupo grupo = listGrupos.get(i);
//				
//				List<RelacaoTrabalho> listTrabalhos = new ArrayList<RelacaoTrabalho>();
//				listTrabalhos = crudRelacaoTrabalho.findByGrupo(grupo);
//				
//				List<RelacaoClasse> listHierarquia = new ArrayList<RelacaoClasse>();
//				
//				for (int j = 0; j < listTrabalhos.size(); j++) {
//					
//					listHierarquia = crudRelacaoClasse.findByClasse(listTrabalhos.get(j).getClasse());
//
//				}
//
//				System.out.println("OK");
//				
//			}
//			
//		}
//			
//		return mv;
//		
//	}
	
	@GetMapping(value = "recuperar-senha", produces = MediaType.TEXT_PLAIN_VALUE)
	public ModelAndView RecuperarSenhaPOST() {
		
		Usuario usuario = new Usuario();
		
		ModelAndView mv = new ModelAndView("recuperar-senha");
		mv.setViewName("recuperar-senha");
		mv.addObject("usuario",usuario);
		
		return mv;
	}
	
	@GetMapping(value = "for-schools", produces = MediaType.TEXT_PLAIN_VALUE)
	public String ForSchools() {
		return "for-schools";
	}
	
	
}