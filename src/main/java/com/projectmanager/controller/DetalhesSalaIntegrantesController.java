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
import org.springframework.web.servlet.ModelAndView;

import com.projectmanager.auxiliar.AuxiliarBd;
import com.projectmanager.model.Classe;
import com.projectmanager.model.CodigoTipoDesc;
import com.projectmanager.model.Codigos;
import com.projectmanager.model.Disciplina;
import com.projectmanager.model.Liberacao;
import com.projectmanager.model.RelacaoAcesso;
import com.projectmanager.model.RelacaoClasse;
import com.projectmanager.model.RelacaoTrabalho;
import com.projectmanager.model.Usuario;
import com.projectmanager.repository.ClasseRepository;
import com.projectmanager.repository.CodigoTipoDescRepository;
import com.projectmanager.repository.CodigosRepository;
import com.projectmanager.repository.CursoRepository;
import com.projectmanager.repository.LiberacaoRepository;
import com.projectmanager.repository.LiberacaoSiglasDescRepository;
import com.projectmanager.repository.RelacaoAcessoRepository;
import com.projectmanager.repository.RelacaoClasseRepository;
import com.projectmanager.repository.RelacaoTrabalhoRepository;
import com.projectmanager.repository.UsuarioRepository;
import com.projectmanager.util.DatasUtil;
import com.projectmanager.util.EnviarEmailModel;
import com.projectmanager.util.GeraCodigoVerificacaooModel;

@Controller
public class DetalhesSalaIntegrantesController {
	
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

	@RequestMapping (method= RequestMethod.POST, path = "/adicionar-aluno")
	public ModelAndView detalheMinhasSalasIntegrantesPOST(String email, Integer idCurso, Integer idClasse) throws IOException {

		UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Usuario usuario = crudUsuario.findByEmail(user.getUsername());
		
		Usuario usuarioCadastrado = crudUsuario.findByEmail(email.toUpperCase());
		
		if(usuarioCadastrado == null) {
			
			ModelAndView mv = recarregaTela("O e-mail informado ainda não foi cadastrado na aplicação, verifique.", idCurso,idClasse);
			mv.addObject("email", email);
			return mv;

		}
		
//		Verifica se o usuário já foi atrelado a alguma escola
		List<RelacaoAcesso> listRelacaoAcesso = crudRelacaoAcesso.findByUsuarioIncluido(usuarioCadastrado);

		if(listRelacaoAcesso.size() > 0) {

				ModelAndView mv = recarregaTela("O e-mail informado já está cadastrado em uma unidade, verifique.", idCurso,idClasse);
				mv.addObject("email", email);
				return mv;


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
		
		List<RelacaoClasse> listRelacaoClasse = crudRelacaoClasse.findByClasseAndCurso(crudClasse.findById(idClasse),crudCurso.findById(idCurso));
		
//		Criando novo registro de Liberação
		Liberacao liberacao = new Liberacao();
		liberacao.setRelacaoClasse(listRelacaoClasse.get(0));
		liberacao.setUsuario(usuario);
		liberacao.setLiberacaoSiglas(crudLiberaSiglasDesc.findByTipo('E'));
		liberacao.setCodigo(registroCodigo);
		crudLiberacao.save(liberacao);
		
//		Enviando e-mail com código de Liberação
		EnviarEmailModel.enviarEmailLiberacao(codigo, email);
		
		ModelAndView mv = recarregaTela("Código de ativação enviado ao aluno.", idCurso,idClasse);
		mv = recarregaMenu(mv);
		
		return mv;
		
	}
	
	@RequestMapping (method= RequestMethod.GET, path = "/trabalhos-classe")
	public ModelAndView trabalhosGET(Integer idCurso, Integer idClasse) {
		
		UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Usuario usuario = crudUsuario.findByEmail(user.getUsername());
		
		List<RelacaoClasse> relacaoClasse = crudRelacaoClasse.findAllByRelacaoClasse(usuario);
		List<RelacaoClasse> listSalasUsuario = AuxiliarBd.listaSalas(relacaoClasse);
		List<RelacaoClasse> listDisciplinasSalaAtual = crudRelacaoClasse.findByClasseAndCurso(crudClasse.findById(idClasse), crudCurso.findById(idCurso));
		
		
		for (int i = 0; i < listSalasUsuario.size(); i++) {
			
			if(!listDisciplinasSalaAtual.contains(listSalasUsuario.get(i))) {
				listSalasUsuario.remove(i);
				i--;
			}
		}
		
		List<RelacaoTrabalho> listTrabalhos = new ArrayList<RelacaoTrabalho>();
		
		for (int i = 0; i < listSalasUsuario.size(); i++) {
			
			List<RelacaoTrabalho> listTrabalhosTemp = crudRelacaoTrabalho.findAllByRelacaoClasse(listSalasUsuario.get(i));
			
			if(listTrabalhosTemp.size() > 0) {
				listTrabalhos.addAll(AuxiliarBd.listaRelacaoTrabalhoPorTrabalho(listTrabalhosTemp));
			}
		}
		
		ModelAndView mv = new ModelAndView("trabalhos-classe");
		
		if(listTrabalhos.size() <= 0) {
			
			mv.addObject("mensagem", "Essa sala ainda não possui nenhum trabalho relacionado as suas disciplinas.");

		}

		Classe classe = crudClasse.findById(idClasse);
		
		mv.setViewName("trabalhos-classe");
		mv = recarregaMenu(mv);
		mv.addObject("listTrabalhos", listTrabalhos);
		mv.addObject("classe", classe);
		mv.addObject("idCurso", idCurso);
		mv.addObject("idClasse", idClasse);
		
		return mv;
	}
	
	public ModelAndView recarregaTela(String mensagem,Integer idCurso, Integer idClasse) {
		
		List<RelacaoClasse> listClasse = crudRelacaoClasse.findByClasseAndCurso(crudClasse.findById(idClasse), crudCurso.findById(idCurso));
		
		List<Disciplina> listDisciplinas = new ArrayList<Disciplina>();
		List<RelacaoAcesso> listAlunos = new ArrayList<RelacaoAcesso>();
//		Carregando lista de Disciplinas e de Alunos
		for (int i = 0; i < listClasse.size(); i++) {
			
			listDisciplinas.add(listClasse.get(i).getDisciplina());
			
			List<RelacaoAcesso> alunosTemp = crudRelacaoAcesso.findByRelacaoClasse(listClasse.get(i));
			
			for (int j = 0; j < alunosTemp.size(); j++) {
				
				Usuario aluno = alunosTemp.get(j).getUsuarioIncluido();
				
				User autoridades = new User(aluno.getUsername(), aluno.getPassword(), true, true, true, true, aluno.getAuthorities());
				
//				Verificando se tem todas as permissões de administrador
				if(autoridades.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_AL"))
						&& !autoridades.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_PR"))) {
					
					alunosTemp.get(j).setDtRegistroFormatada(DatasUtil.convertAndFormatToString(alunosTemp.get(j).getDtRegistro(), "dd/MM/yyyy"));
					alunosTemp.get(j).setDtVencimentoFormatada(DatasUtil.convertAndFormatToString(alunosTemp.get(j).getDtVencimento(), "dd/MM/yyyy"));
					alunosTemp.get(j).getUsuarioIncluido().setEmail(aluno.getEmail().toLowerCase());
					listAlunos.add(alunosTemp.get(j));
				}		
			}					
		}
	
		Classe classe = listClasse.get(0).getClasse();
		classe.setDtCriacaoFormatada(DatasUtil.convertAndFormatToString(classe.getDtCriacao(), "dd/MM/yyyy"));
		
		ModelAndView mv = new ModelAndView("detalhe-sala-integrantes");
		mv.setViewName("detalhe-sala-integrantes");
		mv = recarregaMenu(mv);
		mv.addObject("curso",listClasse.get(0).getCurso());
		mv.addObject("classe",classe);
		mv.addObject("usuario",new Usuario());
		mv.addObject("disciplina", listDisciplinas);
		mv.addObject("idCurso", idCurso);
		mv.addObject("idClasse", idClasse);
		mv.addObject("mensagem", mensagem);
		
		if(listAlunos.size() <= 0) {mv.addObject("mensagemAlunos", "Essa sala ainda não possui alunos.");}
		else {
			mv.addObject("listAlunos", listAlunos);
		}
		
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
