/**
 * 
 */
package com.ITAcademy.M14DausJwt.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ITAcademy.M14DausJwt.dto.User;

/**
 * Interfaz que extiende el repositorio JPA
 * @author Rubén Rodríguez
 */
public interface UserDao extends JpaRepository<User, Long> {
	
	public Boolean existsByNomUsuari(String nomUsuari);
	
	@Query("SELECT u from User u where u.percentExit = (select MIN(uu.percentExit) FROM User uu)")
	public User minUserByPercentExit();
	
	//Podríamos hacerlo como con minBy y que fuera Hibernate quien la generara, pero por motivos didácticos la creamos nosotros. SEría
	//interesante conocer las diferencias en cuanto a eficiencia. 
	@Query("SELECT u from User u where u.percentExit = (select MAX(uu.percentExit) FROM User uu)")
	public User maximoAcierto();
	
}
