package com.devsuperior.dscatalog.services.validation;
/*
 * implementa a logica do UserInsertValid tmb usando um boilerPlate.
 * 
 */
import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.devsuperior.dscatalog.dto.UserInsertDTO;
import com.devsuperior.dscatalog.entities.User;
import com.devsuperior.dscatalog.repositories.UserRepository;
import com.devsuperior.dscatalog.services.exceptions.FieldMessage;

/*
 * Classe do tipo generic, parametrizando com a anotation(UserInsertValid) e 
 * a classe que vai receber a anotation (UserInsertDTO).
 */

public class UserInsertValidator implements ConstraintValidator<UserInsertValid, UserInsertDTO> {
	
	@Autowired
	private UserRepository repository;
	
	//implementa algo na inicialização 
	@Override
	public void initialize(UserInsertValid ann) {
	}
	
	//testa se a condição é verdadeira
	@Override
	public boolean isValid(UserInsertDTO dto, ConstraintValidatorContext context) {
		
		List<FieldMessage> list = new ArrayList<>();
 //Coloque aqui seus testes de validação, acrescentando objetos FieldMessage à lista if else por exemplo
		
			
		
		/*
		 * erro personalizado para testar se o email já existe, caso retorne
		 * diferente de vazio, ele preenche a lista de erro com o campo que ocorreu o erro
		 * e a mensagem para este erro.
		 */
		User user = repository.findByEmail(dto.getEmail());		
		if(user !=null) {
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