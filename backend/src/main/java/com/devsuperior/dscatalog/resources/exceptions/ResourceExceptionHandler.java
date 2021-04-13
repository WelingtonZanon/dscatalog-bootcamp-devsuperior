package com.devsuperior.dscatalog.resources.exceptions;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.devsuperior.dscatalog.resources.ResourseNotFoundException;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ValidationError;

/*
 * Uma classe para o spring escutar os erros dos controladores REST e tratar.
 * Melhora o codigo do controlador para não ter que ficar editando try/cach em todas as requisições
 */
@ControllerAdvice
public class ResourceExceptionHandler {
	/*
	 * identifica a classe para q o spring saiba o tipo de exeção que vai interceptar
	 */
	HttpStatus status;
	
	@ExceptionHandler (ResourseNotFoundException.class)
	public ResponseEntity<StandardError> entityNotFound(ResourseNotFoundException e, HttpServletRequest request){
		status = HttpStatus.NOT_FOUND;
		StandardError err = new StandardError();
		err.setTimestamp(Instant.now());
		err.setStatus(status.value());
		err.setError("Resource not found");
		err.setMessage(e.getMessage());
		err.setPath(request.getRequestURI());
		//comando propio do java para retornar a requizição de erro no body da pagina.
		return ResponseEntity.status(status).body(err);
	}
	@ExceptionHandler (DatabaseException.class)
	public ResponseEntity<StandardError> database(DatabaseException e, HttpServletRequest request){
		status = HttpStatus.BAD_REQUEST;
		StandardError err = new StandardError();
		err.setTimestamp(Instant.now());
		err.setStatus(status.value());
		err.setError("Database exception");
		err.setMessage(e.getMessage());
		err.setPath(request.getRequestURI());
		//comando propio do java para retornar a requizição de erro no body da pagina.
		return ResponseEntity.status(status).body(err);
	}
	@ExceptionHandler (MethodArgumentNotValidException.class)
	public ResponseEntity<ValidationError> validation(MethodArgumentNotValidException e, HttpServletRequest request){
		status = HttpStatus.UNPROCESSABLE_ENTITY;
		ValidationError err = new ValidationError();
		err.setTimestamp(Instant.now());
		err.setStatus(status.value());
		err.setError("Validation exception");
		err.setMessage(e.getMessage());
		err.setPath(request.getRequestURI());
		
	/*
	 * fieldError, para pegar o erro do "valid" do spring que foi passado nos DTO.
	 * o BInding é da biblioteca bins validadion.
	 */
		for(FieldError f : e.getBindingResult().getFieldErrors()) {
			err.addError(f.getField(), f.getDefaultMessage());
		}
		
		//comando propio do java para retornar a requizição de erro no body da pagina.
		return ResponseEntity.status(status).body(err);
	}
}
