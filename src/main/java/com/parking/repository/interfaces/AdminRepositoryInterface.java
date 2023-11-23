package com.parking.repository.interfaces;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public interface AdminRepositoryInterface {

	public JsonArray getEstacionamientos() throws Exception;
	public JsonArray getEmpleados() throws Exception;
	public boolean saveEstacionamiento(JsonObject estacionamiento) throws Exception;
	public JsonArray getRoles() throws Exception;
	public JsonArray getUsuarios() throws Exception;
	public boolean saveEmpleado(JsonObject empleado) throws Exception;
	public boolean saveEmpleadoAdministrador(JsonObject empleado) throws Exception;
	public JsonObject getestacionamientoId(String id) throws Exception;
	public boolean updateEstacionamiento(JsonObject estacionamiento) throws Exception;
	public boolean eliminarEstacionamiento(String id) throws Exception;
	public boolean eliminarEmpleado(String id) throws Exception;
}