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

import com.projectmanager.auxiliar.AuxiliarBd;
import com.projectmanager.model.CodigoTipoDesc;
import com.projectmanager.model.Codigos;
import com.projectmanager.model.Liberacao;
import com.projectmanager.model.RelacaoAcesso;
import com.projectmanager.model.RelacaoClasse;
import com.projectmanager.model.Usuario;
import com.projectmanager.repository.CodigoTipoDescRepository;
import com.projectmanager.repository.CodigosRepository;
import com.projectmanager.repository.LiberacaoRepository;
import com.projectmanager.repository.LiberacaoSiglasDescRepository;
import com.projectmanager.repository.RelacaoAcessoRepository;
import com.projectmanager.repository.RelacaoClasseRepository;
import com.projectmanager.repository.UsuarioRepository;
import com.projectmanager.util.DatasUtil;
import com.projectmanager.util.EnviarEmailModel;
import com.projectmanager.util.GeraCodigoVerificacaooModel;

@Controller
public class MeusColaboradoresController {
	
	@Autowired
	UsuarioRepository crudUsuario;

	@Autowired
	RelacaoAcessoRepository crudRelacaoAcesso;
	
	@Autowired
	RelacaoClasseRepository crudRelacaoClasse;
	
	@Autowired
	CodigosRepository crudCodigos;
	
	@Autowired
	CodigoTipoDescRepository crudCodTipoDesc;

	@Autowired
	LiberacaoRepository crudLiberacao;
	
	@Autowired
	LiberacaoSiglasDescRepository crudLiberaSiglasDesc;
	
	@RequestMapping(method = RequestMethod.POST, path = "/adicionar-colaborador")
	public ModelAndView adicionarColaboradorPOST(@RequestParam(value = "idRelacaoClasse", required = true) Integer idRelacaoClasse, String email, String tela) throws IOException {
		
		if(idRelacaoClasse == 999) {
			
			ModelAndView mv = recarregaTela("Selecione uma unidade.", tela);
			mv.addObject("email", email);
			
			return mv;
		}
		
		Usuario usuarioCadastrado = crudUsuario.findByEmail(email.toUpperCase());
		
		if(usuarioCadastrado != null) {
			
			ModelAndView mv = recarregaTela("O e-mail informado já foi cadastrado, informe outro e-mail.", tela);
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
	
		UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Usuario usuario = crudUsuario.findByEmail(user.getUsername());
		
		registroCodigo = new Codigos();
		registroCodigo = crudCodigos.findByCodigo(codigo);

//		Criando novo registro de Liberação
		Liberacao liberacao = new Liberacao();
		liberacao.setRelacaoClasse(crudRelacaoClasse.findById(idRelacaoClasse));
		liberacao.setUsuario(usuario);
		
		if(tela.equals("AD")) {
			
			liberacao.setLiberacaoSiglas(crudLiberaSiglasDesc.findByTipo('A'));
			
		}
		else if(tela.equals("FI")) {
			
			liberacao.setLiberacaoSiglas(crudLiberaSiglasDesc.findByTipo('F'));
			
		}
		else if(tela.equals("SE")) {
			
			liberacao.setLiberacaoSiglas(crudLiberaSiglasDesc.findByTipo('S'));
			
		}
		
		liberacao.setCodigo(registroCodigo);
		crudLiberacao.save(liberacao);
		
		EnviarEmailModel.enviarEmailLiberacao(codigo, email);
		
		ModelAndView mv = new ModelAndView("mensagem-liberacao-adm");
		mv.setViewName("mensagem-liberacao-adm");
		mv = recarregaMenu(mv);
		mv.addObject("mensagem","Um e-mail com o código de liberação foi enviado ao destinatário descrito, após efetuação de cadastro você poderá ver seu novo admnistrador na sua tela de Admnistradores");
		
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
	
	public ModelAndView recarregaTela(String mensagem, String tela) {
		
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
		}
		else if(tela.equals("SE")) {
			mv = new ModelAndView("meus-secretarios");
			mv.setViewName("meus-secretarios");	
		}
		
		mv = recarregaMenu(mv);
		mv.addObject("colaboradores",listColaboradores);
		mv.addObject("unidadesSemColaborador", listUnidadesSemColaborador);
		mv.addObject("mensagem", mensagem);
		
		return mv;
		
	}
	
	
}
