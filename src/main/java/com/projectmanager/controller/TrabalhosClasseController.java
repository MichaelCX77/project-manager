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

import com.projectmanager.auxiliar.AuxiliarBd;
import com.projectmanager.model.Disciplina;
import com.projectmanager.model.Grupo;
import com.projectmanager.model.RelacaoClasse;
import com.projectmanager.model.RelacaoTrabalho;
import com.projectmanager.model.Trabalho;
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
public class TrabalhosClasseController {

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
	
	@RequestMapping (method= RequestMethod.GET, path = "/novo-trabalho")
	public ModelAndView novoTrabalhoGET(Integer idCurso, Integer idClasse) {
		
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
		mv.addObject("listDisciplina", listDisciplina);
		
		return mv;
	}
	
	@RequestMapping (method= RequestMethod.GET, path = "/detalhe-trabalho")
	public ModelAndView detalheTrabalhoGET(Integer idRelacaoTrabalho) {
		
		RelacaoTrabalho relacaoTrabalho = crudRelacaoTrabalho.findRelacaoTrabalhoById(idRelacaoTrabalho);
		
		List<RelacaoTrabalho> listGruposRelacaoTrabalho = crudRelacaoTrabalho.findAllByTrabalho(relacaoTrabalho.getTrabalho());
		
		List<com.projectmanager.auxiliar.Grupo> listIntegrantes = new ArrayList<com.projectmanager.auxiliar.Grupo>();
		
		for (int i = 0; i < listGruposRelacaoTrabalho.size(); i++) {
			
				if(listGruposRelacaoTrabalho.get(i).getGrupo() == null){
					
					listGruposRelacaoTrabalho.remove(i);
					i--;
				}
				else {
					
					List<Grupo> listIntegrantesTemp = crudGrupo.findByIdGrupo(listGruposRelacaoTrabalho.get(i).getGrupo().getIdGrupo());
					
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
		
		Grupo grupoDefault = new Grupo();
		
		grupoDefault.setIdGrupo(999);
		grupoDefault.setNome("Selecione..");

		List<Grupo> listGrupos = new ArrayList<Grupo>();
		listGrupos.add(grupoDefault);
		
		for (int i = 0; i < listGruposRelacaoTrabalho.size(); i++) {
			listGrupos.add(listGruposRelacaoTrabalho.get(i).getGrupo());
		}
		
		ModelAndView mv = new ModelAndView("detalhe-trabalho");
		mv.setViewName("detalhe-trabalho");
		mv = recarregaMenu(mv);
		mv.addObject("trabalho", relacaoTrabalho.getTrabalho());
		mv.addObject("listIntegrantes", listIntegrantes);
		mv.addObject("listGrupos", listGrupos);
		mv.addObject("idRelacaoTrabalho", idRelacaoTrabalho);
		
		if(listIntegrantes.size() <= 0 ) {mv.addObject("mensagemGrupos", "Esse Trabalho ainda não possui nenhum grupo");}
		
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
