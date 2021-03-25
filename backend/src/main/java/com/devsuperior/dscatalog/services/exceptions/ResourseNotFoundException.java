package com.devsuperior.dscatalog.services.exceptions;

//pode extender de somente de Exception, mas a tratativa é obrigatoria (try/cach).
public class ResourseNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	//contrutor que passa o argumento para a classe extendida (SUPERCLASSE) através do super.
	public ResourseNotFoundException(String msg) {
		super(msg);
	}

}
