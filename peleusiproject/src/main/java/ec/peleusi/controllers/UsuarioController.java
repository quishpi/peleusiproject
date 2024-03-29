package ec.peleusi.controllers;

import java.util.List;

import ec.peleusi.models.daos.DaoFactory;
import ec.peleusi.models.daos.UsuarioDao;
import ec.peleusi.models.entities.Usuario;

public class UsuarioController {
	private UsuarioDao usuarioDao;

	public UsuarioController() {
		usuarioDao = new DaoFactory().getUsuarioDao();
	}

	public String createUsuario(Usuario usuario) {
		return usuarioDao.create(usuario);
	}
	
	public String updateUsuario(Usuario usuario) {
		return usuarioDao.update(usuario);
	}

	public String deleteUsuario(Usuario usuario) {
		return usuarioDao.deleteById(usuario.getId());
	}

	public List<Usuario> usuarioList() {
		return usuarioDao.findAll();
	}

	public List<Usuario> getUsuarioList(String parametro) {
		return usuarioDao.UsuarioList(parametro);
	}

}
