package com.devsuperior.dscatalog.resources.exceptions;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.devsuperior.dscatalog.services.exceptions.EntityNotFoundException;

/*
 * Uma classe para o spring escutar os erros dos controladores REST e tratar.
 * Melhora o codigo do controlador para não ter que ficar editando try/cach em todas as requisições
 */
@ControllerAdvice
public class ResourceExceptionHandler {
	/*
	 * identifica a classe para q o spring saiba o tipo de exeção que vai interceptar
	 */
	@ExceptionHandler (EntityNotFoundException.class)
	public ResponseEntity<StandardError> entityNotFound(EntityNotFoundException e, HttpServletRequest request){
		StandardError err = new StandardError();
		err.setTimestamp(Instant.now());
		err.setStatus(HttpStatus.NOT_FOUND.value());
		err.setError("Resource not found");
		err.setMessage(e.getMessage());
		err.setPath(request.getRequestURI());
		//comando propio do java para retornar a requizição de erro no body da pagina.
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
	}
}
