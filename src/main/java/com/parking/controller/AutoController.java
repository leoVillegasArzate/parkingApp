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
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.parking.models.CobroModel;
import com.parking.service.interfaces.AutoInterface;
import com.parking.utils.Constantes;
import com.parking.utils.ResponseApp;

@Controller
@RequestMapping("/auto")
public class AutoController {
	private Logger log = Logger.getLogger(AutoController.class);
	
	 @Autowired
	 AutoInterface autoInterface;
	 
	 
	@PostMapping("/guardar")	
	public @ResponseBody String guardarAuto(  @RequestBody String ParametersRequest, HttpSession s) throws Exception {
		log.info("Inicia Guardar Auto || " +ParametersRequest);
		 ResponseApp responseApp= new ResponseApp();
		JsonObject auto= new JsonObject();		
		try {			
			if (ParametersRequest.isEmpty() || ParametersRequest==null) {
				responseApp.setStatus(Constantes.ERROR);
				responseApp.setMensaje("No se ha recibido datos, verifique de nuevo..."+ParametersRequest);
			} else {
				auto =new Gson().fromJson(ParametersRequest, JsonObject.class);
				responseApp=autoInterface.save(auto);
				s.setAttribute("s", auto);				
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
	
	@GetMapping("/buscarSalida/{folio}")
	public @ResponseBody String buscarSalida( @PathVariable("folio")  String folio, HttpSession session) {
		log.info("inicia || Folio recibido :: "+folio);
		 ResponseApp responseApp= new ResponseApp();
		try {
			if (folio.isEmpty() ||  folio ==null  ) {
				
				responseApp.setStatus(Constantes.ERROR);
				responseApp.setMensaje("No se ha recibido ningun folio");
			} else {
				
				if (session.getAttribute("cobro")!=null) {
					CobroModel cobro=(CobroModel) session.getAttribute("cobro");
					responseApp=autoInterface.searchFolio(folio,cobro,false);	
				} else {
					responseApp.setStatus(Constantes.ERROR);
					responseApp.setMensaje("No se recupero el sistema de cobro");
				}
			
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
	@GetMapping("/getListaAutos/{idEstacionamiento}")
	public @ResponseBody String getListaAutos( @PathVariable("idEstacionamiento")  String idEstacionamiento, HttpSession session) {
		log.info("Inicia recuperacion de  lista autos ");
		 ResponseApp responseApp= new ResponseApp();
		try {
		      responseApp=autoInterface.getListaAuto(Integer.parseInt(idEstacionamiento));	
		} catch (Exception e) {
			e.printStackTrace();
			responseApp.setStatus(Constantes.ERROR);
			responseApp.setMensaje("ocurrio un error en el servidor "+e.getMessage());
		}finally {			
			log.info("Status :: "+responseApp.getStatus()+" | "+responseApp.getMensaje());
		}				
		return new Gson().toJson(responseApp);
	}
	
	
	
	@PostMapping("/guardarSalida")	
	public @ResponseBody String guardarAutoSalida(@RequestBody String ParametersRequest) throws Exception {
		log.info("Inicia Guardar Auto || " +ParametersRequest);
		JsonObject auto= new JsonObject();		
		 ResponseApp responseApp= new ResponseApp();
		try {			
			if (ParametersRequest.isEmpty() || ParametersRequest==null) {
				responseApp.setStatus(Constantes.ERROR);
				responseApp.setMensaje("No se ha recibido datos, verifique de nuevo..."+ParametersRequest);
			} else {
				auto =new Gson().fromJson(ParametersRequest, JsonObject.class);
				responseApp=autoInterface.saveSalida(auto);						
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
	
	
	@GetMapping("/buscarSalidaBoletoPerdido/{folio}")
	public @ResponseBody String buscarSalidaBoletoPerdido( @PathVariable("folio")  String folio, HttpSession session) {
		log.info("inicia || Folio recibido boleto perdido :: "+folio);
		 ResponseApp responseApp= new ResponseApp();
		try {
			if (folio.isEmpty() ||  folio ==null  ) {
				
				responseApp.setStatus(Constantes.ERROR);
				responseApp.setMensaje("No se ha recibido ningun folio");
			} else {
				
				if (session.getAttribute("cobro")!=null) {
					CobroModel cobro=(CobroModel) session.getAttribute("cobro");
					responseApp=autoInterface.searchFolio(folio,cobro,true);	
				} else {
					responseApp.setStatus(Constantes.ERROR);
					responseApp.setMensaje("No se recupero el sistema de cobro");
				}
			
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
