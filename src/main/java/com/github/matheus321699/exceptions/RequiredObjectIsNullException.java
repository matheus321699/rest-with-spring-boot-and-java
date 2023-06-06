package com.github.matheus321699.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Anotação para definir qual código de erro a exceção vai retornar
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RequiredObjectIsNullException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public RequiredObjectIsNullException() {
		super("It is not allowed to persist a null object");
	}
	
	public RequiredObjectIsNullException(String ex) {
		super(ex);
	}
	
}
