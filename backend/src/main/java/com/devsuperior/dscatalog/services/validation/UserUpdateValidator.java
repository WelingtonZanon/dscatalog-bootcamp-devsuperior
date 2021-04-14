package com.devsuperior.dscatalog.services.validation;
/*
 * implementa a logica do UserInsertValid tmb usando um boilerPlate.
 * 
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import com.devsuperior.dscatalog.dto.UserUpdateDTO;
import com.devsuperior.dscatalog.entities.User;
import com.devsuperior.dscatalog.repositories.UserRepository;
import com.devsuperior.dscatalog.services.exceptions.FieldMessage;

/*
 * Classe do tipo generic, parametrizando com a anotation(UserUpdateValid) e 
 * a classe que vai receber a anotation (UserUpdateDTO).
 */

public class UserUpdateValidator implements ConstraintValidator<UserUpdateValid, UserUpdateDTO> {
	
	@Autowired
	private UserRepository repository;
	
	//injetando um component para ter acesso as informações da requisição para saber o usuario que sera atualizado
	@Autowired
	private HttpServletRequest request;
	
	//implementa algo na inicialização 
	@Override
	public void initialize(UserUpdateValid ann) {
	}
	
	//testa se a condição é verdadeira
	@Override
	public boolean isValid(UserUpdateDTO dto, ConstraintValidatorContext context) {
		
	/*
	 * para pegar um mapinha das variaveis que veio da URL e acessar o ID no caso de update.
	 * um Map para da um upcaster no uri do tipo (string string) que é o timpo suportado pela
	 * internet.
	 */
		@SuppressWarnings("unchecked")
		var uriVars = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		long userId = Long.parseLong(uriVars.get("id"));
		
		List<FieldMessage> list = new ArrayList<>();
 //Coloque aqui seus testes de validação, acrescentando objetos FieldMessage à lista if else por exemplo
		
			
		
		/*
		 * erro personalizado para testar se o email já existe para outro usuario
		 */
		User user = repository.findByEmail(dto.getEmail());		
		if(user != null && userId != user.getId()) {
			list.add(new FieldMessage("email", "Email já esta cadastrado."));
		}
		
		
		
		
		//insere na classe lista  os erros usando o beans validation
		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		//retorna vazia caso n entre em nenhuma condição de erro.
		return list.isEmpty();
	}
}