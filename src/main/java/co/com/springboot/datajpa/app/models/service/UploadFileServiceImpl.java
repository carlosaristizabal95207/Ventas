package co.com.springboot.datajpa.app.models.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadFileServiceImpl implements IUploadFileService {


	private final Logger log = LoggerFactory.getLogger(getClass()); //Para mostrar en tiempo de ejecucion en el log la informacion que se desee

	private final static String UPLOADS_FOLDER = "uploads";

	public Resource load(String filename) throws MalformedURLException {
		// TODO Auto-generated method stub
		Path pathFoto =getPath(filename);
		log.info("pathFoto: " + pathFoto);
		Resource recurso = null;
		recurso = new UrlResource(pathFoto.toUri());
		if(!recurso.exists() || !recurso.isReadable()) 
		{
			throw new RuntimeException("Error: no se puede cargar la imagen: " + pathFoto.toString());
		}
		return recurso;
	}

	public String copy(MultipartFile file) throws IOException {
		String uniqueFilename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
		Path rootPath = getPath(uniqueFilename);

		//Path rootAbsolutPath = rootPath.toAbsolutePath();

		log.info("rootPath " + rootPath);
		//log.info("rootAbsolutPath: " + rootAbsolutPath);


		//			byte[] bytes = foto.getBytes();
		//			Path rutaCompleta = Paths.get(rootPath + "//" + foto.getOriginalFilename());
		//			Files.write(rutaCompleta, bytes);
		Files.copy(file.getInputStream(), rootPath);

		return uniqueFilename;
	}

	public boolean delete(String filename) {
		Path rootPath =  getPath(filename);
		File archivo = rootPath.toFile();
		
		if(archivo.exists() && archivo.canRead()) 
		{
			if(archivo.delete()) 
			{
				//flash.addFlashAttribute("info", "Foto " + cliente.getFoto() + "eliminada con exito!");
				return true;
			}
			
		}
		return false;
	}

	public Path getPath(String filename) 
	{
		return Paths.get(UPLOADS_FOLDER).resolve(filename).toAbsolutePath();
	}

	public void deleteAll() {
		
		FileSystemUtils.deleteRecursively(Paths.get(UPLOADS_FOLDER).toFile());
		
	}

	public void init() throws IOException {
		Files.createDirectory(Paths.get(UPLOADS_FOLDER));
		
	}

}
