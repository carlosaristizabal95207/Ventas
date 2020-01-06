package co.com.springboot.datajpa.app.controllers;

//import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
import java.util.Map;
//import java.util.UUID;

import javax.validation.Valid;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
//import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

//import co.com.springboot.datajpa.app.models.dao.IClienteDao;
import co.com.springboot.datajpa.app.models.entity.Cliente;
import co.com.springboot.datajpa.app.models.service.IClienteService;
import co.com.springboot.datajpa.app.models.service.IUploadFileService;
import co.com.springboot.datajpa.app.util.paginator.PageRender;

@Controller
@SessionAttributes("cliente")
public class ClienteController {

	@Autowired
	private IClienteService clienteService;

	@Autowired
	private IUploadFileService uploadFileService;


	@RequestMapping(value="/uploads/{filename:.+}", method=RequestMethod.GET)
	public ResponseEntity<Resource> verFoto(@PathVariable String filename)
	{
		Resource recurso = null;
		try {
			recurso = uploadFileService.load(filename);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"").body(recurso);

	}


	@RequestMapping(value="/ver/{id}", method=RequestMethod.GET)
	public String ver(@PathVariable(value="id") Long id, Map<String, Object> model, RedirectAttributes flash)
	{
		Cliente cliente = clienteService.findOne(id);
		if(cliente==null) 
		{
			flash.addFlashAttribute("error", "El cliente no existe en la base de datos");
			return "redirect:/listar ";
		}

		model.put("cliente", cliente);
		model.put("titulo", "Detalle cliente: " + cliente.getNombre());

		return "ver";
	}

	@RequestMapping(value="/listar", method=RequestMethod.GET)
	public String listar(@RequestParam(name="page", defaultValue = "0") int page, Model model) 
	{
		Pageable pageRequest = PageRequest.of(page, 4); // of es el Metodo estatico, con el 'new' ya no se utiliza

		Page<Cliente> clientes = clienteService.findAll(pageRequest);

		PageRender<Cliente> pageRender = new PageRender<Cliente>("/listar", clientes);
		model.addAttribute("titulo", "Listado de clientes");
		model.addAttribute("clientes", clientes);
		model.addAttribute("page", pageRender);
		return "listar";	
	}

	@RequestMapping(value="/form", method=RequestMethod.GET)
	public String crear(Map<String, Object> model ) 
	{
		Cliente cliente = new Cliente();
		model.put("cliente", cliente);
		model.put("titulo", "Formulario de cliente");
		return "form";
	}

	@RequestMapping(value="/form/{id}", method=RequestMethod.GET)
	public String editar(@PathVariable(value="id") Long id,  Map<String, Object> model, RedirectAttributes flash ) 
	{
		Cliente cliente = null;
		if(id>0) 
		{
			cliente = clienteService.findOne(id);
			if(cliente == null) 
			{
				flash.addFlashAttribute("error", "El ID del cliente no existe en la base de datos!");
				return "redirect:/listar";
			}
		}
		else 
		{
			flash.addFlashAttribute("error","El ID del cliente no puede ser cero!");
			return "redirect:/listar";
		}
		model.put("cliente", cliente);
		model.put("titulo", "Editar Cliente");
		return "form";
	}


	@RequestMapping(value="/form", method=RequestMethod.POST)
	public String guardar(@Valid Cliente cliente, BindingResult result, Model model, @RequestParam("file") MultipartFile foto, RedirectAttributes flash, SessionStatus status) 
	{
		if(result.hasErrors()) 
		{
			model.addAttribute("titulo", "Formulario de Cliente");
			return "form";
		}
		if(!foto.isEmpty()) 
		{
			if(cliente.getId() != null 
					&& cliente.getId() > 0 
					&& cliente.getFoto() != null
					&& cliente.getFoto().length() > 0) 
			{

				uploadFileService.delete(cliente.getFoto());

				//				Path rootPath = Paths.get(UPLOADS_FOLDER).resolve(cliente.getFoto()).toAbsolutePath();
				//				File archivo = rootPath.toFile();
				//				
				//				if(archivo.exists() && archivo.canRead()) 
				//				{
				//					archivo.delete();
				//					
				//				}
			}

			String uniqueFilename = null;
			try {
				uniqueFilename = uploadFileService.copy(foto);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			flash.addFlashAttribute("info", "Ha subido correctamente '" + uniqueFilename + "'");

			cliente.setFoto(uniqueFilename);

			//Path directorioRecursos = Paths.get("src//main//resources//static/upload");
			//String rootPath = "C://Temp//uploads";/*directorioRecursos.toFile().getAbsolutePath();*/
			//			String uniqueFilename = UUID.randomUUID().toString() + "_" + foto.getOriginalFilename();
			//			Path rootPath = Paths.get(UPLOADS_FOLDER).resolve(uniqueFilename);
			//			
			//			Path rootAbsolutPath = rootPath.toAbsolutePath();
			//			
			//			log.info("rootPath " + rootPath);
			//			log.info("rootAbsolutPath: " + rootAbsolutPath);
			//			
			//			
			//			try {
			////				byte[] bytes = foto.getBytes();
			////				Path rutaCompleta = Paths.get(rootPath + "//" + foto.getOriginalFilename());
			////				Files.write(rutaCompleta, bytes);
			//				Files.copy(foto.getInputStream(), rootAbsolutPath);
			//				flash.addFlashAttribute("info", "Ha subido correctamente '" + uniqueFilename + "'");
			//				
			//				cliente.setFoto(uniqueFilename);
			//			} catch (IOException e) {
			//				// TODO Auto-generated catch block
			//				e.printStackTrace();
			//			}
		}

		String mensajeFlash = (cliente.getId() != null)? "Cliente editado con éxito" : "Cliente creado con éxito";


		clienteService.save(cliente);
		status.setComplete();
		flash.addFlashAttribute("success", mensajeFlash);
		return "redirect:listar";

	}

	@RequestMapping(value="/eliminar/{id}", method=RequestMethod.GET)
	public String eliminar(@PathVariable(value="id") Long id, RedirectAttributes flash) 
	{
		if(id > 0) 
		{
			Cliente cliente = clienteService.findOne(id);
			clienteService.delete(id);
			flash.addFlashAttribute("success", "Cliente eliminado con éxito!");

			//			Path rootPath = Paths.get(UPLOADS_FOLDER).resolve(cliente.getFoto()).toAbsolutePath();
			//			File archivo = rootPath.toFile();
			//			
			//			if(archivo.exists() && archivo.canRead()) 
			//			{
			if(uploadFileService.delete(cliente.getFoto())/*archivo.delete()*/) 
			{
				flash.addFlashAttribute("info", "Foto " + cliente.getFoto() + "eliminada con exito!");
			}

		}

		return "redirect:/listar";

	}

}
