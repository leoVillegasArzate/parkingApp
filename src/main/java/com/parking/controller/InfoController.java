package com.parking.controller;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.parking.models.UserModel;
import com.parking.service.interfaces.InfoInterface;
import com.parking.utils.ResponseApp;

@Controller
@RequestMapping("/info")
public class InfoController {
	private Logger log = Logger.getLogger(InfoController.class);
	
	@Autowired
	InfoInterface infoInterface;

	
	@GetMapping("/data")	
	public @ResponseBody String getInfo(HttpSession session) {		
		 ResponseApp responseApp= new ResponseApp();
		try {
			 UserModel user =(UserModel) session.getAttribute("user");
			 if (user!=null) {
			  responseApp=infoInterface.getInfo(user.getFecha(),user.getIdEmpleado(),user.getIdEstacionamiento());
			  responseApp.setMensaje("Se recuperaron  lo datos correctamente");
			} else {
				responseApp.setStatus("false");
				responseApp.setMensaje("No hay Informacion del usario ingresado");				
			}
		} catch (Exception e) {			
			e.printStackTrace();
			responseApp.setStatus("false");
			responseApp.setMensaje(e.getMessage());		
		}

		return new Gson().toJson(responseApp);
	}

	
//	@GetMapping("/cobro")	
//	public @ResponseBody String getCobro(HttpSession session) {
//		try {
//
//			UserModel user = (UserModel) session.getAttribute("user");
//
//			responseApp.setMensaje("Se recuperaron  lo datos correctamente");
//			if (user != null) {
//				responseApp = infoInterface.getTarifa(user.getIdEstacionamiento());
//
//				if (responseApp.getStatus().equals("true")) {
//					responseApp.setMensaje("Se recupero sistema de cobro");
//				} else {
//					responseApp.setStatus("false");
//					responseApp.setMensaje("No hay Informacion sistema de cobro");
//				}
//			} else {
//				responseApp.setStatus("false");
//				responseApp.setMensaje("No hay Informacion del usario ingresado");
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			responseApp.setStatus("false");
//			responseApp.setMensaje(e.getMessage());
//		}finally {
//			log.info(responseApp);
//		}
//
//		return new Gson().toJson(responseApp);
//	}
}
