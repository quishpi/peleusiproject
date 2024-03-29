package ec.peleusi.models.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id; 
import javax.persistence.Table;

@Entity
@Table(name = "tipopago")
public class TipoPago {
	public TipoPago(){
	}

	public TipoPago( String nombre){
		super();
		this.id = null;
		this.nombre = nombre;
	}
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name= "id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "nombre", unique = true, nullable = false, length = 50)
	private String nombre;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	@Override
	public String toString() {
		return "[id=" + id + ", nombre=" + nombre + "]";
	}
	}
	
	
	

