package co.com.springboot.datajpa.app.models.dao;

import org.springframework.data.repository.CrudRepository;

import co.com.springboot.datajpa.app.models.entity.Factura;

public interface IFacturaDao extends CrudRepository<Factura, Long> {

}
