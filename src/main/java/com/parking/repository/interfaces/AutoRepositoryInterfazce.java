package com.parking.repository.interfaces;

import java.sql.SQLException;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public interface AutoRepositoryInterfazce {

	public boolean save(JsonObject objAuto) throws SQLException, Exception;
	
	public JsonObject searchFolio (JsonObject auto) throws Exception;

	public boolean savePagoAuto(JsonObject auto) throws Exception;

	public JsonArray getListaAutos(int idEstacionamiento);
}
