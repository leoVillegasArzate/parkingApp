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
	public boolean savePensionado(JsonObject objPension) throws Exception {
		log.info("Inicia registro  pensionado repository");
		System.out.println(objPension.get("telefono").getAsString());
		int row=0;
		int idPensionado=0;
		try {
			connection=dataSource.getConnection();
			connection.setAutoCommit(false);
			preparedStatement=connection.prepareStatement(Scripts.SAVE_PENSIONADO);
			preparedStatement.setString(1, objPension.get("nombre").getAsString());
			preparedStatement.setString(2, objPension.get("fechaRegistro").getAsString());
			preparedStatement.setInt(3, Integer.parseInt(objPension.get("telefono").getAsString().replaceAll("\"", "")) );
			preparedStatement.setString(4, objPension.get("correo").getAsString());
			preparedStatement.setString(5, objPension.get("calle").getAsString());
			preparedStatement.setString(6, objPension.get("colonia").getAsString());
			preparedStatement.setString(7, objPension.get("delegacion").getAsString());
			preparedStatement.setString(8, objPension.get("marca").getAsString());
			preparedStatement.setString(9, objPension.get("placas").getAsString());
			preparedStatement.setString(10, objPension.get("color").getAsString());
			preparedStatement.setInt(11, objPension.get("modelo").getAsInt());
			preparedStatement.setDouble(12, objPension.get("otraCantidad").getAsDouble());
			preparedStatement.setInt(13, objPension.get("idEstacionamiento").getAsInt());
			preparedStatement.setString(14, objPension.get("rfc").getAsString());
			
			resultSet=preparedStatement.executeQuery();
			if (resultSet.next()) {
				
				if (resultSet.getInt(1)<1) {
					throw new Exception("No se logro recuperar el id del pensionado "+objPension.get("nombre").getAsString()); 
				} else {
					idPensionado=resultSet.getInt(1);
					log.info("id pension recuperado :: "+idPensionado);
				}
			}else {
				throw new Exception("No se logro recuperar el id del pensionado "+objPension.get("nombre").getAsString()); 
			}
			utileria.closeConnection(null, preparedStatement, resultSet);
			preparedStatement=null;
			resultSet= null;
			
			preparedStatement=connection.prepareStatement(Scripts.SAVE_PAGO_PENSION);
			preparedStatement.setInt(1, idPensionado);
			preparedStatement.setString(2, objPension.get("fechaRegistro").getAsString());
			preparedStatement.setDouble(3, objPension.get("otraCantidad").getAsDouble());
			preparedStatement.setDouble(4, 0);
			 row=preparedStatement.executeUpdate();
				if (row>0) {
					connection.commit();
					return true;
				}else {
					connection.rollback();
					throw new Exception("No se logro registrar el pago del  pensionado "+objPension.get("nombre").getAsString()); 
				} 
				
		} catch (Exception e) {					
			e.printStackTrace();
			log.error(e.getLocalizedMessage());
			log.error(e.getMessage());
			throw new Exception("No se logro hacer el registro "+e.getMessage()); 
		}finally {
			
			utileria.closeConnection(connection, preparedStatement, resultSet);
		}	
	}

	@Override
	public JsonArray lstPensionado(int idEstacionamiento) throws Exception {
      log.info("Inicia recuperacion de lista de pensionados");
		 JsonObject pensionado=null;
		 JsonArray lstPensionados=null;
		try {
			connection=dataSource.getConnection();
			preparedStatement=connection.prepareStatement("SELECT (EXTRACT(YEAR FROM CURRENT_DATE)::TEXT || '-' || EXTRACT(MONTH FROM CURRENT_DATE)::TEXT || '-' ||  EXTRACT(DAY FROM fecharegistro)::TEXT)::DATE AS fechapago,*"
															+ " from pensionado where idestacionamiento=? and activo is true order by fechapago,nombre asc ;");
			preparedStatement.setInt(1, idEstacionamiento);
			resultSet=preparedStatement.executeQuery();
			
			lstPensionados= new JsonArray();
			while (resultSet.next()) {
				pensionado= utileria.getJson(resultSet);
				lstPensionados.add(pensionado);	
			}
			
		} catch (Exception e) {
			   e.printStackTrace();
			  throw new Exception(e.getMessage());
		}finally {
			
			utileria.closeConnection(connection, preparedStatement, resultSet);
			log.info("el tamanio de lista :: "+lstPensionados.size());
		}

		return lstPensionados;
	}

	@Override
	public boolean updatePensionado(JsonObject pensionado) throws Exception {
		log.info("Inicia registro  pensionado repository");
		int row=0;
		try {
			connection=dataSource.getConnection();
			connection.setAutoCommit(false);
			preparedStatement=connection.prepareStatement(Scripts.UPDATE_PENSIONADO);
			preparedStatement.setString(1, pensionado.get("nombre").getAsString());
			preparedStatement.setInt(2, Integer.parseInt(pensionado.get("telefono").getAsString().replaceAll("\"", "")) );
			preparedStatement.setString(3, pensionado.get("correo").getAsString());
			preparedStatement.setString(4, pensionado.get("calle").getAsString());
			preparedStatement.setString(5, pensionado.get("colonia").getAsString());
			preparedStatement.setString(6, pensionado.get("delegacion").getAsString());		
			preparedStatement.setString(7, pensionado.get("placas").getAsString());
			preparedStatement.setString(8, pensionado.get("color").getAsString());
			preparedStatement.setString(9, pensionado.get("rfc").getAsString());			
			preparedStatement.setInt(10, pensionado.get("idPensionado").getAsInt());
			preparedStatement.setInt(11, pensionado.get("idEstacionamiento").getAsInt());

			 row=preparedStatement.executeUpdate();
				if (row>0) {
					connection.commit();
					return true;
				}else {
					connection.rollback();
					throw new Exception("No se logro Actualizar el   pensionado "+pensionado.get("nombre").getAsString()); 
				} 
				
		} catch (Exception e) {					
			e.printStackTrace();
			log.error(e.getLocalizedMessage());
			log.error(e.getMessage());
			throw new Exception("No se logro hacer Actualizacion "+e.getMessage()); 
		}finally {
			
			utileria.closeConnection(connection, preparedStatement, resultSet);
		}	
	}

	@Override
	public boolean delete(JsonObject idObj) throws Exception {
		log.info("Inicia registro  pensionado repository "+idObj);
		int row=0;
		try {
			connection=dataSource.getConnection();
			connection.setAutoCommit(false);
			preparedStatement=connection.prepareStatement(Scripts.DELETE_PENSIONADO);
		
			preparedStatement.setInt(1, idObj.get("idPensionado").getAsInt());
			preparedStatement.setInt(2, idObj.get("idEstacionamiento").getAsInt());

			 row=preparedStatement.executeUpdate();
				if (row>0) {
					connection.commit();
					return true;
				}else {
					connection.rollback();
					throw new Exception("No se logro eliminar el  pensionado "); 
				} 
				
		} catch (Exception e) {					
			e.printStackTrace();
			log.error(e.getLocalizedMessage());
			log.error(e.getMessage());
			throw new Exception("No se logro hacer Actualizacion "+e.getMessage()); 
		}finally {
			
			utileria.closeConnection(connection, preparedStatement, resultSet);
		}	
	}

	@Override
	public String getPagoMax(int idPensionado) throws Exception {
	      log.info("Inicia recuperacion de ultimo pago");
			try {
				connection=dataSource.getConnection();
				preparedStatement=connection.prepareStatement("select max(mescorrespondiente) as ultimoPago from pagopension where idpensionado=?");
				preparedStatement.setInt(1, idPensionado);
				resultSet=preparedStatement.executeQuery();				
		
				if (resultSet.next()) {
					return resultSet.getString(1);
				}else {
					throw new Exception("No se recupero ningun pago");
				}
				
			} catch (Exception e) {
				   e.printStackTrace();
				  throw new Exception(e.getMessage());
			}finally {				
				utileria.closeConnection(connection, preparedStatement, resultSet);			
			}

	
		}

	@Override
	public JsonArray getPensionado(JsonObject idObj) {
		
		return null;
	}

	@Override
	public boolean savePago(JsonObject pago) throws Exception {
		log.info("Inicia registro  pago repository");
		int row=0;
		try {
			connection=dataSource.getConnection();
			connection.setAutoCommit(false);
			preparedStatement=connection.prepareStatement(Scripts.PAGO_PENSION);
			preparedStatement.setInt(1, pago.get("idPensionado").getAsInt());		
			preparedStatement.setString(2, pago.get("mes").getAsString());
			preparedStatement.setDouble(3, pago.get("importe").getAsDouble());
			
			 row=preparedStatement.executeUpdate();
				if (row>0) {
					connection.commit();
					return true;
				}else {
					connection.rollback();
				    return false;
				} 
				
		} catch (Exception e) {					
			e.printStackTrace();
			log.error(e.getLocalizedMessage());
			log.error(e.getMessage());
			throw new Exception("No se logro hacer Actualizacion "+e.getMessage()); 
		}finally {			
			utileria.closeConnection(connection, preparedStatement, resultSet);
		}	
	}

}
