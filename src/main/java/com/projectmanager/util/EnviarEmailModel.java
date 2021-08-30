package com.projectmanager.util;

import java.util.Properties;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

	// Classe padr�o que abre as conex�es que permitem o envio do e-mail

public class EnviarEmailModel {
	
      public static void enviarEmailRecuperarSenha(String codigo,String email) {
            Properties props = new Properties();
            /** Par�metros de conex�o com servidor Gmail */
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "465");
 
            Session session = Session.getInstance(props,
                        new javax.mail.Authenticator() {
                             protected PasswordAuthentication getPasswordAuthentication() 
                             {
                                   return new PasswordAuthentication("pm.manager.service@gmail.com", "owpcugtjwacfsqpr");
                             }
                        });
 
            /** Ativa Debug para sess�o */
            session.setDebug(true);
 
            try {
 
                  Message message = new MimeMessage(session);
                  message.setFrom(new InternetAddress("pm.manager.service@gmail.com")); //Remetente
 
                  Address[] toUser = InternetAddress //Destinat�rio(s)
                             .parse(email.trim());
 
                  message.setRecipients(Message.RecipientType.TO, toUser);
                  message.setSubject("Recuperação de Senha");//Assunto
                  message.setText("Seu código de recuperação de senha é: " + codigo);
                  
                  /**M�todo para enviar a mensagem criada*/
                  
                  Transport.send(message);
 
                  System.out.println("Feito!!!");
 
             } catch (MessagingException e) {
                  throw new RuntimeException(e);

            }

      }
	
      public static void enviarEmailLiberacao(String codigo,String email) {
          Properties props = new Properties();
          /** Par�metros de conex�o com servidor Gmail */
          props.put("mail.smtp.host", "smtp.gmail.com");
          props.put("mail.smtp.socketFactory.port", "465");
          props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
          props.put("mail.smtp.auth", "true");
          props.put("mail.smtp.port", "465");

          Session session = Session.getInstance(props,
                      new javax.mail.Authenticator() {
                           protected PasswordAuthentication getPasswordAuthentication() 
                           {
                                 return new PasswordAuthentication("pm.manager.service@gmail.com", "Proj@1020");
                           }
                      });

          /** Ativa Debug para sess�o */
          session.setDebug(true);

          try {

                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("pm.manager.service@gmail.com")); //Remetente

                Address[] toUser = InternetAddress //Destinat�rio(s)
                           .parse(email.trim());

                message.setRecipients(Message.RecipientType.TO, toUser);
                message.setSubject("Recuperação de Senha");//Assunto
                message.setText("Acesse o link para criar seu cadastro de liberação:" + 
                "https://pm-project-manager.herokuapp.com/libera-cadastro?codigo="+ codigo);
                
                /**M�todo para enviar a mensagem criada*/
                
                Transport.send(message);

                System.out.println("Feito!!!");

           } catch (MessagingException e) {
                throw new RuntimeException(e);

          }

    }
	
}
