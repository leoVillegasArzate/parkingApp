package com.parking.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.parking.models.CobroModel;
import com.parking.repository.interfaces.InfoRepositoryInterface;
import com.parking.utils.Constantes;
import com.parking.utils.ResponseApp;
import com.parking.utils.Scripts;
import com.parking.utils.Utileria;

@Repository
public class InfoRepositoryImpl  implements InfoRepositoryInterface{
	private Logger log = Logger.getLogger(InfoRepositoryImpl.class);

	@Autowired 
	Utileria utileria;
    @Autowired
    DataSource dataSource;

    
	PreparedStatement  preparedStatement= null;
	Connection connection=null;
	ResultSet resultSet= null;
	
	@Override
	public ResponseApp getInfo(String fecha,int idUsuario,int idEstacionamiento) throws Exception {
		 ResponseApp responseApp= new ResponseApp();
		int row=0;
		JsonObject info= new JsonObject();
		try {
			connection= dataSource.getConnection();
			preparedStatement=connection.prepareStatement(Scripts.GET_INFO);
			preparedStatement.setString(1, fecha);
			preparedStatement.setInt(2, idEstacionamiento);
			
			
			resultSet=preparedStatement.executeQuery();
			   if (resultSet.next()) {
				   info.addProperty("folio", resultSet.getInt("rs_folio"));	
				   info.addProperty("entrada", resultSet.getString("rs_entrada"));
				   info.addProperty("pendientes", resultSet.getString("rs_pendientes"));
				   info.addProperty("salidas", resultSet.getString("rs_salidas"));				  
				row++;			
				responseApp.setStatus(Constantes.SUCCESS);
				responseApp.setObject(new Gson().toJson(info));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			responseApp.setStatus(Constantes.ERROR);
			responseApp.setMensaje("ocurrio un error en la base de datos :: "+e.getMessage());
		}finally {
			
			utileria.closeConnection(connection, preparedStatement, null);		
		}
		return responseApp;
	}

	@Override
	public CobroModel getTarifa(int idEstacionamiento) throws Exception {
		log.info("Recuperando  tarifa");
		int row=0;
		CobroModel cobro=null;
		try {
			connection= dataSource.getConnection();
			preparedStatement=connection.prepareStatement(Scripts.GET_COBRO);
			preparedStatement.setInt(1, idEstacionamiento);
			
			//String nombreEmpleado, String estacionamiento, String userName, boolean inUse
			resultSet=preparedStatement.executeQuery();
			 if (resultSet.next()) {
				
				 cobro=new CobroModel( resultSet.getDouble("importeAutoChico"), 
						 			   resultSet.getDouble("importeAutogrande"),						 	
						 			   resultSet.getDouble("importehora"),
						 			   resultSet.getDouble("fraccion1"), 
						 			   resultSet.getDouble("fraccion2"), 
						 			   resultSet.getDouble("freaccion3"),
						 			   resultSet.getDouble("fraccion4"), 
						 			   resultSet.getDouble("horaspromo"), 
						 			   resultSet.getDouble("importepromo"),
						 			   false,
						 			   resultSet. getString("tipopromo"),
						 			   resultSet.getDouble("importeboletoperdido")
						 			   );

				 row++;
				return cobro;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("No se encontro tabla de cobro, ocurrio un error en la base de datos"); 
		}finally {

			utileria.closeConnection(connection, preparedStatement, resultSet);		
		}
		return null;
	}
	

}
