package com.parking.repository.impl;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.parking.repository.interfaces.AutoRepositoryInterfazce;
import com.parking.utils.Scripts;
import com.parking.utils.Utileria;


@Repository
public class AutoRepositoryImpl implements AutoRepositoryInterfazce {
	private Logger log = Logger.getLogger(AutoRepositoryImpl.class);

	@Autowired 
	Utileria utileria;
    @Autowired
    DataSource dataSource; 
	PreparedStatement  preparedStatement= null;
	Connection connection=null;
	ResultSet resultSet= null;
	
	@Override
	public boolean save(JsonObject objAuto) throws Exception {		
		log.info("Save... "+ objAuto);
		log.info(objAuto.get("idEstacionamiento").getAsString());
		int row=0;
		try {		
			connection= dataSource.getConnection();
			connection.setAutoCommit(false);
			preparedStatement=connection.prepareStatement(Scripts.GUARDA_AUTO);
			preparedStatement.setInt(1, objAuto.get("folio").getAsInt());
			preparedStatement.setString(2, objAuto.get("marca").getAsString());
			preparedStatement.setString(3, objAuto.get("color").getAsString());
			preparedStatement.setString(4, objAuto.get("placa").getAsString());
			preparedStatement.setString(5, objAuto.get("fechaEntrada").getAsString());
			preparedStatement.setBoolean(6, false);
			preparedStatement.setString(7, objAuto.get("status").getAsString());
			preparedStatement.setInt(8, objAuto.get("idEstacionamiento").getAsInt());
			preparedStatement.setString(9, objAuto.get("tipoCobro").getAsString());

			 row=preparedStatement.executeUpdate();
			if (row>0) {
				connection.commit();
				return true;
			}else {
				connection.rollback();
			} 
				
			
		} catch (Exception e) {
			e.printStackTrace();
			connection.rollback();
			throw new Exception("No se logro hacer el registro,error en la base de datos"); 
			//return false;
		}finally {
			log.info("se afecto :: "+row+" filas " +connection.isClosed());
			utileria.closeConnection(connection, preparedStatement, resultSet);				
		}
		
		return false;
	}

	@Override
	public JsonObject searchFolio(JsonObject auto) throws Exception {
		log.info("Recuperando auto");
		int row=0;
		JsonObject objAuto= new JsonObject();
		try {
			connection= dataSource.getConnection();
			
			preparedStatement=connection.prepareStatement(Scripts.SEARCH_FOLIO);
			preparedStatement.setLong(1, auto.get("folio").getAsLong());
			preparedStatement.setInt(2, auto.get("idEstacionamiento").getAsInt());
			
			resultSet=preparedStatement.executeQuery();
			 if (resultSet.next()) {				 
				 if (resultSet.getString("status").equals("PAGADO")) {					 
					 throw new Exception("El auto ya se encuentra como saliente"); 
				}else{
					objAuto.addProperty("folio", resultSet.getInt("folio"));	
					objAuto.addProperty("marca", resultSet.getString("marca"));
					objAuto.addProperty("color", resultSet.getString("color"));
					objAuto.addProperty("placa", resultSet.getString("placa"));
					objAuto.addProperty("fechaEntrada", resultSet.getString("entrada"));
					objAuto.addProperty("statusPago", resultSet.getString("status"));
					objAuto.addProperty("idEstacionamiento", resultSet.getString("idestacionamiento"));
					objAuto.addProperty("tipoCobro", resultSet.getString("tipocobro"));	
					objAuto.addProperty("modoPago", resultSet.getString("modopago"));	
					row++;
					return objAuto;					
				}
			 }else {
				 throw new Exception("No se recupero el folio "+auto.get("folio").getAsLong()); 
			 }
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage()); 
		}finally {
			log.info("se recupero :: "+row+" filas " );
			utileria.closeConnection(connection, preparedStatement, null);		
		}
	}

	@Override
	public boolean savePagoAuto(JsonObject auto) throws Exception {		
		log.info("Save... "+ auto);
		int row=0;
		try {		
			connection= dataSource.getConnection();
			connection.setAutoCommit(false);
			preparedStatement=connection.prepareStatement(Scripts.GUARDA_PAGO_AUTO);
			preparedStatement.setString(1, auto.get("fechaSalida").getAsString());
			preparedStatement.setString(2, auto.get("consumo").getAsString());
			preparedStatement.setString(3, auto.get("consumoFraccion").getAsString());
			preparedStatement.setString(4,auto.get("status").getAsString());
			preparedStatement.setDouble(5, auto.get("importePago").getAsDouble());
			preparedStatement.setString(6,auto.get("modoPago").getAsString());
			preparedStatement.setInt(7, auto.get("folioSalida").getAsInt());
			preparedStatement.setInt(8, auto.get("idEstacionamiento").getAsInt());

			 row=preparedStatement.executeUpdate();
			if (row>0) {
				connection.commit();
				return true;
			}else {
				connection.rollback();
			} 
				
			
		} catch (Exception e) {
			e.printStackTrace();
			connection.rollback();
			throw new Exception("No se logro hacer el registro,error en la base de datos"); 
			//return false;
		}finally {
			log.info("se afecto :: "+row+" filas " +connection.isClosed());
			utileria.closeConnection(connection, preparedStatement, resultSet);				
		}
		
		return false;
	}

	@Override
	public JsonArray getListaAutos(int idEstacionamiento) {
		JsonObject	objAuto =null;
		JsonArray lstAuto= new JsonArray();
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Scripts.GET_LISTA_AUTOS);
			preparedStatement.setLong(1,idEstacionamiento);
			
			resultSet=preparedStatement.executeQuery();
			   while (resultSet.next()) {
				objAuto= new JsonObject();
				
				objAuto.addProperty("folio", resultSet.getInt("folio"));	
				objAuto.addProperty("marca", resultSet.getString("marca"));
				objAuto.addProperty("color", resultSet.getString("color"));
				objAuto.addProperty("placa", resultSet.getString("placa"));
				objAuto.addProperty("fechaEntrada", resultSet.getString("entrada"));
				objAuto.addProperty("idEstacionamiento", resultSet.getInt("idestacionamiento"));
				
				lstAuto.add(objAuto);				
			}
			
			   return lstAuto;
		} catch (Exception e) {
			e.printStackTrace();			
		} finally {
			utileria.closeConnection(connection, preparedStatement, resultSet);
		}
		return null;
	}

}
