package com.parking.service.interfaces;

import com.parking.utils.ResponseApp;

public interface PensionadoInterface {

	public ResponseApp getPensionImporte(int idEstacionamiento);

	public ResponseApp savePensionado(String parametersRequest);
}
