package com.projectmanager.util;

import java.io.IOException;
import java.util.Random;


	// Esta classe � respons�vel por gerar c�digo de recupera��o de senha
public class GeraCodigoVerificacaooModel {

		public static String gerarCodigo() throws IOException{

            Random ran = new Random();
            char vetorChar[] = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
            char codigoChar1;
            char codigoChar2;


            int codigoNum1 = ran.nextInt(101);
            int saidaChar1 = ran.nextInt(26);
            int codigoNum2 = ran.nextInt(101);
            int saidaChar2 = ran.nextInt(26);

            codigoChar1 = vetorChar[saidaChar1];
            codigoChar2 = vetorChar[saidaChar2];

            String codigo = Integer.toString(codigoNum1) + codigoChar1 + Integer.toString(codigoNum2) + codigoChar2;
            
            return codigo;
        }
}
