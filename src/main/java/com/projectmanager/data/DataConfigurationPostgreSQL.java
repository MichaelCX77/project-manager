package com.projectmanager.data;

import java.net.URISyntaxException;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataConfigurationPostgreSQL {

//	PostGress Nuvem

//	@Bean
//    public BasicDataSource dataSource() throws URISyntaxException {
//        URI dbUri = new URI(System.getenv("DATABASE_URL"));
//
//        String username = dbUri.getUserInfo().split(":")[0];
//        String password = dbUri.getUserInfo().split(":")[1];
//        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
//
//        BasicDataSource basicDataSource = new BasicDataSource();
//        basicDataSource.setUrl(dbUrl);
//        basicDataSource.setUsername(username);
//        basicDataSource.setPassword(password);
//
//        return basicDataSource;
//    }
	
	@Bean
	public BasicDataSource dataSource() throws URISyntaxException {

		BasicDataSource basicDataSource = new BasicDataSource();
		basicDataSource.setUrl("jdbc:postgresql://localhost:5432/project_manager");
		basicDataSource.setUsername("pmadmin");
		basicDataSource.setPassword("pmadmin2021");
		return basicDataSource;
	}
	
}
