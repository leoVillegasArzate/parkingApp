package com.parking.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.gson.JsonObject;
import com.parking.repository.interfaces.PensionasoRepositoryInterface;
import com.parking.utils.Scripts;
import com.parking.utils.Utileria;

@Repository
public class PensionadoRepositoryInmpl  implements PensionasoRepositoryInterface{
	private Logger log = Logger.getLogger(PensionadoRepositoryInmpl.class);

	@Autowired 
	Utileria utileria;
    @Autowired
    DataSource dataSource; 
	PreparedStatement  preparedStatement= null;
	Connection connection=null;
	ResultSet resultSet= null;
	
	@Override
	public JsonObject getImportePensionado(int idEstacionamiento) throws Exception {

		 JsonObject objImporte = null;
		
		try {
			connection=dataSource.getConnection();
			preparedStatement=connection.prepareStatement(Scripts.GET_PRECIO_PENSION);
			preparedStatement.setInt(1, idEstacionamiento);
			resultSet=preparedStatement.executeQuery();
			
			if (resultSet.next()) {
				objImporte= new JsonObject();
				objImporte.addProperty("autoChico", resultSet.getDouble("importeautochico"));
				objImporte.addProperty("autoGrande", resultSet.getDouble("importeautogrande"));
				objImporte.addProperty("idCobro", resultSet.getDouble("idcobro"));
				objImporte.addProperty("idEstacionamiento",idEstacionamiento);
				
				return objImporte;
			} else {
				 throw new Exception("no se recupero ningun tipo de compro de pension");
			}
			
		} catch (Exception e) {
			  e.printStackTrace();
			  throw new Exception(e.getMessage());
		}finally {
			
			utileria.closeConnection(connection, preparedStatement, resultSet);
		}
	}

	@Override
	public boolean savePensionado(JsonObject objPension) {
		
		try {
			connection=dataSource.getConnection();
			preparedStatement=connection.prepareStatement(Scripts.SAVE_PENSIONADO);
			preparedStatement.setString(1, objPension.get("").getAsString());
			resultSet=preparedStatement.executeQuery();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			
			utileria.closeConnection(connection, preparedStatement, resultSet);
		}
		return false;
	}

}
