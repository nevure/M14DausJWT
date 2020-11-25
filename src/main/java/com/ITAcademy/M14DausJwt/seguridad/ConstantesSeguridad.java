package com.ITAcademy.M14DausJwt.seguridad;

public final class ConstantesSeguridad {
	public static final String AUTH_LOGIN_URL = "/login";

    //Clave secreta par ala generación del key   
    public static final String SECRETO = "8y/B?E(H+MbQeThWmYq3t6w9z$C&F)J@NcRfUjXn2r4u7x!A%D*G-KaPdSgVkYp3";

    // parámetros por defecto JWT token 
    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIJO = "Bearer ";
    public static final String TOKEN_TIPO = "JWT";
}
