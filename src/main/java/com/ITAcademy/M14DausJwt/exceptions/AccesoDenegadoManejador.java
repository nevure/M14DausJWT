package com.ITAcademy.M14DausJwt.exceptions;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
/**
 * Clase para manejar el acceso denegado. Cuando alguien logueado intenta acceder a una url no permitida a su rol.
 * @author ru
 *
 */
public class AccesoDenegadoManejador implements AccessDeniedHandler {
	 
   
 
    @Override
    public void handle(
      HttpServletRequest request,
      HttpServletResponse response, 
      AccessDeniedException exc) throws IOException, ServletException {
        
    	if (!response.isCommitted()) {

            request.getRequestDispatcher("/login").forward(request, response);
    	
    	}
    }
}
      
     