/**
 * 
 */
package com.ITAcademy.M14DausJwt.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ITAcademy.M14DausJwt.dto.User;
import com.ITAcademy.M14DausJwt.services.UserServiceImpl;

/**
 * Clase Controladora para la entidad Usuario.
 * Creación, modificación y eliminación de usuarios. Listado de usuarios.
 * @author Rubén Rodriguez
 *
 */
@RestController
public class UserController {

	// Inyectamos el service de usuario.
	@Autowired
	UserServiceImpl usuarioService;
	/**
	 * Método de creación de usuarios.
	 * Si el body es vacío {} o el nombreUsuario es "anonimo", con idiferencia de mayúsculas y minúsculas, el sistema
	 * crea un usuario con nombreUsuario="ANONIMO" en mayúsculas.
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/players", method = RequestMethod.POST)
	public ResponseEntity<Object> crearUsuario(@RequestBody User user) {
		//El usuario puede haber hecho constar en el body usuario Anónimo o puede enviar el body vacío {} por lo que se considera anónimo también.
		//tanto lo anteior dicho como si el nombre de usuario no está ya en la BBDD creamos el usuario.
		if (user.esAnonimo() || !usuarioService.existeUser(user.getNomUsuari())) {
			return ResponseEntity.ok().body(usuarioService.creaUsuario(user));

		}
		else
			return ResponseEntity.ok().body("El usuario "+user.getNomUsuari()+ " ya existe");
		
	
	}
	
	/**
	 * Método que modifica el nombre de usuario.
	 * Similar al método anterior en cuanto al tema de los anónimos. 
	 * @param id
	 * @param user
	 * @return
	 */
	@PreAuthorize("hasRole('ADMIN')")  //Solo ADMINS pueden acceder a esta url. 
	@PutMapping(value="/players/{id}")
	public ResponseEntity<Object> modifUsuario(@PathVariable(name="id") Long id, @RequestBody User user) {
		
		//Tenemos que controlar que el nombre d eusuario no exista ya en la BBDD. Puede escoger ser anónimo ahora.
		if (user.esAnonimo() || !usuarioService.existeUser(user.getNomUsuari())) {
			//Con el id pasado por parámetro recuperamos el usuario guardado en la BBDD para luego asignarle el nuevo nombre y guardarlo
			User usuarioModificado = usuarioService.selectUserById(id); 
			usuarioModificado.setNomUsuari(user.getNomUsuari());
			return ResponseEntity.ok().body(usuarioService.creaUsuario(usuarioModificado));

		}
		else
			return ResponseEntity.ok().body("El usuario con nombre "+user.getNomUsuari()+ " ya existe");
		
	}
	
	/**
	 * Retorna un llistat dels jugadors 
	 * @return Listado jugadores
	 */
	@RequestMapping(value="/players", method = RequestMethod.GET)
	public List<User> listadoUsuarios(){
		return usuarioService.listUsers();
	}
	
	/**
	 * Elimina usuario por ID
	 * @param id
	 * @return String informando de la eliminación
	 */
	@DeleteMapping("/players/{id}")
	public String eliminaUsuario(@PathVariable(name="id") Long id) {
		usuarioService.eliminaUserById(id);
		return "Usuario Eliminado";
	}
	
	/**
	 * Método que calcula la media del porcentaje de éxito de los jugadores.
	 * @return
	 */
	@GetMapping("/players/ranking")
	public ResponseEntity<Object> rankingMigJugadors(){
		return ResponseEntity.ok().body("El porcentaje medio de éxito de los jugadores es: "+usuarioService.sumaPorcentajesExito()/usuarioService.totalJugadores());	
		
	}
	
	/**
	 * Retorna como respuesta el usuario con peor porcentaje
	 * @return
	 */
	@GetMapping(value="/players/ranking/loser")
	public ResponseEntity<Object> usuarioPeorPorcentaje(){
		return ResponseEntity.ok().body(usuarioService.jugadorMenorExito());
	}
	
	/**
	 * Retorna como respuesta el usuario con mejor porcentaje
	 * @return
	 */
	@RequestMapping(value="/players/ranking/winner", method = RequestMethod.GET)
	public ResponseEntity<Object> usuarioMejorPorcentaje() {
		return ResponseEntity.ok().body(usuarioService.jugadorMayorExito());
	}
}
