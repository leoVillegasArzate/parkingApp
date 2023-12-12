package com.parking.repository.interfaces;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public interface PensionasoRepositoryInterface {

	public JsonObject getImportePensionado(int idEstacionamiento) throws Exception;

	public boolean savePensionado(JsonObject objPension) throws Exception;

	public JsonArray lstPensionado(int idEstacionamiento) throws Exception;

	public boolean updatePensionado(JsonObject pensionado) throws Exception;

	public boolean delete(JsonObject idObj) throws Exception;

	public String getPagoMax(int idPensionado) throws Exception;

	public JsonArray getPensionado(JsonObject idObj);

	public boolean savePago(JsonObject pago) throws Exception;
}
