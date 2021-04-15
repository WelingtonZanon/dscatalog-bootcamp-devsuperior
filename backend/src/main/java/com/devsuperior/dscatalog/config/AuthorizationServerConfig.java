package com.devsuperior.dscatalog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;


@Configuration
//aunotation responsavel por dizer que essa classe é a responsavel pelo autorization server do oauth2
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private JwtAccessTokenConverter accessTokenConverter;
	@Autowired
	private JwtTokenStore tokenStore;
	@Autowired
	private AuthenticationManager authenticationManager;
	
	
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		//vai ser tratado em memoria
		clients.inMemory()
		//nome do cliente front que vai querer acessar
		.withClient("dscatalog")
		//senha da aplicação
		.secret(passwordEncoder.encode("dscatalog123"))
		//tipo de liberação (no caso leitura e escrita)
		.scopes("read","write")
		//o tipo de acesso de loguinho, no caso o tipo "password" usado pelo o oauth2
		.authorizedGrantTypes("password")
		//tempo de validade do token em segundos
		.accessTokenValiditySeconds(86400);		
	}
	//qm vai autorizar e qual o formato do token
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		//responsavel pela autenticação
		endpoints.authenticationManager(authenticationManager)
		//objetos responsaveis por processar o token
		.tokenStore(tokenStore)
		.accessTokenConverter(accessTokenConverter);
	}
	
}
