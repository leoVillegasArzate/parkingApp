package com.parking.service.impl;

import java.time.LocalDate;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.hibernate.validator.internal.util.privilegedactions.NewInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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
	public ResponseApp savePensionado( JsonObject pensionado) {
		log.info("inicia validacion de datos");
		String errores="";
		ResponseApp responseApp = new ResponseApp();

		
		try {
			
			 
			errores=utileria.validaJson(pensionado);			
	        if (!errores.isEmpty() || errores.length()>1) {	        	
	        	responseApp.setStatus(Constantes.ERROR);
				responseApp.setMensaje("Un dato viene vacio, Favor de capturar : "+errores );
				return responseApp;
			 }else {
				 if (!utileria.isDouble(pensionado.get("otraCantidad").getAsString())) {
					 responseApp.setStatus(Constantes.ERROR);
				     responseApp.setMensaje("No es numero "+pensionado.get("otraCantidad").getAsString() );
				    }				 
				 if (!utileria.isTelefono(pensionado.get("telefono").getAsString())) {
					 responseApp.setStatus(Constantes.ERROR);
				     responseApp.setMensaje("No numero telefonico "+pensionado.get("telefono").getAsString() );
				 	return responseApp;
				}
				 if (!utileria.rfc(pensionado.get("rfc").getAsString())) {
					 responseApp.setStatus(Constantes.ERROR);
				     responseApp.setMensaje("No es un rfc correcto "+pensionado.get("otraCantidad").getAsString() );
				 	return responseApp;
				}
				 
//				 if (!utileriaco(pensionado.get("correo").getAsString())) {
//					 responseApp.setStatus(Constantes.ERROR);
//				     responseApp.setMensaje("No es un correo correcto "+pensionado.get("otraCantidad").getAsString() );
//				 	return responseApp;
//				}
				 
				 if (pensionado.has("idPensionado")) {
						 if (pensionasoRepositoryInterface.updatePensionado(pensionado)) {
							 responseApp.setStatus(Constantes.SUCCESS);
							  responseApp.setMensaje("Se ha Actualizado el Pensionado de manera correcta :: "+pensionado.get("nombre").getAsString());
						  }else {					  
							  responseApp.setStatus(Constantes.ERROR);
							  responseApp.setMensaje("No  actualizo el registro" );
						  }
				} else {
					 if (pensionasoRepositoryInterface.savePensionado(pensionado)) {
						 responseApp.setStatus(Constantes.SUCCESS);
						  responseApp.setMensaje("Se ha registrado el Pensionado de manera correcta :: "+pensionado.get("nombre").getAsString());
					  }else {					  
						  responseApp.setStatus(Constantes.ERROR);
						  responseApp.setMensaje("No  se logro realizar el registro" );
					  }
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
	public ResponseApp lstPensionados(int idEstacionamiento) {
		ResponseApp responseApp = new ResponseApp();
		JsonArray lstpensionados= null;
		String pagoMax=null;
		try {
			lstpensionados=pensionasoRepositoryInterface.lstPensionado(idEstacionamiento);
			if (lstpensionados!=null) {
				 if (lstpensionados.size()<1) {
					 responseApp.setStatus(Constantes.ERROR);
						responseApp.setMensaje("mo hay pensiones disponibles");
					} else {						
						for (JsonElement jsonElement : lstpensionados) {
							JsonObject pensionado= jsonElement.getAsJsonObject();
							JsonObject importeStatu=new JsonObject();
							pagoMax=pensionasoRepositoryInterface.getPagoMax(pensionado.get("idpensionado").getAsInt());
							importeStatu =utileria.GetDias(pagoMax, pensionado.get("fechapago").getAsString(), pensionado.get("importe").getAsDouble());
							pensionado.addProperty("statusPago",importeStatu.get("statusPago").getAsString() );//pago pendiente
							pensionado.addProperty("importePago", importeStatu.get("importePago").getAsDouble());//pronto pago
							pensionado.addProperty("ultimoPago", pagoMax);
							System.out.println(pensionado);
						}

						
						responseApp.setStatus(Constantes.SUCCESS);
						responseApp.setMensaje("Se recupero importe de pension ");
						responseApp.setObject(new Gson().toJson(lstpensionados));
					}
				
			} else {
				responseApp.setStatus(Constantes.ERROR);
				responseApp.setMensaje("No se recupero lista de pensiones");
			}
		} catch (Exception e) {
			e.printStackTrace();
			responseApp.setStatus(Constantes.ERROR);
			responseApp.setMensaje(e.getMessage());
		}
		return responseApp;
	}

	@Override
	public ResponseApp deletePensionado(String id) {
	    
		ResponseApp responseApp = new ResponseApp();
		JsonObject idObj=new Gson().fromJson(id, JsonObject.class);
		try {
			
			if(pensionasoRepositoryInterface.delete(idObj)) {
				responseApp.setMensaje("Se logro dar de baja al pensionado selecionado :: "+idObj.get("nombre").getAsString());
				responseApp.setStatus(Constantes.SUCCESS);	
			}else {
				responseApp.setMensaje("No se logro dar de baja al pensionado selecionado");
				responseApp.setStatus(Constantes.ERROR);		
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			responseApp.setMensaje(e.getMessage());
			responseApp.setStatus(Constantes.ERROR);			
		}
		
		return responseApp;
	}

	@Override
	public ResponseApp getMesesPendientes(String id) {
	    
		ResponseApp responseApp = new ResponseApp();
		JsonObject pensionado= null;
		JsonArray lstMesesPago= null;
		lstMesesPago= new JsonArray();
		try {
			pensionado= new Gson().fromJson(id, JsonObject.class);
			lstMesesPago=utileria.getMesesPendientes( pensionado.get("ultimoPago").getAsString(), pensionado.get("fechapago").getAsString(),
										pensionado.get("importe").getAsDouble(),pensionado.get("idpensionado").getAsInt());			
			if (lstMesesPago.size()<1) {
				responseApp.setMensaje("No se Obtuvo informaciomn de sobre el pago");
				responseApp.setStatus(Constantes.ERROR);
			} else {			
				responseApp.setStatus(Constantes.SUCCESS);
				responseApp.setObject(new Gson().toJson(lstMesesPago));				
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			responseApp.setMensaje(e.getMessage());
			responseApp.setStatus(Constantes.ERROR);			
		}
		
		return responseApp;
	}

	@Override
	public ResponseApp savePago(String parametersRequest) {
		log.info("inicia operaciones para guardar pago");
		ResponseApp responseApp = new ResponseApp();
		JsonObject obj=null;
		JsonArray lstPagos=null;
		String ultimoPago="";
		String errores="";
		boolean bandera=false;
		try {
			obj= new Gson().fromJson(parametersRequest, JsonObject.class);
			lstPagos=obj.getAsJsonArray("lstPagos").getAsJsonArray();
			
			if (obj.has("idPensionado")) {				
				if (lstPagos.size()>=1) {
					log.info("Se realizara ["+lstPagos.size()+"] Pgos ");
					ultimoPago=pensionasoRepositoryInterface.getPagoMax(obj.get("idPensionado").getAsInt());
					if (!ultimoPago.isEmpty()) {
						log.info("Ultimo pago Hecho :: "+ultimoPago);
						LocalDate fechaUltimoPago = LocalDate.parse(ultimoPago, Constantes.FECHA);
						int size=lstPagos.size();
						for (int i=0; i<size; i++) {
								bandera=false;
								fechaUltimoPago = fechaUltimoPago.plusMonths(1);	
								System.out.println(" pagos perndientes "+fechaUltimoPago.toString());
								for (int j=0; j<lstPagos.size(); j++) {		
									System.out.println("pagos agregados "+lstPagos.get(j).getAsJsonObject().get("mes").getAsString());
									if (fechaUltimoPago.toString().equals( lstPagos.get(j).getAsJsonObject().get("mes").getAsString() )) {
										bandera=true;
										break;
									 }else {
										 bandera=false;										 
									 }
								}								
								if (!bandera) {errores+=fechaUltimoPago.toString()+", ";}
						}						
						if (errores.isEmpty()) {
							log.info("los pagos agregados son correctos");							
							for (int i=0; i<lstPagos.size(); i++) {
								bandera=false;
								fechaUltimoPago = fechaUltimoPago.plusMonths(1);								
								  if (!pensionasoRepositoryInterface.savePago(lstPagos.get(i).getAsJsonObject())) {									 									   
									    responseApp.setMensaje("Error al guardar pago intenta de nuevo ");
										responseApp.setStatus(Constantes.ERROR);
										return responseApp;									   
								  }
						     }
							responseApp.setMensaje("Se registraron los pagos de manera correcta");
							responseApp.setStatus(Constantes.SUCCESS);
						} else {
							responseApp.setMensaje("Los pagos deben ser consecutivos ["+errores+"] se deben  agregar al pago");
							responseApp.setStatus(Constantes.ERROR);
						}
						
					} else {
						responseApp.setMensaje("No se recupero ultimo pago para guardar pagos nuevos");
						responseApp.setStatus(Constantes.ERROR);
					}
					
				} else {
					responseApp.setMensaje("La lista de pagos esta en 0");
					responseApp.setStatus(Constantes.ERROR);
				}				
			} else {
				responseApp.setMensaje("No Se encontro el id del Pensionado");
				responseApp.setStatus(Constantes.ERROR);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			responseApp.setMensaje(e.getMessage());
			responseApp.setStatus(Constantes.ERROR);	
		}
		// TODO Auto-generated method stub
		return responseApp;
	}
}
