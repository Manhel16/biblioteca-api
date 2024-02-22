package com.biblioteca.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DepartamentoNotFoundException extends DepartamentoException {
    public DepartamentoNotFoundException(String mensaje){
        super(mensaje);
    }

}
