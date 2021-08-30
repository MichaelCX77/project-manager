package com.projectmanager.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	private ImplementsUserDetailsService userDetailsService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		http.authorizeRequests()
		.antMatchers("/css/**", "/js/**","/imagens/**","/assets/**","/materialize/**").permitAll()
//		 Qualquer um
		.antMatchers("/login").permitAll()  
		.antMatchers("/logout").permitAll()
		.antMatchers("/mensagem-logado**").permitAll()  
		.antMatchers("/mensagem-relacionamento**").permitAll()
		.antMatchers("/ativar-codigo**").hasRole("AL")
		
//		 No mínimo permissão de Aluno
		
		.antMatchers("/meus-trabalhos**").hasAnyRole("AL")
		
//		 No mínimo permissão de Professor
		
		.antMatchers("/adicionar-integrantes**").hasAnyRole("PR")
		.antMatchers("/avaliacoes**").hasRole("PR")
		.antMatchers("/atuacao-aluno**").hasAnyRole("PR")
		.antMatchers("/solicitar-licenca**").hasAnyRole("PR")
		
//		 No mínimo permissão de Secretário
		.antMatchers("/criar-classe**").hasAnyRole("SE")
		.antMatchers("/aprovar-licenca**").hasAnyRole("SE")
		.antMatchers("/minhas-salas-adm**").hasAnyRole("SE")
		
//		 No mínimo permissão de Financeiro
		
		.antMatchers("/liberar-licenca**").hasAnyRole("FI")

//		 No mínimo permissão de Administrador
		
		.antMatchers("/detalhe-sala**").hasAnyRole("AD")
		.antMatchers("/criar-curso**").hasAnyRole("AD")
		.antMatchers("/pesquisa-geral**").hasAnyRole("AD")
		.antMatchers("/minhas-disciplinas**").hasAnyRole("AD")
		.antMatchers("/minhas-classes**").hasAnyRole("AD")
		.antMatchers("/meus-cursos**").hasAnyRole("AD")
		.antMatchers("/relacionar-sala**").hasAnyRole("AD")
		
//		 No mínimo permissão de Escola
		
		.antMatchers("/criar-unidade**").hasAnyRole("ES")
		.antMatchers("/minhas-unidades**").hasAnyRole("ES")
		.antMatchers("/meus-administradores**").hasAnyRole("ES")
		
		.anyRequest().authenticated()
		.and()

		.formLogin().loginPage("/login").permitAll()
		.and().logout()
        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
        .logoutSuccessUrl("/login").and().exceptionHandling()
        .accessDeniedPage("/access-denied")
		
		
		.and().formLogin().loginPage("/login").permitAll()
		;
		
	}
	
//	@Override
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception{
//		auth.userDetailsService(userDetailsService)
//		.passwordEncoder(new BCryptPasswordEncoder());
//	}
	
	@Override
	public void configure(WebSecurity web) throws Exception{
		web.ignoring().antMatchers("/assets/**", "/css/**","/imagens/**", "/materialize/**",
				"/js/**","/cadastro-aluno","/recuperar-senha","/libera-cadastro**",
				"/altera-senha","/cadastro-professor","/cadastro-escola",
				"/confirma-codigo","/criar-conta","/for-schools",
				"/for-schools","/mensagem","/recuperar-senha","fragments/footer");
	}
//
//	@Autowired
//	public void configureGlobal(AuthenticationManagerBuilder builder) throws Exception {
//	
//		builder.inMemoryAuthentication()
//		.withUser("aluno@aluno.com").password("{noop}123").roles("AL")
//		.and().withUser("professor@professor.com").password("{noop}123").roles("AL","PR")
//		.and().withUser("secretario@secretario.com").password("{noop}123").roles("AL","SE")
//		.and().withUser("financeiro@financeiro.com").password("{noop}123").roles("FI")
//		.and().withUser("admin@admin.com").password("{noop}123").roles("PR","SE","AD")
//		.and().withUser("escola@escola.com").password("{noop}123").roles("ES","SE","AD");
//	}
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder builder) throws Exception {
	
		builder.userDetailsService(userDetailsService)
		.passwordEncoder(new BCryptPasswordEncoder());
	}

	
}
