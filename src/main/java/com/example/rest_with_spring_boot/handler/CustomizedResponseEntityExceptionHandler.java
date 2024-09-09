package com.example.rest_with_spring_boot.handler;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.rest_with_spring_boot.exceptions.ExceptionResponse;
import com.example.rest_with_spring_boot.exceptions.InvalidJwtAuthenticationException;
import com.example.rest_with_spring_boot.exceptions.RequiredObjectsIsNullException;
import com.example.rest_with_spring_boot.exceptions.ResourceNotFoundException;

@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler extends  ResponseEntityExceptionHandler{

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ExceptionResponse> handleAllExceptions(Exception ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
            new Date(),
            ex.getMessage(),
            request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<ExceptionResponse> handleNotFoundExceptions(Exception ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
            new Date(),
            ex.getMessage(),
            request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RequiredObjectsIsNullException.class)
    public final ResponseEntity<ExceptionResponse> handleBadRequestExceptions(Exception ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
            new Date(),
            ex.getMessage(),
            request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    /*Quando uma InvalidJwtAuthenticationException é lançada, 
    o Spring a captura e invoca o método 
    handleInvalidJwtAuthenticationException. */
    @ExceptionHandler(InvalidJwtAuthenticationException.class)
    public final ResponseEntity<ExceptionResponse> handleInvalidJwtAuthenticationException(Exception ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
            new Date(),
            /*A data e hora atual em que a exceção ocorreu. 
            * Isso ajuda a registrar quando o erro aconteceu. */

            ex.getMessage(),
            /*A mensagem da exceção, fornecendo detalhes sobre o motivo da 
            * falha (por exemplo, "Token inválido ou expirado"). */

            request.getDescription(false));
            /*Descrição detalhada da solicitação que causou o erro. 
            * O argumento false indica que não deve incluir informações 
            * sobre o servlet (por exemplo, URL da requisição). */

        return new ResponseEntity<>(exceptionResponse, HttpStatus.FORBIDDEN);
    }
}
