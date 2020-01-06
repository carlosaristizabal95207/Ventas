package co.com.springboot.datajpa.app.controllers;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import co.com.springboot.datajpa.app.models.entity.Cliente;
import co.com.springboot.datajpa.app.models.entity.Factura;
import co.com.springboot.datajpa.app.models.entity.ItemFactura;
import co.com.springboot.datajpa.app.models.entity.Producto;
import co.com.springboot.datajpa.app.models.service.IClienteService;

@Controller
@RequestMapping(value="/factura", method=RequestMethod.GET)
@SessionAttributes("factura")
public class FacturaController {

	@Autowired
	private IClienteService clienteService;
	
	private final Logger log =  LoggerFactory.getLogger(getClass());

	@RequestMapping(value="/form/{clienteId}", method=RequestMethod.GET)
	public String crear(@PathVariable(value="clienteId") Long clienteId, Map<String, Object> model, RedirectAttributes flash ) 
	{
		Cliente cliente = clienteService.findOne(clienteId);
		if(cliente == null) 
		{
			flash.addFlashAttribute("error", "El cliente no existe en la base de datos");
			return "redirect/listar";
		}

		Factura factura = new Factura();
		factura.setCliente(cliente);

		model.put("factura", factura);
		model.put("titulo", "crear factura");

		return "factura/form";
	}

	@RequestMapping(value="/cargar-productos/{term}", method=RequestMethod.GET, produces= {"application/json"})
	public @ResponseBody List<Producto> cargarProductos(@PathVariable String term){
		//El response Body suprime el cargar una lista de thymeleaf y en vez de eso va a tomar lo que se esta retornando convertido en json, json lo va a poblar y lo va a registrar dentro del body de la respuesta
		return clienteService.findByNombre(term);
	}
	
	@RequestMapping(value="/form", method=RequestMethod.POST)
	public String guardar(@Valid Factura factura,
			BindingResult result,
			Model model,
			@RequestParam(name="item_id[]", required=false) Long[] itemId,
			@RequestParam(name="cantidad[]", required=false) Integer[] cantidad,
			RedirectAttributes flash, 
			SessionStatus status) 
	{
		if(result.hasErrors()) 
		{
			model.addAttribute("titulo", "Crear Factura");
			return "factura/form";
		}
		if(itemId == null || itemId.length == 0) 
		{
			model.addAttribute("titulo", "Crear Factura");
			model.addAttribute("error", "Error: La factura NO puede no tener lineas !");
			
			return "factura/form";
		}
		for(int i=0; i< itemId.length; i++) 
		{
			Producto producto = clienteService.findProductoById(itemId[i]);
			
			ItemFactura linea = new ItemFactura();
			linea.setCantidad(cantidad[i]);
			linea.setProducto(producto);
			factura.addItemFactura(linea);
			
			log.info("ID: " + itemId[i].toString() + ", cantidad: " + cantidad[i].toString());
		}
		clienteService.saveFactura(factura);
		status.setComplete();
		
		flash.addFlashAttribute("success", "Factura Creada Con Exito");
		
		return "redirect:/ver/" + factura.getCliente().getId();
	}
}
