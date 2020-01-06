package co.com.springboot.datajpa.app.models.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="clientes")
public class Cliente implements Serializable{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	//@Column(name="NOMBRE_CLIENTE")
	@NotEmpty
	//@Size(min=4, max=12)//para decirle en minimo y el maximo de un campo
	private String nombre;
	
	@NotEmpty
	private String apellido;
	
	@NotEmpty //solo se usa con String, es una validacion para saber que el campo no venga vacio
	@Email
	private String email;

	@NotNull //se usa con una variable que no sea Strin es decir, Integer, Date, entre otros, validacion para saber que el campo no llegue nulo
	@Column(name="CREATE_AT")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date createAt;
																				 //mappedBy va a definir la bidireccion, cliente va a tener una lista de facturas pero la factura va a tener un solo cliente, va en ambos sentidos
	@OneToMany(mappedBy="cliente", fetch=FetchType.LAZY, cascade=CascadeType.ALL, orphanRemoval = true)// este es one to many, se coloca en el lado de uno, es decir una factura tiene un solo Cliente, un cliente puede estar en muchas facturas
	private List<Factura> facturas;
	
	@PrePersist
	public void prePersist() 
	{
		createAt = new Date();
	}
	
	public Cliente() {
		
		facturas = new ArrayList<Factura>();
	}

	private String foto;

	private static final long serialVersionUID = -4417263995066686179L;

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<Factura> getFacturas() {
		return facturas;
	}

	public void setFacturas(List<Factura> facturas) {
		this.facturas = facturas;
	}

	public void addFactura(Factura factura) 
	{
		facturas.add(factura); // para que agregue factura por factura una por una
	}


}
