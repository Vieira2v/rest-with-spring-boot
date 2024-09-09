package com.example.rest_with_spring_boot.security.jwt;

import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.rest_with_spring_boot.data_vo_v1.security.TokenVO;
import com.example.rest_with_spring_boot.exceptions.InvalidJwtAuthenticationException;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;


@Service
public class JwtTokenProvider {
    
    @Value("${security.jwt.token.secret-key:secret}")
    private String secretKey = "secret";

    @Value("${security.jwt.token.expire-lenght:3600000}")
    private final long validityInMilliseconds = 3600000;

    @Autowired 
    private UserDetailsService userDetailsService;

    Algorithm algorithm = null;

    /*Anotação que indica que o método deve ser executado imediatamente 
    após a construção da instância da classe e após a 
    injeção de dependências. */
    @PostConstruct
    protected void init() {
        //Codifica a secretKey em Base64: A chave secreta é convertida em uma versão codificada em Base64.
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());

        //Configura o algoritmo HMAC256: O algoritmo HMAC256 é inicializado com a chave secreta, o que será usado para assinar os tokens JWT.
        algorithm = Algorithm.HMAC256(secretKey.getBytes());
    }

    public TokenVO createAccessToken(String username, List<String> roles) {
        Date now = new Date();
        //Obtém a data e hora atuais. Usado para definir quando o token foi emitido.

        Date validity = new Date(now.getTime() + validityInMilliseconds);
        //Define a data e hora de expiração do token, adicionando o tempo de validade à data atual.

        var accessToken = getAccessToken(username, roles, now, validity);
        //Cria o token de acesso JWT chamando o método getAccessToken(), passando o nome de usuário, permissões, data de emissão e data de validade.

        var refreshToken = getRefreshToken(username, roles, now);
        //Cria o token de atualização (refresh token) chamando o método getRefreshToken(), passando o nome de usuário, permissões e data de emissão.

        return new TokenVO(username, true, now, validity, accessToken, refreshToken);
        //Retorna um objeto TokenVO que encapsula o nome de usuário, a validade do token, e os tokens gerados (access token e refresh token).
    }

    	public TokenVO refreshToken(String refreshToken) {
		if (refreshToken.contains("Bearer ")) refreshToken =
				refreshToken.substring("Bearer ".length());
		
		JWTVerifier verifier = JWT.require(algorithm).build();
        //Cria um objeto JWTVerifier configurado com o algoritmo de assinatura. Usado para verificar a validade do token.

		DecodedJWT decodedJWT = verifier.verify(refreshToken);
        //Verifica e decodifica o token JWT, retornando um objeto DecodedJWT que contém as informações do token.

		String username = decodedJWT.getSubject();
        //Obtém o nome de usuário (subject) do token decodificado.

		List<String> roles = decodedJWT.getClaim("roles").asList(String.class);
        //Obtém a lista de permissões (roles) armazenadas no token.

		return createAccessToken(username, roles);
        //Gera um novo token de acesso usando o nome de usuário e permissões extraídas do refresh token.
	}

    private String getAccessToken(String username, List<String> roles, Date now, Date validity) {
        String issuerUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        // Obtém a URL do emissor do token, que geralmente é a URL base da aplicação. Essa informação ajuda a identificar qual servidor emitiu o token.
        
        return JWT.create()
                .withClaim("roles", roles)
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .withSubject(username)
                .withIssuer(issuerUrl)
                .sign(algorithm)
                .strip();
        /*Cria um novo construtor de token JWT.
         * Adiciona uma reivindicação (claim) ao token, neste caso, a lista de permissões.
         * Define a data e hora em que o token foi emitido.
         *  Define a data e hora de expiração do token.
         * Define o nome de usuário como o subject do token.
         *  Define a URL do emissor do token.
         * Assina o token usando o algoritmo configurado.
         * Remove qualquer formatação extra no token, retornando apenas o valor do token JWT.
         */
    }

    private String getRefreshToken(String username, List<String> roles, Date now) {
        Date validityRefreshToken = new Date(now.getTime() + (validityInMilliseconds) * 3);
        //Define a validade do refresh token como três vezes a validade do access token, permitindo um período mais longo para renovação do token.

        return JWT.create()
                .withClaim("roles", roles)
                .withIssuedAt(now)
                .withExpiresAt(validityRefreshToken)
                .withSubject(username)
                .sign(algorithm)
                .strip();
    }

    public Authentication getAuthentication(String token) {
        DecodedJWT decodedJWT = decodedToken(token);
        /*O token JWT é decodificado usando o método decodedToken(), 
        que verifica a validade e extrai as informações (claims) do token, 
        como o nome de usuário (subject) e as permissões (roles). */

        UserDetails userDetails = this.userDetailsService.loadUserByUsername(decodedJWT.getSubject());
        /*O método obtém o nome de usuário (subject) do token JWT 
        decodificado. Com base nesse nome de usuário, ele carrega os 
        detalhes do usuário (como senha, permissões) usando o 
        UserDetailsService, que é um serviço padrão do Spring Security para
         carregar os dados do usuário. O método loadUserByUsername retorna 
         um objeto UserDetails que contém todas as informações sobre o usuário. */

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
        /*Aqui, um objeto UsernamePasswordAuthenticationToken é criado, 
        representando uma autenticação baseada em nome de usuário e senha.
        */
    }

    private DecodedJWT decodedToken(String token) {
        Algorithm alg = Algorithm.HMAC256(secretKey.getBytes());
        //Configura o algoritmo de assinatura HMAC256 usando a chave secreta codificada em base64, que foi inicializada no método init().

        JWTVerifier verifier = JWT.require(alg).build();
        //Cria um verificador de JWT usando o algoritmo configurado. Este verificador garante que o token JWT seja válido e não tenha sido adulterado.

        DecodedJWT decodedJWT = verifier.verify(token);
        //Verifica o token JWT recebido. Se a verificação for bem-sucedida,
        //retorna um objeto DecodedJWT que contém as informações do token 
        //(claims, subject, etc.). Caso contrário, 
        //ele lança uma exceção indicando que o token é inválido.

        return decodedJWT;
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");

        //Por padrão o bearer token vem com, "Bearer" e logo em seguida a senha criptografada
        //Na condition abaixo irei tirar o texto "Bearer" pra ele retorna apenas a senha.

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring("Bearer ".length());
        }

        return null;
    }

    public boolean validateToken(String token) {
        DecodedJWT decodedJWT = decodedToken(token);
        //Decodifica e valida o token JWT com o método decodedToken(), 
        //retornando as informações do token se a verificação for bem-sucedida.

        try {
            /*Verifica a validade do token comparando a data de expiração 
            (getExpiresAt()) com a data atual. Se o token ainda não expirou, retorna true. */
            if (!decodedJWT.getExpiresAt().before(new Date())) {
            } else {
                return false;
                //Se o token expirou, retorna false, indicando que ele não é mais válido.
            }
            return true;
        } catch (Exception e) {
            throw new InvalidJwtAuthenticationException("Expired or invalid JWT token!");
        }
    }
}
