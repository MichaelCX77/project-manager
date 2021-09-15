# Project Manager <img align="right" height="100em" src="https://github.com/MichaelCX77/assets/blob/master/assets-project-manager/jpg/logo.jpeg"></a>
[![NPM](https://img.shields.io/npm/l/react)](https://github.com/MichaelCX77/project-manager/blob/main/LICENSE)

<br/>
O Project Manager é a idealização de uma plataforma de gerenciamento de atividades em grupo direcionada para a área de educação. Com essa plataforma a Instituição Educacional pode medir o desempenho interpessoal e adicionar mais um parâmetro a avaliação dos alunos.

O Project Manager também tem objetivo de destacar Soft Skills de alunos para o Mercado de Trabalho, agregando aos parâmetros de avaliação dos alunos também em eventuais oportunidades empregatícias.

<br/>

## <li> Sobre o Projeto

Clique <a href="https://pm-project-manager.herokuapp.com/login">aqui</a> e acesse a aplicação em núvem<br/>

<br/>
    
<b><i>Observação: O Project Manager foi desenvolvido como objeto de estudo, com objetivo de fazer parte do meu Trabalho de Conclusão de curso, sendo que o Front-End não foi de minha autoria</i></b>
    
<br/>

## <li> Planos para a Plataforma

A plataforma precisa terminar de ser desenvolvida já que o escopo da solução é bem grande. Algumas das propostas para a plataforma estão descritas abaixo do título.

<br/>

## <li> Layout da Plataforma - (Desenho para Pitch)

<br/>
<div align="center">
    <img height="500em" src="https://github.com/MichaelCX77/assets/blob/master/assets-project-manager/png/pag1.png"></a><br/><br/>
    <img height="500em" src="https://github.com/MichaelCX77/assets/blob/master/assets-project-manager/png/pag2.png"></a><br/><br/>
    <img height="500em" src="https://github.com/MichaelCX77/assets/blob/master/assets-project-manager/png/pag3.png"></a><br/><br/>
    <img height="500em" src="https://github.com/MichaelCX77/assets/blob/master/assets-project-manager/png/pag4.png"></a><br/><br/>
    <img height="500em" src="https://github.com/MichaelCX77/assets/blob/master/assets-project-manager/png/pag5.png"></a><br/><br/>
</div>
<br/>

# Tecnologias utilizadas


## Front end

<li> HTML
<li> CSS
<li> Javascript
<li> BootsTrap
<li> Thymeleaf

  
<br/>
  
## Back end
  
<li> Java / SpringBoot
<li> Maven
<li> Segurança: Spring Security / User Details
<li> Outras Biblitecas e Ferramentas: Crud Repository, JPA, Javax Mail, JodaTime, ModelAndView.

<br/>
  
## Implantação em Produção

<li> Hospedagem da aplicação em Núvem Heroku
<li> Banco de dados Postgresql em Nuvem Heroku (AWS)
  
<br/><br/>
  
# Como criar o banco de dados local
    
  Pré Requisitos: PSQL com variáveis de ambiente configuradas
    
 ```bash
  #1 Logar no console PSQL
    Logar em uma database do PSQL (PostgreSQL) com usuário que possua permissões de administrador
  
  #2 Executar script de criação do banco
    Executar script "Criação do banco Local.sql" presente na pasta "doc" do projeto
  ```
<br/>   
   
# Como executar o Projeto

Pré Requisito: Java 8 e Maven com variáveis de ambiente configuradas

  ```bash
#1 clonar repositório
  git clone https://github.com/MichaelCX77/project-manager
  
#2 entrar na pasta do projeto
  cd project-manager
 
#3 executar build maven
  mvn clean package
    
#4 executar artefato
  java -jar target/projetc.manager-{VERSION}.jar
    
  ```
  
  
  
  
  
