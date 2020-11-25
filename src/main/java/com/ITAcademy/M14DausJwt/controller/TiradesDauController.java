/**
 * 
 */
package com.ITAcademy.M14DausJwt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ITAcademy.M14DausJwt.dto.TiradesDau;
import com.ITAcademy.M14DausJwt.dto.User;
import com.ITAcademy.M14DausJwt.services.TiradesDauServiceImpl;
import com.ITAcademy.M14DausJwt.services.UserServiceImpl;

/**
 * Controlador de las tiradas de dados
 * @author Rubén Rodríguez Fernández
 *
 */
@RestController
public class TiradesDauController {
	
	//Inyectamos los dos servicios
	@Autowired
	TiradesDauServiceImpl dausService;
	
	@Autowired
	UserServiceImpl usuarioService;
	
	/**
	 * Lanzamos un dado.
	 * 
	 * @param id  Identificador del jugador
	 * @return resultado de la tirada
	 */
	@PostMapping("/players/{id}/games")
	public ResponseEntity<Object> lanzamientoDados(@PathVariable("id") Long id) {
		TiradesDau tirada;
		//Verificamos que le usuario exista
		if (usuarioService.existeUserById(id)) {
			//Buscamos usuario y lo agregamos a la tirada y creamos la tirada (lo que provoca que relize la tirada de dados)
			User usuario = usuarioService.selectUserById(id);
			tirada = new TiradesDau(usuario);
			
			//si la tirada es certera incrementamos el número de victorias del usuario.
			if (tirada.isResultat())
				usuario.setNumVictories(usuario.getNumVictories()+1);
			//Incrementamos el número de lanzamientos realizado por el jugador.
			usuario.setNumLanzamientos(usuario.getNumLanzamientos()+1);
			//System.out.println("num victorias: "+usuario.getNumVictories()+" y resultado "+usuario.getNumVictories() / usuario.getNumLanzamientos());
			//usuario.setPercentExit(
			//Actualizamos el porcentaje de éxito del jugado
			usuario.calculoExito();
			//actualizamos estos datos en la base de datos.
			usuarioService.creaUsuario(usuario);
			//dentro del return guardamos la tirada en la BD
			return ResponseEntity.ok().body(dausService.novaTirada(tirada));
			
		}
		else
			return ResponseEntity.ok().body("El usuario con id "+id+" No existe");		
	
	}
	
	/**
	 * Eliminamos las tiradas del jugador con identificado id
	 * @param id
	 * @return
	 */
	@PreAuthorize("hasRole('ADMIN')")  //Solo ADMINS pueden acceder a esta url. 
	@RequestMapping(value="/players/{id}/games", method = RequestMethod.DELETE)
	public String eliminaTiradasUser(@PathVariable("id") Long id) {
		User usuario = usuarioService.selectUserById(id);
		usuario.setNumLanzamientos(0);
		usuario.setNumVictories(0);
		usuario.setPercentExit(0);
		usuarioService.creaUsuario(usuario);
		dausService.eliminarTiradasByUser(id);
;		return "Tiradas Eliminadas";
	}
	@PreAuthorize("hasRole('ADMIN')")  //Solo ADMINS pueden acceder a esta url. 
	@GetMapping("/players/{id}/games")
	public List<TiradesDau> listadoTiradas(@PathVariable("id")Long id){
		return dausService.listaTiradaByUser(id);
	
	}
	
	
	

	
}
