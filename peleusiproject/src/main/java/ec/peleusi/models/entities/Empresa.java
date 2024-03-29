package ec.peleusi.models.entities;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import ec.peleusi.models.entities.Ciudad;

@Entity
@Table(name = "empresa")
public class Empresa {

	public Empresa(String nombre, String identificacion, String direccion, String telefono, String fax, String email,
			String url, byte[] foto, Ciudad ciudad) {
		super();
		this.id = null;
		this.nombre = nombre;
		this.identificacion = identificacion;
		this.direccion = direccion;
		this.telefono = telefono;
		this.fax = fax;
		this.email = email;
		this.url = url;
		this.foto = foto;
		this.ciudad = ciudad;
	}

	public Empresa() {

	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@Column(name = "nombre", unique = true, nullable = false, length = 256)
	private String nombre;

	@Column(name = "identificacion", unique = true, nullable = false, length = 13)
	private String identificacion;

	@Column(name = "direcion", nullable = false, length = 256)
	private String direccion;

	@Column(name = "telefono", nullable = false, length = 50)
	private String telefono;

	@Column(name = "fax", nullable = true, length = 50)
	private String fax;

	@Column(name = "email", nullable = true, length = 256)
	private String email;

	@Column(name = "url", nullable = true, length = 256)
	private String url;

	@Column(name = "foto", nullable = true, length = 16777215)
	private byte[] foto;

	@ManyToOne(cascade = CascadeType.REFRESH, optional = false)
	private Ciudad ciudad;

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

	public String getIdentificacion() {
		return identificacion;
	}

	public void setIdentificacion(String identificacion) {
		this.identificacion = identificacion;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public byte[] getFoto() {
		return foto;
	}

	public void setFoto(byte[] foto) {
		this.foto = foto;
	}

	public Ciudad getCiudad() {
		return ciudad;
	}

	public void setCiudad(Ciudad ciudad) {
		this.ciudad = ciudad;
	}

	@Override
	public String toString() {
		return nombre;
	}

}
