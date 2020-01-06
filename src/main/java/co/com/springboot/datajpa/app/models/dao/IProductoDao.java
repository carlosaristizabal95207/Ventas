package co.com.springboot.datajpa.app.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import co.com.springboot.datajpa.app.models.entity.Producto;

public interface IProductoDao extends CrudRepository<Producto, Long> {

	@Query("select p from Producto p where p.nombre like %?1%") // es una consulta a traves de Entity y no de tabla. %1 reemplaza en el query a la variable term con signo ? se hace referencia al parametro
	public List<Producto> findByNombre(String term);
	
	public List<Producto> findByNombreLikeIgnoreCase(String term); //esta es una forma de implementar un query a traves del nombre del metod, respetando el orden del query y respetando el camel case para mas informacion ver la documentacion https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#reference
}
