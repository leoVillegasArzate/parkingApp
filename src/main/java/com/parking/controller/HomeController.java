package com.parking.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parking.models.CobroModel;
import com.parking.models.UserModel;
import com.parking.service.interfaces.InfoInterface;
import com.parking.service.interfaces.UserAuthInterface;
import com.parking.utils.ResponseApp;

@Controller
public class HomeController {
	
	private Logger log = Logger.getLogger(HomeController.class);	
	
	@Autowired
	UserAuthInterface sessionService;
	@Autowired
	InfoInterface infoInterface;

	@GetMapping("/")
	public String Home( Authentication authentication,HttpServletRequest request , RedirectAttributes redirectAttributes )  {	
		 ResponseApp responseApp= new ResponseApp();
		try {
			
			if (  request.getSession().getAttribute("user")!=null) {
					    return "redirect:/app";
			} else {				
				log.info(authentication.getName());
				   CobroModel cobro=null;
				   responseApp=sessionService.getAutenthication(authentication.getName());
				   if (responseApp.getStatus().equals("true")) {							 
					   ObjectMapper objectMapper = new ObjectMapper();
					   UserModel user=objectMapper.readValue(responseApp.getObject(), UserModel.class);
					   log.info("datos class"+user.toString());	
					   
					   if (user.getRol().equals("OPERADOR")) {
						   cobro= infoInterface.getTarifa(user.getIdEstacionamiento());
						    if (cobro!=null) {							
								   request.getSession().setAttribute("user", user);						  
								   request.getSession().setAttribute("cobro", cobro);
								   return "redirect:/app";
							} else {
								 redirectAttributes.addFlashAttribute("sesion_error", "No se recupero Sistema de cobro "+authentication.getName());
								 return "redirect:/login1";
							}
					   }
					   else {
						   request.getSession().setAttribute("user", user);	
						   return"redirect:/admin/home";
					   }
					} else {
						 redirectAttributes.addFlashAttribute("sesion_error", "No se encontraron datos del usuario :: "+authentication.getName());
						 return "redirect:/login1";
					} 
			}
		} catch (Exception e) {
			e.printStackTrace();
			 redirectAttributes.addFlashAttribute("sesion_error", "Error al recuperar informacion del usuario :: "+authentication.getName());
			 return "redirect:/login1";
		}      	
	}
	
	@GetMapping("/app" )
	public String app(HttpSession session, RedirectAttributes redirectAttributes ) {
		
		if (  session.getAttribute("user")!=null) {
			
			UserModel user=(UserModel) session.getAttribute("user");
			
			if (user.getRol().equals("OPERADOR")) {
				return "home";
			} else {
				  return"redirect:/admin/home";
			}
		} else {
			
			return "redirect:/login1";
		}
		
	}
	
	@GetMapping("/acceso-denegado" )
	public String accesoDenegado(RedirectAttributes redirectAttributes ) {
		
		 redirectAttributes.addFlashAttribute("sesion_error", "No tienes permisos para acceder a esta url.favor de ingresar de nuevo");
		 return "redirect:/login1";
    		
	}
	 
	@GetMapping("/login1" )
	public String mostrarLogin() {

	return "formLogin";
	}
	
	@GetMapping("/logout1")
	public String logout(HttpServletRequest request,HttpServletResponse response){
		
	System.out.println("voy de salida ");
	
	Cookie cookie = new Cookie("JSESSIONID", null);
    cookie.setPath(request.getContextPath());
    cookie.setMaxAge(0); // Tiempo de vida negativo para borrar la cookie
    response.addCookie(cookie);
    
    
	SecurityContextLogoutHandler logoutHandler =new SecurityContextLogoutHandler();
	logoutHandler.logout(request, response, null);
	 return "formLogin"; 
	}
	
}
