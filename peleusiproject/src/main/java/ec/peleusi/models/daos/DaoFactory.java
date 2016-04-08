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
	/*public TipoGastoDeducibleDao getTipoGastoDeducibleDao() {
		return new TipoGastoDeducibleDao();
	}
	public EmpresaDao getEmpresaDao() {
		return new EmpresaDao();
	}*/
	public TarifaIvaDao getTarifaIvaDao() {
		return new TarifaIvaDao();
	}
	public TarifaIceDao getTarifaIceDao() {
		return new TarifaIceDao();
	}
	public ProductoDao getProductoDao() {
		return new ProductoDao();
	}

}
