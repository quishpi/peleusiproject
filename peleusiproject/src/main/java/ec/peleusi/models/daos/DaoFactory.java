package ec.peleusi.models.daos;

public class DaoFactory {
	public static DaoFactory factory = null;

	public static void setFactory(DaoFactory factory) {
		DaoFactory.factory = factory;
	}

	public static DaoFactory getFactory() {
		if (factory == null) {
			factory = new DaoFactory();
		}
		return factory;
	}

	public CiudadDao getCiudadDao() {
		return new CiudadDao();
	}

	public UnidadMedidaDao getUnidadMedidaDao() {
		return new UnidadMedidaDao();
	}

	public CategoriaProductoDao getCategoriaProductoDao() {
		return new CategoriaProductoDao();
	}

}
