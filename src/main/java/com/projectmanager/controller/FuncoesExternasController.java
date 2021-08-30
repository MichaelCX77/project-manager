package com.projectmanager.controller;

import java.io.IOException;
import java.util.Calendar;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.projectmanager.model.CodigoTipoDesc;
import com.projectmanager.model.Codigos;
import com.projectmanager.model.Escola;
import com.projectmanager.model.Liberacao;
import com.projectmanager.model.RelacaoAcesso;
import com.projectmanager.model.RelacaoClasse;
import com.projectmanager.model.RelacaoPerfil;
import com.projectmanager.model.Usuario;
import com.projectmanager.repository.CodigoTipoDescRepository;
import com.projectmanager.repository.CodigosRepository;
import com.projectmanager.repository.EscolaRepository;
import com.projectmanager.repository.LiberacaoRepository;
import com.projectmanager.repository.PerfilDescRepository;
import com.projectmanager.repository.RelacaoAcessoRepository;
import com.projectmanager.repository.RelacaoClasseRepository;
import com.projectmanager.repository.RelacaoPerfilRepository;
import com.projectmanager.repository.UsuarioRepository;
import com.projectmanager.util.DatasUtil;
import com.projectmanager.util.EnviarEmailModel;
import com.projectmanager.util.GeraCodigoVerificacaooModel;

@Controller
public class FuncoesExternasController {

	@Autowired
	private UsuarioRepository crudUsuario;

	@Autowired
	private CodigosRepository crudCodigos;
	
	@Autowired
	private EscolaRepository crudEscola;
	
	@Autowired
	private RelacaoPerfilRepository crudRelacaoPerfil;
	
	@Autowired
	private RelacaoClasseRepository crudRelacaoClasse;
	
	@Autowired
	private PerfilDescRepository crudPerfilDesc;
	
	@Autowired
	private RelacaoAcessoRepository crudRelacaoAcesso;
	
	@Autowired
	private CodigoTipoDescRepository crudCodTipoDesc;
	
	@Autowired
	private LiberacaoRepository crudLiberacao;
	
	@PostMapping(value = "cadastro-aluno", produces = MediaType.TEXT_PLAIN_VALUE)
	public ModelAndView cadastroAluno(Usuario form, BindingResult result) {
		
		return cadastrar(form, result, 1);
	}

	@PostMapping(value = "cadastro-professor", produces = MediaType.TEXT_PLAIN_VALUE)
	public ModelAndView cadastroProfessor(Usuario form, BindingResult result) {
		
		return cadastrar(form, result, 2);
	}
	
	public ModelAndView cadastrar(Usuario form, BindingResult result, int tipo) {
		
		String cpf = form.getCpf().replaceAll("\\D","");
		form.setCpf(cpf);
		ModelAndView mv = null;
		
		if(result.hasErrors()) {
			mv = new ModelAndView("cadastro-aluno");
			mv.addObject("usuario", form);
			mv.addObject("mensagem","Verifique os campos!");
			return mv;
		}
		
		Calendar cal = DatasUtil.getCalendarDate();
	    java.sql.Date sqlDate = new java.sql.Date(cal.getTimeInMillis());
		
		Usuario verCpf = crudUsuario.findByCpf(form.getCpf());
		Usuario verEmail = crudUsuario.findByEmail(form.getEmail().toUpperCase());

		if (verCpf != null) {
			
			if(tipo == 1) {
				mv = new ModelAndView("cadastro-aluno");
			}
			else if(tipo == 2) {
				
				mv = new ModelAndView("cadastro-professor");
				
			}
				mv.addObject("usuario", form);
				mv.addObject("mensagem","Já existe um usuário cadastrado com este CPF!");
				return mv;
		}
		else if(verEmail != null){
			
			if(tipo == 1) {
				mv = new ModelAndView("cadastro-aluno");
			}
			else if(tipo == 2) {
				
				mv = new ModelAndView("cadastro-professor");
				
			}
			mv.addObject("usuario", form);
			mv.addObject("mensagem","Já existe um usuário cadastrado com este e-mail!");
			return mv;
		}
		
		form.setAtivo('N');
		form.setDtCriacao(sqlDate);
		form.setEmail(form.getEmail().toUpperCase());
		form.setSenha(new BCryptPasswordEncoder().encode(form.getSenha()));
		crudUsuario.save(form);
		
//		Dando permissões de acordo com o tipo
		RelacaoPerfil relacaoPerfil = new RelacaoPerfil();
		relacaoPerfil.setPerfil(crudPerfilDesc.findById(tipo));
		relacaoPerfil.setUsuario(crudUsuario.findByCpf(form.getCpf()));
		crudRelacaoPerfil.save(relacaoPerfil);
		
//		Dando acesso as mesmas telas do aluno ao professor
		if(tipo != 1) {
			relacaoPerfil = new RelacaoPerfil();
			relacaoPerfil.setPerfil(crudPerfilDesc.findById(1));
			relacaoPerfil.setUsuario(crudUsuario.findByCpf(form.getCpf()));
			crudRelacaoPerfil.save(relacaoPerfil);
		}
		
		mv = new ModelAndView("mensagem");
		mv.addObject("mensagem", "Cadastro efetuado com sucesso!");
		
		return mv;
		
	}
	
	@GetMapping(value="/criar-conta", produces = MediaType.TEXT_PLAIN_VALUE)
	public String criarConta() {
		
		return "criar-conta";
		
	}
	
	@GetMapping(value="cadastro-aluno", produces = MediaType.TEXT_PLAIN_VALUE)
	public ModelAndView cadastrarAluno() {
		
		Usuario usuario = new Usuario();
		
		ModelAndView mv = new ModelAndView("cadastro-aluno");
		mv.setViewName("cadastro-aluno");
		mv.addObject("usuario",usuario);
		
		return mv;
		
	}
	
	@GetMapping(value="cadastro-professor", produces = MediaType.TEXT_PLAIN_VALUE)
	public ModelAndView cadastrarProfessor() {
		
		Usuario usuario = new Usuario();
		
		ModelAndView mv = new ModelAndView("cadastro-professor");
		mv.setViewName("cadastro-professor");
		mv.addObject("usuario",usuario);
		
		return mv;
		
	}
	
	@PostMapping(value="recuperar-senha", produces = MediaType.TEXT_PLAIN_VALUE)
	public ModelAndView RecuperarSenhaPOST(@Valid Usuario form) throws IOException {
		
		ModelAndView mv = null;
		
		Usuario usuario = crudUsuario.findByEmail(form.getEmail().toUpperCase());
		
		if(usuario == null){
			
			mv = new ModelAndView("recuperar-senha");
			mv.addObject("usuario", form);
			mv.addObject("mensagem","Usuário Inexistente");
			return mv;
		}
		
//		Pesquisa o usuário e verifica se já existe um código de recuperação de senha ativo para o usuário
		Codigos codigoAtivoExiste = crudCodigos.findByUsuarioEmailAndAtivoAndCodigoTipo(usuario.getEmail(),'S',crudCodTipoDesc.findById('S'));
		
		
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
	    }
	    
//		Salvando novo registro com código enviado
		Codigos registroCodigo = new Codigos();
		registroCodigo.setAtivo('S');
		registroCodigo.setCodigo(codigo);
		registroCodigo.setTipo(new CodigoTipoDesc('S'));
		registroCodigo.setUsuarioEmail(usuario.getEmail());
		
		Calendar cal = DatasUtil.getCalendarDate();
	    java.sql.Date sqlDate = new java.sql.Date(cal.getTimeInMillis());
	    
		registroCodigo.setDtGeracao(sqlDate);
		crudCodigos.save(registroCodigo);
		
		EnviarEmailModel.enviarEmailRecuperarSenha(codigo, form.getEmail());
		
		mv = new ModelAndView("redirect:/confirma-codigo");
		
		mv.addObject("email",usuario.getEmail());

		return mv;
		
	}
	
	@GetMapping(value="/confirma-codigo",produces = MediaType.TEXT_PLAIN_VALUE)
	public ModelAndView insereCodigo(String email){
		
		Codigos codigos = new Codigos();
		codigos.setUsuarioEmail(email);
		ModelAndView mv = new ModelAndView("confirma-codigo");
		mv.addObject("codigos", codigos);
		return mv;
		
	}
	
	@PostMapping(value="confirma-codigo",produces = MediaType.TEXT_PLAIN_VALUE)
	public ModelAndView confirmaCodigoPOST(Codigos form){
		
		ModelAndView mv = null;
		
		Codigos registroCodigos = crudCodigos.findByCodigo(form.getCodigo());
		
		if(registroCodigos == null) {
			mv = new ModelAndView("confirma-codigo");
			mv.addObject(form);
			mv.addObject("mensagem","Código Inválido");
			
			return mv;
		}
		else if(registroCodigos.getAtivo() == 'N') {
			mv = new ModelAndView("confirma-codigo");
			mv.addObject("mensagem","Esse código é antigo, tente gerar um novo código.");
			return mv;
		}
		else if(!registroCodigos.getUsuarioEmail().equals(form.getUsuarioEmail())) {
			
			mv = new ModelAndView("confirma-codigo");
			mv.addObject("codigos", form);
			mv.addObject("mensagem","A forma que você está utilizando para recuperar a senha não é permitida");
			return mv;
		}
		else {

			mv = new ModelAndView("redirect:/altera-senha");
			mv.addObject("emailUsuario",form.getUsuarioEmail());
			mv.addObject("codigo",registroCodigos.getCodigo());
			
			return mv;
			
		}
	}
	
	@GetMapping(value="/altera-senha",produces = MediaType.TEXT_PLAIN_VALUE)
	public ModelAndView alterasenhaGET(String emailUsuario, String codigo){
		
		Usuario usuario = new Usuario();
		usuario.setEmail(emailUsuario);
		ModelAndView mv = new ModelAndView("altera-senha");
		mv.addObject("usuario", usuario);
		mv.addObject("codigo", codigo);
		return mv;
		
	}

	@PostMapping(value="/altera-senha",produces = MediaType.TEXT_PLAIN_VALUE)
	public ModelAndView alteraSenhaPOST(Usuario form, String codigo){
		
		Codigos registroCodigos = crudCodigos.findByCodigo(codigo);
		ModelAndView mv = null;
		
		if(registroCodigos.getAtivo() == 'N') {
			mv = new ModelAndView("altera-senha");
			mv.addObject(form);
			mv.addObject("mensagem","Esse código é antigo, tente gerar um novo código.");
			return mv;
		}
		
		Usuario usuario = crudUsuario.findByEmail(form.getEmail());
		usuario.setSenha(new BCryptPasswordEncoder().encode(form.getSenha()));
		crudUsuario.save(usuario);
		
		registroCodigos.setAtivo('N');
		crudCodigos.save(registroCodigos);
		
		mv = new ModelAndView("mensagem");
		mv.addObject("mensagem","Senha alterada com sucesso");
		
		return mv;
	}
	
	
	@GetMapping(value="cadastro-escola",produces = MediaType.TEXT_PLAIN_VALUE)
	public ModelAndView cadastroEscola(){
		
		ModelAndView mv = new ModelAndView("cadastro-escola");
		Escola escola = new Escola();
		mv.addObject("escola",escola);
		return mv;
	}
	
	@PostMapping(value="cadastro-escola",produces = MediaType.TEXT_PLAIN_VALUE)
	public ModelAndView cadastroEfscola(Escola form){
		
		String cnpj = form.getCnpj().replaceAll("\\D","");
		String cpf = form.getCpfFundador().replaceAll("\\D","");
		String telefone = form.getCpfFundador().replaceAll("\\D","");
		
		form.setEmailFundador(form.getEmailFundador().toUpperCase());
		
		ModelAndView mv = null;
		
		Escola escola = new Escola();
		escola = crudEscola.findByCnpj(cnpj);
		
		if(escola != null) {
			
			mv = new ModelAndView("cadastro-escola");
			mv.setViewName("cadastro-escola");
			mv.addObject("escola", form);
			mv.addObject("mensagem","Já existe uma escola cadastrada com esse CNPJ, caso seja o proprietário do CNPJ entre em contato com nosso atendimento \" pm-atendimento@project.manager.com \"");
			return mv;
		}
		
		Usuario usuario = new Usuario();
		usuario = crudUsuario.findByCpf(cpf);
		
		if(usuario != null) {
			
			mv = new ModelAndView("cadastro-escola");
			mv.setViewName("cadastro-escola");
			mv.addObject("escola", form);
			mv.addObject("mensagem","Já existe um usuário cadastrado com esse CPF, caso seja o proprietário do CPF entre em contato com nosso atendimento \" pm-atendimento@project.manager.com \"");
			
			return mv;
		}
		usuario = new Usuario();
		usuario = crudUsuario.findByEmail(form.getEmailFundador());
		
		if(usuario != null) {
			
			mv = new ModelAndView("cadastro-escola");
			mv.setViewName("cadastro-escola");
			mv.addObject("escola", form);
			mv.addObject("mensagem","Já existe um usuário cadastrado com esse E-mail, caso seja o proprietário do Email entre em contato com nosso atendimento \" pm-atendimento@project.manager.com \"");
			
			return mv;
		}
		
//		Salvando Escola
		form.setCnpj(cnpj);
		form.setTelefoneFundador(telefone);
		form.setCpfFundador(cpf);
		crudEscola.save(form);
		
		Calendar cal = DatasUtil.getCalendarDate();
	    java.sql.Date sqlDate = new java.sql.Date(cal.getTimeInMillis());
		
//		Salvando Relação Classe
		escola = crudEscola.findByCnpj(cnpj);
		RelacaoClasse relacClasse = new RelacaoClasse();
		relacClasse.setEscola(escola);
		relacClasse.setDtRegistro(sqlDate);
		crudRelacaoClasse.save(relacClasse);
		
//		Salvando Usuário
		usuario = new Usuario();
		usuario.setAtivo('N');
		usuario.setCpf(form.getCpfFundador());
		usuario.setDtCriacao(sqlDate);
		usuario.setDtNasc(form.getDtNascFundador());
		usuario.setEmail(form.getEmailFundador());
		usuario.setNome(form.getNomeFundador());
		usuario.setRegistro("FUNDADOR");
		usuario.setSenha(new BCryptPasswordEncoder().encode(form.getSenha()));
		crudUsuario.save(usuario);
		
//		Salvando relação de acesso
		RelacaoAcesso relacaoAcesso = new RelacaoAcesso();
		relacaoAcesso.setAcesso('S');
		relacaoAcesso.setDtRegistro(sqlDate);
		relacaoAcesso.setRelacaoClasse(relacClasse);
		relacaoAcesso.setUsuarioIncluido(usuario);
		relacaoAcesso.setUsuarioSolicitante(usuario);
		crudRelacaoAcesso.save(relacaoAcesso);
		
//		Dando Permissão de Escola e Secretário
			RelacaoPerfil relacaoPerfil = new RelacaoPerfil();
			relacaoPerfil.setPerfil(crudPerfilDesc.findById(6));
			relacaoPerfil.setUsuario(crudUsuario.findByEmail(form.getEmailFundador()));
			crudRelacaoPerfil.save(relacaoPerfil);
			
			relacaoPerfil = new RelacaoPerfil();
			relacaoPerfil.setPerfil(crudPerfilDesc.findById(3));
			relacaoPerfil.setUsuario(crudUsuario.findByEmail(form.getEmailFundador()));
			crudRelacaoPerfil.save(relacaoPerfil);
			
		mv = new ModelAndView("mensagem");
		mv.setViewName("mensagem");
		mv.addObject("mensagem", "Escola Criada com sucesso, efetue login para acessar as opções");
		
		return mv;
		
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/libera-cadastro")
	public ModelAndView liberaCadastroGET(@RequestParam(value = "codigo", required = true) String codigo){
		
		ModelAndView mv = null;
		
		Codigos codigos = crudCodigos.findByCodigo(codigo);
		
		if(codigos == null) {
			mv = new ModelAndView("mensagem");
			mv.setViewName("mensagem");
			mv.addObject("mensagem","Código inexistente.");
			return mv;
		} 
		else if(codigos.getTipo().getId() != 'L') {
			mv = new ModelAndView("mensagem");
			mv.setViewName("mensagem");
			mv.addObject("mensagem","Tipo de código incorreto.");
			return mv;
		}
		else if(codigos.getAtivo() == 'N' && codigos.getTipo().getId() == 'L') {
			mv = new ModelAndView("mensagem");
			mv.setViewName("mensagem");
			mv.addObject("mensagem","Código antigo, verifique.");
			return mv;
		}
		
		mv = new ModelAndView("libera-cadastro");
		mv.setViewName("libera-cadastro");

		Liberacao liberacao = crudLiberacao.findByCodigo(crudCodigos.findByCodigo(codigo));
		
		String cadastro = null;
		if(liberacao.getLiberacaoSiglas().getTipo() == 'A') {cadastro = "Cadastro de Administrador";}
		else if(liberacao.getLiberacaoSiglas().getTipo() == 'S'){cadastro = "Cadastro de Secretário";}
		else if(liberacao.getLiberacaoSiglas().getTipo() == 'F'){cadastro = "Cadastro de Financeiro";}
		else {  
			mv = new ModelAndView("mensagem");
			mv.setViewName("mensagem");
			mv.addObject("mensagem","Tipo de código incorreto.");
			return mv;
		}
		
		Usuario usuario = new Usuario();
		mv.addObject("usuario",usuario);
		usuario.setEmail(codigos.getUsuarioEmail().toLowerCase());
		mv.addObject("tipoCadastro",cadastro);
		mv.addObject("codigo",codigo);

		return mv;
		
	}
	
	@RequestMapping(method = RequestMethod.POST, path = "/libera-cadastro")
	public ModelAndView liberaCadastroPOST(Usuario form, @RequestParam(value = "codigo", required = true) String codigo, BindingResult result){
			
		String cpf = form.getCpf().replaceAll("\\D","");
		form.setCpf(cpf);
		ModelAndView mv = null;
		
		if(result.hasErrors()) {
			mv = new ModelAndView("libera-cadastro");
			mv.setViewName("libera-cadastro");
			mv.addObject("usuario", form);
			mv.addObject("mensagem","Verifique os campos!");
			return mv;
		}
		
		Liberacao liberacao = crudLiberacao.findByCodigo(crudCodigos.findByCodigo(codigo));
		
		Calendar cal = DatasUtil.getCalendarDate();
	    java.sql.Date sqlDate = new java.sql.Date(cal.getTimeInMillis());
		
		Usuario verCpf = crudUsuario.findByCpf(form.getCpf());
		Usuario verEmail = crudUsuario.findByEmail(form.getEmail().toUpperCase());

		String cadastro = null;
		if (verCpf != null) {
			
			if(liberacao.getLiberacaoSiglas().getTipo() == 'A') {cadastro = "Cadastro de Administrador";}
			else if(liberacao.getLiberacaoSiglas().getTipo() == 'S'){cadastro = "Cadastro de Secretário";}
			else if(liberacao.getLiberacaoSiglas().getTipo() == 'F'){cadastro = "Cadastro de Financeiro";}
			
			mv = new ModelAndView("libera-cadastro");
			mv.setViewName("libera-cadastro");
			mv.addObject("usuario", form);
			mv.addObject("codigo", codigo);
			mv.addObject("tipoCadastro",cadastro);
			mv.addObject("mensagem","Já existe um usuário cadastrado com este CPF!");
			return mv;
		}
		else if(verEmail != null){
			
			if(liberacao.getLiberacaoSiglas().getTipo() == 'A') {cadastro = "Cadastro de Administrador";}
			else if(liberacao.getLiberacaoSiglas().getTipo() == 'S'){cadastro = "Cadastro de Secretário";}
			else if(liberacao.getLiberacaoSiglas().getTipo() == 'F'){cadastro = "Cadastro de Financeiro";}
			
			mv = new ModelAndView("libera-cadastro");
			mv.setViewName("libera-cadastro");
			mv.addObject("usuario", form);
			mv.addObject("codigo", codigo);
			mv.addObject("tipoCadastro",cadastro);
			mv.addObject("mensagem","Já existe um usuário cadastrado com este CPF!");
			return mv;
		}
		
		form.setAtivo('S');
		form.setDtCriacao(sqlDate);
		form.setEmail(form.getEmail().toUpperCase());
		form.setSenha(new BCryptPasswordEncoder().encode(form.getSenha()));
		crudUsuario.save(form);
		
//		Dando permissões
		
		if(liberacao.getLiberacaoSiglas().getTipo() == 'A') {

			RelacaoPerfil relacaoPerfil = new RelacaoPerfil();
			relacaoPerfil.setUsuario(crudUsuario.findByCpf(form.getCpf()));
			relacaoPerfil.setPerfil(crudPerfilDesc.findById(2));
			crudRelacaoPerfil.save(relacaoPerfil);
			
			relacaoPerfil = new RelacaoPerfil();
			relacaoPerfil.setUsuario(crudUsuario.findByCpf(form.getCpf()));
			relacaoPerfil.setPerfil(crudPerfilDesc.findById(3));
			crudRelacaoPerfil.save(relacaoPerfil);
			
			relacaoPerfil = new RelacaoPerfil();
			relacaoPerfil.setUsuario(crudUsuario.findByCpf(form.getCpf()));
			relacaoPerfil.setPerfil(crudPerfilDesc.findById(5));
			crudRelacaoPerfil.save(relacaoPerfil);
		
		}
		else if(liberacao.getLiberacaoSiglas().getTipo() == 'S'){

			RelacaoPerfil relacaoPerfil = new RelacaoPerfil();
			relacaoPerfil.setUsuario(crudUsuario.findByCpf(form.getCpf()));
			relacaoPerfil.setPerfil(crudPerfilDesc.findById(1));
			crudRelacaoPerfil.save(relacaoPerfil);
			
			relacaoPerfil = new RelacaoPerfil();
			relacaoPerfil.setUsuario(crudUsuario.findByCpf(form.getCpf()));
			relacaoPerfil.setPerfil(crudPerfilDesc.findById(3));
			crudRelacaoPerfil.save(relacaoPerfil);
	
		}
		else if(liberacao.getLiberacaoSiglas().getTipo() == 'F'){
			
			RelacaoPerfil relacaoPerfil = new RelacaoPerfil();
			relacaoPerfil.setUsuario(crudUsuario.findByCpf(form.getCpf()));
			relacaoPerfil.setPerfil(crudPerfilDesc.findById(4));
			crudRelacaoPerfil.save(relacaoPerfil);

		}
		
		Usuario usuario = crudUsuario.findByEmail(form.getEmail().toUpperCase());
		
//		Criando Relacao Entre o Usuario e a Unidade
		RelacaoAcesso relacaoAcesso = new RelacaoAcesso();
		relacaoAcesso.setRelacaoClasse(liberacao.getRelacaoClasse());
		relacaoAcesso.setDtRegistro(sqlDate);
		relacaoAcesso.setAcesso('S');
		relacaoAcesso.setUsuarioIncluido(usuario);
		relacaoAcesso.setUsuarioSolicitante(liberacao.getUsuario());
		crudRelacaoAcesso.save(relacaoAcesso);
		
//		Inativando código de liberação
		Codigos codigos = crudCodigos.findByCodigo(codigo);
		codigos.setAtivo('N');
		crudCodigos.save(codigos);
		
		mv = new ModelAndView("mensagem");
		mv.addObject("mensagem", "Cadastro efetuado com sucesso!");
		
		return mv;
		
	}

}
