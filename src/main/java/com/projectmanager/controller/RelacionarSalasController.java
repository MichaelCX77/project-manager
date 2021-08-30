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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.projectmanager.auxiliar.AuxiliarBd;
import com.projectmanager.model.Classe;
import com.projectmanager.model.Curso;
import com.projectmanager.model.Disciplina;
import com.projectmanager.model.RelacaoAcesso;
import com.projectmanager.model.RelacaoClasse;
import com.projectmanager.model.Usuario;
import com.projectmanager.repository.ClasseRepository;
import com.projectmanager.repository.CursoRepository;
import com.projectmanager.repository.DisciplinaRepository;
import com.projectmanager.repository.RelacaoAcessoRepository;
import com.projectmanager.repository.RelacaoClasseRepository;
import com.projectmanager.repository.UsuarioRepository;
import com.projectmanager.util.DatasUtil;

@Controller
public class RelacionarSalasController {

	@Autowired
	private UsuarioRepository crudUsuario;
	
	@Autowired
	private RelacaoClasseRepository crudRelacaoClasse;
	
	@Autowired
	private RelacaoAcessoRepository crudRelacaoAcesso;
	
	@Autowired
	private CursoRepository crudCurso;
	
	@Autowired
	private DisciplinaRepository crudDisciplina;
	
	@Autowired
	private ClasseRepository crudClasse;
	
	
	@RequestMapping(method = RequestMethod.POST, path = "/relacionar-sala")
	public ModelAndView relacionarSalaPOST(@RequestParam(value = "idCurso", required = true) Integer idCurso,
			@RequestParam(value = "idDisciplina", required = true) Integer idDisciplina,
			@RequestParam(value = "idClasse", required = true) Integer idClasse) {
		
		ModelAndView mv = null;
		
		UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Usuario usuario = crudUsuario.findByEmail(user.getUsername());
		
		ArrayList<RelacaoAcesso> acessos = crudRelacaoAcesso.findAllByUsuarioIncluidoAndAcesso(usuario,'S');
		List<RelacaoClasse> listaRelacaoClasse = crudRelacaoClasse.findRelacaoClasseByUnidade(acessos.get(0).getRelacaoClasse().getUnidade());
		
		List<Curso> listCursos = new ArrayList<Curso>();
		List<Disciplina> listDisciplinas = new ArrayList<Disciplina>();
		List<Classe> listClasses = new ArrayList<Classe>();
		
		boolean formIncompleto = false;
		
		if(idCurso.equals(999)) {listCursos.addAll(AuxiliarBd.listaCurso(listaRelacaoClasse)); formIncompleto = true;}
		else {listCursos.add(crudCurso.findById(idCurso));}
		
		if(idDisciplina.equals(999)) {listDisciplinas.addAll(AuxiliarBd.listaDisciplina(listaRelacaoClasse)); formIncompleto = true;}
		else {listDisciplinas.add(crudDisciplina.findById(idDisciplina));}
		
		if(idClasse.equals(999)) {listClasses.addAll(AuxiliarBd.listaClasse(listaRelacaoClasse)); formIncompleto = true;}
		else {listClasses.add(crudClasse.findById(idClasse)); }
		
		if(formIncompleto) {
			mv = new ModelAndView("relacionar-sala");
			mv.setViewName("relacionar-sala");
			mv = recarregaMenu(mv);
			mv.addObject("curso",listCursos);
			mv.addObject("disciplina",listDisciplinas);
			mv.addObject("classe",listClasses);
			mv.addObject("mensagem","Todos os campos devem ser selecionados");
			return mv;
		}
		
		RelacaoClasse relacaoClasse = new RelacaoClasse();
		relacaoClasse.setEscola(acessos.get(0).getRelacaoClasse().getEscola());
		relacaoClasse.setUnidade(acessos.get(0).getRelacaoClasse().getUnidade());
		relacaoClasse.setCurso(crudCurso.findById(idCurso));
		relacaoClasse.setDisciplina(crudDisciplina.findById(idDisciplina));
		relacaoClasse.setClasse(crudClasse.findById(idClasse));
		
		boolean relacaoExiste = crudRelacaoClasse.existsByEscolaAndUnidadeAndCursoAndDisciplinaAndClasse(relacaoClasse.getEscola(),relacaoClasse.getUnidade(),relacaoClasse.getCurso(),relacaoClasse.getDisciplina(),relacaoClasse.getClasse());
		
		if(relacaoExiste) {
			
			mv = new ModelAndView("relacionar-sala");
			mv.setViewName("relacionar-sala");
			mv = recarregaMenu(mv);
			mv.addObject("curso",listCursos);
			mv.addObject("disciplina",listDisciplinas);
			mv.addObject("classe",listClasses);
			mv.addObject("mensagem","A relação que está sendo criada já existe nesta unidade.");
			return mv;

		}

		Calendar cal = DatasUtil.getCalendarDate();
	    java.sql.Date sqlDate = new java.sql.Date(cal.getTimeInMillis());

		relacaoClasse.setDtRegistro(sqlDate);
		crudRelacaoClasse.save(relacaoClasse);

		mv = new ModelAndView("mensagem-relacionamento");
		mv.setViewName("mensagem-relacionamento");
		mv = recarregaMenu(mv);
		mv.addObject("mensagem","Relacionamento efetuado com sucesso");
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
