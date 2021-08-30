package com.projectmanager.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.projectmanager.model.CodigoTipoDesc;
import com.projectmanager.model.Codigos;
import com.projectmanager.model.Curso;
import com.projectmanager.model.Liberacao;
import com.projectmanager.model.RelacaoAcesso;
import com.projectmanager.model.RelacaoClasse;
import com.projectmanager.model.Usuario;
import com.projectmanager.repository.ClasseRepository;
import com.projectmanager.repository.CodigoTipoDescRepository;
import com.projectmanager.repository.CodigosRepository;
import com.projectmanager.repository.CursoRepository;
import com.projectmanager.repository.LiberacaoRepository;
import com.projectmanager.repository.LiberacaoSiglasDescRepository;
import com.projectmanager.repository.RelacaoAcessoRepository;
import com.projectmanager.repository.RelacaoClasseRepository;
import com.projectmanager.repository.UsuarioRepository;
import com.projectmanager.util.DatasUtil;
import com.projectmanager.util.EnviarEmailModel;
import com.projectmanager.util.GeraCodigoVerificacaooModel;

@Controller
public class DetalheSalaAdmController {
	
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
	
	@RequestMapping(method = RequestMethod.POST, path = "/adicionar-professor")
	public ModelAndView adicionarColaboradorPOST(@RequestParam(value = "idRelacaoClasse", required = true) Integer idRelacaoClasse, String email, Integer idCurso, Integer idClasse) throws IOException {
	
		UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Usuario usuario = crudUsuario.findByEmail(user.getUsername());
		
		if(idRelacaoClasse == 999) {
			
			ModelAndView mv = recarregaTela("Selecione uma Disciplina.",idCurso,idClasse);
			return mv;
		}
		
		Usuario usuarioCadastrado = crudUsuario.findByEmail(email.toUpperCase());
		
		if(usuarioCadastrado == null) {
			
			ModelAndView mv = recarregaTela("O e-mail informado ainda não foi cadastrado na aplicação, verifique.", idCurso,idClasse);
			mv.addObject("email", email);
			return mv;

		}
		
		List<RelacaoAcesso> listRelacaoAcesso = crudRelacaoAcesso.findByUsuarioIncluido(usuarioCadastrado);
		
//		Verifica se o usuário já foi atrelado a alguma escola
		if(listRelacaoAcesso.size() > 0) {
			
			List<RelacaoAcesso> listUsuarioLogado = crudRelacaoAcesso.findByUsuarioIncluido(usuarioCadastrado);

//			Comparando unidade do usuário logado com unidade do usuário criado - caso seja diferente não deixa conceder acesso a sala
			if(listRelacaoAcesso.get(0).getRelacaoClasse().getUnidade().equals(listUsuarioLogado.get(0).getRelacaoClasse().getUnidade())) {
				
				ModelAndView mv = recarregaTela("O e-mail informado já está cadastrado em outra unidade, verifique.", idCurso,idClasse);
				mv.addObject("email", email);
				return mv;
				
			}

		}
		
		
		
//		Pesquisa o usuário e verifica se já existe um código de liberação
		Codigos codigoAtivoExiste = crudCodigos.findByUsuarioEmailAndAtivoAndCodigoTipo(email.toUpperCase(), 'S', crudCodTipoDesc.findById('L'));
		
		String codigo = null;
//		Verifica se o código gerado já existe
		for (boolean codigoJaExiste = true; codigoJaExiste == true;) {
			codigo = GeraCodigoVerificacaooModel.gerarCodigo();
			codigoJaExiste = crudCodigos.existsByCodigo(codigo);
		}
		
//		Inativando o código antigo
	    if(codigoAtivoExiste != null) {
	    	codigoAtivoExiste.setAtivo('N');
	    	crudCodigos.save(codigoAtivoExiste);
	    	
//			Deletando registro de liberacao antigo
			Liberacao liberacao = crudLiberacao.findByCodigo(crudCodigos.findById(codigoAtivoExiste.getId()));
			crudLiberacao.delete(liberacao);
	    }
	    
//		Salvando novo registro com código enviado
		Codigos registroCodigo = new Codigos();
		registroCodigo.setAtivo('S');
		registroCodigo.setCodigo(codigo);
		registroCodigo.setTipo(new CodigoTipoDesc('L'));
		registroCodigo.setUsuarioEmail(email.toUpperCase());
		
		Calendar cal = DatasUtil.getCalendarDate();
	    java.sql.Date sqlDate = new java.sql.Date(cal.getTimeInMillis());
	    
		registroCodigo.setDtGeracao(sqlDate);
		crudCodigos.save(registroCodigo);
		
		registroCodigo = new Codigos();
		registroCodigo = crudCodigos.findByCodigo(codigo);

//		Criando novo registro de Liberação
		Liberacao liberacao = new Liberacao();
		liberacao.setRelacaoClasse(crudRelacaoClasse.findById(idRelacaoClasse));
		liberacao.setUsuario(usuario);
		liberacao.setLiberacaoSiglas(crudLiberaSiglasDesc.findByTipo('P'));
		liberacao.setCodigo(registroCodigo);
		crudLiberacao.save(liberacao);
		
//		Enviando e-mail com código de Liberação
		EnviarEmailModel.enviarEmailLiberacao(codigo, email);
		
		ModelAndView mv = new ModelAndView("mensagem-liberacao-professor");
		mv.setViewName("mensagem-liberacao-professor");
		mv = recarregaMenu(mv);
		mv.addObject("mensagem","Um e-mail com o código de liberação foi enviado ao destinatário descrito, após a ativação de código você poderá ver seu professor na lista de detalhes da sala");
		mv.addObject("idCurso",idCurso);
		mv.addObject("idClasse",idClasse);
		
		return mv;
	}
	
	public ModelAndView recarregaTela(String mensagem,Integer idCurso, Integer idClasse) {
			
			List<RelacaoClasse> listClasse = crudRelacaoClasse.findByClasseAndCurso(crudClasse.findById(idClasse), crudCurso.findById(idCurso));
			
			Classe classe = listClasse.get(0).getClasse();
			Curso curso = listClasse.get(0).getCurso();
			
			List<RelacaoAcesso> listProfessores = new ArrayList<RelacaoAcesso>();
			List<RelacaoClasse> listSalasSemProfessor = new ArrayList<RelacaoClasse>();
			
			for (int i = 0; i < listClasse.size(); i++) {
				
				List<RelacaoAcesso> listTemp = crudRelacaoAcesso.findByRelacaoClasse(listClasse.get(i));
				
				if(listTemp.size() > 0) {
					
					boolean prof = false;
					
					for (int k = 0; k < listTemp.size() || prof == false; k++) {
						
						Usuario usuarioTemp = listTemp.get(i).getUsuarioIncluido();
						
						User autoridades = new User(usuarioTemp.getUsername(), usuarioTemp.getPassword(), true, true, true, true, usuarioTemp.getAuthorities());
						
						if(autoridades.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_PR"))) {
							
							listProfessores.add(listTemp.get(i));
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
			
			ModelAndView mv = new ModelAndView("detalhe-sala");
			mv.setViewName("detalhe-sala");
			mv = recarregaMenu(mv);
			mv.addObject("classe",classe);
			mv.addObject("curso",curso);
			mv.addObject("listDisciplinasProfessores", listProfessores);
			mv.addObject("listSalasSemProfessor", listSalasSemProfessor);
			mv.addObject("idCurso", idCurso);
			mv.addObject("idClasse", idClasse);
			mv.addObject("mensagem", mensagem);
			
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
