package com.parking.service.impl;

import java.sql.SQLException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.parking.models.CobroModel;
import com.parking.repository.interfaces.AutoRepositoryInterfazce;
import com.parking.service.interfaces.AutoInterface;
import com.parking.utils.Constantes;
import com.parking.utils.ResponseApp;
import com.parking.utils.Utileria;


@Service
public class AutoImpl  implements AutoInterface{
	private Logger log = Logger.getLogger(AutoImpl.class);
	@Autowired
	AutoRepositoryInterfazce autoRepositoryInterfazce;	

	@Autowired
	Utileria utileria;
	
	@Override
	public ResponseApp save(JsonObject auto) throws SQLException {
		log.info("inicia SaveService auto");
		 ResponseApp responseApp= new ResponseApp();
		try {
			if ( auto.has("placa" ) && auto.has("color" ) && auto.has("marca" )) {
				if (auto.get("placa").toString()!=null  && auto.get("color").toString()!=null&& auto.get("marca").toString()!=null  ) {
					auto.addProperty("fechaEntrada", utileria.getFecha());
					auto.addProperty("status", Constantes.PENDIENTE);				

					 if (autoRepositoryInterfazce.save(auto)) {
						 responseApp.setStatus(Constantes.SUCCESS);
						 responseApp.setMensaje("Se ha registrado el auto "+auto.get("marca").getAsString()+" con placas "+ auto.get("placa").toString()  +"correctamente");	
					 }else {
						    responseApp.setStatus(Constantes.ERROR);
							responseApp.setMensaje("Ocurrio un error al guardar el auto, favor de comunicarse con el administrador");
					 }
																	
				} else {
					responseApp.setStatus(Constantes.ERROR);
					responseApp.setMensaje("Los datos solicitado estan vacios, debe tener color,placa,marca \n los datos recibidos son "+auto);
				}				
			}else {				
				responseApp.setStatus(Constantes.ERROR);
				responseApp.setMensaje("Los datos solicitado estan imcompletos, debe tener color,placa,marca \n los datos recibidos son "+auto);
			}
		} catch (Exception e) {
			e.printStackTrace();
			responseApp.setStatus(Constantes.ERROR);
			responseApp.setMensaje(e.getMessage());
		}

		return  responseApp;
	}

	@Override
	public ResponseApp searchFolio(String folio,CobroModel cobro,boolean CobroPerdido) {
		log.info("inicia searchService auto folio :: "+folio);
		JsonObject autoSalida = new JsonObject();	
		JsonObject auto = new JsonObject();	
		 ResponseApp responseApp= new ResponseApp();
		 JsonObject detalle= new JsonObject();
				 
		auto= new Gson().fromJson(folio, JsonObject.class);
		try {
			if(utileria.isNumero(auto.get("folio").getAsString())) {
				
				autoSalida=autoRepositoryInterfazce.searchFolio(auto);
				if (autoSalida==null) {
					responseApp.setStatus(Constantes.ERROR);
					responseApp.setMensaje("No se encontro auto con el folio recibido, verifique de nuevo");
				}else {
					 
					autoSalida.addProperty("fechaSalida", utileria.getFecha());
					long segundos=utileria.getSegundos(autoSalida.get("fechaEntrada").getAsString(), autoSalida.get("fechaSalida").getAsString());
					autoSalida.addProperty("consumo", utileria.getEstancia(segundos));
					autoSalida.addProperty("consumoFraccion", utileria.getEstanciaFraccion(segundos));
					detalle=utileria.getcobro(segundos,cobro,autoSalida.get("tipoCobro").getAsString(),CobroPerdido);
					autoSalida.addProperty("Cobro", detalle.get("importe").getAsDouble());
					autoSalida.addProperty("modoPago", detalle.get("cobro").getAsString());
					responseApp.setObject(new Gson().toJson(autoSalida));
					responseApp.setStatus(Constantes.SUCCESS);
					responseApp.setMensaje("Mostrando datos de salida del auto");
				}								
			}else {
				responseApp.setStatus(Constantes.ERROR);
				responseApp.setMensaje("El folio recibido no es numero,verificar de nuevo...");
			}
		} catch (Exception e) {
			e.printStackTrace();
			responseApp.setStatus(Constantes.ERROR);
			responseApp.setMensaje("ocurrio un error en busqueda de auto "+e.getMessage());
		}
	
		return responseApp;
	}

	@Override
	public ResponseApp saveSalida(JsonObject auto) {
		log.info("Inicia guardar salida");
		String errores="";
		 ResponseApp responseApp= new ResponseApp();
		try {			
			log.info("Datos a guardar :"+auto);

	       // Map<String, Object> attributes = new HashMap<String, Object>();	        			
	        Set<Entry<String, JsonElement>> entrySet = auto.entrySet();  //pasa de json a una coleccion llave valor
	        
	        for(Map.Entry<String,JsonElement> entry : entrySet){ //itera la colecion con clave valor	        
	        	if (auto.get(entry.getKey()).getAsString().isEmpty() || auto.get(entry.getKey()).getAsString().length()<1 || auto.get(entry.getKey()).getAsString()==null) {
				log.info("existe un llave vacia "+entry.getKey()+ auto.get(entry.getKey()));
			
				errores+=(entry.getKey().toString()+auto.get(entry.getKey()).getAsString());
				}	        		        
	        }

	        if (!errores.isEmpty() || errores.length()>1) {	        	
	        	responseApp.setStatus(Constantes.ERROR);
				responseApp.setMensaje("Un dato viene vacio, Favor de capturar : "+errores );
			} else {
				     auto.addProperty("status", "PAGADO");
				     
				 if (autoRepositoryInterfazce.savePagoAuto(auto)) {
					 responseApp.setStatus(Constantes.SUCCESS);
					 responseApp.setMensaje("Se ha guardado el pago de manera correcta");	
				 }else {
					    responseApp.setStatus(Constantes.ERROR);
						responseApp.setMensaje("Ocurrio un erroral guardar el pago");
				 }				
			}
		} catch (Exception e) {
			e.printStackTrace();
			responseApp.setStatus(Constantes.ERROR);
			responseApp.setMensaje(e.getMessage());
		}						
		return responseApp;
	}

	@Override
	public ResponseApp getListaAuto(int idEstacionamiento) {
		
		ResponseApp responseApp = new ResponseApp();
		JsonArray ltAutos= new JsonArray();
		try {
			ltAutos=autoRepositoryInterfazce.getListaAutos(idEstacionamiento);
			
			if (ltAutos==null) {
				responseApp.setStatus(Constantes.ERROR);
				responseApp.setMensaje("No se recuperaron carros");
			}else {
				responseApp.setStatus(Constantes.SUCCESS);
				responseApp.setMensaje("Se muestrar tabla con autos");
				responseApp.setObject(new Gson().toJson(ltAutos));
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("se genero un error "+e.getMessage());
			responseApp.setStatus(Constantes.ERROR);
			responseApp.setMensaje("No se recuperar autos ");
		}
		// TODO Auto-generated method stub
		return responseApp;
	}

}
