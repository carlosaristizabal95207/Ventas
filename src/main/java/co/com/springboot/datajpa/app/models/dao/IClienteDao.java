package co.com.springboot.datajpa.app.models.dao;


//import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import co.com.springboot.datajpa.app.models.entity.Cliente;
//no es necesario indicar una anotacion para decir que es un componente de spring para inyectarlo, pues ya por debajo lo asigna como un componente spring
public interface IClienteDao extends PagingAndSortingRepository<Cliente, Long> /*CrudRepository<Cliente, Long>*/ {
	
//	public List<Cliente> findAll();
//
//	public void save(Cliente cliente);
//	
//	public Cliente findOne(Long id);
//	
//	public void delete(Long id);
}
