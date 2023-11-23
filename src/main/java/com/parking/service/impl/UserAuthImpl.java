package com.parking.service.impl;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.parking.models.UserModel;
import com.parking.repository.interfaces.UserAuthRepositoryInterface;
import com.parking.service.interfaces.UserAuthInterface;
import com.parking.utils.Constantes;
import com.parking.utils.ResponseApp;

@Service
public class UserAuthImpl  implements UserAuthInterface{
	
	private Logger log = Logger.getLogger(UserAuthImpl.class);

	@Autowired
	UserAuthRepositoryInterface userRepository;
	
	@Override
	public ResponseApp getAutenthication(String userName) {
		UserModel userModel = null;
		 ResponseApp responseApp= new ResponseApp();
		try {
			if (!userName.isEmpty() || userName != null) {

				userModel = userRepository.getAuthentication(userName);

				if (userModel != null) {
					log.info("se recupero usuario con username :: " + userName);
					userModel.setFecha(Constantes.FORMATO_FECHA.format(new Date()));

					JsonObject objSession = new Gson().toJsonTree(userModel).getAsJsonObject();
					responseApp.setObject(new Gson().toJson(objSession));
					responseApp.setStatus(Constantes.SUCCESS);
					responseApp.setMensaje("Se recupero los datos del usuario");
				} else {
					responseApp.setStatus(Constantes.ERROR);
					responseApp.setMensaje("No se recupero ningun usuario con :: "+userName);	
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			responseApp.setStatus(Constantes.ERROR);
			responseApp.setMensaje("getAutenthication :: "+e.getMessage());	
		}
		finally {
			log.info(responseApp);
		}
		


		return responseApp;
	}



}
