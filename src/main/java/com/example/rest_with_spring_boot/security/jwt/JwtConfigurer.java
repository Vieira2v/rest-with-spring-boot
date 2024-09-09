package com.example.rest_with_spring_boot.security.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;



public class JwtConfigurer {
    
    @Autowired
    private final JwtTokenProvider tokenProvider;

    public JwtConfigurer(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    public void configure(HttpSecurity http) throws Exception {
        JwtTokenFilter customFilter = new JwtTokenFilter(tokenProvider);
        /*Aqui, é instanciado o JwtTokenFilter, que é um filtro customizado
        *responsável por interceptar as requisições HTTP e verificar se elas
        *possuem um token JWT válido no cabeçalho de autorização. 
        *Esse filtro usará o JwtTokenProvider para validar os tokens. */
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
        /*Cada requisição HTTP que passar pelo sistema será interceptada 
        *pelo JwtTokenFilter antes de qualquer outra autenticação padrão. 
        *Se o token for válido, o usuário será autenticado com base nas 
        *permissões e dados do token. Caso contrário, a requisição será 
        *rejeitada. */
    }   
}
