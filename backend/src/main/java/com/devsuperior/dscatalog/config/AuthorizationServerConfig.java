package com.devsuperior.dscatalog.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import com.devsuperior.dscatalog.components.JwtTokenEnhancer;


@Configuration
//aunotation responsavel por dizer que essa classe é a responsavel pelo autorization server do oauth2
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	@Value("${security.oauth2.client.client-id}")
	private String clientId;
	@Value("${security.oauth2.client.client-secret}")
	private String clientSecret;
	@Value("${jwt.duration}")
	private Integer duration;
	
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private JwtAccessTokenConverter accessTokenConverter;
	@Autowired
	private JwtTokenStore tokenStore;
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtTokenEnhancer tokenEnhancer;
	
	
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		
		//vai ser tratado em memoria
		clients.inMemory()
		//nome do cliente front que vai querer acessar
		.withClient(clientId)
		//senha da aplicação
		.secret(passwordEncoder.encode(clientSecret))
		//tipo de liberação (no caso leitura e escrita)
		.scopes("read","write")
		//o tipo de acesso de loguinho, no caso o tipo "password" usado pelo o oauth2
		.authorizedGrantTypes("password")
		//tempo de validade do token em segundos
		.accessTokenValiditySeconds(duration);		
	}
	//qm vai autorizar e qual o formato do token
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		

		//usando o token com mais informações, colocando em uma lista
		TokenEnhancerChain chain = new TokenEnhancerChain();
		chain.setTokenEnhancers(Arrays.asList(accessTokenConverter,tokenEnhancer));
		
		
		//responsavel pela autenticação
		endpoints.authenticationManager(authenticationManager)
		//objetos responsaveis por processar o token
		.tokenStore(tokenStore)
		.accessTokenConverter(accessTokenConverter)		
		//adiciona a lista com as info extras na resposta
		.tokenEnhancer(chain);
	}
	
}
