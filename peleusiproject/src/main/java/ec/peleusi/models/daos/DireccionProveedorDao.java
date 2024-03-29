package ec.peleusi.models.daos;

import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import ec.peleusi.models.entities.DireccionProveedor;
import ec.peleusi.models.entities.Proveedor;
import ec.peleusi.utils.HibernateUtil;


public class DireccionProveedorDao extends GenericDao<DireccionProveedor, Integer> {
	public DireccionProveedorDao() {
		super(DireccionProveedor.class);
	}

	@SuppressWarnings("unchecked")
	public List<DireccionProveedor> DireccionProveedorList(Proveedor proveedor) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<DireccionProveedor> lista = null;

		try {
			session.beginTransaction();  
			Query query = session.createQuery("from DireccionProveedor C WHERE C.proveedor=:proveedor");
			query.setParameter("proveedor", proveedor);
			lista = query.list();
			session.getTransaction().commit();
			System.out.println("--->------- " + proveedor);
		} catch (HibernateException e) {
			if (session.getTransaction() != null)
				session.getTransaction().rollback();
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return lista;
	}
	
	public DireccionProveedor getDireccionProveedorPorDefecto(Proveedor proveedor) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			session.beginTransaction();
			Query query = session.createQuery("from DireccionProveedor T WHERE T.proveedor = :proveedor and porDefecto=1");
			query.setParameter("proveedor", proveedor);
			if (!query.list().isEmpty()) {
				return (DireccionProveedor) query.list().get(0);
			}
			session.getTransaction().commit();

		} catch (HibernateException e) {
			if (session.getTransaction() != null)
				e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return null;
	}		
}

