package com.example.rest_with_spring_boot.security.jwt;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

public class JwtTokenFilter extends GenericFilterBean {

    @Autowired
    private final JwtTokenProvider tokenProvider;

    public JwtTokenFilter(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    /*Este é o método principal de um filtro de servlet. Ele intercepta 
    *todas as requisições e respostas HTTP antes de serem processadas pelo 
    restante da aplicação. */
    /*ServletRequest request: A requisição HTTP feita pelo cliente.
    *ServletResponse response: A resposta HTTP que será enviada ao cliente.
    *FilterChain chain: A cadeia de filtros. Esse parâmetro permite que a requisição passe para o próximo filtro na cadeia. */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String token = tokenProvider.resolveToken((HttpServletRequest) request);
        /*Este método usa o tokenProvider para resolver (extrair) o token 
        *JWT da requisição. Normalmente, o token JWT vem no cabeçalho 
        *Authorization da requisição HTTP, prefixado com "Bearer ". */

        if (token != null && tokenProvider.validateToken(token)) {
            Authentication auth = tokenProvider.getAuthentication(token);
            /* Usa o JWT para criar uma instância de Authentication. 
            *O Authentication representa o usuário autenticado com suas 
            *permissões e dados de segurança. */

            if (auth != null) {
                SecurityContextHolder.getContext().setAuthentication(auth);
                /* Se a autenticação foi realizada com sucesso, 
                *ela é armazenada no SecurityContext do Spring, que mantém 
                *as informações de segurança da sessão corrente. 
                *Isso permite que a aplicação reconheça o usuário 
                *autenticado durante o ciclo de vida da requisição. */
            }
        }
        chain.doFilter(request, response);
        /*Finalmente, a requisição é passada adiante para o próximo filtro 
        *ou componente no fluxo de processamento, através do FilterChain. */
    }
    
}
