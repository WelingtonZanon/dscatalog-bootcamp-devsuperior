package com.devsuperior.dscatalog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/*
 * classe de configuração, para criar ou configurar itens especificos
 * do aplicativo
 */

//anotação JPA para configuração
@Configuration
public class AppConfig {
	
	
/*
 * anotação Bean, para dizer que essa stancia sera um componente
 * gerenciada pelo spring. BCryp um metodo de criptografar senha do
 * spring
 */
	@Bean
	public BCryptPasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}
	
}
