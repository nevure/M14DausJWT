package com.ITAcademy.M14DausJwt.seguridad;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class FiltroDeLogin extends AbstractAuthenticationProcessingFilter {

	 public FiltroDeLogin(String url, AuthenticationManager authManager) {
	        super(new AntPathRequestMatcher(url));
	        setAuthenticationManager(authManager);
	    }



	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) 
			throws AuthenticationException, IOException, ServletException {
	
		// obtenemos el body de la peticion . Entendemos que será un json
	    InputStream body = request.getInputStream();

	     /* El json será del tipo  {"username":"nombre", "password":"xxxxxx"} o saltará  la excepción.
	      * Realizamos un mapeo a nuestra clase UserSeguridad donde tenemos los usuarios del sistema 
	      * 
	      * Luego lanzamos el método authenticate con el nombre de usuario y contraseña. Spring compara con 
	      * el definido en la clase de configuración 
	     */
	     
	    try {
		    UserSeguridad user = new ObjectMapper().readValue(body, UserSeguridad.class);
		    return getAuthenticationManager().authenticate(
		             new UsernamePasswordAuthenticationToken(
		                 user.getUserName(),
		                 user.getPassword(),
		                 Collections.emptyList()
		                )
		        );
			
		} catch (Exception e) {

            e.printStackTrace(); 
            return null;
        }
	  
	}
	
	/**
	 * En caso que la autenticación sea válida crearemos el token
	 * 
	 */
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResultado)
	    throws IOException, ServletException {
		
	       // SignatureAlgorithm AlgoritmoFirma = SignatureAlgorithm.HS256;
	        List<String> roles = authResultado.getAuthorities()
	            .stream()
	            .map(GrantedAuthority::getAuthority)
	            .collect(Collectors.toList());

	        // Creamos la clave secreta de un array de bytes que hemos convertido previamente nuestro secreto
	     //   byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(ConstantesSeguridad.SECRETO);
	     //   Key signingKey = new SecretKeySpec(apiKeySecretBytes, AlgoritmoFirma.getJcaName());  .secret("{noop}secret")

	        //Construímos el token con los datos deseados, nombre del usuario, roles, fecha de expiración. el tipo de token (JWT), y la firma.
	        String token = Jwts.builder()
	            .signWith(SignatureAlgorithm.HS512, JwtUtil.generateKeySign())
	            .setHeaderParam("typ", ConstantesSeguridad.TOKEN_TIPO)
	            .setExpiration(new Date(System.currentTimeMillis() + 7200000))  //2 horas
	            .setSubject(authResultado.getName())
	            .claim("rol", roles)
	            .compact();

	        //Agregamos a la cabecera de la respuesta el token)
	        response.addHeader(ConstantesSeguridad.TOKEN_HEADER, ConstantesSeguridad.TOKEN_PREFIJO + token);
	}

}