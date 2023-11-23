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
import com.parking.models.CobroModel;
import com.parking.models.UserModel;
import com.parking.service.interfaces.AdminInterface;
import com.parking.utils.Constantes;
import com.parking.utils.ResponseApp;

@Controller
@RequestMapping("/admin")
public class AdminController {
	private Logger log = Logger.getLogger(AdminController.class);

	@Autowired
	AdminInterface adminInterface;
	
	
	@GetMapping("/home")
	public String home(HttpSession session, RedirectAttributes redirectAttributes ) {
		
		if (session.getAttribute("user")==null) {
			 redirectAttributes.addFlashAttribute("sesion_error", "No se encontraron datos del usuario");
			 return "redirect:/login1";
		}else {
			 return "adminHome";
		}
	}
	
	
	@GetMapping("/getEstacionamientos")
	public @ResponseBody String listEstacionamiento() {	
		 ResponseApp responseApp= new ResponseApp();
		responseApp=null;
		try {
		  responseApp=	adminInterface.getEstacionamientos();
		} catch (Exception e) {
			e.printStackTrace();
			responseApp.setStatus(Constantes.ERROR);
			responseApp.setMensaje(e.getMessage());
		}
		finally {			
		}
				
		return new Gson().toJson(responseApp);
	}
	@GetMapping("/getEmpleados")
	public @ResponseBody String listEmpleados() {
		 ResponseApp responseApp= new ResponseApp();
		try {
		  responseApp=	adminInterface.getEmpleados();
		} catch (Exception e) {
			e.printStackTrace();
			responseApp.setStatus(Constantes.ERROR);
			responseApp.setMensaje(e.getMessage());
		}
		finally {				
		}
				
		return new Gson().toJson(responseApp);
	}
	
	
	 
	@PostMapping("/saveEstacionamiento")	
	public @ResponseBody String guardarAuto(  @RequestBody String ParametersRequest, HttpSession s) throws Exception {
		 ResponseApp responseApp= new ResponseApp();
		log.info("Inicia Guardar Estacionamiento || " +ParametersRequest);
		JsonObject estacionamiento= new JsonObject();
		try {			
			if (ParametersRequest.isEmpty() || ParametersRequest==null) {
				responseApp.setStatus(Constantes.ERROR);
				responseApp.setMensaje("No se ha recibido datos, verifique de nuevo..."+ParametersRequest);
			} else {
				estacionamiento =new Gson().fromJson(ParametersRequest, JsonObject.class);
				responseApp=adminInterface.saveEstacionamiento(estacionamiento);		
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
	
	@GetMapping("/getRoles")
	public @ResponseBody String ltsRoles() {
		 ResponseApp responseApp= new ResponseApp();
	
		try {
		  responseApp=	adminInterface.getRoles();
		} catch (Exception e) {
			e.printStackTrace();
			responseApp.setStatus(Constantes.ERROR);
			responseApp.setMensaje(e.getMessage());
		}
				
		return new Gson().toJson(responseApp);
	}
	
	@GetMapping("/getUsuarios")
	public @ResponseBody String ltsUsuario() {	
		 ResponseApp responseApp= new ResponseApp();
		try {
		  responseApp=	adminInterface.getusuarios();
		} catch (Exception e) {
			e.printStackTrace();
			responseApp.setStatus(Constantes.ERROR);
			responseApp.setMensaje(e.getMessage());
		}
				
		return new Gson().toJson(responseApp);
	}
	
	@PostMapping("/saveEmpleado")	
	public @ResponseBody String guardarEmpleado(  @RequestBody String ParametersRequest, HttpSession s) throws Exception {
		log.info("Inicia Guardar Estacionamiento || " +ParametersRequest);
		JsonObject empleado= new JsonObject();
		 ResponseApp responseApp= new ResponseApp();
		try {			
			if (ParametersRequest.isEmpty() || ParametersRequest==null) {
				responseApp.setStatus(Constantes.ERROR);
				responseApp.setMensaje("No se ha recibido datos, verifique de nuevo..."+ParametersRequest);
			} else {
				empleado =new Gson().fromJson(ParametersRequest, JsonObject.class);
				responseApp=adminInterface.saveEmpleado(empleado);		
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
	
	@GetMapping("/getestacionamientoid/{id}")
	public @ResponseBody String getestacionamientoid( @PathVariable("id")  String id, HttpSession session) {
		log.info("inicia || estacionamiento id recibido :: "+id);
		 ResponseApp responseApp= new ResponseApp();
		 JsonObject odjId= null;
		try {
			if (id.isEmpty() ||  id ==null  ) {
				
				responseApp.setStatus(Constantes.ERROR);
				responseApp.setMensaje("No se ha recibido ningun id");
			} else {				
				odjId=adminInterface.getEstacionamientoid(id);
				if (odjId!=null) {
					responseApp.setStatus(Constantes.SUCCESS);
					responseApp.setMensaje("Se recupero datos del estacionamiento");
					responseApp.setObject(new Gson().toJson(odjId));
				} else {
					responseApp.setStatus(Constantes.ERROR);
					responseApp.setMensaje("No se recupero los datos del estacionamiento");
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
	
	@GetMapping("/eliminarEstacionamiento/{id}")
	public @ResponseBody String elemininarEstacionamiento( @PathVariable("id")  String id, HttpSession session) {
		log.info("inicia || estacionamiento id recibido :: "+id);
		 ResponseApp responseApp= new ResponseApp();
		try {
			if (id.isEmpty() ||  id ==null  ) {
				
				responseApp.setStatus(Constantes.ERROR);
				responseApp.setMensaje("No se ha recibido ningun id");
			} else {				
				
				if (adminInterface.eliminarEstacionamiento(id)) {
					responseApp.setStatus(Constantes.SUCCESS);
					responseApp.setMensaje("El estacionamiento  se ha eliminado de manera correcta");
				} else {
					responseApp.setStatus(Constantes.ERROR);
					responseApp.setMensaje("No se recupero los datos del estacionamiento");
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
	
	
	@GetMapping("/eliminarEmpleado/{id}")
	public @ResponseBody String eliminarEmpleado( @PathVariable("id")  String id, HttpSession session) {
		log.info("inicia || estacionamiento id recibido :: "+id);
		 ResponseApp responseApp= new ResponseApp();
		try {
			if (id.isEmpty() ||  id ==null  ) {
				
				responseApp.setStatus(Constantes.ERROR);
				responseApp.setMensaje("No se ha recibido ningun id");
			} else {	
				 UserModel use= null;
				   if (session.getAttribute("user")!=null) {
					
					    use= (UserModel) session.getAttribute("user");
					    
					    if (id.equals(String.valueOf(use.getIdEmpleado()))) {
					    	responseApp.setStatus(Constantes.ERROR);
							responseApp.setMensaje("El empleado  que desea eliminar es el mismo que esta en la sesison ");
						} else {
							if (adminInterface.eliminarEmpoleado(id)) {
								responseApp.setStatus(Constantes.SUCCESS);
								responseApp.setMensaje("El empleado  se ha eliminado de manera correcta");
							} else {
								responseApp.setStatus(Constantes.ERROR);
								responseApp.setMensaje("No se recupero los datos del empleado");
							}
						}

				}else {
					responseApp.setStatus(Constantes.ERROR);
					responseApp.setMensaje("No se recupero el id del empleado  de la session");
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
