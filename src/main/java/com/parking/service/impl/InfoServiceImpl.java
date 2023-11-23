package com.parking.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.parking.models.CobroModel;
import com.parking.repository.interfaces.InfoRepositoryInterface;
import com.parking.service.interfaces.InfoInterface;
import com.parking.utils.ResponseApp;

@Service
public class InfoServiceImpl  implements InfoInterface{

	@Autowired
	InfoRepositoryInterface infoRepositoryInterface;

	
	@Override
	public ResponseApp getInfo(String fecha,int idUsuario,int idEstacionamiento) {		
		 ResponseApp responseApp= new ResponseApp();
		     try {
		    	 responseApp=infoRepositoryInterface.getInfo(fecha,idUsuario,idEstacionamiento);		    	 
		    	 if (responseApp==null) {	
		    		  responseApp= new ResponseApp();
		    		 responseApp.setMensaje("No se recupero informacion de la base nde datos");
		    		 responseApp.setStatus("false");
				 }								
			} catch (Exception e) {				
				  responseApp.setMensaje(e.getMessage());
	    		 responseApp.setStatus("false");
			}
		return responseApp;
	}

	@Override
	public CobroModel getTarifa(int idEstacionamiento) throws Exception {
		CobroModel objCobro=null;
		try {
			 objCobro=infoRepositoryInterface.getTarifa(idEstacionamiento);
			if (objCobro == null) {
					return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());			
		}
		
		return objCobro;
	}
	

}
