package com.ITAcademy.M14DausJwt.seguridad;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
/**
 * Clase de configuración de la seguridad en spring.
 * @author Rubén Rodriguez
 *
 */
@EnableGlobalMethodSecurity(prePostEnabled = true)  //Para poder usar la anotación @PreAuthorized en los controllers
@Configuration
@EnableWebSecurity
public class SeguridadConfig extends WebSecurityConfigurerAdapter {
	
	  @Override
	    protected void configure(HttpSecurity http) throws Exception {
	        http.csrf().disable().authorizeRequests()
	            .antMatchers("/login").permitAll() //login puede entrar cualquiera
	            .anyRequest().authenticated() //cualquier otro acceso necesita estar autenticado.
	            .and()
	            .exceptionHandling().accessDeniedPage("/errorLogin") //no acaba de funcionar como quiero
	            .and()
	            .addFilterBefore(new FiltroDeLogin("/login", authenticationManager()),
	                    UsernamePasswordAuthenticationFilter.class)
	                
	            // Las demás peticiones pasarán por este filtro para validar el token -> Hay algunas url restringidas a ADMIN.
	            .addFilterBefore(new FiltroAutorizados(),
	                    UsernamePasswordAuthenticationFilter.class)

	            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
;
	    }

	   /**
	    * Configuramos los usuarios en memoria.
	    */
	    @Override
	    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	        BCryptPasswordEncoder encoder = passwordEncoder();
	    	auth.inMemoryAuthentication()
            .withUser("user")
            .password(encoder.encode("micontrasenya"))
            .authorities("USER");
	    	
	    	auth.inMemoryAuthentication()
	                .withUser("admin")
	                .password(new BCryptPasswordEncoder().encode("micontrasenya"))
	                .roles("ADMIN");
	    }
	    
	    @Bean
	    public BCryptPasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }
	    
	    
	}

