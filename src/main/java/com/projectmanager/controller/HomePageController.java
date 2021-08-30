package com.projectmanager.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpSession;

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
import com.projectmanager.auxiliar.Sala;
import com.projectmanager.model.Classe;
import com.projectmanager.model.Codigos;
import com.projectmanager.model.Curso;
import com.projectmanager.model.Disciplina;
import com.projectmanager.model.RelacaoAcesso;
import com.projectmanager.model.RelacaoClasse;
import com.projectmanager.model.RelacaoTrabalho;
import com.projectmanager.model.Tarefa;
import com.projectmanager.model.Unidade;
import com.projectmanager.model.Usuario;
import com.projectmanager.repository.RelacaoAcessoRepository;
import com.projectmanager.repository.RelacaoClasseRepository;
import com.projectmanager.repository.RelacaoTrabalhoRepository;
import com.projectmanager.repository.StatusTarefaDescRepository;
import com.projectmanager.repository.UsuarioRepository;
import com.projectmanager.util.DatasUtil;

@Controller
public class HomePageController {
	
	@Autowired
	private UsuarioRepository crudUsuario;
	
	@Autowired
	private RelacaoAcessoRepository crudRelacaoAcesso;
	
	
	@Autowired
	private RelacaoTrabalhoRepository crudTrabalho;
	
	@Autowired
	private StatusTarefaDescRepository crudStatusTarefaDesc;
	
	@Autowired
	private RelacaoClasseRepository crudRelacaoClasse;
	
	
//	-------------------- GETS ------------------------------------//
	
	@RequestMapping (method = RequestMethod.GET, path = "/home")
	public ModelAndView homeGET() {
		
		ModelAndView mv = new ModelAndView("home");
		mv.setViewName("home");
		mv = recarregaMenu(mv);
		
		UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Usuario usuario = crudUsuario.findByEmail(user.getUsername());
		ArrayList<RelacaoAcesso> acessos = crudRelacaoAcesso.findAllByUsuarioIncluidoAndAcesso(usuario,'S');
		
		if(acessos.size() <= 0) {
			mv.setViewName("home");
			mv.addObject("mensagem","Você ainda não foi incluído em nenhuma escola, ou sua matrícula está inativa.");
			return mv;
		}

		List<Tarefa> listTarefas = new ArrayList<Tarefa>();
		
		for (int i = 0; i < acessos.size(); i++) {
			
			RelacaoAcesso acesso = acessos.get(i);
			
			List<RelacaoTrabalho> relacaoTrabalhos = new ArrayList<RelacaoTrabalho>();
			relacaoTrabalhos = crudTrabalho.findAllByRelacaoClasse(acesso.getRelacaoClasse());
			
			for (int j = 0; j < relacaoTrabalhos.size(); j++) {
				
				RelacaoTrabalho relacaoTrabalho = relacaoTrabalhos.get(j);
				
				if(relacaoTrabalho.getTarefa() != null) {
					if(relacaoTrabalho.getTarefa().getResponsavel() != null && relacaoTrabalho.getTarefa().getResponsavel().equals(usuario)
							&& relacaoTrabalho.getTarefa().getStatusId() != crudStatusTarefaDesc.findById(4)
							&& relacaoTrabalho.getTarefa().getStatusId() != crudStatusTarefaDesc.findById(3)) {
						
						Tarefa tarefa = relacaoTrabalho.getTarefa();
						tarefa.setDisciplina(relacaoTrabalho.getRelacaoClasse().getDisciplina());
						tarefa.setClasse(relacaoTrabalho.getRelacaoClasse().getClasse());
						tarefa.setCurso(relacaoTrabalho.getRelacaoClasse().getCurso());
						listTarefas.add(tarefa);
					}
				}
			}
		}
		
		if(listTarefas.size() <= 0 ) {
			mv.addObject("mensagem","Você não possui nenhuma tarefa pendente");
			return mv;
		}

//		Convertendo datas para exibição
		for(int i =0; i < listTarefas.size();i++) {
			listTarefas.get(i).setDtInicialFormatada(DatasUtil.convertAndFormatToString(listTarefas.get(i).getDtInicial(), "dd/MM/yyyy - HH:mm"));
			listTarefas.get(i).setDtFinalFormatada(DatasUtil.convertAndFormatToString(listTarefas.get(i).getDtFinal(), "dd/MM/yyyy - HH:mm"));
		}
		
		mv.addObject("tarefa", listTarefas);
		mv.setViewName("home");
		
		return mv;
		
	}
	
	@RequestMapping (method = RequestMethod.GET, path = "/logout")
	public String logoutGET(HttpSession session) {
		session.invalidate();
		return "redirect:/login";
		
	}
	
	@RequestMapping (method = RequestMethod.GET, path = "/meus-cursos")
	public ModelAndView meusCursosGET() {
		
		ModelAndView mv = new ModelAndView("meus-cursos");
		mv.setViewName("meus-cursos");
		mv = recarregaMenu(mv);
		
		UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Usuario usuario = crudUsuario.findByEmail(user.getUsername());
		ArrayList<RelacaoAcesso> acessos = crudRelacaoAcesso.findAllByUsuarioIncluidoAndAcesso(usuario,'S');
		
		if(acessos.size() <= 0) {
			
			mv.addObject("mensagem","Você ainda não está relacionado a nenhuma unidade.");
			return mv;
			
		}
		
		List<RelacaoClasse> relacaoClasse = crudRelacaoClasse.findRelacaoClasseByUnidade(acessos.get(0).getRelacaoClasse().getUnidade());
		List<Curso> listCursos = AuxiliarBd.listaCurso(relacaoClasse);
		
		if(listCursos.size() <= 0) {
			mv.addObject("mensagem","Você ainda não possui nenhum curso.");
			return mv;
		}
		
		mv.addObject("curso",listCursos);
		
		return mv;
		
	}
	
	@RequestMapping (method = RequestMethod.GET, path = "/relacionar-sala")
	public ModelAndView relacionarSalaGET() {

		UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Usuario usuario = crudUsuario.findByEmail(user.getUsername());
		ArrayList<RelacaoAcesso> acessos = crudRelacaoAcesso.findAllByUsuarioIncluidoAndAcesso(usuario,'S');
		
		List<RelacaoClasse> listaRelacaoClasse = crudRelacaoClasse.findRelacaoClasseByUnidade(acessos.get(0).getRelacaoClasse().getUnidade());
		
		List<Curso> listCursos = new ArrayList<Curso>();
		List<Disciplina> listDisciplinas = new ArrayList<Disciplina>();
		List<Classe> listClasses = new ArrayList<Classe>();
		
		listCursos.addAll(AuxiliarBd.listaCurso(listaRelacaoClasse));
		listDisciplinas.addAll(AuxiliarBd.listaDisciplina(listaRelacaoClasse));
		listClasses.addAll(AuxiliarBd.listaClasse(listaRelacaoClasse));
		
		ModelAndView mv = new ModelAndView("relacionar-sala");
		mv.setViewName("relacionar-sala");
		mv = recarregaMenu(mv);
		mv.addObject("curso",listCursos);
		mv.addObject("disciplina",listDisciplinas);
		mv.addObject("classe",listClasses);
			
		return mv;
		
	}
	
	@RequestMapping (method = RequestMethod.GET, path = "/minhas-classes")
	public ModelAndView minhasClassesGET() {

		ModelAndView mv = new ModelAndView("minhas-classes");
		mv.setViewName("minhas-classes");
		mv = recarregaMenu(mv);
		
		UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Usuario usuario = crudUsuario.findByEmail(user.getUsername());
		ArrayList<RelacaoAcesso> acessos = crudRelacaoAcesso.findAllByUsuarioIncluidoAndAcesso(usuario,'S');
		
		if(acessos.size() <= 0) {
			
			mv.addObject("mensagem","Você ainda não está relacionado a nenhuma unidade.");
			return mv;
			
		}
		
		List<RelacaoClasse> relacaoClasse = crudRelacaoClasse.findRelacaoClasseByUnidade(acessos.get(0).getRelacaoClasse().getUnidade());
		List<Classe> listClasses = AuxiliarBd.listaClasse(relacaoClasse);
		
		if(listClasses.size() <= 0) {
			mv.addObject("mensagem","Você ainda não possui nenhuma classe.");
			return mv;
		}
		
		mv.addObject("classe",listClasses);
		
		return mv;
	}
	
	@RequestMapping (method = RequestMethod.GET, path = "/meus-trabalhos")
	public ModelAndView meusTrabalhosGET() {

		ModelAndView mv = new ModelAndView("meus-trabalhos");
		mv.setViewName("meus-trabalhos");
		
		return mv;
		
	}
	
	@RequestMapping (method = RequestMethod.GET, path = "/avaliacoes")
	public ModelAndView avaliacoesGET() {

		ModelAndView mv = new ModelAndView("avaliacoes");
		mv.setViewName("atuacao-aluno");
		
		return mv;
		
	}
	
	@RequestMapping (method = RequestMethod.GET, path = "/atuacao-aluno")
	public ModelAndView atuacaoAlunoGET() {

		ModelAndView mv = new ModelAndView("atuacao-aluno");
		mv.setViewName("atuacao-aluno");
		
		return mv;
		
	}
	
	@RequestMapping (method = RequestMethod.GET, path = "/solicitar-licenca")
	public ModelAndView solicitarLicencaGET() {

		ModelAndView mv = new ModelAndView("solicitar-licenca");
		mv.setViewName("solicitar-licenca");
		
		return mv;
		
	}
	
	@RequestMapping (method = RequestMethod.GET, path = "/aprovar-licenca")
	public ModelAndView aprovarLicencaGET() {

		ModelAndView mv = new ModelAndView("aprovar-licenca");
		mv.setViewName("aprovar-licenca");
		
		return mv;
		
	}
	
	@RequestMapping (method = RequestMethod.GET, path = "/liberar-licenca")
	public ModelAndView liberarLicencaGET() {

		ModelAndView mv = new ModelAndView("liberar-licenca");
		mv.setViewName("liberar-licenca");
		
		return mv;
		
	}
	
	@RequestMapping (method = RequestMethod.GET, path = "/criar-classe")
	public ModelAndView criarClasseGET() {

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
		
		ModelAndView mv = null;
		
		if (listaRelacaoClasse.size() <= 0) {
			
			mv = new ModelAndView("mensagem-logado");
			mv = recarregaMenu(mv);
			mv.setViewName("mensagem-logado");
			mv.addObject("mensagem","Para criar uma classe, primeiro você deve criar uma unidade.");
			return mv;
		}
		
		List<Unidade> listaUnidade = new ArrayList<Unidade>();
		
//		Adicionando seleção default
		Unidade unidade = new Unidade();
		unidade.setId(999);
		unidade.setNome("Selecione");
		
		listaUnidade.add(unidade);
		listaUnidade.addAll(AuxiliarBd.listaUnidade(listaRelacaoClasse));
		
		Classe classe =new Classe();
		
//		Adicionando seleção default
		Curso curso = new Curso();
		curso.setId(999);
		curso.setNomeCurso("Selecione");
		
		mv = new ModelAndView("criar-classe");
		mv = recarregaMenu(mv);
		mv.addObject("unidade", listaUnidade);
		mv.addObject("classe", classe);
		mv.addObject("curso", curso);
		mv.setViewName("criar-classe");
	
		return mv;
		
	}
	
	@RequestMapping (method = RequestMethod.GET, path = "/criar-unidade")
	public ModelAndView criarUnidadeGET() {

		Unidade unidade = new Unidade();
		ModelAndView mv = new ModelAndView("criar-unidade");
		mv = recarregaMenu(mv);
		mv.setViewName("criar-unidade");
		mv.addObject("unidade", unidade);
		
		return mv;
		
	}
	
	@RequestMapping (method = RequestMethod.GET, path = "/perfil")
	public ModelAndView perfilGET() {
		ModelAndView mv = new ModelAndView("perfil");
		mv = recarregaMenu(mv);
		mv.setViewName("perfil");
		
		UserDetails userSession = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Usuario usuario = crudUsuario.findByEmail(userSession.getUsername());
		
		usuario.setEmail(usuario.getEmail().toLowerCase());
		mv.addObject("usuario",usuario);
		
		Calendar cal = Calendar.getInstance();
    	cal.setTime(usuario.getDtNasc());
		Integer idade = DatasUtil.calcAnosPorDataDeNascimento(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), Calendar.DAY_OF_MONTH);
		
		mv.addObject("idade",idade);
		
		return mv;
		
	}
	
	@RequestMapping (method = RequestMethod.GET, path = "/notificacoes")
	public ModelAndView notificacoesGET() {

		ModelAndView mv = new ModelAndView("notificacoes");
		mv.setViewName("notificacoes");
		
		return mv;
		
	}
	
	@RequestMapping (method = RequestMethod.GET, path = "/conversas")
	public ModelAndView conversasGET() {

		ModelAndView mv = new ModelAndView("conversas");
		mv.setViewName("conversas");
		
		return mv;
		
	}
	
	@RequestMapping (method = RequestMethod.GET, path = "/minhas-unidades")
	public ModelAndView minhasUnidadesGET() {
		
		ModelAndView mv = new ModelAndView("minhas-unidades");
		mv.setViewName("minhas-unidades");
		mv = recarregaMenu(mv);
		
		UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Usuario usuario = crudUsuario.findByEmail(user.getUsername());
		ArrayList<RelacaoAcesso> acessos = crudRelacaoAcesso.findAllByUsuarioIncluidoAndAcesso(usuario,'S');
		
		List<RelacaoClasse> relacaoClasse = crudRelacaoClasse.findAllByEscola(acessos.get(0).getRelacaoClasse().getEscola());
		List<Unidade> listUnidades = new ArrayList<Unidade>();
		
		for (int i = 0; i < relacaoClasse.size(); i++) {
			
			Unidade unidade = relacaoClasse.get(i).getUnidade();	
			if (unidade != null && !listUnidades.contains(unidade)) {listUnidades.add(unidade);}
		}
		
		if(listUnidades.size() <= 0) {
			mv.addObject("mensagem","Você ainda não criou nenhuma unidade");
			return mv;
		}

		mv.addObject("unidade",listUnidades);
		return mv;
		
	}
	
	@RequestMapping (method = RequestMethod.GET, path = "/criar-disciplina")
	public ModelAndView criarDisciplinaGET() {

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
		
		ModelAndView mv = null;
		
		if (listaRelacaoClasse.size() <= 0) {
			
			mv = new ModelAndView("mensagem-logado");
			mv = recarregaMenu(mv);
			mv.setViewName("mensagem-logado");
			mv.addObject("mensagem","Para criar uma disciplina, primeiro você deve criar uma unidade.");
			return mv;
		}
		
		List<Unidade> listaUnidade = new ArrayList<Unidade>();
		
//		Adicionando seleção default
		Unidade unidade = new Unidade();
		unidade.setId(999);
		unidade.setNome("Selecione");
		
		listaUnidade.add(unidade);
		listaUnidade.addAll(AuxiliarBd.listaUnidade(listaRelacaoClasse));
		
		Disciplina disciplina = new Disciplina();
		
//		Adicionando seleção default
		Curso curso = new Curso();
		curso.setId(999);
		curso.setNomeCurso("Selecione");
		
		mv = new ModelAndView("criar-disciplina");
		mv = recarregaMenu(mv);
		mv.addObject("unidade", listaUnidade);
		mv.addObject("disciplina", disciplina);
		mv.addObject("curso", curso);
		mv.setViewName("criar-disciplina");
	
		return mv;
		
	}
	
	@RequestMapping (method = RequestMethod.GET, path = "/ajuda")
	public ModelAndView ajudaGET() {

		ModelAndView mv = new ModelAndView("ajuda");
		mv.setViewName("ajuda");
		return mv;
		
	}

	@RequestMapping (method = RequestMethod.GET, path = "/configuracoes")
	public ModelAndView configuracoesGET() {

		ModelAndView mv = new ModelAndView("configuracoes");
		mv.setViewName("configuracoes");
		return mv;
		
	}
	
//	carrega lista de Unidades
	@RequestMapping (method = RequestMethod.GET, path = "/criar-curso")
	public ModelAndView criarCursosGET() {

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
		ModelAndView mv = null;
		
		if (listaRelacaoClasse.size() <= 1) {
			
			mv = new ModelAndView("mensagem-logado");
			mv = recarregaMenu(mv);
			mv.setViewName("mensagem-logado");
			mv.addObject("mensagem","Para criar um curso, primeiro você deve criar uma unidade.");
			return mv;
		}
		
		List<Unidade> listaUnidade = new ArrayList<Unidade>();
		
		Unidade unidade = new Unidade();
		unidade.setId(999);
		unidade.setNome("Selecione");
		
		listaUnidade.add(unidade);
		listaUnidade.addAll(AuxiliarBd.listaUnidade(listaRelacaoClasse));
		
		Curso curso = new Curso();
		mv = new ModelAndView("criar-curso");
		mv = recarregaMenu(mv);
		mv.addObject("unidade", listaUnidade);
		mv.addObject("curso", curso);
		mv.setViewName("criar-curso");
		
		return mv;
		
	}
	
	@RequestMapping (method = RequestMethod.GET, path = "/meus-grupos")
	public ModelAndView meusGruposGET() {

		ModelAndView mv = new ModelAndView("meus-grupos");
		mv.setViewName("meus-grupos");
		return mv;
		
	}
	
	@RequestMapping (method = RequestMethod.GET, path = "/minhas-salas-integrantes")
	public ModelAndView minhasSalasIntegrantesGET() {
		
		ModelAndView mv = new ModelAndView("minhas-salas-integrantes");
		mv.setViewName("minhas-salas-integrantes");
		mv = recarregaMenu(mv);
		
		UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Usuario usuario = crudUsuario.findByEmail(user.getUsername());
		ArrayList<RelacaoAcesso> acessos = crudRelacaoAcesso.findAllByUsuarioIncluidoAndAcesso(usuario,'S');
		
		if(acessos.size() <= 0) {
			
			mv.addObject("mensagem","Você ainda não está relacionado a nenhuma unidade.");
			return mv;
			
		}
		
		List<RelacaoClasse> relacaoClasse = crudRelacaoClasse.findAllByRelacaoClasse(usuario);
		
		List<RelacaoClasse> listSalas = AuxiliarBd.listaSalas(relacaoClasse);
		
		if(listSalas.size() <= 0) {
			mv.addObject("mensagem","Você ainda não possui nenhuma sala relacionada.");
			return mv;
		}
		
		List<Sala> salas = new ArrayList<Sala>();
		Sala sala = null;
		for (int i = 0; i < listSalas.size(); i++) {
			
			RelacaoClasse salaAnterior = null;
			
			
			if(i > 0) {
				salaAnterior = listSalas.get(i-1);
			}
			
			RelacaoClasse salaAtual = listSalas.get(i);
			
			
				if(i == 0) {
					sala = new Sala(salaAtual.getCurso(),salaAtual.getClasse(), new ArrayList<Disciplina>());
					sala.getDisciplinas().add(salaAtual.getDisciplina());
				}
				else if((salaAtual.getClasse() != salaAnterior.getClasse() || salaAtual.getCurso() != salaAnterior.getCurso())) {
					salas.add(sala);
					sala = new Sala(salaAtual.getCurso(),salaAtual.getClasse(), new ArrayList<Disciplina>());
					sala.getDisciplinas().add(salaAtual.getDisciplina());
				}
				else {
					
					sala.getDisciplinas().add(salaAtual.getDisciplina());
				}
				
				if(i == listSalas.size()-1) {
					salas.add(sala);
				}
			
		}
		
		mv.addObject("salas",salas);
		
		return mv;
	}
	
	
	@RequestMapping (method = RequestMethod.GET, path = "/minhas-salas-adm")
	public ModelAndView minhaSalasGET() {

		ModelAndView mv = new ModelAndView("minhas-salas-adm");
		mv.setViewName("minhas-salas-adm");
		mv = recarregaMenu(mv);
		
		UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Usuario usuario = crudUsuario.findByEmail(user.getUsername());
		ArrayList<RelacaoAcesso> acessos = crudRelacaoAcesso.findAllByUsuarioIncluidoAndAcesso(usuario,'S');
		
		if(acessos.size() <= 0) {
			
			mv.addObject("mensagem","Você ainda não está relacionado a nenhuma unidade.");
			return mv;
			
		}
		
		List<RelacaoClasse> relacaoClasse = crudRelacaoClasse.findAllByUnidade(acessos.get(0).getRelacaoClasse().getUnidade());
		List<RelacaoClasse> listSalas = AuxiliarBd.listaSalas(relacaoClasse);
		
		if(listSalas.size() <= 0) {
			mv.addObject("mensagem","Você ainda não possui nenhuma sala relacionada.");
			return mv;
		}
		
		List<Sala> salas = new ArrayList<Sala>();
		Sala sala = null;
		for (int i = 0; i < listSalas.size(); i++) {
			
			RelacaoClasse salaAnterior = null;
			
			
			if(i > 0) {
				salaAnterior = listSalas.get(i-1);
			}
			
			RelacaoClasse salaAtual = listSalas.get(i);
			
			
				if(i == 0) {
					sala = new Sala(salaAtual.getCurso(),salaAtual.getClasse(), new ArrayList<Disciplina>());
					sala.getDisciplinas().add(salaAtual.getDisciplina());
				}
				else if((salaAtual.getClasse() != salaAnterior.getClasse() || salaAtual.getCurso() != salaAnterior.getCurso())) {
					salas.add(sala);
					sala = new Sala(salaAtual.getCurso(),salaAtual.getClasse(), new ArrayList<Disciplina>());
					sala.getDisciplinas().add(salaAtual.getDisciplina());
				}
				else {
					
					sala.getDisciplinas().add(salaAtual.getDisciplina());
				}
				
				if(i == listSalas.size()-1) {
					salas.add(sala);
				}
			
		}
		
		mv.addObject("salas",salas);

		return mv;
	}
	
	@RequestMapping (method = RequestMethod.GET, path = "/minhas-disciplinas")
	public ModelAndView minhasDisciplinasGET() {
		
		ModelAndView mv = new ModelAndView("minhas-disciplinas");
		mv.setViewName("minhas-disciplinas");
		mv = recarregaMenu(mv);
		
		UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Usuario usuario = crudUsuario.findByEmail(user.getUsername());
		ArrayList<RelacaoAcesso> acessos = crudRelacaoAcesso.findAllByUsuarioIncluidoAndAcesso(usuario,'S');
		
		if(acessos.size() <= 0) {
			
			mv.addObject("mensagem","Você ainda não está relacionado a nenhuma unidade.");
			return mv;
			
		}
		
		List<RelacaoClasse> relacaoClasse = crudRelacaoClasse.findRelacaoClasseByUnidade(acessos.get(0).getRelacaoClasse().getUnidade());
		List<Disciplina> listDisciplina = AuxiliarBd.listaDisciplina(relacaoClasse);
		
		if(listDisciplina.size() <= 0) {
			mv.addObject("mensagem","Você ainda não possui nenhuma disciplina.");
			return mv;
		}
		
		mv.addObject("disciplina",listDisciplina);
		
		return mv;
		
	}
	
	@RequestMapping (method = RequestMethod.GET, path = "/opcoes")
	public ModelAndView opcoesGET() {

		ModelAndView mv = new ModelAndView("opcoes");
		mv.setViewName("opcoes");
		return mv;
		
	}
	
	@RequestMapping (method = RequestMethod.GET, path = "/pesquisa-geral")
	public ModelAndView pesquisaGeralGET() {

		ModelAndView mv = new ModelAndView("pesquisa-geral");
		mv.setViewName("pesquisa-geral");
		return mv;
		
	}
	
	@RequestMapping (method = RequestMethod.GET, path = "/visualizar-notas")
	public ModelAndView visualizarNotasGET() {
		
		ModelAndView mv = new ModelAndView("visualizar-notas");
		mv.setViewName("visualizar-notas");
		return mv;
		
	}
	
	@RequestMapping (method = RequestMethod.GET, path = "/acess-denied")
	public ModelAndView acessDeniedGET() {
		
		ModelAndView mv = new ModelAndView("acess-denied");
		mv.setViewName("acess-denied");
		return mv;
		
	}
	
	@RequestMapping (method = RequestMethod.POST, path = "/acess-denied")
	public ModelAndView acessDeniedPOST() {
		
		ModelAndView mv = new ModelAndView("acess-denied");
		mv.setViewName("acess-denied");
		return mv;
		
	}
	
	@RequestMapping (method = RequestMethod.GET, path = "/meus-financeiros")
	public ModelAndView meusFinanceirosGET() {
		
		ModelAndView mv = retornaMeusColaboradores("FI");
		
		return mv;
		
	}
	
	@RequestMapping (method = RequestMethod.GET, path = "/meus-secretarios")
	public ModelAndView meusSecretariosGET() {
		
		ModelAndView mv = retornaMeusColaboradores("SE");
		
		return mv;
		
	}
	
	@RequestMapping (method = RequestMethod.GET, path = "/meus-administradores")
	public ModelAndView meusAdministradoresGET() {
		
		ModelAndView mv = retornaMeusColaboradores("AD");
		
		return mv;
		
	}
	
	@RequestMapping (method = RequestMethod.GET, path = "/ativar-codigo")
	public ModelAndView ativarCodigoGET() {
		
		ModelAndView mv = new ModelAndView("ativar-codigo");
		mv.setViewName("ativar-codigo");
		mv = recarregaMenu(mv);
		mv.addObject("codigos", new Codigos());
		
		return mv;
		
	}
	
	public ModelAndView retornaMeusColaboradores(String tela) {
		
		UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Usuario usuario = crudUsuario.findByEmail(user.getUsername());
		User autoridades = new User(usuario.getUsername(), usuario.getPassword(), true, true, true, true, usuario.getAuthorities());
		
		ArrayList<RelacaoAcesso> acessos = crudRelacaoAcesso.findAllByUsuarioIncluidoAndAcesso(usuario,'S');
		
		List<RelacaoClasse> listaRelacaoClasse = new ArrayList<RelacaoClasse>();
		
		
		if(tela.equals("AD") && autoridades.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ES"))) {
			
			listaRelacaoClasse = crudRelacaoClasse.findAllByEscola(acessos.get(0).getRelacaoClasse().getEscola());
			
		} else {
			
			listaRelacaoClasse = crudRelacaoClasse.findRelacaoClasseByUnidade(acessos.get(0).getRelacaoClasse().getUnidade());
			
		}
		
		List<RelacaoClasse> listaRelacaoClasseUnidade = AuxiliarBd.listaRelacaoClassePorUnidade(listaRelacaoClasse);
		
		List<RelacaoAcesso> listColaboradores = new ArrayList<RelacaoAcesso>();
		List<RelacaoClasse> listUnidadesSemColaborador = new ArrayList<RelacaoClasse>();
		
//		Verificando os usuários atrelados a unidade e separando lista de Admnistradores.
		for (int i = 0; i < listaRelacaoClasseUnidade.size(); i++) {
			
			List<RelacaoAcesso> usuariosRelacionadosAUnidade = crudRelacaoAcesso.findByRelacaoClasse(listaRelacaoClasseUnidade.get(i));
			
			if(usuariosRelacionadosAUnidade.size() <= 0) {
				
				listUnidadesSemColaborador.add(listaRelacaoClasseUnidade.get(i));
				
			}
			else {
				boolean adm = false;
				
				for (int j = 0; j < usuariosRelacionadosAUnidade.size() && adm == false; j++) {
						
						Usuario usuarioTemp = usuariosRelacionadosAUnidade.get(j).getUsuarioIncluido();
						
						User autoridadesTemp = new User(usuarioTemp.getUsername(), usuarioTemp.getPassword(), true, true, true, true, usuarioTemp.getAuthorities());
						
//						Verificando se tem todas as permissões de administrador
						if(tela.equals("AD") && autoridadesTemp.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_AD"))
								&& autoridadesTemp.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SE"))
								&& autoridadesTemp.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_PR"))) {
							
							usuariosRelacionadosAUnidade.get(j).getUsuarioIncluido().setEmail(usuarioTemp.getEmail().toLowerCase());
							listColaboradores.add(usuariosRelacionadosAUnidade.get(j));
							
							if(tela.equals("AD")) {
								adm = true;
							}
							
						}
//						Verificando se tem todas as permissões de financeiro
						else if(tela.equals("FI") && autoridadesTemp.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_FI"))) {
							
							usuariosRelacionadosAUnidade.get(j).getUsuarioIncluido().setEmail(usuarioTemp.getEmail().toLowerCase());
							listColaboradores.add(usuariosRelacionadosAUnidade.get(j));
							
							if(tela.equals("AD")) {
								adm = true;
							}
							
						}
//						Verificando se tem todas as permissões de sectário
						else if(tela.equals("SE") && autoridadesTemp.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SE"))
								&& autoridadesTemp.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_AL"))) {
							
							usuariosRelacionadosAUnidade.get(j).getUsuarioIncluido().setEmail(usuarioTemp.getEmail().toLowerCase());
							listColaboradores.add(usuariosRelacionadosAUnidade.get(j));
							
							if(tela.equals("AD")) {
								adm = true;
							}
							
						}
				}
				
				if(!adm) {
					
					listUnidadesSemColaborador.add(listaRelacaoClasseUnidade.get(i));
				}
			}

		}

		ModelAndView mv = null;
		
		if(tela.equals("AD")) {
			mv = new ModelAndView("meus-administradores");
			mv.setViewName("meus-administradores");
		}
		else if(tela.equals("FI")) {
			mv = new ModelAndView("meus-financeiros");
			mv.setViewName("meus-financeiros");	
			mv.addObject("idRelacaoClasse", acessos.get(0).getRelacaoClasse().getId());
		}
		else if(tela.equals("SE")) {
			mv = new ModelAndView("meus-secretarios");
			mv.setViewName("meus-secretarios");	
			mv.addObject("idRelacaoClasse", acessos.get(0).getRelacaoClasse().getId());
		}
		
		mv = recarregaMenu(mv);
		mv.addObject("colaboradores",listColaboradores);
		mv.addObject("unidadesSemColaborador", listUnidadesSemColaborador);
		
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
