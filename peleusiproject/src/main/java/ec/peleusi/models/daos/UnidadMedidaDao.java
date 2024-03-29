package ec.peleusi.models.daos;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import ec.peleusi.models.entities.UnidadMedida;
import ec.peleusi.utils.HibernateUtil;

public class UnidadMedidaDao extends GenericDao<UnidadMedida, Integer> {

	public UnidadMedidaDao() {
		super(UnidadMedida.class);

	}
	
	public UnidadMedida getUnidadMedidaIdentificacion(String id) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			session.beginTransaction();
			Query query = session.createQuery("from UnidadMedida T WHERE T.id = :id");
			query.setParameter("id", id);
			if (!query.list().isEmpty()) {
				return (UnidadMedida) query.list().get(0);
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
	
	
	@SuppressWarnings("unchecked")
	public List<UnidadMedida> UnidadMedidaList(String parametro) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			session.beginTransaction();
			Query query = session.createQuery("from UnidadMedida T WHERE CONCAT(T.id,T.nombre,T.abreviatura) LIKE CONCAT('%', :parametro, '%')");
			query.setParameter("parametro", parametro); 
			if (!query.list().isEmpty()) {
				return query.list();
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