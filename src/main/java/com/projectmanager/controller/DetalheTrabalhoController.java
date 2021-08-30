package com.projectmanager.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.projectmanager.auxiliar.Grupo;
import com.projectmanager.model.RelacaoTrabalho;
import com.projectmanager.model.Usuario;
import com.projectmanager.repository.ClasseRepository;
import com.projectmanager.repository.CodigoTipoDescRepository;
import com.projectmanager.repository.CodigosRepository;
import com.projectmanager.repository.CursoRepository;
import com.projectmanager.repository.GrupoRepository;
import com.projectmanager.repository.LiberacaoRepository;
import com.projectmanager.repository.LiberacaoSiglasDescRepository;
import com.projectmanager.repository.RelacaoAcessoRepository;
import com.projectmanager.repository.RelacaoClasseRepository;
import com.projectmanager.repository.RelacaoTrabalhoRepository;
import com.projectmanager.repository.UsuarioRepository;
import com.projectmanager.util.DatasUtil;

@Controller
public class DetalheTrabalhoController {

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
	
	@Autowired
	CodigosRepository crudCodigos;
	
	@Autowired
	CodigoTipoDescRepository crudCodTipoDesc;

	@Autowired
	LiberacaoRepository crudLiberacao;
	
	@Autowired
	LiberacaoSiglasDescRepository crudLiberaSiglasDesc;
	
	@Autowired
	RelacaoTrabalhoRepository crudRelacaoTrabalho;
	
	@Autowired
	GrupoRepository crudGrupo;
	
	
	@RequestMapping (method= RequestMethod.GET, path = "/novo-grupo")
	public ModelAndView novoGrupoGET(Integer idRelacaoTrabalho, String nomeGrupo) {
		
		RelacaoTrabalho relacaoTrabalho = crudRelacaoTrabalho.findRelacaoTrabalhoById(idRelacaoTrabalho);
		
		List<RelacaoTrabalho> listGruposTrabalho = crudRelacaoTrabalho.findAllByTrabalho(relacaoTrabalho.getTrabalho());
		
		for(int i = 0; i < listGruposTrabalho.size(); i++) {
			
			if(listGruposTrabalho.get(i).getGrupo() != null) {
				
				if(listGruposTrabalho.get(i).getGrupo().getNome().equals(nomeGrupo)) {
					
					ModelAndView mv = new ModelAndView("detalhe-trabalho");
					mv = RecarregaTela(mv, idRelacaoTrabalho, "Já existe um grupo com esse nome, verifique.");
					mv.setViewName("detalhe-trabalho");
					return mv;
					
				}
			}
		}

		ModelAndView mv = new ModelAndView("detalhe-trabalho");
		mv = RecarregaTela(mv, idRelacaoTrabalho, "Grupo criado com sucesso.");
		mv.setViewName("detalhe-trabalho");
		return mv;

	}
	
	@RequestMapping (method= RequestMethod.GET, path = "/adicionar-integrante")
	public ModelAndView adicionarIntegranteGET(Integer idRelacaoTrabalho, String nomeGrupo) {
		
		ModelAndView mv = new ModelAndView("novo-grupo");
		mv.setViewName("novo-grupo");
		mv = recarregaMenu(mv);
		mv.addObject("idRelacaoTrabalho",idRelacaoTrabalho);
		
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
	
	public ModelAndView RecarregaTela (ModelAndView mv, Integer idRelacaoTrabalho, String mensagemNovoGrupo) {
		
		RelacaoTrabalho relacaoTrabalho = crudRelacaoTrabalho.findRelacaoTrabalhoById(idRelacaoTrabalho);
		
		List<RelacaoTrabalho> listGrupos = crudRelacaoTrabalho.findAllByTrabalho(relacaoTrabalho.getTrabalho());
		
		List<com.projectmanager.auxiliar.Grupo> listIntegrantes = new ArrayList<com.projectmanager.auxiliar.Grupo>();
		
		for (int i = 0; i < listGrupos.size(); i++) {
			
				if(listGrupos.get(i).getGrupo() != null){
					
					List<com.projectmanager.model.Grupo> listIntegrantesTemp = crudGrupo.findByIdGrupo(listGrupos.get(i).getGrupo().getIdGrupo());
					
	//				Objetos para carregar modelo de grupo
					List<Usuario> listIntegrantesUsuarioTemp = new ArrayList<Usuario>();
					Usuario usuarioLider = new Usuario();
					
					for (int j = 0; j < listIntegrantesTemp.size(); j++) {
						
						if(listIntegrantesTemp.get(j).getIdGrupo() != listIntegrantesTemp.get(j).getId()){
							
							Usuario usuario = crudUsuario.findById(listIntegrantesTemp.get(j).getUsuario());
							
							if(listIntegrantesTemp.get(j).getUsuarioLider().equals("S")) {usuarioLider = usuario;}
							else {listIntegrantesUsuarioTemp.add(usuario);}
							
						}
					
					}
					
					com.projectmanager.auxiliar.Grupo modeloGrupo = new com.projectmanager.auxiliar.Grupo(listIntegrantesTemp.get(0), new ArrayList<Usuario>(), usuarioLider);
					modeloGrupo.getLisUsuarios().addAll(listIntegrantesUsuarioTemp);
					listIntegrantes.add(modeloGrupo);
					
				}
				
			}
		
		relacaoTrabalho.getTrabalho().setDtCriacaoFormatada(DatasUtil.convertAndFormatToString(relacaoTrabalho.getTrabalho().getDtCriacao(), "dd/MM/yyyy"));
		relacaoTrabalho.getTrabalho().setDtEntregaFormatada(DatasUtil.convertAndFormatToString(relacaoTrabalho.getTrabalho().getDtEntrega(), "dd/MM/yyyy"));
		
		mv = recarregaMenu(mv);
		mv.addObject("trabalho", relacaoTrabalho.getTrabalho());
		mv.addObject("listGrupos", listIntegrantes);
		mv.addObject("idRelacaoTrabalho", idRelacaoTrabalho);
		mv.addObject("mensagemNovoGrupo", mensagemNovoGrupo);
		if(listIntegrantes.size() <= 0 ) {mv.addObject("mensagemGrupos", "Esse Trabalho ainda não possui nenhum grupo");}
		
		return mv;

	}
	
}
