package com.projectmanager.controller;

import java.util.Calendar;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.projectmanager.model.Codigos;
import com.projectmanager.model.Liberacao;
import com.projectmanager.model.RelacaoAcesso;
import com.projectmanager.model.RelacaoClasse;
import com.projectmanager.model.RelacaoPerfil;
import com.projectmanager.model.Usuario;
import com.projectmanager.repository.CodigosRepository;
import com.projectmanager.repository.LiberacaoRepository;
import com.projectmanager.repository.PerfilDescRepository;
import com.projectmanager.repository.RelacaoAcessoRepository;
import com.projectmanager.repository.RelacaoClasseRepository;
import com.projectmanager.repository.RelacaoPerfilRepository;
import com.projectmanager.repository.UsuarioRepository;
import com.projectmanager.util.DatasUtil;

@Controller
public class AtivarCodigo {

	@Autowired
	private UsuarioRepository crudUsuario;
	
	@Autowired
	private LiberacaoRepository crudLiberacao;
	
	@Autowired
	private CodigosRepository crudCodigos;
	
	@Autowired
	private RelacaoPerfilRepository crudRelacaoPerfil;
	
	@Autowired
	private PerfilDescRepository crudPerfilDesc;
	
	@Autowired
	private RelacaoAcessoRepository crudRelacaoAcesso;
	
	@Autowired
	private RelacaoClasseRepository crudRelacaoClasse;
	
	@RequestMapping(method = RequestMethod.POST,path = "/ativar-codigo")
	public ModelAndView ativarcodigoPOST(@Valid Codigos codigo) {
		
		UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Usuario usuarioLogado = crudUsuario.findByEmail(user.getUsername());
		
		ModelAndView mv = null;
		
		Codigos codigos = crudCodigos.findByCodigo(codigo.getCodigo());
		
		if(codigos == null) {
			mv = new ModelAndView("ativar-codigo");
			mv.setViewName("ativar-codigo");
			mv.addObject("mensagem","Código inexistente.");
			return mv;
		} 
		else if(codigos.getTipo().getId() != 'L') {
			mv = new ModelAndView("ativar-codigo");
			mv.setViewName("ativar-codigo");
			mv.addObject("mensagem","Tipo de código incorreto.");
			return mv;
		}
		else if(codigos.getAtivo() == 'N') {
			mv = new ModelAndView("ativar-codigo");
			mv.addObject("mensagem","Esse código é antigo, tente gerar um novo código.");
			return mv;
		}
		else if(!codigos.getUsuarioEmail().equals(usuarioLogado.getEmail())) {
			mv = new ModelAndView("ativar-codigo");
			mv.addObject("mensagem","Esse código é referente a outro usuário, você não pode ativá-lo");
			return mv;
		}
		
		mv = new ModelAndView("ativar-codigo");
		mv.setViewName("ativar-codigo");

		Liberacao liberacao = crudLiberacao.findByCodigo(crudCodigos.findByCodigo(codigo.getCodigo()));
		
		if(liberacao.getLiberacaoSiglas().getTipo() != 'P' && liberacao.getLiberacaoSiglas().getTipo() != 'E') {  
			mv = new ModelAndView("ativar-codigo");
			mv.setViewName("ativar-codigo");
			mv.addObject("mensagem","Tipo de código incorreto.");
			return mv;
		}
		
		Calendar cal = DatasUtil.getCalendarDate();
	    java.sql.Date sqlDate = new java.sql.Date(cal.getTimeInMillis());

		Calendar calVencimento = DatasUtil.getCalendarDate();
		calVencimento = DatasUtil.alteraData(calVencimento, 180, 00, 00, 00, 00);
		java.sql.Date sqlDateVencimento = new java.sql.Date(calVencimento.getTimeInMillis());
	    
//		Dando permissões
		
//		Criando Relacao Entre o Professors e a sala
		if(liberacao.getLiberacaoSiglas().getTipo() == 'P') {

			RelacaoPerfil relacaoPerfil = new RelacaoPerfil();
			relacaoPerfil.setUsuario(crudUsuario.findByCpf(usuarioLogado.getCpf()));
			relacaoPerfil.setPerfil(crudPerfilDesc.findById(2));
			crudRelacaoPerfil.save(relacaoPerfil);
			
			RelacaoAcesso relacaoAcesso = new RelacaoAcesso();
			relacaoAcesso.setRelacaoClasse(liberacao.getRelacaoClasse());
			relacaoAcesso.setDtRegistro(sqlDate);
			relacaoAcesso.setDtVencimento(sqlDateVencimento);
			relacaoAcesso.setAcesso('S');
			relacaoAcesso.setUsuarioIncluido(usuarioLogado);
			relacaoAcesso.setUsuarioSolicitante(liberacao.getUsuario());
			crudRelacaoAcesso.save(relacaoAcesso);
		
		}
//		Criando Relacao Entre o aluno e a sala - todas as disciplinas
		else if(liberacao.getLiberacaoSiglas().getTipo() == 'E') {
			
			RelacaoClasse  relacaoClasseTemp = crudRelacaoClasse.findById(liberacao.getRelacaoClasse().getId());
			List<RelacaoClasse> listRelacaoClasse = crudRelacaoClasse.findByClasseAndCurso(relacaoClasseTemp.getClasse(), relacaoClasseTemp.getCurso());
			
			for (int i = 0; i < listRelacaoClasse.size(); i++) {
			
				RelacaoAcesso relacaoAcesso = new RelacaoAcesso();
				relacaoAcesso.setRelacaoClasse(listRelacaoClasse.get(i));
				relacaoAcesso.setDtRegistro(sqlDate);
				relacaoAcesso.setDtVencimento(sqlDateVencimento);
				relacaoAcesso.setAcesso('S');
				relacaoAcesso.setUsuarioIncluido(usuarioLogado);
				relacaoAcesso.setUsuarioSolicitante(liberacao.getUsuario());
				crudRelacaoAcesso.save(relacaoAcesso);

			}
		}
		
//		Inativando código de liberação
		codigos.setAtivo('N');
		crudCodigos.save(codigos);
		
		mv = new ModelAndView("mensagem-logado");
		mv.addObject("mensagem", "Ativação efetuada com sucesso!");
		
		return mv;
	}
	
	
}
