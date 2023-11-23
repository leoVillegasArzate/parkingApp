package com.parking.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.gson.JsonObject;
import com.parking.models.UserModel;
import com.parking.repository.interfaces.UserAuthRepositoryInterface;
import com.parking.utils.Scripts;
import com.parking.utils.Utileria;

@Repository
public class UserRepositoryImpl  implements UserAuthRepositoryInterface{
	private Logger log = Logger.getLogger(UserRepositoryImpl.class);

	
	@Autowired 
	Utileria utileria;
    @Autowired
    DataSource dataSource; 
	PreparedStatement  preparedStatement= null;
	Connection connection=null;
	ResultSet resultSet= null;
	
	@Override
	public UserModel getAuthentication(String username) throws Exception {
		log.info("Recuperando  usario");
		int row=0;
		UserModel userModel = null;
		try {
			connection= dataSource.getConnection();
			preparedStatement=connection.prepareStatement(Scripts.GET_AUTHENTICATION);
			preparedStatement.setString(1, username);
			
			//String nombreEmpleado, String estacionamiento, String userName, boolean inUse
			resultSet=preparedStatement.executeQuery();
			 if (resultSet.next()) {
				 userModel = new UserModel(resultSet.getInt("idEstacionamiento"),
						 resultSet.getInt("idusuario"),
						 resultSet.getString("nombreEmpleado"),
						 resultSet.getString("estacionamiento"), 
						 resultSet.getString("username"), 
						 resultSet.getBoolean("asignado"),
						 resultSet.getString("rol"));

				 row++;
				return userModel;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("No se encontro Usuario, ocurrio un error en la base de datos"); 
		}finally {
			log.info("se recupero :: "+row+" filas " );
			utileria.closeConnection(connection, preparedStatement, resultSet);		
		}
		return null;
	}



}
