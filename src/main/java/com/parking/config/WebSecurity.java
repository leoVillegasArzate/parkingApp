package com.parking.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

@EnableGlobalMethodSecurity(securedEnabled=true, prePostEnabled=true)
@Configuration
public class WebSecurity  extends WebSecurityConfigurerAdapter {

	@Autowired 
	private DataSource dataSource;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	public void configurerGlobal(AuthenticationManagerBuilder build) throws Exception
	{
		build.jdbcAuthentication()
		.dataSource(dataSource)
		.passwordEncoder(passwordEncoder)
		.usersByUsernameQuery(" select u.username as usuario, em.pass as  password, u.activo as estatus \r\n"
				+ " from usuario as u\r\n"
				+ " inner join empleado em  on (u.idusuario=em.idusuario) where u.username=?")
		
		.authoritiesByUsernameQuery(" select u.username as usuario ,p.rol as perfil\r\n"
				+ " from usuario u \r\n"
				+ "inner join perfil p on (p.idperfil=u.idperfil) \r\n"
				+ "where u.username=?");
	}
	
	 @Bean
	    public SessionRegistry sessionRegistry() {
	        return new SessionRegistryImpl();
	    }
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable().authorizeRequests().antMatchers("/css/**","/img/**","/js/**","/vendor/**","/java-scrip/**","/estilos/**").permitAll()
		.antMatchers("/auto/**").hasAnyAuthority("OPERADOR")
		.antMatchers("/admin/**").hasAnyAuthority("ADMINISTRADOR")
		.anyRequest().authenticated()
		.and()
        .exceptionHandling()
            .accessDeniedPage("/acceso-denegado")
		
		.and().formLogin().loginPage("/login1").permitAll()
		 .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
		 .maximumSessions(1).maxSessionsPreventsLogin(false)
		 .expiredUrl("/login1?expired").sessionRegistry(sessionRegistry());		
		
		http.logout()
		.invalidateHttpSession(true)
		.clearAuthentication(true)
		.deleteCookies("JSESSIONID")
		;
	}
	
 
	
	@Bean
	public HttpSessionEventPublisher httpSessionEventPublisher() {
	    return new HttpSessionEventPublisher();
	}

}
