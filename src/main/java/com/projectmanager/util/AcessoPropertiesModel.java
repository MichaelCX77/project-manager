package com.projectmanager.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class AcessoPropertiesModel {
	
	// M�todo para acessar as properties
	
	public Properties getProp(String arquivo) throws IOException {
        Properties props = new Properties();
        FileInputStream file = new FileInputStream(
                "./properties/"+ arquivo);
        props.load(file);
        return props;
    }
	
	//M�todo que retorna o valor correspondente de uma chave na propertie
	
	public String  getProperties(String arquivo,String key) throws IOException {
         
        Properties prop = getProp(arquivo);
        String valor = prop.getProperty(key);
        
        return valor;
    }
	
}
