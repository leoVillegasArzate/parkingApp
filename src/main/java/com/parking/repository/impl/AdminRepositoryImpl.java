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
import com.parking.repository.interfaces.AdminRepositoryInterface;
import com.parking.utils.Scripts;
import com.parking.utils.Utileria;

@Repository
public class AdminRepositoryImpl  implements AdminRepositoryInterface{
	private Logger log = Logger.getLogger(AdminRepositoryImpl.class);
	@Autowired 
	Utileria utileria;
    @Autowired
    DataSource dataSource; 
	PreparedStatement  preparedStatement= null;
	Connection connection=null;
	ResultSet resultSet= null;
	
	@Override
	public JsonArray getEstacionamientos() throws Exception {

		JsonObject objAuto=null; 
		JsonArray ltsEstacionamientos=new JsonArray();
		try {
			connection= dataSource.getConnection();
			preparedStatement=connection.prepareStatement(Scripts.LIST_ESTACIONAMIENTOS);						
			resultSet=preparedStatement.executeQuery();
			 while (resultSet.next()) {		 
				objAuto= new JsonObject();	
				objAuto.addProperty("id", resultSet.getInt("idestacionamiento"));	
				objAuto.addProperty("direccion", resultSet.getString("direccion"));
				objAuto.addProperty("hora", resultSet.getString("importehora"));
				objAuto.addProperty("autoChico", resultSet.getString("importeAutoChico"));
				objAuto.addProperty("autoGrande", resultSet.getString("importeAutogrande"));
				objAuto.addProperty("tipoPgo", resultSet.getString("tipopromo"));
				
			     ltsEstacionamientos.add(objAuto);
						
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("No se logro recupera el listado de estacionamientos");  
		}finally {
			
			utileria.closeConnection(connection, preparedStatement, null);		
		}
		 return ltsEstacionamientos;
	}

	@Override
	public JsonArray getEmpleados() throws Exception {
	
		JsonObject objEmpleado=null; 
		JsonArray ltsEmpleado=new JsonArray();
		try {
			connection= dataSource.getConnection();
			preparedStatement=connection.prepareStatement(Scripts.LIST_EMPLEADOS);						
			resultSet=preparedStatement.executeQuery();
			 while (resultSet.next()) {				 
				 objEmpleado= new JsonObject();					 
				 objEmpleado.addProperty("empleado", resultSet.getString("nombreEmpleado"));	
				 objEmpleado.addProperty("estacionamiento", resultSet.getString("estacionamiento"));
				 objEmpleado.addProperty("usuario", resultSet.getString("username"));
				 objEmpleado.addProperty("perfil", resultSet.getString("perfil"));
				 objEmpleado.addProperty("estatus", resultSet.getString("estatus"));	
				 objEmpleado.addProperty("idUsuario", resultSet.getString("idUsuario"));	
				 
				ltsEmpleado.add(objEmpleado);
							
			}			 
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("No se logro recupera el listado de estacionamientos"); 
		}finally {
			
			utileria.closeConnection(connection, preparedStatement, null);		
		}
		
		 return ltsEmpleado;
	}

	@Override
	public boolean saveEstacionamiento(JsonObject estacionamiento) throws Exception {		
		try {
			log.info("inicia save estacionamiento");
			connection= dataSource.getConnection();
			connection.setAutoCommit(false);
			preparedStatement=connection.prepareStatement(Scripts.SAVE_ESTACIONAMIENTO);	
			preparedStatement.setString(1, estacionamiento.toString());
			resultSet=preparedStatement.executeQuery();
				if (resultSet.next()) {
					if (resultSet.getBoolean(1)) {
						connection.commit();
						return true;
					}else {
						connection.rollback();
						throw new Exception("No se logro guardar el registro intente de nuevo");
					}
					
				}else {
					connection.rollback();
					throw new Exception("No se logro guardar el registro intente de nuevo");
				} 
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}finally {			
			utileria.closeConnection(connection, preparedStatement, resultSet);
		}
	}

	@Override
	public JsonArray getRoles() throws Exception {
		JsonObject objRol=null; 
		JsonArray ltsRol=new JsonArray();
		try {
			connection= dataSource.getConnection();
			preparedStatement=connection.prepareStatement(Scripts.GET_ROLES);						
			resultSet=preparedStatement.executeQuery();
			 while (resultSet.next()) {
				 
				 objRol= new JsonObject();	
				 
				 objRol.addProperty("rol", resultSet.getString("rol"));	
				 objRol.addProperty("activo", resultSet.getString("activo"));
				 objRol.addProperty("id", resultSet.getString("idperfil"));
									
				 ltsRol.add(objRol);
							
			}			 			
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("No se logro recupera el listado de roles"); 
		}finally {
			
			utileria.closeConnection(connection, preparedStatement, null);		
		}
		return ltsRol;
	}

	@Override
	public JsonArray getUsuarios() throws Exception {
	
		log.info("Inicia get usuario");
	
		JsonObject objUser=null; 
		JsonArray ltsUser=new JsonArray();
		try {
			connection= dataSource.getConnection();
			preparedStatement=connection.prepareStatement(Scripts.GET_USUARIOS);						
			resultSet=preparedStatement.executeQuery();
			 while (resultSet.next()) {
				 
				 objUser= new JsonObject();	
				 
				 objUser.addProperty("id", resultSet.getString("idusuario"));
				 objUser.addProperty("usuario", resultSet.getString("usuario"));	
				 objUser.addProperty("estacionamiento", resultSet.getString("estacionamiento"));
				 objUser.addProperty("rol", resultSet.getString("rol"));									
				 ltsUser.add(objUser);							
			}			 			
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("No se logro recupera el listado de usuarios"); 
		}finally {
			
			utileria.closeConnection(connection, preparedStatement, null);		
		}
		 return ltsUser;
	}

	@Override
	public boolean saveEmpleado(JsonObject empleado) throws Exception {		
		try {
			int row=0;
			log.info("inicia save empleado");
			connection= dataSource.getConnection();
			connection.setAutoCommit(false);
			preparedStatement=connection.prepareStatement(Scripts.SAVE_EMPLEADO);	
			preparedStatement.setString(1, empleado.get("nombre").getAsString());
			preparedStatement.setString(2, empleado.get("apellidos").getAsString());
			preparedStatement.setInt(3, empleado.get("usuario").getAsInt());
			preparedStatement.setString(4, empleado.get("pass").getAsString());
			row=preparedStatement.executeUpdate();
				if (row<1) {												
					connection.rollback();
					throw new Exception("No se logro guardar el registro intente de nuevo");
				}				
				preparedStatement.close(); preparedStatement=null;
				resultSet.close(); resultSet=null;
				
				preparedStatement=connection.prepareStatement("update usuario set asignado=true where idusuario=? and activo is true");
				preparedStatement.setInt(1, empleado.get("usuario").getAsInt());
				row=0;
				row=preparedStatement.executeUpdate();
				if (row<1) {												
					connection.rollback();
					throw new Exception("No se logro guardar el registro intente de nuevo");
				 }else {
					 //connection.rollback();
					 connection.commit();
					 return true;
				 }								 
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}finally {			
			utileria.closeConnection(connection, preparedStatement, resultSet);
		}
	}

	@Override
	public boolean saveEmpleadoAdministrador(JsonObject empleado) throws Exception {		
		try {
			log.info("inicia save Admin");
			connection= dataSource.getConnection();
			connection.setAutoCommit(false);
			preparedStatement=connection.prepareStatement(Scripts.SAVE_EMPLEADO_ADMIN);	
			preparedStatement.setString(1, empleado.toString());
			resultSet=preparedStatement.executeQuery();
				if (resultSet.next()) {
					if (resultSet.getBoolean(1)) {
						connection.commit();
						return true;
					}else {
						connection.rollback();
						throw new Exception("No se logro guardar el registro intente de nuevo");
					}
					
				}else {
					connection.rollback();
					throw new Exception("No se logro guardar el registro intente de nuevo");
				} 
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}finally {			
			utileria.closeConnection(connection, preparedStatement, resultSet);
		}
	}

	@Override
	public JsonObject getestacionamientoId(String id) throws Exception {
		JsonObject objEstacionamiento=null; 
		try {
			connection= dataSource.getConnection();
			preparedStatement=connection.prepareStatement(Scripts.GET_ESTACIONAMIENTO_ID);
			preparedStatement.setInt(1, Integer.parseInt(id));
			resultSet=preparedStatement.executeQuery();
						
			 if (resultSet.next()) {		 
				 objEstacionamiento= utileria.getJson(resultSet);
				 if (objEstacionamiento!=null) {
					 return objEstacionamiento;
				} else {
					throw new Exception("No se logro obtener informacion");
				}	 				
			}else {
				throw new Exception("No se encontro infromacion en la base");
			}			
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());  
		}finally {	
			utileria.closeConnection(connection, preparedStatement, null);		
		}
	}

	@Override
	public boolean updateEstacionamiento(JsonObject estacionamiento) throws Exception {		
		try {
			log.info("inicia save Admin");
			connection= dataSource.getConnection();
			connection.setAutoCommit(false);
			preparedStatement=connection.prepareStatement(Scripts.UPDATE_ESTACIONAMIENTO);	
			preparedStatement.setString(1, estacionamiento.toString());
			resultSet=preparedStatement.executeQuery();
				if (resultSet.next()) {
					if (resultSet.getBoolean(1)) {
						connection.commit();
						return true;
					}else {
						connection.rollback();
						log.error("No se logro actualizar intente de nuevo");
						throw new Exception("No se logro actualizar intente de nuevo");
					}
					
				}else {
					connection.rollback();
					log.error("No se logro actualizar intente de nuevo");
					throw new Exception("No se logro actualizar intente de nuevo");
				} 
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}finally {			
			utileria.closeConnection(connection, preparedStatement, resultSet);
		}
	}

	@Override
	public boolean eliminarEstacionamiento(String id) throws Exception {		
		try {
			log.info("inicia save Admin");
			connection= dataSource.getConnection();
			connection.setAutoCommit(false);
			preparedStatement=connection.prepareStatement(Scripts.ELIMINAR_ESTACIONAMIENTO);	
			preparedStatement.setInt(1, Integer.parseInt(id));
			resultSet=preparedStatement.executeQuery();
				if (resultSet.next()) {
					if (resultSet.getBoolean(1)) {
						connection.commit();
						return true;
					}else {
						connection.rollback();
						log.error("No se logro eliminar intente de nuevo");
						throw new Exception("No se logro eliminar intente de nuevo");
					}
					
				}else {
					connection.rollback();
					log.error("No se logro eliminar intente de nuevo");
					throw new Exception("No se logro eliminar intente de nuevo");
				} 
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}finally {			
			utileria.closeConnection(connection, preparedStatement, resultSet);
		}
	}

	@Override
	public boolean eliminarEmpleado(String id) throws Exception {		
		try {
			log.info("inicia save eliminar empleado");
			connection= dataSource.getConnection();
			connection.setAutoCommit(false);
			preparedStatement=connection.prepareStatement(Scripts.ELIMINAR_EMPLEADO);	
			preparedStatement.setInt(1, Integer.parseInt(id));
			resultSet=preparedStatement.executeQuery();
				if (resultSet.next()) {
					if (resultSet.getBoolean(1)) {
						connection.commit();
						return true;
					}else {
						connection.rollback();
						log.error("No se logro eliminar intente de nuevo");
						throw new Exception("No se logro eliminar intente de nuevo");
					}
					
				}else {
					connection.rollback();
					log.error("No se logro eliminar intente de nuevo");
					throw new Exception("No se logro eliminar intente de nuevo");
				} 
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}finally {			
			utileria.closeConnection(connection, preparedStatement, resultSet);
		}
	}

	
}
