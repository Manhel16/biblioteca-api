package com.biblioteca.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DepartamentoBadRequestException extends DepartamentoException{
    public DepartamentoBadRequestException(String mensaje){
        super(mensaje);
    }
}
