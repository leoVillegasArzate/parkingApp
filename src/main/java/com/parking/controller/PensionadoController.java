package com.parking.controller;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.parking.models.UserModel;
import com.parking.service.interfaces.PensionadoInterface;
import com.parking.utils.Constantes;
import com.parking.utils.ResponseApp;

@Controller
@RequestMapping("/pensionado")
public class PensionadoController {
	private Logger log = Logger.getLogger(PensionadoController.class);
	@Autowired
	PensionadoInterface pensionadoInterface;

	@GetMapping("/home")
	public String home(HttpSession session,RedirectAttributes redirectAttributes ) {
		if (session.getAttribute("user")==null) {
			 redirectAttributes.addFlashAttribute("sesion_error", "No se encontraron datos del usuario");
			 return "redirect:/login1";
		}else {
			return"homePensionado";
		}
		
	}
	
	@GetMapping("/getPension")
	public @ResponseBody String buscarSalida( HttpSession session) {

		 ResponseApp responseApp= new ResponseApp();
		try {
			if (session.getAttribute("user")!=null) {					
				UserModel user =(UserModel) session.getAttribute("user");
				responseApp=pensionadoInterface.getPensionImporte(user.getIdEstacionamiento());				  
				} else {
					responseApp.setStatus(Constantes.ERROR);
					responseApp.setMensaje("No se recupero usuario de la session");
				}

		} catch (Exception e) {
			e.printStackTrace();
			responseApp.setStatus(Constantes.ERROR);
			responseApp.setMensaje("ocurrio un error en el servidor "+e.getMessage());
		}finally {			
			log.info("Status :: "+responseApp.getStatus()+" | "+responseApp.getMensaje());
		}				
		return new Gson().toJson(responseApp);
	}
	@GetMapping("/lstPensionados")
	public @ResponseBody String lstPensionados( HttpSession session) {
		log.info("Inicia recuperacion de pensionados");
		 ResponseApp responseApp= new ResponseApp();
		try {
			if (session.getAttribute("user")!=null) {					
				UserModel user =(UserModel) session.getAttribute("user");
				responseApp=pensionadoInterface.lstPensionados(user.getIdEstacionamiento());				  
				} else {
					responseApp.setStatus(Constantes.ERROR);
					responseApp.setMensaje("No se recupero usuario de la session");
				}

		} catch (Exception e) {
			e.printStackTrace();
			responseApp.setStatus(Constantes.ERROR);
			responseApp.setMensaje("ocurrio un error en el servidor "+e.getMessage());
		}finally {			
			log.info("Status :: "+responseApp.getStatus()+" | "+responseApp.getMensaje());
		}				
		return new Gson().toJson(responseApp);
	}
	
	@GetMapping("/delete/{id}")
	public @ResponseBody String deletePensionado(@PathVariable("id") String id,HttpSession session) {
		log.info("Inicia eliminacion de pensionados");
		 ResponseApp responseApp= new ResponseApp();
		try {
			if (session.getAttribute("user")!=null) {									
				if (id.equals("") || id.isEmpty()) {
					responseApp.setStatus(Constantes.ERROR);
					responseApp.setMensaje("No se recupero el id de pensionado ");
				} else {
					responseApp=pensionadoInterface.deletePensionado(id);	
				}
							  
				} else {
					responseApp.setStatus(Constantes.ERROR);
					responseApp.setMensaje("No se recupero usuario de la session");
				}

		} catch (Exception e) {
			e.printStackTrace();
			responseApp.setStatus(Constantes.ERROR);
			responseApp.setMensaje("ocurrio un error en el servidor "+e.getMessage());
		}finally {			
			log.info("Status :: "+responseApp.getStatus()+" | "+responseApp.getMensaje());
		}				
		return new Gson().toJson(responseApp);
	}
	
	@PostMapping("/save")
	public @ResponseBody String savePension( @RequestBody String ParametersRequest,HttpSession session) {

		 ResponseApp responseApp= new ResponseApp();
		 JsonObject pensionado = new  JsonObject();
		 
		 log.info("inicia save  pension / "+ParametersRequest);
		try {
			if (session.getAttribute("user")==null) {
				responseApp.setStatus(Constantes.ERROR);
				responseApp.setMensaje("Hay nmo existe una session de usuario");
				return new Gson().toJson(responseApp);
			}
			
			if (ParametersRequest!=null) {					
				pensionado =new Gson().fromJson(ParametersRequest, JsonObject.class);
				responseApp=pensionadoInterface.savePensionado(pensionado);
				} else {
					responseApp.setStatus(Constantes.ERROR);
					responseApp.setMensaje("No se recibieron datos");
				}

		} catch (Exception e) {
			e.printStackTrace();
			responseApp.setStatus(Constantes.ERROR);
			responseApp.setMensaje("ocurrio un error en el servidor "+e.getMessage());
		}finally {			
			log.info("Status :: "+responseApp.getStatus()+" | "+responseApp.getMensaje());
		}				
		return new Gson().toJson(responseApp);
	}
    
	@GetMapping("/getMesesPAgo/{id}")
	public  @ResponseBody String getMesesPAgo(@PathVariable("id") String id,HttpSession session) {
		log.info("Inicia recuperacion de get Meses PAgo por id :: "+id);
		 ResponseApp responseApp= new ResponseApp();
		try {
			if (session.getAttribute("user")!=null) {									
				if (id.equals("") || id.isEmpty()) {
					responseApp.setStatus(Constantes.ERROR);
					responseApp.setMensaje("No se recupero el id de pensionado ");
				} else {
					responseApp=pensionadoInterface.getMesesPendientes(id);	
				}
							  
				} else {
					responseApp.setStatus(Constantes.ERROR);
					responseApp.setMensaje("No se recupero usuario de la session");
				}

		} catch (Exception e) {
			e.printStackTrace();
			responseApp.setStatus(Constantes.ERROR);
			responseApp.setMensaje("ocurrio un error en el servidor "+e.getMessage());
		}finally {			
			log.info("Status :: "+responseApp.getStatus()+" | "+responseApp.getMensaje());
		}				
		return new Gson().toJson(responseApp);
	}
	
	@PostMapping("/savePago")
	public  @ResponseBody String savePago( @RequestBody String ParametersRequest,HttpSession session) {
		log.info("Inicia recuperacion de get Meses PAgo por id :: "+ParametersRequest);
		 ResponseApp responseApp= new ResponseApp();
		try {
			if (session.getAttribute("user")!=null) {									
				if (ParametersRequest.equals("") || ParametersRequest.isEmpty()) {
					responseApp.setStatus(Constantes.ERROR);
					responseApp.setMensaje("No se recibieron pagos ");
				} else {
					responseApp=pensionadoInterface.savePago(ParametersRequest);	
				}
							  
				} else {
					responseApp.setStatus(Constantes.ERROR);
					responseApp.setMensaje("No se recupero usuario de la session ,favor de actualizar la pagina (f5)");
				}

		} catch (Exception e) {
			e.printStackTrace();
			responseApp.setStatus(Constantes.ERROR);
			responseApp.setMensaje("ocurrio un error en el servidor "+e.getMessage());
		}finally {			
			log.info("Status :: "+responseApp.getStatus()+" | "+responseApp.getMensaje());
		}				
		return new Gson().toJson(responseApp);
	}
	
}
