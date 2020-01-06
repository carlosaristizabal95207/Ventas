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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name="facturas")
public class Factura implements Serializable {

	/**
	 * 
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty
	private String descripcion;
	private String observacion;
	@Temporal(TemporalType.DATE)
	@Column(name="create_at")
	private Date createAt;
	
	@ManyToOne(fetch=FetchType.LAZY) // Lazy es carga perezosa, es la forma mas recomendada ya que eager trae toda la consulta de una vez, por cada factura va a traer a su cliente en su totalidad y eso carga la bd si es por el EAGER, en cambio el LAZY trae la factura de ese cliente elemento por elemento cada que se llame cada metodo  // many to one se coloca en el muchos, aqui es una factura tiene un solo cliente pero un cliente puede tener muchas facturas
	private Cliente cliente;
	
	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name="factura_id") // como no hay una relacion en ambos sentidos aqui es necesario el JoinColumn pero a diferencia de factura con cliente que si tenia Factura y cliente, en la clase cliente donde se crea una lista de facturas se le coloca a esta lista mappedBy pues esta mapeada por el cliente
	private List<ItemFactura> items;
	
	

	public Factura() {

		this.items = new ArrayList<ItemFactura>();
	}
	
	@PrePersist
	public void prePersist() 
	{
		createAt = new Date();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
	
	
	public List<ItemFactura> getItems() {
		return items;
	}

	public void setItems(List<ItemFactura> items) {
		this.items = items;
	}
	
	public void addItemFactura(ItemFactura item)
	{
		this.items.add(item);
	}
	
	public Double getTotal() 
	{
		Double total = 0.0;
		
		int size = items.size();
		
		for(int i=0; i< size; i++) 
		{
			total += items.get(i).calcularImporte();
		}
		return total;
	}

	private static final long serialVersionUID = 1L;

}
