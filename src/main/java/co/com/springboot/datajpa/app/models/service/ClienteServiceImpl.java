package co.com.springboot.datajpa.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.com.springboot.datajpa.app.models.dao.IClienteDao;
import co.com.springboot.datajpa.app.models.dao.IFacturaDao;
import co.com.springboot.datajpa.app.models.dao.IProductoDao;
import co.com.springboot.datajpa.app.models.entity.Cliente;
import co.com.springboot.datajpa.app.models.entity.Factura;
import co.com.springboot.datajpa.app.models.entity.Producto;

@Service
public class ClienteServiceImpl implements IClienteService {

	@Autowired
	private IClienteDao clienteDao;
	
	@Autowired
	private IProductoDao productoDao;
	
	@Autowired
	private IFacturaDao facturaDao;
	
	@Transactional(readOnly=true)
	public List<Cliente> findAll() {

		return (List<Cliente>) clienteDao.findAll();
	}
	
	@Transactional(readOnly=true)
	public Cliente findOne(Long id) {

		return clienteDao.findById(id).orElse(null);
	}
	
	@Transactional
	public void save(Cliente cliente) {

		clienteDao.save(cliente);
	}

	@Transactional
	public void delete(Long id) {

		clienteDao.deleteById(id);
	}

	@Transactional(readOnly=true)
	public Page<Cliente> findAll(Pageable pageable) {
		return clienteDao.findAll(pageable);
	}

//	public List<Producto> findByNombre(String term) {
//		// TODO Auto-generated method stub
//		return productoDao.findByNombre(term);
//	}

	public List<Producto> findByNombre(String term) {

		return productoDao.findByNombreLikeIgnoreCase("%"+term+"%");
	}

	@Transactional
	public void saveFactura(Factura factura) {

		facturaDao.save(factura);
	}

	@Transactional(readOnly=true)
	public Producto findProductoById(Long id) {

		return productoDao.findById(id).orElse(null);
	}

	@Override
	@Transactional(readOnly=true)
	public Factura findFacturaById(Long id) {
		// TODO Auto-generated method stub
		return facturaDao.findById(id).orElse(null);
	}
}
