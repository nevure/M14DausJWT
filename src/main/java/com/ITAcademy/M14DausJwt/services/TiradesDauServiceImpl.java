/**
 * 
 */
package com.ITAcademy.M14DausJwt.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ITAcademy.M14DausJwt.dao.TiradesDauDAO;
import com.ITAcademy.M14DausJwt.dto.TiradesDau;

/**
 * @author ru
 *
 */
@Service
public class TiradesDauServiceImpl implements ITiradesDauServices {
	
	@Autowired
	TiradesDauDAO tiradasDao;

	@Override
	public TiradesDau novaTirada(TiradesDau tirada) {
		return tiradasDao.save(tirada);

	}

	@Override
	public List<TiradesDau> listaTiradaByUser(Long id) {
		return tiradasDao.findAllByUsuarioId(id);
	}

	@Override
	public int cuantasTiradasByUser(Long id) {
		return 0;
	}

	@Override
	public int cuantasVictoriasByUser(Long id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	@Transactional
	public void eliminarTiradasByUser(Long id) {
		tiradasDao.deleteAllByUsuarioId(id);

	}

	@Override
	public List<TiradesDau> listaTodasTiradas() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean reiniciojuego() {
		// TODO Auto-generated method stub
		return false;
	}

}
