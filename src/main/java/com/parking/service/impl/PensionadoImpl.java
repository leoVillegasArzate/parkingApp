package com.parking.service.impl;

import org.apache.log4j.Logger;
import org.hibernate.validator.internal.util.privilegedactions.NewInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.parking.repository.interfaces.PensionasoRepositoryInterface;
import com.parking.service.interfaces.PensionadoInterface;
import com.parking.utils.Constantes;
import com.parking.utils.ResponseApp;
import com.parking.utils.Utileria;

@Service
public class PensionadoImpl  implements PensionadoInterface{
	private Logger log = Logger.getLogger(PensionadoImpl.class);
	
	@Autowired
	PensionasoRepositoryInterface pensionasoRepositoryInterface;
	@Autowired
	Utileria utileria;
	
	@Override
	public ResponseApp getPensionImporte(int idEstacionamiento) {
		
		ResponseApp responseApp = new ResponseApp();
		JsonObject obImportePension= null;
		try {
			obImportePension=pensionasoRepositoryInterface.getImportePensionado(idEstacionamiento);
			if (obImportePension!=null) {
				responseApp.setStatus(Constantes.SUCCESS);
				responseApp.setMensaje("Se recupero importe de pension ");
				responseApp.setObject(new Gson().toJson(obImportePension));
			} else {
				responseApp.setStatus(Constantes.ERROR);
				responseApp.setMensaje("No se recupero importe de pension ");
			}
		} catch (Exception e) {
			e.printStackTrace();
			responseApp.setStatus(Constantes.ERROR);
			responseApp.setMensaje(e.getMessage());
		}
		return responseApp;
	}

	@Override
	public ResponseApp savePensionado(String pensionado) {
		log.info("inicia validacion de datos");
		String errores="";
		ResponseApp responseApp = new ResponseApp();
		JsonObject objPension=null;
		
		try {
			
			objPension= new Gson().fromJson(pensionado, JsonObject.class); 
			 
			errores=utileria.validaJson(objPension);			
	        if (!errores.isEmpty() || errores.length()>1) {	        	
	        	responseApp.setStatus(Constantes.ERROR);
				responseApp.setMensaje("Un dato viene vacio, Favor de capturar : "+errores );
				return responseApp;
			 }else {
				 if (!utileria.isDouble(objPension.get("otraCantidad").getAsString())) {
					 responseApp.setStatus(Constantes.ERROR);
				     responseApp.setMensaje("No es numero "+objPension.get("otraCantidad").getAsString() );
				    }				 
				 if (!utileria.isTelefono(objPension.get("otraCantidad").getAsString())) {
					 responseApp.setStatus(Constantes.ERROR);
				     responseApp.setMensaje("No es importe correcto "+objPension.get("otraCantidad").getAsString() );
				 	return responseApp;
				}
				 if (!utileria.rfc(objPension.get("rfc").getAsString())) {
					 responseApp.setStatus(Constantes.ERROR);
				     responseApp.setMensaje("No es un rfc correcto "+objPension.get("otraCantidad").getAsString() );
				 	return responseApp;
				}
				 
				 if (!utileria.rfc(objPension.get("correo").getAsString())) {
					 responseApp.setStatus(Constantes.ERROR);
				     responseApp.setMensaje("No es un correo correcto "+objPension.get("otraCantidad").getAsString() );
				 	return responseApp;
				}
				 
				 if (pensionasoRepositoryInterface.savePensionado(objPension)) {
					
				}
			 }
		} catch (Exception e) {
			e.printStackTrace();
			responseApp.setStatus(Constantes.ERROR);
			responseApp.setMensaje(e.getMessage());
		}
		

		return responseApp;
	}

}
