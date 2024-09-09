package com.example.rest_with_spring_boot.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class InvalidJwtAuthenticationException extends AuthenticationException{

    private static final long serialVersionUID = 1L;

    public InvalidJwtAuthenticationException(String ex) {
        super(ex);

        /*O construtor recebe uma mensagem de erro (String ex) e a 
        * passa para o construtor da superclasse (super(ex)). 
        * Isso faz com que a exceção leve uma mensagem personalizada sobre 
        * o erro de autenticação relacionado ao JWT, que pode ser 
        * registrada ou exibida no log. */
    }


}
