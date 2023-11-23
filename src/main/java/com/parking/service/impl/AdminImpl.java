package com.parking.service.impl;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.parking.repository.interfaces.AdminRepositoryInterface;
import com.parking.service.interfaces.AdminInterface;
import com.parking.utils.Constantes;
import com.parking.utils.ResponseApp;
import com.parking.utils.Utileria;

@Service
public class AdminImpl implements AdminInterface{
	private Logger log = Logger.getLogger(AdminImpl.class);
	@Autowired
	AdminRepositoryInterface adminRepository;

	@Autowired
	Utileria utileria;
	
	@Autowired
	BCryptPasswordEncoder encoder;
	
	@Override
	public ResponseApp getEstacionamientos() {
		 ResponseApp responseApp= new ResponseApp();
		
		       try {
				JsonArray lts= adminRepository.getEstacionamientos();
				if (lts.size()<1) {
					responseApp.setStatus(Constantes.ERROR);
					responseApp.setMensaje("No se recuperaron Registros");
				} else {
					responseApp.setStatus(Constantes.SUCCESS);
					responseApp.setMensaje("Se recupero informacion de manera corecta");
					responseApp.setObject(new Gson().toJson(lts));
				}
			} catch (Exception e) {				
				e.printStackTrace();
				responseApp.setStatus(Constantes.ERROR);
				responseApp.setMensaje("No se recuperaron Registros");
			}
		       
		return responseApp;
	}
	@Override
	public ResponseApp getEmpleados() {
		 ResponseApp responseApp= new ResponseApp();
	       try {
			JsonArray lts= adminRepository.getEmpleados();
			if (lts.size()<1) {
				responseApp.setStatus(Constantes.ERROR);
				responseApp.setMensaje("No se recuperaron Registros");
			} else {
				responseApp.setStatus(Constantes.SUCCESS);
				responseApp.setMensaje("Se recupero informacion de manera corecta");
				responseApp.setObject(new Gson().toJson(lts));
			}
		} catch (Exception e) {				
			e.printStackTrace();
			responseApp.setStatus(Constantes.ERROR);
			responseApp.setMensaje("No se recuperaron Registros");
		}
	       
	return responseApp;
}
	@Override
	public ResponseApp saveEstacionamiento(JsonObject estacionamiento) {
		String errores = "";	
		 ResponseApp responseApp= new ResponseApp();
		try {
			if (estacionamiento.isJsonNull()) {
				responseApp.setStatus(Constantes.ERROR);
				responseApp.setMensaje("No Se recibieron Dtaos para guardar en Estacionamiento");
			} else {
				Set<Entry<String, JsonElement>> entrySet = estacionamiento.entrySet(); // pasa de json a una coleccion
																						// llave valor
				for (Map.Entry<String, JsonElement> entry : entrySet) {					
					if (estacionamiento.get(entry.getKey()).getAsString().isEmpty()
							|| estacionamiento.get(entry.getKey()).getAsString().length() < 1
							|| estacionamiento.get(entry.getKey()).getAsString() == null) {
						log.info("existe un llave vacia " + entry.getKey() + estacionamiento.get(entry.getKey()));
						errores += (entry.getKey().toString() + estacionamiento.get(entry.getKey()).getAsString());
					} else {
						if (entry.getKey().equals("fraccion1") || entry.getKey().equals("fraccion2")
								|| entry.getKey().equals("fraccion3") || entry.getKey().equals("fraccion4")
								|| entry.getKey().equals("hora") || entry.getKey().equals("pensionAutoChico")
								|| entry.getKey().equals("pensionAutoGrande")) {

							if (! utileria.isDouble(estacionamiento.get(entry.getKey()).getAsString())) {														
								errores += " no es numero " + (estacionamiento.get(entry.getKey()).getAsString());
							}
						}
					}
				}
				if (estacionamiento.get("tipoCobro").getAsString().equals("a") ||estacionamiento.get("tipoCobro").getAsString().equals("h*p") ) {					
					if (!utileria.isNumero( estacionamiento.get("horasPromocion").getAsString())) {
						errores += "no es numero" + estacionamiento.get("horasPromocion").getAsString();
					}
					if ( !utileria.isDouble( estacionamiento.get("importePromocion").getAsString())) {
						errores += "no es numero" + estacionamiento.get("importePromocion").getAsString();
					}
				}				
				else {
					estacionamiento.addProperty("horasPromocion", 0);
					estacionamiento.addProperty("importePromocion", 0);
				}						
				/*****************************************************************/				
		        if (!errores.isEmpty() || errores.length()>1) {	        	
		        	responseApp.setStatus(Constantes.ERROR);
					responseApp.setMensaje("Un dato viene vacio, Favor de capturar : "+errores );
				} else {					
				
					if (estacionamiento.has("idEstacionamiento")) {					
						if (utileria.isNumero(estacionamiento.get("idEstacionamiento").getAsString())) {						
							if (estacionamiento.get("idEstacionamiento").getAsInt()>0) {
								if(adminRepository.updateEstacionamiento(estacionamiento)) {									  
									responseApp.setStatus(Constantes.SUCCESS);
									responseApp.setMensaje("Se ha actualizado el Estacionamiento correctamente ");
								 }else {						 
									 responseApp.setStatus(Constantes.ERROR);
									 responseApp.setMensaje("No ha actualizado el Estacionamiento correctamente ");
								 }
							} else {
								responseApp.setStatus(Constantes.ERROR);
								responseApp.setMensaje("El id recibido es menor a 1 por lo cual no existe informacion ");
							}
							
						} else {
							responseApp.setStatus(Constantes.ERROR);
							responseApp.setMensaje("El id no es numero ");
						}
					} else {
						if(adminRepository.saveEstacionamiento(estacionamiento)) {
							responseApp.setStatus(Constantes.SUCCESS);
							responseApp.setMensaje("Se ha registrado el Estacionamiento correctamente ");
						 }else {						 
							 responseApp.setStatus(Constantes.ERROR);
							 responseApp.setMensaje("No ha registrado el Estacionamiento correctamente ");
						 }
					}

					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			responseApp.setStatus(Constantes.ERROR);
			responseApp.setMensaje("Error : "+e.getMessage());
		}

		return responseApp;
	}
	@Override
	public ResponseApp getRoles() {
		 ResponseApp responseApp= new ResponseApp();
	       try {
			JsonArray lts= adminRepository.getRoles();
			if (lts.size()<1) {
				responseApp.setStatus(Constantes.ERROR);
				responseApp.setMensaje("No se recuperaron Registros");
			} else {
				responseApp.setStatus(Constantes.SUCCESS);
				responseApp.setMensaje("Se recupero informacion de manera corecta");
				responseApp.setObject(new Gson().toJson(lts));
			}
		} catch (Exception e) {				
			e.printStackTrace();
			responseApp.setStatus(Constantes.ERROR);
			responseApp.setMensaje("No se recuperaron Registros");
		}
	       
	return responseApp;
}
	@Override
	public ResponseApp getusuarios() {
		 ResponseApp responseApp= new ResponseApp();
		JsonArray lts= new JsonArray();
	       try {
			 lts= adminRepository.getUsuarios();
			if (lts.size()<1) {
				responseApp.setStatus(Constantes.ERROR);
				responseApp.setMensaje("No se recuperaron Registros,favor de crear uno,si desea liberar un usuario en lista de usuario click en  eliminar ");
				responseApp.setObject(new Gson().toJson(lts));
			} else {
				responseApp.setStatus(Constantes.SUCCESS);
				responseApp.setMensaje("Se recupero informacion de manera corecta");
				responseApp.setObject(new Gson().toJson(lts));
			}
		} catch (Exception e) {				
			e.printStackTrace();
			responseApp.setStatus(Constantes.ERROR);
			responseApp.setMensaje("No se recuperaron Registros");
		}
	       
	return responseApp;
}
	@Override
	public ResponseApp saveEmpleado(JsonObject empleado) {
		 ResponseApp responseApp= new ResponseApp();
		String errores = "";		
		try {
			if (empleado.isJsonNull()) {
				responseApp.setStatus(Constantes.ERROR);
				responseApp.setMensaje("No Se recibieron Dtaos para guardar en Estacionamiento");
			} else {
				Set<Entry<String, JsonElement>> entrySet = empleado.entrySet(); // pasa de json a una coleccion
																						// llave valor
				for (Map.Entry<String, JsonElement> entry : entrySet) {					
					if (empleado.get(entry.getKey()).getAsString().isEmpty()
							|| empleado.get(entry.getKey()).getAsString().length() < 1
							|| empleado.get(entry.getKey()).getAsString() == null) {
						log.info("existe un llave vacia " + entry.getKey() + empleado.get(entry.getKey()));
						errores += (entry.getKey().toString() + empleado.get(entry.getKey()).getAsString());
					}
				}
				
				if (empleado.get("password").getAsString().equals(empleado.get("confirmarPassword").getAsString())) {
					
					empleado.addProperty("pass", encoder.encode(empleado.get("password").getAsString()));
					empleado.remove("password");
					empleado.remove("confirmarPassword");
				}				
				else {
					 errores+=", su password  no coiciden, verfica de nuevo";
				}						
				/*****************************************************************/				
		        if (!errores.isEmpty() || errores.length()>1) {	        	
		        	responseApp.setStatus(Constantes.ERROR);
					responseApp.setMensaje("Un dato viene vacio, Favor de capturar : "+errores );
				} else {
					
					 if (empleado.get("rol").getAsString().equals("OPERADOR")) {
							if(adminRepository.saveEmpleado(empleado)) {
								responseApp.setStatus(Constantes.SUCCESS);
								responseApp.setMensaje("Se ha registrado el Empleado correctamente ");
							 }else {						 
								 responseApp.setStatus(Constantes.ERROR);
								 responseApp.setMensaje("No ha registrado el Empleado correctamente ");
							 }
					} else {
						if(adminRepository.saveEmpleadoAdministrador(empleado)) {
							responseApp.setStatus(Constantes.SUCCESS);
							responseApp.setMensaje("Se ha registrado el Empleado correctamente ");
						 }else {						 
							 responseApp.setStatus(Constantes.ERROR);
							 responseApp.setMensaje("No ha registrado el Empleado correctamente ");
						 }
					}
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			responseApp.setStatus(Constantes.ERROR);
			responseApp.setMensaje("Error : "+e.getMessage());
		}

		return responseApp;
	}
	
	@Override
	public JsonObject getEstacionamientoid(String id) throws Exception {
		JsonObject objId=null;
		try {
			if(utileria.isNumero(id)) {				
				objId=adminRepository.getestacionamientoId(id);
				if(objId!=null) {					
					return objId;
				}
			}
			else {
				throw new Exception("No es numero ");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}

		return null;
	}
	@Override
	public boolean eliminarEstacionamiento(String id) throws Exception {
	
		try {
			if(utileria.isNumero(id)) {					
				if(adminRepository.eliminarEstacionamiento(id)) {
					return true;
				}
			}
			else {
				throw new Exception("No es numero ");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		return false;
	}
	@Override
	public boolean eliminarEmpoleado(String id) throws Exception {
	
		try {
			if(utileria.isNumero(id)) {	
				
				if(adminRepository.eliminarEmpleado(id)) {
					return true;
				}
			}
			else {
				throw new Exception("No es numero ");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		return false;
	}

}
