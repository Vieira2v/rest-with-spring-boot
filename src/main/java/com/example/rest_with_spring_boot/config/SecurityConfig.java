package com.example.rest_with_spring_boot.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm;
import org.springframework.security.web.SecurityFilterChain;

import com.example.rest_with_spring_boot.security.jwt.JwtConfigurer;
import com.example.rest_with_spring_boot.security.jwt.JwtTokenProvider;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    
    @Autowired
    private JwtTokenProvider tokenProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            /*O CSRF (Cross-Site Request Forgery) é desabilitado. 
            Como a aplicação está usando tokens JWT e não sessões baseadas 
            em cookies, CSRF é menos relevante.*/
            .csrf(csrf -> csrf.disable())
             /* Configura a aplicação para não manter estado de sessão. 
             Ou seja, cada requisição precisa conter o token JWT, 
             e o servidor não armazena estado de sessão do cliente.*/
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/auth/signin", "/auth/refresh/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .requestMatchers("/api/**").authenticated()
                .requestMatchers("/users").denyAll()
            );

            JwtConfigurer jwtConfigurer = new JwtConfigurer(tokenProvider);
            jwtConfigurer.configure(http);
            /*Usa o JwtConfigurer para configurar o filtro JWT na cadeia de
            *filtros de segurança. Isso garante que o filtro de autenticação
            *baseado em JWT seja executado antes da autenticação padrão. */

        return http.build(); //Constrói o SecurityFilterChain
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        /*O encoders é um HashMap que mapeia uma String (chave) para um 
        * objeto do tipo PasswordEncoder (valor). 
        * Ele permite que você defina múltiplos algoritmos de codificação 
        * de senha dentro da aplicação. */

        /*PBKDF2 é um algoritmo que aplica uma função de hash iterativa 
        *para fortalecer senhas, tornando mais difícil atacantes 
        *descobrirem as senhas originais. */		
		Pbkdf2PasswordEncoder pbkdf2Encoder = new Pbkdf2PasswordEncoder("", 8, 185000, SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);
        /*Parametros: A chave secreta inicial (vazia por padrão, mas pode ser configurada).
         * 8: O comprimento do sal usado no processo de codificação.
         * 185000: O número de iterações de hash. Um valor mais alto oferece maior segurança.
         * SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256: O algoritmo que será usado para derivar a chave.
         */

		encoders.put("pbkdf2", pbkdf2Encoder);
        /*Você está mapeando o codificador PBKDF2 à chave "pbkdf2", 
        * permitindo que o DelegatingPasswordEncoder saiba qual codificador
        * usar quando encontrar o prefixo {pbkdf2} nas senhas. */

		DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("pbkdf2", encoders);
        /* Você está criando um codificador que usa o PBKDF2 por padrão 
        * para novas codificações de senha, mas também pode reconhecer e 
        * usar outros algoritmos, como aqueles que você definiu no mapa encoders. */

		passwordEncoder.setDefaultPasswordEncoderForMatches(pbkdf2Encoder);
        /*Se você encontrar uma senha no banco de dados que não tem um 
        * prefixo indicando qual algoritmo foi usado para codificá-la, 
        * use o pbkdf2Encoder (instância do Pbkdf2PasswordEncoder) 
        * como o algoritmo padrão para verificar essa senha */

		return passwordEncoder;
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(
    		AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();

       /*O AuthenticationManager é responsável por autenticar um usuário 
       * baseado nas credenciais fornecidas (neste caso, através de tokens 
       * JWT ou senhas). */ 
    }
}
