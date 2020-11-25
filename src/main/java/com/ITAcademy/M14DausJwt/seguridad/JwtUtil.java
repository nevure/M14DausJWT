package com.ITAcademy.M14DausJwt.seguridad;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;

import java.util.List;
import java.util.stream.Collectors;


import java.security.Key;

public class JwtUtil {
	
	protected static Key generateKeySign() {
        SignatureAlgorithm AlgoritmoFirma = SignatureAlgorithm.HS256;
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary("{noop}"+ConstantesSeguridad.SECRETO);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, AlgoritmoFirma.getJcaName());
        return signingKey;
        
	}


	// Validamos el token que nos llega en la rquest
    protected static UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        
    	String token = request.getHeader(ConstantesSeguridad.TOKEN_HEADER);
        // Si el token tiene datos y el prefijo es el adecuado entramos a intentar parsearlo y extraer los datos.
    	if (StringUtils.hasText(token) && token.startsWith(ConstantesSeguridad.TOKEN_PREFIJO)) {
            try {

            	//Parseamos el token y lo verificamos.
            	Jws<Claims> parsedToken = Jwts.parser()
                    .setSigningKey(JwtUtil.generateKeySign())
                    .parseClaimsJws(token.replace(ConstantesSeguridad.TOKEN_PREFIJO, ""));
            	System.out.println("token-"+parsedToken);
            	//Obtenemos los datos del user del token parseado.
                String user = parsedToken
                	.getBody()
                    .getSubject();
                System.out.println("user-"+user);
                //Guardamos en una lista los roles del usuario que son pasados en el token.
                List<SimpleGrantedAuthority> roles = ((List<?>) parsedToken.getBody()
                    .get("rol")).stream()
                    .map(authority -> new SimpleGrantedAuthority((String) authority))
                    .collect(Collectors.toList());
           
                System.out.println("roles-"+roles);
                if (StringUtils.hasText(user)) {
                    return new UsernamePasswordAuthenticationToken(user, null, roles);
                }
            } catch (ExpiredJwtException e) {
                System.out.println("Tiempo expirado del token : "+token+" Fallo : "+ e.getMessage());
            } catch (UnsupportedJwtException e) {
                System.out.println("No soportado : "+token+" Fallo : "+ e.getMessage());
            } catch (MalformedJwtException e) {
                System.out.println("Inválido : "+token+" Fallo : "+ e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Vacio o nulo: "+token+" Fallo : "+ e.getMessage());
            } 
        }

        return null;
    }
    	
} 	
    	
    