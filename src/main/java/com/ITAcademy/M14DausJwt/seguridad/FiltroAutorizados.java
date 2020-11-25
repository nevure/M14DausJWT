package com.ITAcademy.M14DausJwt.seguridad;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
/**
 * Clase que filtra las autorizaciones a las url de la api
 * @author Rubén Rodríguez
 *
 */
public class FiltroAutorizados extends GenericFilterBean {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		//Llamamos al método getAuthenticacion para verificar el token del request.
        Authentication authentication = JwtUtil.getAuthentication((HttpServletRequest)request);

	    SecurityContextHolder.getContext().setAuthentication(authentication);
	    chain.doFilter(request, response);
		
	}
	
}
	