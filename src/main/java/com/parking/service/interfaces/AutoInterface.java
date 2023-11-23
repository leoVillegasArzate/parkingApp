package com.parking.service.interfaces;

import java.sql.SQLException;

import com.google.gson.JsonObject;
import com.parking.models.CobroModel;
import com.parking.utils.ResponseApp;


public interface AutoInterface {

	public ResponseApp save(JsonObject auto) throws SQLException;
	public ResponseApp saveSalida(JsonObject auto);
	public ResponseApp searchFolio(String folio,CobroModel cobro,boolean CobroPerdido);
	public ResponseApp getListaAuto(int idEstacionamiento);
}
