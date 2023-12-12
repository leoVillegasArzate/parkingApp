package com.parking.service.interfaces;

import com.google.gson.JsonObject;
import com.parking.utils.ResponseApp;

public interface PensionadoInterface {

	public ResponseApp getPensionImporte(int idEstacionamiento);

	public ResponseApp savePensionado( JsonObject pensionado);

	public ResponseApp lstPensionados(int idEstacionamiento);


	public ResponseApp deletePensionado(String id);

	public ResponseApp getMesesPendientes(String id);

	public ResponseApp savePago(String parametersRequest);
}
