package com.parking.config;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.context.WebApplicationContext;

import com.parking.utils.ResponseApp;
import com.parking.utils.Utileria;

@Configuration
public class ConfigurationClass {

	 @Primary
	 @Bean(name = "postgesql")
	 
     DataSource dataSource() {  	
      DriverManagerDataSource dataSource = new DriverManagerDataSource();
      dataSource.setDriverClassName("org.postgresql.Driver");
      dataSource.setUrl("jdbc:postgresql://localhost:5432/ESTACIONAMIENTO");
      dataSource.setUsername("postgres");
      dataSource.setPassword("demo");
      return dataSource;
  }
	 
		
		@Bean
		public BCryptPasswordEncoder passwordEncoder() {
			return new BCryptPasswordEncoder();
		}
	 	
	 	@Bean
	 	Utileria utileria() {	 		
	 		return new Utileria();
	 	}
	 	

	 	
	 	

}
