package com.parking.controller;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;
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
	
	@GetMapping("/save")
	public @ResponseBody String savePension( @RequestBody String ParametersRequest) {

		 ResponseApp responseApp= new ResponseApp();
		try {
			if (ParametersRequest!=null) {					
				 
				pensionadoInterface.savePensionado(ParametersRequest);
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
}
