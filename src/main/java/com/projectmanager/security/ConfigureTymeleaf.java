//package com.projectmanager.security;
//
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//import org.thymeleaf.TemplateEngine;
//import org.thymeleaf.spring5.SpringTemplateEngine;
//import org.thymeleaf.templateresolver.DefaultTemplateResolver;
//
//import com.projectmanager.controller.HomePageController;
//
//@ComponentScan(basePackageClasses = HomePageController.class)
//public class ConfigureTymeleaf implements WebMvcConfigurer{
//
//	    @Bean
//	    public TemplateEngine templateEngine(ApplicationContext applicationContext) {
//	        final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
//	        templateEngine.setEnableSpringELCompiler(true);
//	        templateEngine.addDialect(new org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect());
//	        templateEngine.setTemplateResolver(new DefaultTemplateResolver());
//	        return templateEngine;
//	    }
//	
//	
//}
