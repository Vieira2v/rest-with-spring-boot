package com.example.rest_with_spring_boot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.rest_with_spring_boot.data_vo_v1.security.AccountCredencialVO;
import com.example.rest_with_spring_boot.services.AuthServices;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name="Authentication Endpoint")
@RestController //Define que os métodos dessa classe pode responder a request HTTP e retornar dados em formato JSON ou XML.
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    AuthServices authServices;

    @SuppressWarnings("rawtypes")
    @Operation(summary="Authenticates a user and returns a token")
    @PostMapping(value="/signin")
    public ResponseEntity signin(@RequestBody AccountCredencialVO data) {
        /*O método recebe um corpo da requisição 
        (representado por AccountCredencialVO) e retorna uma resposta HTTP. */

        if (checkIfParamsIsNotNull(data)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
        }

        var token = authServices.signin(data);

        if (token == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!"); 
        }

        /*Este método estou autenticando se o usuário é válido com base
         * nas credênciais fornecidas e retorna um token.
         */

        return token;
    }

    @SuppressWarnings("rawtypes")
	@Operation(summary = "Refresh token for authenticated user and returns a token")
	@PutMapping(value = "/refresh/{username}")
	public ResponseEntity refreshToken(@PathVariable("username") String username,
			@RequestHeader("Authorization") String refreshToken) {
                
		if (checkIfParamsIsNotNull(username, refreshToken))
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
		var token = authServices.refreshToken(username, refreshToken);
		if (token == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
		return token;
	}


    private boolean checkIfParamsIsNotNull(AccountCredencialVO data) {
        return data == null || data.getUsername() == null || data.getUsername().isBlank() || data.getPassword() == null || data.getPassword().isBlank();
    }

    private boolean checkIfParamsIsNotNull(String username, String refreshToken) {
		return refreshToken == null || refreshToken.isBlank() ||
				username == null || username.isBlank();
	}
}
