package com.parking.repository.interfaces;

import com.google.gson.JsonObject;

public interface PensionasoRepositoryInterface {

	public JsonObject getImportePensionado(int idEstacionamiento) throws Exception;

	public boolean savePensionado(JsonObject objPension);
}
