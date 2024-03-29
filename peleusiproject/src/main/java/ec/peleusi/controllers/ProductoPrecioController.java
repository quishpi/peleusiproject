package ec.peleusi.controllers;

import java.util.List;

import ec.peleusi.models.daos.DaoFactory;
import ec.peleusi.models.daos.ProductoPrecioDao;
import ec.peleusi.models.entities.ProductoPrecio;
import ec.peleusi.models.entities.TipoPrecio;
import ec.peleusi.models.entities.Producto;

public class ProductoPrecioController {
	private ProductoPrecioDao productoPrecioDao;

	public ProductoPrecioController() {
		productoPrecioDao = new DaoFactory().getPrecioProductoDao();
	}

	public String createProductoPrecio(ProductoPrecio precioProducto) {
		return productoPrecioDao.create(precioProducto);
	}

	public List<ProductoPrecio> productoPrecioList() {
		return productoPrecioDao.findAll();
	}

	public List<ProductoPrecio> productoPrecioList(Producto producto) {
		return productoPrecioDao.productoPrecioList(producto);
	}

	public String deleteProductoPrecio(ProductoPrecio productoPrecio) {
		return productoPrecioDao.deleteById(productoPrecio.getId());
	}

	public List<ProductoPrecio> productoConPrecioList(String txtBuscar, TipoPrecio tipoPrecio) {
		return productoPrecioDao.productoPrecioListPorTipoPrecio(txtBuscar, tipoPrecio);
	}

}
