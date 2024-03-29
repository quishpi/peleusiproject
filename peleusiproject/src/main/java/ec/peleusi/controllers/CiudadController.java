package ec.peleusi.controllers;

import ec.peleusi.models.daos.DaoFactory;
import java.util.List;
import ec.peleusi.models.daos.CiudadDao;
import ec.peleusi.models.entities.Ciudad;

public class CiudadController {
	private CiudadDao ciudadDao;

	public CiudadController() {
		ciudadDao = new DaoFactory().getCiudadDao();
	}

	public String createCiudad(Ciudad ciudad) {
		return ciudadDao.create(ciudad);
	}

	public String updateCiudad(Ciudad ciudad) {
		return ciudadDao.update(ciudad);
	}

	public String deleteCiudad(Ciudad ciudad) {
		return ciudadDao.deleteById(ciudad.getId());
	}

	public List<Ciudad> ciudadList() {
		return ciudadDao.findAll();
	}

	public List<Ciudad> ciudadList(String parametro) {
		return ciudadDao.CiudadList(parametro);
	}
}
