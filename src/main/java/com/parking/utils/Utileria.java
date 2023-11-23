package com.parking.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.parking.models.CobroModel;

public class Utileria {
	private Logger log = Logger.getLogger(Utileria.class);
	
	public boolean isNumero(String numero) {return numero.matches("[0-9]+");}
	public boolean isDouble(String numero) {
		if (numero.matches("[-+]?\\d*\\.?\\d+")) {
			return true;
		} else {
			return false;
		}
		}
	
	
	public String getFecha() {
		Date fechaActual= new Date();
		return Constantes.FECHA_HORA.format(fechaActual);   //  fecha.format() ;			
	}
	
	public String getEstancia(long segundos) {	
		long horas = (segundos / 3600);
		long minutos = ((segundos - horas * 3600) / 60);
		long segundos1 = segundos - (horas * 3600 + minutos * 60);
		String tiempEstancia = (horas<10?("0"+horas):horas) + ":" + ( minutos<10?("0"+minutos):minutos) + ":" +  ( segundos1<10?("0"+segundos1):segundos1)+" hrs";
		log.info(" enstancia " + horas + ":" + minutos + ":" + segundos1);
		return tiempEstancia;
	}
	
	
	public String getEstanciaFraccion(long segundos) {	
		long horas = (segundos / 3600);
		long minutos = ((segundos - horas * 3600) / 60);
		

		
		 String fraccion = null;
		 
		 
		 if (horas<1) {
			 fraccion="1 h";			
		} else {
		    
			if (minutos>=0 && minutos<=15) {// 1era freaccion
		    	 fraccion=horas+" 1/4 h";
			 }
		     if (minutos>=16 && minutos<=30) {//2da fraccion
		    	 fraccion=horas+" 1/2 h";
			 }
		     if (minutos>=31 && minutos<=45) {//3era fraccion
		    	 fraccion=horas+" 3/4 h";
			 }
		     if (minutos>=46 && minutos<=59) {//4ta fracion que seria una hora mas
		    	 horas++;
		    	 fraccion=horas+" h";
			 }	
		     
		     
		}
		 
		 
		 log.info(fraccion);

		return fraccion;
	}
	
	public long getSegundos(String entrada, String salida) {
		
		String a =entrada.toString();
		LocalDateTime fechaEntrada = LocalDateTime.parse(a,Constantes.FORMATO_FECHA_HORA);	
		LocalDateTime fechaSalida = LocalDateTime.parse(salida,Constantes.FORMATO_FECHA_HORA);		
		log.info(" entrada "+fechaEntrada +" || salida "+fechaSalida);	
		long segundos = Duration.between(fechaEntrada, fechaSalida).getSeconds(); // segundos	
		return segundos;
	}
	
	public void closeConnection(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) 
	{
		try
		{
			if( resultSet != null )
				resultSet.close();
			
			if( preparedStatement != null)
				preparedStatement.close();
			
			if(connection!=null && !connection.isClosed())
				connection.close();
			
		}catch(Exception sql)
		{
			log.fatal(sql);
		}		
	}
	public JsonObject getcobro(long segundos, CobroModel cobro, String tipoEntrada,boolean cobroPerdido) {
		long horas = (segundos / 3600);
		long minutos = ((segundos - horas * 3600) / 60);
		double tarifa=0;
		double tarifaAux=0;
		int horasAux=0;
		long horasresiduo=0;
		 log.info(" SISTEMA DE COBRO  Hora "+cobro.getHora() +" horas promo "+cobro.getHorasPromo() +" importe promo "+cobro.getImportePromo());
		 log.info(" CONSUMO horas "+horas +" minutos  "+minutos);
		JsonObject detalle = new  JsonObject();
		 
		if (tipoEntrada.equals("HORA")) {//si no tiene promocion se cobrara por hora 			 
			 tarifa= importe(horas, minutos, cobro);			
			if (cobroPerdido) {
				tarifa+=cobro.getCobroBoletoPerdido();
				detalle.addProperty("cobro", "HORA/BOLETOPERDIDO");
				detalle.addProperty("importe", tarifa);
			}else {
				detalle.addProperty("cobro", "HORA");
				detalle.addProperty("importe", tarifa);
			}
		} else if (tipoEntrada.equals("AUTOMATICO")) {
			
			if (horas <=cobro.getHorasPromo()) {
				tarifa = importe(horas, minutos, cobro);
				if (tarifa >= cobro.getImportePromo()) {
					tarifa = cobro.getImportePromo();
					
					if (cobroPerdido) {
						tarifa+=cobro.getCobroBoletoPerdido();
						detalle.addProperty("cobro", "PROMOCION/BOLETOPERDIDO");
						detalle.addProperty("importe", tarifa);
					}else {
						detalle.addProperty("cobro", "PROMOCION");
						detalle.addProperty("importe", tarifa);
					}
					
				}else {
					if (cobroPerdido) {
						tarifa+=cobro.getCobroBoletoPerdido();
						detalle.addProperty("cobro", "HORA/BOLETOPERDIDO");
						detalle.addProperty("importe", tarifa);
					}else {
						detalle.addProperty("cobro", "HORA");
						detalle.addProperty("importe", tarifa);
					}
				}
			} else {
				horasAux = (int) (horas / cobro.getHorasPromo());
				horasresiduo = (long) (horas % cobro.getHorasPromo());

				tarifa = horasAux * cobro.getImportePromo();
				tarifaAux = importe(horasresiduo, minutos, cobro);

				if (tarifaAux >= cobro.getImportePromo()) {
					tarifa += cobro.getImportePromo();
				} else {
					tarifa += tarifaAux;
				}				
				if (cobroPerdido) {
					tarifa+=cobro.getCobroBoletoPerdido();
					detalle.addProperty("cobro", "PROMOCION/BOLETOPERDIDO");
					detalle.addProperty("importe", tarifa);
				}else {
					detalle.addProperty("cobro", "PROMOCION");
					detalle.addProperty("importe", tarifa);
				}
			}		
		} else {
			if (tipoEntrada.equals("PROMOCION")) {

				if (horas < 1 && minutos >= 0) {
					tarifa = cobro.getImportePromo();
				} else {
					if (horas <= cobro.getHorasPromo()) {
						tarifa = cobro.getImportePromo();
						if (horas == cobro.getHorasPromo()) {
							if (minutos >= 0) {
								tarifa += cobro.getImportePromo();
							}
						}
					} else {
						horasAux = (int) (horas / cobro.getHorasPromo());
						horasresiduo = (long) (horas % cobro.getHorasPromo());
						tarifa = horasAux * cobro.getImportePromo();
						if (horasresiduo >= 1 || horasresiduo <= cobro.getHorasPromo()) {
							tarifa += cobro.getImportePromo();
							if (horasresiduo == cobro.getHorasPromo()) {
								tarifa += cobro.getImportePromo();
							}
						}
					}
				}
				if (cobroPerdido) {
					tarifa+=cobro.getCobroBoletoPerdido();
					detalle.addProperty("cobro", "PROMOCION/BOLETOPERDIDO");
					detalle.addProperty("importe", tarifa);
				}else {
					detalle.addProperty("cobro", "PROMOCION");
					detalle.addProperty("importe", tarifa);
				}
				log.info(" tarifa es: " + tarifa);
				return detalle;
			}
		}
		return detalle;
	}
	
 public double importe(long horas, long minutos,CobroModel cobro) {
	   double tarifa =0; 
		if (horas<1) {
			 tarifa+=(cobro.getHora());			
		} else {
			tarifa+=(horas*cobro.getHora());
			
			if (minutos>=0 && minutos<=15) {// 1era freaccion
		    	tarifa+=cobro.getFraccion1();
			 }
		     if (minutos>=16 && minutos<=30) {//2da fraccion
		    	 tarifa+=cobro.getFraccion1()+cobro.getFraccion2();
			 }
		     if (minutos>=31 && minutos<=45) {//3era fraccion
		    	 tarifa+=cobro.getFraccion1()+cobro.getFraccion2()+cobro.getFraccion3();
			 }
		     if (minutos>=46 && minutos<=59) {//4ta fracion que seria una hora mas			    	
		    	 tarifa+=cobro.getFraccion1()+cobro.getFraccion2()+cobro.getFraccion3()+cobro.getFraccion4();
			 }				
		}		
		return tarifa;
 }	
	
	public JsonObject getJson(ResultSet resultSet) throws Exception {
		ResultSetMetaData metaData;
		JsonObject object= new JsonObject();
		try {
			metaData = resultSet.getMetaData();
			  int columnCount = metaData.getColumnCount();

		        for (int i = 1; i <= columnCount; i++) {
		            String columnName = metaData.getColumnName(i);
		            Object columnValue = resultSet.getObject(i);
		             
		            object.addProperty(columnName, columnValue.toString());		           		      
		        }
		} catch (SQLException e) {			
			e.printStackTrace();
			throw new Exception("Ocurrio un error en la respuesta de la base ");
		}      
		return object;
	}

	public String validaJson(JsonObject object) {
		String errores="";

		// Map<String, Object> attributes = new HashMap<String, Object>();
		Set<Entry<String, JsonElement>> entrySet = object.entrySet(); // pasa de json a una coleccion llave valor

		for (Map.Entry<String, JsonElement> entry : entrySet) { // itera la colecion con clave valor
			if (object.get(entry.getKey()).getAsString().isEmpty() || object.get(entry.getKey()).getAsString().length() < 1
					|| object.get(entry.getKey()).getAsString() == null) {
				
				log.info("existe un llave vacia " + entry.getKey() + object.get(entry.getKey()));

				errores += (entry.getKey().toString() + object.get(entry.getKey()).getAsString());
			}
		}
		return errores;
	}

	public boolean isTelefono(String text) {return text.matches("\\d{10}");}
	public boolean correo(String correo) { return correo.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");}
	public boolean rfc(String rfc) { return rfc.matches("[a-zA-Z0-9]{12,13}");}
}

