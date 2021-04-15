package com.devsuperior.dscatalog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/*
 * classe padrão para rodar os end point sem ter q ficar colocando
 * senha enquanto testa  apos incluir o spreg security no pom
 */

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
//injetando os dois para configurar AuthenticationManagerBuilder	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserDetailsService userDetailsService;

	/*
	 * uma liberação do websecurity sem o oauth2 para os endpoint
	 * 
	 * @Override
	 * public void configure(WebSecurity web) throws Exception {
	 * web.ignoring().antMatchers("/**"); }
	 */
	
	
	
	//agora passa por uma biblioteca do spring security
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/actuator/**");
	}

	/*
	 * responsavel implementar o algoritimo de imcriptar a senha (bcrypt) e
	 * configurar quem é o userdetailservice-> interface implementada.
	 * Fazendo isso o sptring sabe como buscar o email e como analizar a senha
	 * criptografada.
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
	}

	//criando de maneira explicita para ficar disponivel no sistema com bean para autorizationserve
	@Override
	@Bean
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
	
	
}