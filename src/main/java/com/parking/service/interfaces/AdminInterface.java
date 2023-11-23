package com.parking.service.interfaces;

import com.google.gson.JsonObject;
import com.parking.utils.ResponseApp;

public interface AdminInterface {

	public ResponseApp getEstacionamientos();
	public ResponseApp getEmpleados();
	public ResponseApp saveEstacionamiento (JsonObject estacionamiento);
	public ResponseApp getRoles();
	public ResponseApp getusuarios();
	public ResponseApp saveEmpleado(JsonObject estacionamiento);
	public JsonObject getEstacionamientoid(String id) throws Exception;
	public boolean eliminarEstacionamiento(String id) throws Exception;
	public boolean eliminarEmpoleado(String id) throws Exception;
}
